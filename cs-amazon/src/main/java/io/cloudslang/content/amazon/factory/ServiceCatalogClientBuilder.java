package io.cloudslang.content.amazon.factory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.servicecatalog.AWSServiceCatalog;
import com.amazonaws.services.servicecatalog.AWSServiceCatalogAsyncClientBuilder;
import com.amazonaws.services.servicecatalog.AWSServiceCatalogClientBuilder;
import org.apache.commons.lang3.StringUtils;

public class ServiceCatalogClientBuilder {
    public static AWSServiceCatalog getServiceCatalogClientBuilder(
            String accessKeyId,
            String secretAccessKey,
            String proxyHost,
            Integer proxyPort,
            String proxyUsername,
            String proxyPassword,
            Integer connectTimeoutMs,
            Integer executionTimeoutMs,
            String region,
            boolean async) {

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(connectTimeoutMs);
        clientConfiguration.setClientExecutionTimeout(executionTimeoutMs);

        if(!StringUtils.isEmpty(proxyHost)){
            clientConfiguration.setProxyHost(proxyHost);
            clientConfiguration.setProxyPort(proxyPort);
            if(!StringUtils.isEmpty(proxyUsername)){
                clientConfiguration.setProxyUsername(proxyUsername);
                clientConfiguration.setProxyPassword(proxyPassword);
            }
        }
        if(!async) {
            return AWSServiceCatalogAsyncClientBuilder.standard()
                    .withRegion(region)
                    .withClientConfiguration(clientConfiguration)
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                    .build();
        }
        return AWSServiceCatalogClientBuilder.standard()
                .withRegion(region)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .build();


    }
}
