package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.ErrorMessages;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CreateServerCustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.compute.domain.*;
import org.jclouds.compute.domain.internal.HardwareImpl;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationScope;
import org.jclouds.domain.internal.LocationImpl;
import org.jclouds.ec2.domain.Hypervisor;
import org.jclouds.ec2.domain.InstanceState;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.features.InstanceApi;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Mihai Tusa.
 * 2/23/2016.
 */
public class AmazonComputeServiceHelper {
    private static final String UNRECOGNIZED = "unrecognized";

    private static final double DEFAULT_CORES_PER_PROCESSOR = 1;
    private static final double DEFAULT_PROCESSOR_SPEED = 2.4;
    private static final int DEFAULT_SSH_PORT = 22;

    public InstanceState getInstanceState(InstanceApi instanceApi, CustomInputs customInputs) throws Exception {
        Set<? extends Reservation<? extends RunningInstance>> reservationSet = instanceApi
                .describeInstancesInRegion(customInputs.getRegion(), customInputs.getServerId());
        if (reservationSet.iterator().hasNext()) {
            Reservation<? extends RunningInstance> reservation = reservationSet.iterator().next();
            if (reservation.iterator().hasNext()) {
                RunningInstance runningInstance = reservation.iterator().next();
                return runningInstance.getInstanceState();
            }
        }
        throw new Exception(ErrorMessages.SERVER_NOT_FOUND);
    }

    public void stopAndWaitToStopInstance(InstanceApi instanceApi, InstanceState instanceState, CustomInputs customInputs)
            throws Exception {
        if (!InstanceState.STOPPED.equals(instanceState)) {
            instanceApi.stopInstancesInRegion(customInputs.getRegion(), true, customInputs.getServerId());
            waitLoop(instanceApi, instanceState, customInputs);
        }
    }

    public org.jclouds.compute.domain.Image getImage(CommonInputs commonInputs, CreateServerCustomInputs createServerInputs) {
        Set<String> iso3166Codes = getIso3166CodesSet(commonInputs, createServerInputs);
        Set<String> tags = getStringsSet(createServerInputs.getImageTags(), commonInputs.getDelimiter());
        Location location = getLocation(createServerInputs, iso3166Codes);

        OperatingSystem operatingSystem = new OperatingSystem.Builder()
                .family(OsFamily.fromValue(createServerInputs.getOsFamily()))
                .description(createServerInputs.getOsDescription())
                .is64Bit(createServerInputs.is64Bit())
                .build();

        return new org.jclouds.compute.domain.ImageBuilder()
                .providerId(createServerInputs.getImageProviderId())
                .name(createServerInputs.getImageName())
                .id(createServerInputs.getImageId())
                .location(location)
                .uri(createServerInputs.getImageUri())
                .userMetadata(new HashMap<String, String>())
                .tags(tags)
                .operatingSystem(operatingSystem)
                .status(Image.Status.AVAILABLE)
                .description(createServerInputs.getImageDescription())
                .build();
    }

    public Hardware getHardware(CommonInputs commonInputs, CreateServerCustomInputs createServerInputs) {
        Map<String, String> hardwareUserMetadata = getTagsMap(createServerInputs.getHardwareUserMetadataKeys(),
                createServerInputs.getHardwareUserMetadataValues(), commonInputs.getDelimiter());
        Set<String> tags = getStringsSet(createServerInputs.getHardwareTags(), commonInputs.getDelimiter());
        List<Processor> processors = getProcessorsList(createServerInputs.getCoresPerProcessor(),
                createServerInputs.getProcessorsGhzSpeed(), commonInputs.getDelimiter(),
                createServerInputs.getProcessorsNumber());
        List<Volume> volumes = getVolumesList(createServerInputs);
        String hypervisor = StringUtils.isBlank(createServerInputs.getHypervisor())
                ? Hypervisor.OVM.toString() : Hypervisor.fromValue(createServerInputs.getHypervisor()).toString();

        return new HardwareImpl(createServerInputs.getHardwareProviderId(), createServerInputs.getHardwareName(),
                createServerInputs.getHardwareId(), null, createServerInputs.getHardwareUri(), hardwareUserMetadata, tags,
                processors, createServerInputs.getRamGbAmount(), volumes, null, hypervisor);
    }

