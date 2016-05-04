package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.ErrorMessages;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;

/**
 * Created by Mihai Tusa.
 * 4/21/2016.
 */
public class CreateServerCustomInputs {
    private static final String EMPTY = "";
    private static final String DEFAULT_VOLUME_TYPE = "LOCAL";

    private static final int BLOCK_PORT_SECONDS = 0;

    private CustomInputs customInputs;
    private URI imageUri;
    private URI hardwareUri;

    private String productCodes;
    private String imageTags;
    private String group;
    private String imageProviderId;
    private String imageName;
    private String imageId;
    private String imageDescription;
    private String locationId;
    private String locationDescription;
    private String osFamily;
    private String osDescription;
    private String hardwareProviderId;
    private String hardwareName;
    private String hardwareId;
    private String hardwareUserMetadataKeys;
    private String hardwareUserMetadataValues;
    private String hardwareTags;
    private String coresPerProcessor;
    private String processorsGhzSpeed;
    private String volumeId;
    private String volumeType;
    private String volumeName;
    private String hypervisor;
    private String inboundPorts;
    private String publicKey;
    private String privateKey;
    private String runScript;
    private String templateTagsString;
    private String networksString;
    private String nodeNames;
    private String securityGroups;
    private String templateUserMetadataKeys;
    private String templateUserMetadataValues;

    private float volumeGbSize;
    private int nodesCount;
    private int processorsNumber;
    private int ramGbAmount;
    private int blockPort;
    private int blockPortSeconds;

    private boolean is64Bit;
    private boolean bootDevice;
    private boolean is_Durable;
    private boolean blockUntilRunning;
    private boolean blockOnComplete;

    private CreateServerCustomInputs(CreateServerCustomInputs.CreateServerCustomInputsBuilder builder) {
        this.customInputs = builder.customInputs;
        this.imageUri = builder.imageUri;
        this.hardwareUri = builder.hardwareUri;

        this.productCodes = builder.productCodes;
        this.imageTags = builder.imageTags;
        this.group = builder.group;
        this.imageProviderId = builder.imageProviderId;
        this.imageName = builder.imageName;
        this.imageId = builder.imageId;
        this.imageDescription = builder.imageDescription;
        this.locationId = builder.locationId;
        this.locationDescription = builder.locationDescription;
        this.osFamily = builder.osFamily;
        this.osDescription = builder.osDescription;
        this.hardwareProviderId = builder.hardwareProviderId;
        this.hardwareName = builder.hardwareName;
        this.hardwareId = builder.hardwareId;
        this.hardwareUserMetadataKeys = builder.hardwareUserMetadataKeys;
        this.hardwareUserMetadataValues = builder.hardwareUserMetadataValues;
        this.hardwareTags = builder.hardwareTags;
        this.volumeId = builder.volumeId;
        this.volumeType = builder.volumeType;
        this.volumeName = builder.volumeName;
        this.hypervisor = builder.hypervisor;
        this.inboundPorts = builder.inboundPorts;
        this.publicKey = builder.publicKey;
        this.privateKey = builder.privateKey;
        this.runScript = builder.runScript;
        this.templateTagsString = builder.templateTagsString;
        this.networksString = builder.networksString;
        this.nodeNames = builder.nodeNames;
        this.securityGroups = builder.securityGroups;
        this.templateUserMetadataKeys = builder.templateUserMetadataKeys;
        this.templateUserMetadataValues = builder.templateUserMetadataValues;

        this.volumeGbSize = builder.volumeGbSize;
        this.nodesCount = builder.nodesCount;
        this.processorsNumber = builder.processorsNumber;
        this.coresPerProcessor = builder.coresPerProcessor;
        this.processorsGhzSpeed = builder.processorsGhzSpeed;
        this.ramGbAmount = builder.ramGbAmount;
        this.blockPort = builder.blockPort;
        this.blockPortSeconds = builder.blockPortSeconds;

        this.is64Bit = builder.is64Bit;
        this.bootDevice = builder.bootDevice;
        this.is_Durable = builder.is_Durable;
        this.blockUntilRunning = builder.blockUntilRunning;
        this.blockOnComplete = builder.blockOnComplete;
    }

