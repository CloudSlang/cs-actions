
package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteSnapshotInputs;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.nutanix.prism.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.API;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.APPLICATION_API_JSON;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.BASIC;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.DELETE;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.GET;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.PATH_SEPARATOR;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.*;

public class SnapshotImpl {

    // v2.0 snapshots path (aligned with VMImpl /api/{version})
    private static final String SNAPSHOTS_PATH = "/snapshots";

    /**
     * DELETE /PrismGateway/services/rest/{apiVersion}/snapshots/{snapshotUUID}
     * If logicalTimestamp is provided, append query params for deleteSnapshots and logicalTimestamp,
     * mirroring the VM delete flow.
     */
    @NotNull
    public static Map<String, String> deleteSnapshot(@NotNull final NutanixDeleteSnapshotInputs inputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(deleteSnapshotURL(inputs.getCommonInputs(), inputs.getSnapshotUUID()));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setUsername(inputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(inputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);

       
        if (!inputs.getLogicalTimestamp().isEmpty()) {
            httpClientInputs.setQueryParams(
                    getDeleteSnapshotQueryParams(inputs.getDeleteSnapshots(), inputs.getLogicalTimestamp())
            );
        }

        setCommonHttpInputs(httpClientInputs, inputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }


    /**
     * Optional list snapshots, kept consistent with VMImpl style.
     */
    @NotNull
    public static Map<String, String> listSnapshots(@NotNull final NutanixCommonInputs commonInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listSnapshotsURL(commonInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String listSnapshotsURL(@NotNull final NutanixCommonInputs commonInputs) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder(commonInputs);
        final StringBuilder path = new StringBuilder()
                .append(API)
                .append(commonInputs.getAPIVersion())
                .append(SNAPSHOTS_PATH);
        uriBuilder.setPath(path.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String deleteSnapshotURL(@NotNull final NutanixCommonInputs commonInputs,
                                           @NotNull final String snapshotUUID) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder(commonInputs);
        final StringBuilder path = new StringBuilder()
                .append(API)
                .append(commonInputs.getAPIVersion())
                .append(SNAPSHOTS_PATH)
                .append(PATH_SEPARATOR)
                .append(snapshotUUID);
        uriBuilder.setPath(path.toString());
        return uriBuilder.build().toURL().toString();
    }
}

