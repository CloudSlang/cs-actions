package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.services.TagService;
import io.cloudslang.content.jclouds.services.impl.AmazonTagServiceImpl;
import io.cloudslang.content.jclouds.services.impl.OpenstackTagServiceImpl;

/**
 * Created by Mihai Tusa.
 * 7/20/2016.
 */
public class TagFactory {
    public static TagService getTagService(CommonInputs commonInputs) throws Exception {
        TagService tagService;
        switch (commonInputs.getProvider().toLowerCase()) {
            case Constants.Providers.OPENSTACK:
                tagService = new OpenstackTagServiceImpl(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
                break;
            default:
                tagService = new AmazonTagServiceImpl(
                        commonInputs.getEndpoint(),
                        commonInputs.getIdentity(),
                        commonInputs.getCredential(),
                        commonInputs.getProxyHost(),
                        commonInputs.getProxyPort());
        }

        return tagService;
    }
}