    public CustomInputs getCustomInputs() {
        return customInputs;
    }

    public URI getImageUri() {
        return imageUri;
    }

    public URI getHardwareUri() {
        return hardwareUri;
    }

    public String getCoresPerProcessor() {
        return coresPerProcessor;
    }

    public String getProcessorsGhzSpeed() {
        return processorsGhzSpeed;
    }

    public String getProductCodes() {
        return productCodes;
    }

    public String getImageTags() {
        return imageTags;
    }

    public String getGroup() {
        return group;
    }

    public String getImageProviderId() {
        return imageProviderId;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public String getOsDescription() {
        return osDescription;
    }

    public String getHardwareProviderId() {
        return hardwareProviderId;
    }

    public String getHardwareName() {
        return hardwareName;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public String getHardwareUserMetadataKeys() {
        return hardwareUserMetadataKeys;
    }

    public String getHardwareUserMetadataValues() {
        return hardwareUserMetadataValues;
    }

    public String getHardwareTags() {
        return hardwareTags;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public String getHypervisor() {
        return hypervisor;
    }

    public String getInboundPorts() {
        return inboundPorts;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getRunScript() {
        return runScript;
    }

    public String getTemplateTagsString() {
        return templateTagsString;
    }

    public String getNetworksString() {
        return networksString;
    }

    public String getNodeNames() {
        return nodeNames;
    }

    public String getSecurityGroups() {
        return securityGroups;
    }

    public String getTemplateUserMetadataKeys() {
        return templateUserMetadataKeys;
    }

    public String getTemplateUserMetadataValues() {
        return templateUserMetadataValues;
    }

    public float getVolumeGbSize() {
        return volumeGbSize;
    }

    public int getNodesCount() {
        return nodesCount;
    }

    public int getProcessorsNumber() {
        return processorsNumber;
    }

    public int getRamGbAmount() {
        return ramGbAmount;
    }

    public int getBlockPort() {
        return blockPort;
    }

    public int getBlockPortSeconds() {
        return blockPortSeconds;
    }

    public boolean is64Bit() {
        return is64Bit;
    }

    public boolean isBootDevice() {
        return bootDevice;
    }

    public boolean is_Durable() {
        return is_Durable;
    }

    public boolean isBlockUntilRunning() {
        return blockUntilRunning;
    }

    public boolean isBlockOnComplete() {
        return blockOnComplete;
    }

    public static class CreateServerCustomInputsBuilder {
        private CustomInputs customInputs;
        private URI imageUri;
        private URI hardwareUri;

        private String productCodes;
        private String imageTags;
        private String group;
        private String imageProviderId;
        private String imageName;
        private String imageId;
        private String imageDescription;
        private String locationId;
        private String locationDescription;
        private String osFamily;
        private String osDescription;
        private String hardwareProviderId;
        private String hardwareName;
        private String hardwareId;
        private String hardwareUserMetadataKeys;
        private String hardwareUserMetadataValues;
        private String hardwareTags;
        private String coresPerProcessor;
        private String processorsGhzSpeed;
        private String volumeId;
        private String volumeType;
        private String volumeName;
        private String hypervisor;
        private String inboundPorts;
        private String publicKey;
        private String privateKey;
        private String runScript;
        private String templateTagsString;
        private String networksString;
        private String nodeNames;
        private String securityGroups;
        private String templateUserMetadataKeys;
        private String templateUserMetadataValues;

        private float volumeGbSize;
        private int nodesCount;
        private int processorsNumber;
        private int ramGbAmount;
        private int blockPort;
        private int blockPortSeconds;

        private boolean is64Bit;
        private boolean bootDevice;
        private boolean is_Durable;
        private boolean blockUntilRunning;
        private boolean blockOnComplete;

        public CreateServerCustomInputs build() {
            return new CreateServerCustomInputs(this);
        }

        public CreateServerCustomInputsBuilder withCustomInputs(CustomInputs inputs) {
            customInputs = inputs;
            return this;
        }

        public CreateServerCustomInputsBuilder withImageUri(String inputValue) {
            imageUri = URI.create(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withHardwareUri(String inputValue) {
            hardwareUri = URI.create(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withProductCodes(String inputValue) {
            productCodes = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withImageTags(String inputValue) {
            imageTags = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withGroup(String inputValue) {
            group = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withImageProviderId(String inputValue) {
            imageProviderId = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withImageName(String inputValue) {
            imageName = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withImageId(String inputValue) {
            imageId = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withImageDescription(String inputValue) {
            imageDescription = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withLocationId(String inputValue) {
            locationId = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withLocationDescription(String inputValue) {
            locationDescription = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withOsFamily(String inputValue) {
            osFamily = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withOsDescription(String inputValue) {
            osDescription = (StringUtils.isBlank(inputValue)) ? EMPTY : inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withHardwareProviderId(String inputValue) {
            hardwareProviderId = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withHardwareName(String inputValue) {
            hardwareName = (StringUtils.isBlank(inputValue)) ? EMPTY : inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withHardwareId(String inputValue) {
            hardwareId = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withHardwareUserMetadataKeys(String inputValue) {
            hardwareUserMetadataKeys = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withHardwareUserMetadataValues(String inputValue) {
            hardwareUserMetadataValues = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withHardwareTags(String inputValue) {
            hardwareTags = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withVolumeId(String inputValue) {
            volumeId = (StringUtils.isBlank(inputValue)) ? null : inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withVolumeType(String inputValue) {
            volumeType = (StringUtils.isBlank(inputValue)) ? DEFAULT_VOLUME_TYPE : inputValue;;
            return this;
        }

        public CreateServerCustomInputsBuilder withVolumeName(String inputValue) {
            volumeName = (StringUtils.isBlank(inputValue)) ? EMPTY : inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withHypervisor(String inputValue) {
            hypervisor = (StringUtils.isBlank(inputValue)) ? EMPTY : inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withInboundPorts(String inputValue) {
            inboundPorts = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withPublicKey(String inputValue) {
            publicKey = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withPrivateKey(String inputValue) {
            privateKey = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withRunScript(String inputValue) {
            runScript = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withTemplateTagsString(String inputValue) {
            templateTagsString = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withNetworksString(String inputValue) {
            networksString = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withNodeNames(String inputValue) {
            nodeNames = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withSecurityGroups(String inputValue) {
            securityGroups = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withTemplateUserMetadataKeys(String inputValue) {
            templateUserMetadataKeys = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withTemplateUserMetadataValues(String inputValue) {
            templateUserMetadataValues = inputValue;
            return this;
        }

        public CreateServerCustomInputsBuilder withVolumeGbSize(String inputValue) {
            volumeGbSize = InputsUtil.getValidVolumeAmount(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withCoresPerProcessor(String inputs) {
            coresPerProcessor = inputs;
            return this;
        }

        public CreateServerCustomInputsBuilder withProcessorGhzSpeed(String inputs) {
            processorsGhzSpeed = inputs;
            return this;
        }

        public CreateServerCustomInputsBuilder withNodesCount(String inputValue) {
            nodesCount = InputsUtil.getValidNodesCount(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withProcessorsNumber(String inputValue) {
            processorsNumber = InputsUtil.getValidProcessorsNumber(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withRamGbAmount(String inputValue) {
            ramGbAmount = InputsUtil.getValidMemoryAmount(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withBlockPort(String inputValue) {
            blockPort = InputsUtil.getValidPort(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withBlockPortSeconds(String inputValue) {
            blockPortSeconds = InputsUtil.getValidInt(inputValue, BLOCK_PORT_SECONDS);
            return this;
        }

        public CreateServerCustomInputsBuilder withIs64Bit(String inputValue) {
            is64Bit = Boolean.parseBoolean(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withIsBootDevice(String inputValue) {
            bootDevice = Boolean.parseBoolean(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withIsDurable(String inputValue) {
            is_Durable = Boolean.parseBoolean(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withBlockUntilRunning(String inputValue) {
            blockUntilRunning = Boolean.parseBoolean(inputValue);
            return this;
        }

        public CreateServerCustomInputsBuilder withBlockOnComplete(String inputValue) {
            blockOnComplete = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}