    public TemplateOptions getTemplateOptions(CommonInputs commonInputs,
                                              CreateServerCustomInputs createServerInputs,
                                              int[] inputsArray) {
        TemplateOptions templateOptions = new TemplateOptions()
                .inboundPorts(inputsArray)
                .blockOnComplete(createServerInputs.isBlockOnComplete())
                .blockUntilRunning(createServerInputs.isBlockUntilRunning());

        if (StringUtils.isNotBlank(createServerInputs.getPublicKey())
                && StringUtils.isNotBlank(createServerInputs.getPrivateKey())) {
            templateOptions.authorizePublicKey(createServerInputs.getPublicKey());
            templateOptions.installPrivateKey(createServerInputs.getPrivateKey());
        }

        if (createServerInputs.getBlockPort() >= 0 && createServerInputs.getBlockPortSeconds() > 0) {
            templateOptions.blockOnPort(createServerInputs.getBlockPort(), createServerInputs.getBlockPortSeconds());
        }

        if (StringUtils.isNotBlank(createServerInputs.getRunScript())) {
            templateOptions.runScript(createServerInputs.getRunScript());
        }

        if (StringUtils.isNotBlank(createServerInputs.getTemplateTagsString())) {
            Set<String> templateTagsSet = getStringsSet(createServerInputs.getTemplateTagsString(), commonInputs.getDelimiter());
            templateOptions.tags(templateTagsSet);
        }

        if (StringUtils.isNotBlank(createServerInputs.getNetworksString())) {
            Set<String> networksSet = getStringsSet(createServerInputs.getNetworksString(), commonInputs.getDelimiter());
            templateOptions.networks(networksSet);
        }

        if (StringUtils.isNotBlank(createServerInputs.getNodeNames())) {
            Set<String> nodesSet = getStringsSet(createServerInputs.getNodeNames(), commonInputs.getDelimiter());
            templateOptions.nodeNames(nodesSet);
        }

        if (StringUtils.isNotBlank(createServerInputs.getSecurityGroups())) {
            Set<String> securityGroupsSet = getStringsSet(createServerInputs.getSecurityGroups(), commonInputs.getDelimiter());
            templateOptions.securityGroups(securityGroupsSet);
        }

        if (StringUtils.isNotBlank(createServerInputs.getTemplateUserMetadataKeys())
                && StringUtils.isNotBlank(createServerInputs.getTemplateUserMetadataValues())) {
            Map<String, String> templateUserMetadata = getTagsMap(createServerInputs.getTemplateUserMetadataKeys(),
                    createServerInputs.getTemplateUserMetadataValues(), commonInputs.getDelimiter());
            templateOptions.userMetadata(templateUserMetadata);
        }

        return templateOptions;
    }

    public int[] getPortsArray(String input, String delimiter) {
        int[] inboundPorts;
        if (StringUtils.isNotBlank(input)) {
            Set<String> stringsSet = new HashSet<>(Arrays.asList(input.split(Pattern.quote(delimiter))));
            Set<Integer> integersSet = new HashSet<>();
            for (String item : stringsSet) {
                int port = InputsUtil.getValidPort(item.trim());
                integersSet.add(port);
            }

            inboundPorts = new int[integersSet.size()];

            int counter = 0;
            for (Integer intElement : integersSet) {
                inboundPorts[counter] = intElement;
                counter++;
            }

            return inboundPorts;
        }
        inboundPorts = new int[1];
        inboundPorts[0] = DEFAULT_SSH_PORT;

        return inboundPorts;
    }

    public Location getLocation(CommonInputs commonInputs, CreateServerCustomInputs createServerInputs) {
        Set<String> iso3166Codes = getIso3166CodesSet(commonInputs, createServerInputs);

        return getLocation(createServerInputs, iso3166Codes);
    }

    private Location getLocation(CreateServerCustomInputs createServerInputs, Set<String> iso3166Codes) {
        return new LocationImpl(LocationScope.REGION, createServerInputs.getLocationId(),
                createServerInputs.getLocationDescription(), null, iso3166Codes, new HashMap<String, Object>());
    }

