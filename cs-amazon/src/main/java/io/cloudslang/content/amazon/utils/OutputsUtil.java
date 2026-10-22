/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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
/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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



package io.cloudslang.content.amazon.utils;

import software.amazon.awssdk.services.cloudformation.model.DescribeStackResourcesResponse;
import software.amazon.awssdk.services.cloudformation.model.Stack;
import software.amazon.awssdk.services.costexplorer.model.GetRightsizingRecommendationResponse;
import software.amazon.awssdk.services.rds.model.AddTagsToResourceResponse;
import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesResponse;
import software.amazon.awssdk.services.servicecatalog.model.DescribeProvisionedProductResponse;
import software.amazon.awssdk.services.servicecatalog.model.ProvisionProductResponse;
import software.amazon.awssdk.services.servicecatalog.model.UpdateProvisionedProductResponse;
import software.amazon.awssdk.services.support.model.DescribeTrustedAdvisorCheckResultResponse;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.amazon.entities.aws.AuthorizationHeader;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.xml.actions.XpathQuery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.AUTHORIZATION_HEADER_RESULT;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SIGNATURE_RESULT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.DescribeDBInstanceInputs.AVAILABLE;
import static io.cloudslang.content.amazon.entities.constants.Outputs.*;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.xml.utils.Constants.Defaults.DELIMITER;
import static io.cloudslang.content.xml.utils.Constants.Defaults.XML_DOCUMENT_SOURCE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.SELECTED_VALUE;
import static io.cloudslang.content.xml.utils.Constants.QueryTypes.VALUE;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class OutputsUtil {

    private static final String XMLNS = "xmlns";
    private static final String WORKAROUND = "workaround";

    private OutputsUtil() {
    }

    public static Map<String, String> populateSignatureResultsMap(AuthorizationHeader authorizationHeader) {
        Map<String, String> signatureReturnResultMap = getResultsMap(authorizationHeader.getSignature());

        signatureReturnResultMap.put(SIGNATURE_RESULT, authorizationHeader.getSignature());
        signatureReturnResultMap.put(AUTHORIZATION_HEADER_RESULT, authorizationHeader.getAuthorizationHeader());

        return signatureReturnResultMap;
    }

    public static Map<String, String> getValidResponse(Map<String, String> queryMapResult) {
        if (queryMapResult != null) {
            if (queryMapResult.containsKey(STATUS_CODE) && (valueOf(SC_OK).equals(queryMapResult.get(STATUS_CODE))) && queryMapResult.containsKey(RETURN_RESULT) && !isEmpty(queryMapResult.get(RETURN_RESULT))) {
                queryMapResult.put(RETURN_CODE, SUCCESS);
            } else {
                queryMapResult.put(RETURN_CODE, FAILURE);
            }
            return queryMapResult;
        } else {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put(EXCEPTION, "Null response!");
            resultMap.put(RETURN_CODE, FAILURE);
            resultMap.put(RETURN_RESULT, "The query returned null response!");
            return resultMap;
        }
    }

    public static void putResponseIn(Map<String, String> queryMapResult, String outputName, String xPathQuery) {
        XpathQuery xpathQueryAction = new XpathQuery();
        String xmlString = queryMapResult.get(RETURN_RESULT);
        //We make this workaround because the xml has an xmlns property in the tag and our operation can not parse the xml
        //this should be removed when the xml operation will be enhanced
        if (!isBlank(xmlString)) {
            xmlString = xmlString.replace(XMLNS, WORKAROUND);
            Map<String, String> result = xpathQueryAction.execute(xmlString, XML_DOCUMENT_SOURCE, xPathQuery, VALUE, DELIMITER, valueOf(true));
            if (result.containsKey(RETURN_CODE) && SUCCESS.equals(result.get(RETURN_CODE))) {
                queryMapResult.put(outputName, result.get(SELECTED_VALUE));
            } else {
                queryMapResult.put(RETURN_CODE, FAILURE);
                queryMapResult.put(EXCEPTION, result.get(ERROR_MESSAGE));
            }
        } else {
            queryMapResult.put(RETURN_RESULT, "Empty response.");
            queryMapResult.put(RETURN_CODE, FAILURE);
        }
    }

    private static Map<String, String> getResultsMap(String returnResult) {
        Map<String, String> results = new HashMap<>();
        results.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
        results.put(Outputs.RETURN_RESULT, returnResult);

        return results;
    }

    public static Map<String, String> getSuccessResultMapProvisionProduct(ProvisionProductResponse result) {

        Map<String, String> results = getSuccessResultsMap(result.toString());

        results.put(CREATED_TIME, result.recordDetail().createdTime().toString());
        results.put(PATH_ID, result.recordDetail().pathId());
        results.put(PRODUCT_ID, result.recordDetail().productId());
        results.put(PROVISIONED_PRODUCT_ID, result.recordDetail().provisionedProductId());
        results.put(PROVISIONED_PRODUCT_NAME, result.recordDetail().provisionedProductName());
        results.put(PROVISIONED_PRODUCT_TYPE, result.recordDetail().provisionedProductType());
        results.put(PROVISIONING_ARTIFACT_ID, result.recordDetail().provisioningArtifactId());
        results.put(STATUS, result.recordDetail().statusAsString());

        return results;
    }

    public static Map<String, String> getSuccessResultMapUpdateProvisioningProduct(UpdateProvisionedProductResponse result) {

        Map<String, String> results = getSuccessResultsMap(result.toString());

        results.put(CREATED_TIME, result.recordDetail().createdTime().toString());
        results.put(PATH_ID, result.recordDetail().pathId());
        results.put(PRODUCT_ID, result.recordDetail().productId());
        results.put(PROVISIONED_PRODUCT_ID, result.recordDetail().provisionedProductId());
        results.put(PROVISIONED_PRODUCT_NAME, result.recordDetail().provisionedProductName());
        results.put(PROVISIONED_PRODUCT_TYPE, result.recordDetail().provisionedProductType());
        results.put(PROVISIONING_ARTIFACT_ID, result.recordDetail().provisioningArtifactId());
        results.put(UPDATE_TIME, result.recordDetail().updatedTime().toString());
        results.put(RECORD_ID, result.recordDetail().recordId());
        results.put(RECORD_TYPE, result.recordDetail().recordType());
        results.put(RECORD_ERRORS, result.recordDetail().recordErrors().toString());
        results.put(RECORD_TAGS, result.recordDetail().recordErrors().toString());
        results.put(STATUS, result.recordDetail().statusAsString());

        return results;
    }

    public static Map<String, String> getSuccessResultMapDescribedProvisionedProduct(DescribeProvisionedProductResponse result) {

        Map<String, String> results = getSuccessResultsMap(result.toString());

        results.put(PROVISIONED_PRODUCT_ARN, result.provisionedProductDetail().arn());
        results.put(PROVISIONED_PRODUCT_CREATED_TIME, result.provisionedProductDetail().createdTime().toString());
        results.put(PROVISIONED_PRODUCT_ID, result.provisionedProductDetail().id());
        results.put(PROVISIONED_PRODUCT_STATUS, result.provisionedProductDetail().statusAsString());
        results.put(PROVISIONED_PRODUCT_NAME, result.provisionedProductDetail().name());
        results.put(PROVISIONED_PRODUCT_TYPE, result.provisionedProductDetail().type());

        return results;
    }

    public static String getStackResourcesToJson(DescribeStackResourcesResponse describeStackResources) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final ObjectMapper mapper = newSdkModelMapper();
        mapper.disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        mapper.writeValue(out, describeStackResources.stackResources());

        final byte[] stackResources = out.toByteArray();

        return new String(stackResources);
    }

    public static String getStackOutputsToJson(Stack stack) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final ObjectMapper mapper = newSdkModelMapper();
        mapper.disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        mapper.writeValue(out, stack.outputs());

        final byte[] stackOutputs = out.toByteArray();

        return new String(stackOutputs);
    }

    public static Map<String, String> getSuccessResultMapDescribedDBInstance(DescribeDbInstancesResponse result) throws Exception {
        ObjectMapper objectMapper = newSdkModelMapper();
        DBInstance dbInstance = result.dbInstances().get(0);


        Map<String, String> results = getSuccessResultsMap(objectMapper.writeValueAsString(dbInstance));
        String status = dbInstance.dbInstanceStatus();
        results.put(DB_INSTANCE_STATUS, status);
        if (status.equalsIgnoreCase(AVAILABLE)) {
            results.put(ENDPOINT_ADDRESS, dbInstance.endpoint().address());
            results.put(DB_INSTANCE_ARN, dbInstance.dbInstanceArn());
        }
        return results;
    }

    public static Map<String, String> getSuccessResultMapDBInstance(DBInstance result) throws Exception {
        ObjectMapper objectMapper = newSdkModelMapper();
        return getSuccessResultsMap(objectMapper.writeValueAsString(result));
    }

    public static Map<String, String> getSuccessResultMapDBInstanceTags(AddTagsToResourceResponse result) throws Exception {
        ObjectMapper objectMapper = newSdkModelMapper();
        return getSuccessResultsMap(objectMapper.writeValueAsString(result));
    }

    public static Map<String, String> getSuccessResultMapDescribedRecommendations(DescribeTrustedAdvisorCheckResultResponse result) throws Exception {
        ObjectMapper objectMapper = newSdkModelMapper();
        Map<String, String> results = getSuccessResultsMap(objectMapper.writeValueAsString(result));

        return results;
    }

    public static Map<String, String> getSuccessResultMapDescribedRecommendations(GetRightsizingRecommendationResponse result) throws Exception {
        ObjectMapper objectMapper = newSdkModelMapper();
        Map<String, String> results = getSuccessResultsMap(objectMapper.writeValueAsString(result));

        return results;
    }

    private static ObjectMapper newSdkModelMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }
}
