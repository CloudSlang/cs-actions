package io.cloudslang.content.jclouds.services;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.location.reference.LocationConstants;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;

import java.util.Properties;

/**
 * Created by persdana on 7/13/2015.
 */
public class JCloudsComputeService {
    private String endpoint;
    private String identity;
    private String credential;
    private String proxyHost;
    private String proxyPort;

    public JCloudsComputeService(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        this.endpoint = endpoint;
        this.identity = identity;
        this.credential = credential;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    protected ContextBuilder init(String region, String provider) {
        Iterable<Module> modules = ImmutableSet.<Module>of(new SLF4JLoggingModule());

        Properties overrides = new Properties();
        if (StringUtils.isNotBlank(proxyHost)) {
            overrides.setProperty(Constants.PROPERTY_PROXY_HOST, proxyHost);
            overrides.setProperty(Constants.PROPERTY_PROXY_PORT, proxyPort);
        }
        if (StringUtils.isNotBlank(region)) {
            overrides.setProperty(LocationConstants.PROPERTY_REGIONS, region);
        }

        return ContextBuilder.newBuilder(provider)
                .endpoint(endpoint)
                .credentials(identity, credential)
                .overrides(overrides)
                .modules(modules);
    }
}