    private List<Volume> getVolumesList(CreateServerCustomInputs createServerInputs) {
        List<Volume> volumes = new ArrayList<>();
        Volume volume = new VolumeBuilder()
                .id(createServerInputs.getVolumeId())
                .type(Volume.Type.valueOf(createServerInputs.getVolumeType()))
                .size(createServerInputs.getVolumeGbSize())
                .device(createServerInputs.getVolumeName())
                .bootDevice(createServerInputs.isBootDevice())
                .durable(createServerInputs.is_Durable())
                .build();
        volumes.add(volume);

        return volumes;
    }

    private List<Processor> getProcessorsList(String keysString, String valuesString, String delimiter, int condition) {
        List<Processor> processors = new ArrayList<>();
        if (StringUtils.isNotBlank(keysString) && StringUtils.isNotBlank(valuesString)) {
            String[] coresArray = keysString.split(Pattern.quote(delimiter));
            String[] speedsArray = valuesString.split(Pattern.quote(delimiter));
            if (coresArray.length != speedsArray.length) {
                throw new RuntimeException(ErrorMessages.DIFFERENT_LENGTH);
            } else if (coresArray.length != condition) {
                throw new RuntimeException(ErrorMessages.UNCORRELATED_PROCESSOR_INPUTS_VALUES);
            } else {
                for (int i = 0; i < coresArray.length; i++) {
                    double cores = InputsUtil.getValidDouble(coresArray[i], DEFAULT_CORES_PER_PROCESSOR);
                    double speed = InputsUtil.getValidDouble(speedsArray[i], DEFAULT_PROCESSOR_SPEED);
                    Processor processor = new Processor(cores, speed);
                    processors.add(processor);
                }
            }
        } else {
            processors.add(new Processor(DEFAULT_CORES_PER_PROCESSOR, DEFAULT_PROCESSOR_SPEED));
        }

        return processors;
    }

    private Set<String> getIso3166CodesSet(CommonInputs commonInputs, CreateServerCustomInputs createServerInputs) {
        Set<String> productCodes = new HashSet<>();
        if (StringUtils.isNotBlank(createServerInputs.getProductCodes())) {
            String[] stringsArray = createServerInputs.getProductCodes().split(Pattern.quote(commonInputs.getDelimiter()));
            Collections.addAll(productCodes, stringsArray);
        }

        return productCodes;
    }

    private Set<String> getStringsSet(String inputsString, String delimiter) {
        if (StringUtils.isNotBlank(inputsString)) {
            return new HashSet<>(Arrays.asList(inputsString.split(Pattern.quote(delimiter))));
        }
        Set<String> emptyStringSet = new HashSet<String>();
        emptyStringSet.add("");
        return emptyStringSet;
    }

    private Map<String, String> getTagsMap(String tagKeys, String tagValues, String delimiter) {
        String[] tagKeysArray = null;
        String[] tagValuesArray = null;
        Map<String, String> tagsMap = new HashMap<>();

        if (StringUtils.isNotBlank(tagKeys)) {
            tagKeysArray = tagKeys.split(Pattern.quote(delimiter));
        }

        if (StringUtils.isNotBlank(tagValues)) {
            tagValuesArray = tagValues.split(Pattern.quote(delimiter));
        }

        if (tagKeysArray != null && tagValuesArray != null) {
            if (tagKeysArray.length != tagValuesArray.length) {
                throw new RuntimeException(ErrorMessages.DIFFERENT_KEYS_AND_VALUES_NUMBER);
            }

            for (int i = 0; i < tagKeysArray.length; i++) {
                tagsMap.put(tagKeysArray[i], tagValuesArray[i]);
            }
        }

        return tagsMap;
    }

    private void waitLoop(InstanceApi instanceApi, InstanceState instanceState, CustomInputs customInputs) throws Exception {
        long waitTime = 0;
        while (!InstanceState.STOPPED.equals(instanceState) && waitTime <= customInputs.getCheckStateTimeout()) {
            Thread.sleep(customInputs.getPolingInterval());
            waitTime += 4000;
            instanceState = getInstanceState(instanceApi, customInputs);
        }
    }
}