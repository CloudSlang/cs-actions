/*
 * Copyright 2020-2025 Open Text
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


package io.cloudslang.content.sitescope.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.sitescope.constants.Constants;
import io.cloudslang.content.sitescope.constants.Outputs;
import io.cloudslang.content.sitescope.constants.SuccessMsgs;
import io.cloudslang.content.sitescope.entities.GetMonitorsDeployedAtInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.utils.HttpUtils;
import io.cloudslang.content.sitescope.utils.RemoteServer;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.constants.Constants.GetMonitorsDeployedAt.*;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.FETCH_FULL_CONFIG;
import static io.cloudslang.content.sitescope.constants.SuccessMsgs.*;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;
import static jdk.nashorn.internal.runtime.PropertyDescriptor.GET;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class GetMonitorsDeployedAtService {

    private String colDelimiter;
    private String rowDelimiter;
    private List<RemoteServer> remoteServers;

    public @NotNull Map<String, String> execute(@NotNull GetMonitorsDeployedAtInputs getMonitorsDeployedAtInputs) throws Exception {

        Map<String,String> resultMap;
        colDelimiter = getMonitorsDeployedAtInputs.getColDelimiter();
        rowDelimiter = getMonitorsDeployedAtInputs.getRowDelimiter();
        Map<String, String> fullConfigurationHttpOutputs = getFullConfigurationSnapshot(getMonitorsDeployedAtInputs);
        int statusCode = Integer.parseInt(fullConfigurationHttpOutputs.get(Outputs.STATUS_CODE));
        if (statusCode == 200) {
            String fullConfiguration = fullConfigurationHttpOutputs.get(Outputs.RETURN_RESULT);
            remoteServers = getRemoteServers(fullConfiguration, getMonitorsDeployedAtInputs.getTargetServer());
            StringBuilder result = new StringBuilder();

            if(remoteServers.size() == 1) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(fullConfiguration);
                processNode(root, result, getMonitorsDeployedAtInputs.getRowDelimiter(), new StringBuilder());
                if (result.length() == 0)
                    result.append(String.format(NO_MONITORS_DEPLOYED,remoteServers.get(0).getName()));
                else
                    result.insert(0,GET_MONITORS_DEPLOYED_AT + rowDelimiter);
                resultMap = populateResultMap(result.toString(),ReturnCodes.SUCCESS, statusCode);
            }else if(remoteServers.size() > 1){
                StringBuilder servers = new StringBuilder();
                for(RemoteServer rs : remoteServers)
                    servers.append(rs.getName()).append(colDelimiter);
                result.append(String.format(MULTIPLE_SERVERS_MESSAGE,servers.substring(0,servers.length()-colDelimiter.length())));
                resultMap =  populateResultMap(result.toString(),Constants.ReturnCodes.MULTIPLE_SERVERS,statusCode);
            }else{
                resultMap = populateResultMap(NO_SERVER_FOUND,ReturnCodes.SUCCESS,200);
            }
        return  resultMap;
        } else {
            return HttpUtils.convertToSitescopeResultsMap(fullConfigurationHttpOutputs, SuccessMsgs.GET_MONITORS_DEPLOYED_AT);
        }
    }

    private Map<String,String> populateResultMap(String result, String returnCode, int statusCode){
        Map<String, String> results = new HashMap();
        results.put(RETURN_RESULT, result);
        results.put(RETURN_CODE, returnCode);
        results.put(Outputs.STATUS_CODE, String.valueOf(statusCode));
        results.put(EXCEPTION,EMPTY);
        return results;
    }

    private StringBuilder processNode(JsonNode node, StringBuilder resultBuilder, String delimiter, StringBuilder currentPath) {
        StringBuilder copy = new StringBuilder(currentPath);
        if (node.has(ENTITY_PROPERTIES)) {
            currentPath.append(node.path(ENTITY_PROPERTIES).get(NAME).asText()).append(DEFAULT_DELIMITER);
        }

        if (node.has(MONITOR_CHILDREN)) {
            JsonNode monitorSnapshotChildren = node.path(MONITOR_CHILDREN);
            Iterator<Map.Entry<String, JsonNode>> iterator = monitorSnapshotChildren.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> monitor = iterator.next();
                verifyMonitor(monitor, resultBuilder, currentPath.toString());
            }
        }
        if (node.has(GROUP_CHILDREN)) {
            JsonNode groupSnapshotChildren = node.path(GROUP_CHILDREN);
            Iterator<Map.Entry<String, JsonNode>> iterator = groupSnapshotChildren.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> group = iterator.next();
                currentPath = processNode(group.getValue(), resultBuilder, delimiter, currentPath);
            }
        }
        return copy;
    }

    private void verifyMonitor(Map.Entry<String, JsonNode> monitor, StringBuilder pathBuilder, String currentPath) {
        StringBuilder remoteIDBuilder = new StringBuilder();
        if (monitor.getValue().path(ENTITY_PROPERTIES).has(REMOTE_ID)) {
            String remoteID = monitor.getValue().path(ENTITY_PROPERTIES).get(REMOTE_ID).asText();
            for (RemoteServer remoteServer : remoteServers) {
                String remoteSvID = remoteIDBuilder.append(REMOTE).append(remoteServer.getOs()).
                        append(INSTANCE_PREFERENCES).append(remoteServer.getId()).toString();
                if (remoteID.equals(remoteSvID)) {
                    String isEnabled = monitor.getValue().path(ENTITY_PROPERTIES).get(ENABLED).asText();
                    String monitorName = monitor.getValue().path(ENTITY_PROPERTIES).get(NAME).asText();
                    String status;
                    if (isEnabled.equalsIgnoreCase(BOOLEAN_TRUE))
                        status = ENABLED.substring(1);
                    else
                        status = DISABLED;
                    pathBuilder.append(currentPath).append(monitorName).append(colDelimiter).append(status).append(rowDelimiter);
                    break;
                }
            }
        }
    }

    public Map<String, String> getFullConfigurationSnapshot(GetMonitorsDeployedAtInputs getMonitorsDeployedAtInputs) throws
            Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final SiteScopeCommonInputs commonInputs = getMonitorsDeployedAtInputs.getCommonInputs();

        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setUrl(getUrl(getMonitorsDeployedAtInputs));
        httpClientInputs.setQueryParamsAreURLEncoded(String.valueOf(true));
        httpClientInputs.setMethod(GET);
        return new HttpClientService().execute(httpClientInputs);
    }

    private String getUrl(GetMonitorsDeployedAtInputs inputs) throws URISyntaxException {
        URIBuilder urlBuilder = new URIBuilder();
        urlBuilder.setScheme(inputs.getCommonInputs().getProtocol());
        urlBuilder.setHost(inputs.getCommonInputs().getHost());
        urlBuilder.setPort(Integer.parseInt(inputs.getCommonInputs().getPort()));
        urlBuilder.setPath(SITESCOPE_ADMIN_API + GET_FULL_CONFIGURATION_SNAPSHOT);
        urlBuilder.addParameter(FETCH_FULL_CONFIG, BOOLEAN_TRUE);
        return urlBuilder.build().toString();
    }


    private List<RemoteServer> getRemoteServers(String config, String targetServer) throws IOException {
        List<RemoteServer> remoteServersList = new LinkedList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(config);
        JsonNode preferenceSnapShotNode = root.path(PREFERENCE_SNAPSHOT);
        if (preferenceSnapShotNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> prefSnapshotIterator = preferenceSnapShotNode.fields();
            while (prefSnapshotIterator.hasNext()) {
                Map.Entry<String, JsonNode> fields = prefSnapshotIterator.next();
                String key = fields.getKey();
                if (key.startsWith(SNAPSHOT_REMOTE) && key.endsWith(SNAPSHOT_CHILDREN)) {
                    searchTargetServer(fields.getValue(), targetServer, remoteServersList);
                }
            }
        }
        return remoteServersList;
    }

    public static void searchTargetServer(JsonNode remoteSnapshotChildren,
                                          String targetServer, List<RemoteServer> remoteServersList) {

        String serverHostShort = "";
        if (targetServer.startsWith("\\\\")) {
            serverHostShort = targetServer.substring(2);
        }
        Iterator<Map.Entry<String, JsonNode>> remoteSnapshotChildrenIterator = remoteSnapshotChildren.fields();
        while (remoteSnapshotChildrenIterator.hasNext()) {
            Map.Entry<String, JsonNode> remoteServers = remoteSnapshotChildrenIterator.next();
            if (remoteServers.getValue().path(ENTITY_PROPERTIES).get(HOST).asText().equalsIgnoreCase(targetServer)
                    || remoteServers.getValue().path(ENTITY_PROPERTIES).get(HOST).asText().equalsIgnoreCase(serverHostShort)
                    || remoteServers.getValue().path(ENTITY_PROPERTIES).get(NAME).asText().equalsIgnoreCase(targetServer)) {
                addRemoteServerToList(remoteServersList, remoteServers.getValue());
            }
        }
    }

    public static void addRemoteServerToList(List<RemoteServer> remoteServerList, JsonNode remoteServersNode) {
        JsonNode entitySnapshotProperties = remoteServersNode.path(ENTITY_PROPERTIES);
        remoteServerList.add(new RemoteServer(entitySnapshotProperties.get(ID).asText(),
                entitySnapshotProperties.get(OS).asText(),
                entitySnapshotProperties.get(NAME).asText(),
                entitySnapshotProperties.get(HOST).asText()));
    }
}
