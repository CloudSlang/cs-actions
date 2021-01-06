package io.cloudslang.content.sitescope.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.sitescope.constants.Inputs;
import io.cloudslang.content.sitescope.constants.SuccessMsgs;
import io.cloudslang.content.sitescope.entities.DeleteMonitorGroupInputs;
import io.cloudslang.content.sitescope.entities.DeployTemplateInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.constants.Inputs.DeployTemplate.*;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;

public class DeployTemplateService {

    public @NotNull
    Map<String,String> execute(@NotNull DeployTemplateInputs deployTemplateInputs) throws Exception{
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final SiteScopeCommonInputs commonInputs = deployTemplateInputs.getCommonInputs();

        httpClientInputs.setUrl(commonInputs.getProtocol() + "://" + commonInputs.getHost() + COLON + commonInputs.getPort() +
                SITESCOPE_TEMPLATES_API + DEPLOY_TEMPLATE_ENDPOINT);

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(X_WWW_FORM);
        httpClientInputs.setFormParams(populateDeployTemplateFormParams(deployTemplateInputs));
        httpClientInputs.setFormParamsAreURLEncoded(String.valueOf(true));
//        httpClientInputs.setBody("pathToTemplate:TestNG_sis_path_delimiter_Test_Template\n" +
//                "pathToTargetGroup:Template_Group");
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());

        Map<String, String> httpClientOutputs = new HttpClientService().execute(httpClientInputs);

        return HttpUtils.convertToSitescopeResultsMap(httpClientOutputs, SuccessMsgs.DEPLOY_TEMPLATE);
    }

    public static String populateDeployTemplateFormParams(DeployTemplateInputs deployTemplateInputs){
        String delimiter = deployTemplateInputs.getDelimiter();
        String pathToTemplate = deployTemplateInputs.getPathToTemplate();
        String pathToTargetGroup = deployTemplateInputs.getPathToTargetGroup();
        String connectToServer = deployTemplateInputs.getConnectToServer();
        String testRemotes = deployTemplateInputs.getTestRemotes();
        String customParameters = deployTemplateInputs.getCustomParameters();

        if(!delimiter.isEmpty()){
            pathToTemplate = pathToTemplate.replace(delimiter,SITE_SCOPE_DELIMITER);
            pathToTargetGroup = pathToTargetGroup.replace(delimiter,SITE_SCOPE_DELIMITER);
        }

        Map<String, String> inputsMap = new HashMap<>();
        inputsMap.put(PATH_TO_TARGET_GROUP, pathToTargetGroup);
        inputsMap.put(CONNECT_TO_SERVER, connectToServer);
        inputsMap.put(TEST_REMOTES, testRemotes);
      //  inputsMap.put(CUSTOM_PARAMETERS, customParameters);
        inputsMap.put(PATH_TO_TEMPLATE, pathToTemplate);


        URIBuilder ub = new URIBuilder();

        for (Map.Entry<String, String> entry : inputsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!value.isEmpty())
                ub.addParameter(key, value);
        }

        return ub.toString();
    }
}
