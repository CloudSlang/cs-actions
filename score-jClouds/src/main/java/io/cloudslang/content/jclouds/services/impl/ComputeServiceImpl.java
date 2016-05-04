package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CreateServerCustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JCloudsComputeService;
import io.cloudslang.content.jclouds.services.helpers.AmazonComputeServiceHelper;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.internal.TemplateImpl;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.Location;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by persdana on 6/5/2015.
 */
public class ComputeServiceImpl extends JCloudsComputeService implements ComputeService {
    private static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not implemented. Use 'amazon\' or 'openstack' providers " +
            "in the provider input";

    private static final String AWS_EC2 = "aws-ec2";

    org.jclouds.compute.ComputeService computeService = null;

    private String provider;
    protected String region;

    public ComputeServiceImpl(String provider, String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
        this.provider = provider;
    }

    protected void init() {
        ContextBuilder contextBuilder = super.init(region, provider);
        ComputeServiceContext context = contextBuilder.buildView(ComputeServiceContext.class);
        computeService = context.getComputeService();
    }

    void lazyInit() {
        if (computeService == null) {
            this.init();
        }
    }

    void lazyInit(String region) {
        if (this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if (computeService == null) {
            this.init();
        }
    }

    private org.jclouds.compute.ComputeService createNodesInit(String region, String provider) {
        ContextBuilder contextBuilder = super.init(region, provider);
        ComputeServiceContext context = contextBuilder.buildView(ComputeServiceContext.class);
        return context.getComputeService();
    }

    @Override
    public void resume(String region, String serverId) {
        lazyInit(region);
        computeService.resumeNode(region + "/" + serverId);
    }

    @Override
    public String removeServer(String region, String serverId) {
        lazyInit(region);
        computeService.destroyNode(serverId);
        return "Server Removed";
    }

    @Override
    public String suspend(String region, String serverId) {
        lazyInit(region);
        computeService.suspendNode(region + "/" + serverId);

        return "";
    }

    @Override
    public String start(String region, String serverId) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public String stop(String region, String serverId) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    public void softReboot(String region, String serverId) {
        reboot(region, serverId);
    }

    public void hardReboot(String region, String serverId) {
        reboot(region, serverId);
    }

    public Set<String> listRegions() {
        lazyInit();
        Set<? extends Location> locations = computeService.listAssignableLocations();
        Set<String> res = new HashSet<>();
        for (Location l : locations) {
            res.add(l.getDescription());
        }

        return res;
    }

    @Override
    public Set<String> listNodes(String region) {
        lazyInit(region);
        Set<? extends ComputeMetadata> nodes = computeService.listNodes();
        Set<String> result = new HashSet<>();
        for (ComputeMetadata cm : nodes) {
            result.add(cm.toString());
        }
        return result;
    }

    @Override
    public Set<? extends NodeMetadata> createNodesInGroup(CommonInputs commonInputs,
                                                          CreateServerCustomInputs createServerInputs) throws Exception {
        org.jclouds.compute.ComputeService service = createNodesInit(createServerInputs.getCustomInputs().getRegion(), AWS_EC2);

//        org.jclouds.compute.ComputeService service = ContextBuilder.newBuilder(AWS_EC2)
//                .credentials(commonInputs.getIdentity(), commonInputs.getCredential())
//                .buildView(ComputeServiceContext.class)
//                .getComputeService();

        AmazonComputeServiceHelper helper = new AmazonComputeServiceHelper();
        org.jclouds.compute.domain.Image image = helper.getImage(commonInputs, createServerInputs);
        Hardware hardware = helper.getHardware(commonInputs, createServerInputs);
        Location location = helper.getLocation(commonInputs, createServerInputs);
        TemplateOptions templateOptions = getTemplateOptions(commonInputs, createServerInputs);

        Template template = new TemplateImpl(image, hardware, location, templateOptions);

        return service.createNodesInGroup(createServerInputs.getGroup(), createServerInputs.getNodesCount(), template);
    }

    @Override
    public String updateInstanceType(CustomInputs customInputs) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public Reservation<? extends RunningInstance> runServer(CommonInputs commonInputs, CustomInputs customInputs)
            throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    protected void reboot(String region, String serverId) {
        lazyInit(region);
        computeService.rebootNode(region + "/" + serverId);
    }

    private TemplateOptions getTemplateOptions(CommonInputs commonInputs, CreateServerCustomInputs createServerInputs) {
        TemplateOptions templateOptions;
        if (StringUtils.isBlank(createServerInputs.getInboundPorts())
                && StringUtils.isBlank(createServerInputs.getPublicKey())
                && StringUtils.isBlank(createServerInputs.getPrivateKey())
                && StringUtils.isBlank(createServerInputs.getRunScript())
                && StringUtils.isBlank(createServerInputs.getTemplateTagsString())
                && StringUtils.isBlank(createServerInputs.getNetworksString())
                && StringUtils.isBlank(createServerInputs.getNodeNames())
                && StringUtils.isBlank(createServerInputs.getSecurityGroups())
                && StringUtils.isNotBlank(createServerInputs.getTemplateUserMetadataKeys())
                && StringUtils.isNotBlank(createServerInputs.getTemplateUserMetadataValues())
                && !createServerInputs.isBlockOnComplete()
                && !createServerInputs.isBlockUntilRunning()
                && createServerInputs.getBlockPortSeconds() == 0) {
            templateOptions = TemplateOptions.NONE;
        } else {
            AmazonComputeServiceHelper helper = new AmazonComputeServiceHelper();
            int[] inboundPorts = helper.getPortsArray(createServerInputs.getInboundPorts(), commonInputs.getDelimiter());
            templateOptions = helper.getTemplateOptions(commonInputs, createServerInputs, inboundPorts);
        }
        return templateOptions;
    }
}