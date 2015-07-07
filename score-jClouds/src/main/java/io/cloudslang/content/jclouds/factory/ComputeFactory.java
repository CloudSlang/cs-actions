package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.impl.AmazonComputeService;
import io.cloudslang.content.jclouds.services.impl.ComputeServiceImpl;
import io.cloudslang.content.jclouds.services.impl.OpenstackComputeService;

/**
 * Created by persdana on 5/27/2015.
 */
public class ComputeFactory {

    private static final String ENDPOINT_NOT_SPECIFIED = "Endpoint input is not specified!";
    private static final String PROVIDER_NOT_SPECIFIED = "Provider input is not specified!";

    private static String provider;
    private static String endpoint;
    private static String identity;
    private static String credential;
    private static String proxyHost;
    private static String proxyPort;

        public static ComputeService getComputeService(CommonInputs commonInputs) throws Exception {
            processCommonInputs(commonInputs);
            ComputeService computeService;
            switch (provider) {
                case "openstack":
                    computeService = new OpenstackComputeService(endpoint, identity, credential, proxyHost, proxyPort);
                    break;
                case "amazon":
                    computeService = new AmazonComputeService(endpoint, identity, credential, proxyHost, proxyPort);
                    break;
                default:
                    computeService = new ComputeServiceImpl(provider, endpoint, identity, credential, proxyHost, proxyPort);
                break;
            }

            return computeService;
        }


    protected static void processCommonInputs(CommonInputs commonInputs) throws Exception {
        provider = commonInputs.getProvider().trim().toLowerCase();
        if(provider == null || provider.isEmpty()) {
            throw new Exception(PROVIDER_NOT_SPECIFIED);
        }
        endpoint = commonInputs.getEndpoint();
        if (null == endpoint || endpoint.equals("")) {
            throw new Exception(ENDPOINT_NOT_SPECIFIED);
        } else {
            endpoint = endpoint.trim();
        }
        identity = commonInputs.getIdentity();
        credential = commonInputs.getCredential();

        proxyHost = commonInputs.getProxyHost();
        proxyPort = commonInputs.getProxyPort();
    }
}
