/*
 * (c) Copyright 2023 Micro Focus, L.P.
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
package io.cloudslang.content.sharepoint.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.httpclient.actions.HttpClientGetAction;
import io.cloudslang.content.sharepoint.utils.Constants;
import io.cloudslang.content.sharepoint.utils.Descriptions;

import java.util.Map;
import java.util.stream.StreamSupport;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.sharepoint.services.SharepointService.*;
import static io.cloudslang.content.sharepoint.utils.Constants.*;
import static io.cloudslang.content.sharepoint.utils.Constants.VALUE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetEntitiesFromDriveService {
    public static Map<String, String> getEntitiesFromDrive(
            String authToken,
            String driveId,
            String path,
            String entitiesType,
            String proxyHost,
            String proxyPort,
            String proxyUsername,
            String proxyPassword,
            String trustAllRoots,
            String x509HostnameVerifier,
            String trustKeystore,
            String trustPassword,
            String tlsVersion,
            String allowedCiphers,
            String connectTimeout,
            String executionTimeout,
            SerializableSessionObject sessionCookies,
            GlobalSessionObject sessionConnectionPool) {

        try {

            String endpoint = path == null || path.isEmpty() ? ROOT_CHILDREN_ENDPOINT : ROOT_PATH_ENDPOINT + encodeFileName(path) + CHILDREN_PATH_ENDPOINT;

            Map<String, String> result = new HttpClientGetAction().execute(
                    GRAPH_API_ENDPOINT + DRIVES_ENDPOINT + driveId + endpoint,
                    ANONYMOUS,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    proxyHost,
                    proxyPort,
                    proxyUsername,
                    proxyPassword,
                    tlsVersion,
                    allowedCiphers,
                    trustAllRoots,
                    x509HostnameVerifier,
                    trustKeystore,
                    trustPassword,
                    EMPTY,
                    EMPTY,
                    FALSE,
                    CONNECTIONS_MAX_PER_ROUTE_CONST,
                    CONNECTIONS_MAX_TOTAL_CONST,
                    EMPTY,
                    EMPTY,
                    AUTHORIZATION_BEARER + authToken,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    connectTimeout,
                    EMPTY,
                    executionTimeout,
                    sessionCookies,
                    sessionConnectionPool
            );

            processHttpResult(result, Descriptions.GetEntitiesFromDrive.EXCEPTION_DESC);

            if (result.get(RETURN_CODE).equals(NEGATIVE_RETURN_CODE))
                return result;

            JsonObject returnResult = JsonParser.parseString(result.get(RETURN_RESULT)).getAsJsonObject();

            StreamSupport
                    .stream(returnResult.get(VALUE).getAsJsonArray().spliterator(), true)
                    .filter(entity -> entity.getAsJsonObject().has(FOLDER))
                    .forEach(folder -> {

                        Map<String, String> folderResult = getEntitiesFromDrive(
                                authToken,
                                driveId,
                                path + SLASH + folder.getAsJsonObject().get(Constants.NAME).getAsString(),
                                entitiesType,
                                proxyHost,
                                proxyPort,
                                proxyUsername,
                                proxyPassword,
                                trustAllRoots,
                                x509HostnameVerifier,
                                trustKeystore,
                                trustPassword,
                                tlsVersion,
                                allowedCiphers,
                                connectTimeout,
                                executionTimeout,
                                sessionCookies,
                                sessionConnectionPool
                        );

                        try {
                            returnResult
                                    .get(VALUE)
                                    .getAsJsonArray()
                                    .addAll(JsonParser.parseString(folderResult.get(RETURN_RESULT)).getAsJsonObject().get(VALUE).getAsJsonArray());
                        } catch (Exception ignored) {
                        }
                    });

            result.put(RETURN_RESULT, returnResult.toString());

            processHttpGetEntitiesFromDrive(result, Descriptions.GetEntitiesFromDrive.EXCEPTION_DESC, entitiesType);
            return result;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
