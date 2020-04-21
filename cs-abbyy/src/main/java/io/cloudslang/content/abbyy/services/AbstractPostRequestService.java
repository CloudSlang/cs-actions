/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.abbyy.services;

import io.cloudslang.content.abbyy.constants.ConnectionConstants;
import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.constants.MiscConstants;
import io.cloudslang.content.abbyy.constants.OutputNames;
import io.cloudslang.content.abbyy.entities.AbbyyRequest;
import io.cloudslang.content.abbyy.entities.AbbyyResponse;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ClientSideException;
import io.cloudslang.content.abbyy.exceptions.ServerSideException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.utils.AbbyyResponseParser;
import io.cloudslang.content.abbyy.utils.ResultUtils;
import io.cloudslang.content.abbyy.utils.XmlResponseParser;
import io.cloudslang.content.abbyy.validators.AbbyyRequestValidator;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientAction;
import io.cloudslang.content.httpclient.entities.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import javax.xml.parsers.ParserConfigurationException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public abstract class AbstractPostRequestService<R extends AbbyyRequest> {

    protected final HttpClientAction httpClientAction;
    private final AbbyyResponseParser responseParser;
    private final AbbyyRequestValidator<R> requestValidator;
    String lastStatusCode;


    AbstractPostRequestService(AbbyyResponseParser responseParser, HttpClientAction httpClientAction,
                               AbbyyRequestValidator<R> requestValidator) throws ParserConfigurationException {
        this.responseParser = (responseParser != null) ? responseParser : new XmlResponseParser();

        this.httpClientAction = (httpClientAction != null) ? httpClientAction : new HttpClientAction();

        if (requestValidator == null) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_ARGUMENT, "requestValidator"));
        }
        this.requestValidator = requestValidator;
    }


    public Map<String, String> execute(R request) throws Exception {
        if (request == null) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_ARGUMENT, "request"));
        }

        Map<String, String> results = ResultUtils.createNewEmptyMap();

        try {
            ValidationException validationEx = this.requestValidator.validate(request);
            if (validationEx != null) {
                throw validationEx;
            }

            AbbyyResponse response = postRequest(request);
            if (response.getTaskStatus() == AbbyyResponse.TaskStatus.NOT_ENOUGH_CREDITS) {
                throw new AbbyySdkException(String.format(ExceptionMsgs.NOT_ENOUGH_CREDITS, response.getCredits()));
            }
            results.put(OutputNames.TASK_ID, response.getTaskId());
            results.put(OutputNames.CREDITS, String.valueOf(response.getCredits()));

            response = waitTaskToFinish(request, response.getTaskId(), response.getEstimatedProcessingTime());
            switch (response.getTaskStatus()) {
                case COMPLETED:
                    handleTaskCompleted(request, response, results);
                    break;
                case PROCESSING_FAILED:
                    throw new ServerSideException(String.format(ExceptionMsgs.TASK_PROCESSING_FAILED, response.getErrorMessage()));
                case DELETED:
                    throw new AbbyySdkException(ExceptionMsgs.TASK_DELETED);
                case NOT_ENOUGH_CREDITS:
                    throw new AbbyySdkException(ExceptionMsgs.NOT_ENOUGH_CREDITS);
                default:
                    throw new ClientSideException(ExceptionMsgs.UNEXPECTED_STATUS);
            }
            return results;
        } catch (AbbyySdkException ex) {
            if (ex.getResultsMap() == null) {
                ex.setResultsMap(results);
            }
            throw ex;
        } catch (Exception ex) {
            throw new ClientSideException(ex, results);
        } finally {
            results.put(OutputNames.STATUS_CODE, this.lastStatusCode);
        }
    }


    private AbbyyResponse postRequest(R request) throws Exception {
        Map<String, String> rawResponse = this.httpClientAction.execute(
                buildUrl(request),
                Constants.TLSv12,
                MiscConstants.ALLOWED_CYPHERS,
                ConnectionConstants.Headers.AUTH_TYPE,
                String.valueOf(true),
                request.getApplicationId(),
                request.getPassword(),
                null,
                null,
                null,
                request.getProxyHost(),
                String.valueOf(request.getProxyPort()),
                request.getProxyUsername(),
                request.getProxyPassword(),
                String.valueOf(request.isTrustAllRoots()),
                request.getX509HostnameVerifier(),
                request.getTrustKeystore(),
                request.getTrustPassword(),
                null,
                null,
                String.valueOf(request.getConnectTimeout()),
                String.valueOf(request.getSocketTimeout()),
                String.valueOf(false),
                String.valueOf(request.isKeepAlive()),
                String.valueOf(request.getConnectionsMaxPerRoute()),
                String.valueOf(request.getConnectionsMaxTotal()),
                null,
                request.getResponseCharacterSet(),
                null,
                StringUtils.EMPTY,
                null,
                String.valueOf(true),
                String.valueOf(false),
                null,
                String.valueOf(true),
                request.getSourceFile().getAbsolutePath(),
                null,
                ConnectionConstants.Headers.CONTENT_TYPE,
                null,
                null,
                null,
                null,
                null,
                String.valueOf(true),
                null,
                ConnectionConstants.HttpMethods.POST,
                null,
                null);
        this.lastStatusCode = rawResponse.get(MiscConstants.HTTP_STATUS_CODE_OUTPUT);
        if (rawResponse.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE) != null &&
                rawResponse.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE).equals(ResponseNames.FAILURE) ||
                rawResponse.get(MiscConstants.HTTP_STATUS_CODE_OUTPUT) == null) {
            throw new AbbyySdkException(rawResponse.get(io.cloudslang.content.constants.OutputNames.EXCEPTION));
        }
        if (this.lastStatusCode.equals("401")) {
            throw new ClientSideException(ExceptionMsgs.INVALID_CREDENTIALS);
        }
        return this.responseParser.parseResponse(rawResponse);
    }


    private AbbyyResponse waitTaskToFinish(R request, String taskId, long timeToWait) throws Exception {
        final int numberOfAttempts = 5;
        int crtAttemptNr = 0;
        final long minTimeToWait = 3000, maxTimeToWait = 10000;
        AbbyyResponse response;

        do {
            crtAttemptNr++;
            if (crtAttemptNr == 2) {
                if (timeToWait < minTimeToWait) {
                    timeToWait = minTimeToWait;
                } else if (timeToWait > maxTimeToWait) {
                    timeToWait = maxTimeToWait;
                }
            }
            Thread.sleep(timeToWait);

            Map<String, String> rawResponse = getTaskStatus(request, taskId);
            if (rawResponse.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE) != null &&
                    rawResponse.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE).equals(ResponseNames.FAILURE) ||
                    rawResponse.get(MiscConstants.HTTP_STATUS_CODE_OUTPUT) == null) {
                throw new AbbyySdkException(rawResponse.get(io.cloudslang.content.constants.OutputNames.EXCEPTION));
            }
            this.lastStatusCode = rawResponse.get(MiscConstants.HTTP_STATUS_CODE_OUTPUT);
            response = this.responseParser.parseResponse(rawResponse);
        } while (!isTaskFinished(response.getTaskStatus()) && crtAttemptNr < numberOfAttempts);

        if (!isTaskFinished(response.getTaskStatus())) {
            throw new TimeoutException(ExceptionMsgs.OPERATION_TIMEOUT);
        }

        return response;
    }


    private Map<String, String> getTaskStatus(R request, String taskId) throws URISyntaxException {
        String url = new URIBuilder()
                .setScheme(ConnectionConstants.PROTOCOL)
                .setHost(String.format(ConnectionConstants.HOST_TEMPLATE, request.getLocationId(), ConnectionConstants.Endpoints.GET_TASK_STATUS))
                .addParameter(ConnectionConstants.QueryParams.TASK_ID, taskId)
                .build().toString();

        return this.httpClientAction.execute(
                url,
                Constants.TLSv12,
                MiscConstants.ALLOWED_CYPHERS,
                ConnectionConstants.Headers.AUTH_TYPE,
                String.valueOf(true),
                request.getApplicationId(),
                request.getPassword(),
                null,
                null,
                null,
                request.getProxyHost(),
                String.valueOf(request.getProxyPort()),
                request.getProxyUsername(),
                request.getProxyPassword(),
                String.valueOf(request.isTrustAllRoots()),
                request.getX509HostnameVerifier(),
                request.getTrustKeystore(),
                request.getTrustPassword(),
                null,
                null,
                String.valueOf(request.getConnectTimeout()),
                String.valueOf(request.getSocketTimeout()),
                String.valueOf(false),
                String.valueOf(request.isKeepAlive()),
                String.valueOf(request.getConnectionsMaxPerRoute()),
                String.valueOf(request.getConnectionsMaxTotal()),
                null,
                request.getResponseCharacterSet(),
                null,
                StringUtils.EMPTY,
                null,
                String.valueOf(true),
                String.valueOf(false),
                null,
                String.valueOf(true),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                String.valueOf(true),
                null,
                ConnectionConstants.HttpMethods.GET,
                null,
                null);
    }


    private boolean isTaskFinished(AbbyyResponse.TaskStatus status) {
        return status == AbbyyResponse.TaskStatus.COMPLETED ||
                status == AbbyyResponse.TaskStatus.PROCESSING_FAILED ||
                status == AbbyyResponse.TaskStatus.DELETED;
    }


    protected void handleTaskCompleted(R request, AbbyyResponse response, Map<String, String> results) throws Exception {
        results.put(io.cloudslang.content.constants.OutputNames.RETURN_RESULT, MiscConstants.DOCUMENT_PROCESSED_SUCCESSFULLY);
        results.put(io.cloudslang.content.constants.OutputNames.RETURN_CODE, ReturnCodes.SUCCESS);
    }


    protected abstract String buildUrl(R request) throws Exception;
}