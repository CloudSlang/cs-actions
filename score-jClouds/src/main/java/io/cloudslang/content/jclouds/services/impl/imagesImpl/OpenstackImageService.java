package io.cloudslang.content.jclouds.services.impl.imagesImpl;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import org.jclouds.ContextBuilder;
import org.jclouds.openstack.nova.v2_0.NovaApi;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class OpenstackImageService extends JCloudsComputeService implements ImageService {
    private NovaApi novaApi = null;
    private String region;

    public void setRegion(String region) {
        this.region = region;
    }

    public OpenstackImageService(String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
    }

    @Override
    public String createImageInRegion(String region, String name, String serverId, String imageDescription, boolean imageNoReboot)
            throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    @Override
    public String deregisterImageInRegion(String region, String imageId) throws Exception {
        throw new Exception(Constants.ErrorMessages.NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE);
    }

    private void init() {
        ContextBuilder contextBuilder = super.init(region, Constants.Apis.OPENSTACK_PROVIDER);
        novaApi = contextBuilder.buildApi(NovaApi.class);
    }

    private void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (novaApi == null) {
            this.init();
        }
    }
}
