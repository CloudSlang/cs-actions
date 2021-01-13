package io.cloudslang.content.sitescope.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.sitescope.constants.Outputs;
import io.cloudslang.content.sitescope.constants.SuccessMsgs;
import io.cloudslang.content.sitescope.entities.GetMonitorsDeployedAtInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;
import static jdk.nashorn.internal.runtime.PropertyDescriptor.GET;


public class GetMonitorsDeployedAtService {

    public @NotNull Map<String,String> execute(@NotNull GetMonitorsDeployedAtInputs getMonitorsDeployedAtInputs) throws Exception {

        Map<String,String> config = getFullConfigurationSnapshot(getMonitorsDeployedAtInputs);
        int statusCode = Integer.parseInt(config.get(Outputs.STATUS_CODE));
        if(statusCode == 200){
             String fullConfiguration = config.get(Outputs.RETURN_RESULT);
            //FILTER LOGIC
             return new HashMap<>();
        }else{
            return HttpUtils.convertToSitescopeResultsMap(config, SuccessMsgs.GET_MONITORS_DEPLOYED_AT);
        }
    }

    public Map<String,String> getFullConfigurationSnapshot(GetMonitorsDeployedAtInputs getMonitorsDeployedAtInputs) throws Exception {
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
        urlBuilder.setPath("SiteScope/api/admin/config/snapshot");
        urlBuilder.addParameter("fetchFullConfig","true");
        return urlBuilder.build().toString();
    }
}
