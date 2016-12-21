package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.S3_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HEADER_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.COLON;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 12/21/2016.
 */
public class HeadersMapBuilder {
    private static final String HOST = "Host";

    private HeadersMapBuilder() {
        // prevent instantiation
    }

    public static Map<String, String> getHeadersMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> headersMap = isBlank(wrapper.getCommonInputs().getHeaders()) ? new HashMap<String, String>() :
                InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(), wrapper.getCommonInputs().getHeaders(),
                        HEADER_DELIMITER, COLON, true);

        switch (wrapper.getCommonInputs().getApiService()) {
            case S3_API:
                headersMap.put(HOST, InputsUtil.getS3HostHeaderValue(wrapper.getCommonInputs().getApiService(),
                        wrapper.getStorageInputs().getBucketName()));
                return headersMap;
            default:
                return headersMap;
        }
    }
}