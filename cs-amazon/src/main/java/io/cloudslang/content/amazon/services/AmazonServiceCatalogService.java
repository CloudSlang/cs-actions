package io.cloudslang.content.amazon.services;

import com.amazonaws.services.servicecatalog.AWSServiceCatalog;
import com.amazonaws.services.servicecatalog.model.ProvisionProductRequest;
import com.amazonaws.services.servicecatalog.model.ProvisionProductResult;
import com.amazonaws.services.servicecatalog.model.ProvisioningParameter;
import com.amazonaws.services.servicecatalog.model.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AmazonServiceCatalogService {

    public static ProvisionProductResult provisionProduct(final String provisionedProductName,
                                                          final List<ProvisioningParameter> provisioningParameters,
                                                          final String productId,
                                                          final String provisionToken,
                                                          final String provisioningArtifactId,
                                                          final List<Tag> tags,
                                                          final String acceptLanguage,
                                                          final String notificationArns,
                                                          final String pathId,
                                                          final AWSServiceCatalog serviceCatalogClient) {
        ProvisionProductRequest provisionProductRequest = new ProvisionProductRequest()
                .withProvisionedProductName(provisionedProductName)
                .withProvisioningParameters(provisioningParameters)
                .withProductId(productId)
                .withProvisioningArtifactId(provisioningArtifactId)
                .withTags(tags)
                .withAcceptLanguage(acceptLanguage);

        if (!StringUtils.isEmpty(notificationArns)) {
            provisionProductRequest.withNotificationArns(notificationArns);
        }

        if (!StringUtils.isEmpty(pathId)) {
            provisionProductRequest.withPathId(pathId);
        }

        if (!StringUtils.isEmpty(provisionToken)) {
            provisionProductRequest.withProvisionToken(provisionToken);
        }

        return serviceCatalogClient.provisionProduct(provisionProductRequest);
    }


}
