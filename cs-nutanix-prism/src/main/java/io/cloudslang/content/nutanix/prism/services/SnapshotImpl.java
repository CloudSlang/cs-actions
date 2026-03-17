package io.cloudslang.content.nutanix.prism.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixCreateSnapshotInputs;
import io.cloudslang.content.nutanix.prism.services.models.snapshots.CreateSnapshotRequestBody;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.nutanix.prism.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getUriBuilder;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SnapshotImpl {

    public static Map<String, String> createSnapshot(@NotNull final NutanixCreateSnapshotInputs nutanixCreateSnapshotInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(createSnapshotURL(nutanixCreateSnapshotInputs));
        setCommonHttpInputs(httpClientInputs, nutanixCreateSnapshotInputs.getCommonInputs());
        try {
            httpClientInputs.setBody(createSnapshotBody(nutanixCreateSnapshotInputs));
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setUsername(nutanixCreateSnapshotInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixCreateSnapshotInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String createSnapshotURL(NutanixCreateSnapshotInputs nutanixCreateSnapshotInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixCreateSnapshotInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixCreateSnapshotInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixCreateSnapshotInputs.getVmUUID())
                .append("/snapshots");
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String createSnapshotBody(NutanixCreateSnapshotInputs nutanixCreateSnapshotInputs) {
        String requestBody = EMPTY;
        ObjectMapper snapshotMapper = new ObjectMapper();
        CreateSnapshotRequestBody createSnapshotRequestBody = new CreateSnapshotRequestBody();

        // Create snapshot spec array with single snapshot
        CreateSnapshotRequestBody.SnapshotSpec[] snapshotSpecs = new CreateSnapshotRequestBody.SnapshotSpec[1];
        CreateSnapshotRequestBody.SnapshotSpec snapshotSpec = new CreateSnapshotRequestBody.SnapshotSpec();
        snapshotSpec.setSnapshotName(nutanixCreateSnapshotInputs.getSnapshotName());
        snapshotSpec.setVmUUID(nutanixCreateSnapshotInputs.getVmUUID());

        // Parse the vm_logical_timestamp if provided, otherwise set to 0
        long vmLogicalTimestamp = 0;
        if (nutanixCreateSnapshotInputs.getVmLogicalTimestamp() != null &&
            !nutanixCreateSnapshotInputs.getVmLogicalTimestamp().isEmpty()) {
            try {
                vmLogicalTimestamp = Long.parseLong(nutanixCreateSnapshotInputs.getVmLogicalTimestamp());
            } catch (NumberFormatException e) {
                vmLogicalTimestamp = 0;
            }
        }
        snapshotSpec.setVmLogicalTimestamp(vmLogicalTimestamp);
        snapshotSpecs[0] = snapshotSpec;

        createSnapshotRequestBody.setSnapshotSpecs(snapshotSpecs);

        try {
            requestBody = snapshotMapper.writeValueAsString(createSnapshotRequestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;
    }
}

