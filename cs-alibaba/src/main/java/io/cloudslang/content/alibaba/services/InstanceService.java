package io.cloudslang.content.alibaba.services;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest.DataDisk;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceResponse;
import com.aliyuncs.exceptions.ClientException;
import io.cloudslang.content.alibaba.utils.ProxyUtil;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InstanceService {
    public static String createInstance(final String proxyHost,
                                        final String proxyPort,
                                        final String proxyUsername,
                                        final String proxyPassword,
                                        final String regionId,
                                        final String imageId,
                                        final String instanceType,
                                        final String securityGroupId,
                                        final String zoneId,
                                        final String instanceName,
                                        final String description,
                                        final String internetChargeType,
                                        final Integer internetMaxBandwidthIn,
                                        final Integer internetMaxBandwidthOut,
                                        final String hostname,
                                        final String password,
                                        final boolean passwordInherit,
                                        final String isOptimized,
                                        final String systemDiskCategory,
                                        final Integer systemDiskSize,
                                        final String systemDiskName,
                                        final String systemDiskDescription,
                                        final List<DataDisk> dataDisks,
                                        final String clusterId,
                                        final String hpcClusterId,
                                        final String vSwitchId,
                                        final String privateIpAddress,
                                        final String instanceChargeType,
                                        final String spotStrategy,
                                        final Float spotPriceLimit,
                                        final Integer period,
                                        final String periodUnit,
                                        final Boolean autoRenew,
                                        final Integer autoRenewPeriod,
                                        final String userData,
                                        final String clientToken,
                                        final String keyPairName,
                                        final String deploymentSetId,
                                        final String ramRoleName,
                                        final String securityEnhancementStrategy,
                                        final List<String> tagsKeys,
                                        final List<String> tagsValues,
                                        final IAcsClient client) throws RuntimeException {
        // Set JVM proxies during runtime
        ProxyUtil.setProxies(proxyHost, proxyPort, proxyUsername, proxyPassword);

        // Instantiate the CreateInstanceRequest object
        final CreateInstanceRequest request = new CreateInstanceRequest();
        request.setRegionId(regionId);
        request.setDataDisks(dataDisks);
        request.setAutoRenew(autoRenew);
        request.setAutoRenewPeriod(autoRenewPeriod);
        request.setClientToken(clientToken);
        request.setClusterId(clusterId);
        request.setDeploymentSetId(deploymentSetId);
        request.setDescription(description);
        request.setHostName(hostname);
        request.setHpcClusterId(hpcClusterId);
        request.setImageId(imageId);
        request.setInstanceChargeType(instanceChargeType);
        request.setInternetMaxBandwidthIn(internetMaxBandwidthIn);
        request.setInternetMaxBandwidthOut(internetMaxBandwidthOut);
        request.setInternetChargeType(internetChargeType);
        request.setInstanceName(instanceName);
        request.setInstanceType(instanceType);
        request.setIoOptimized(isOptimized);
        request.setKeyPairName(keyPairName);
        request.setPassword(password);
        request.setPasswordInherit(passwordInherit);
        request.setPeriod(period);
        request.setPeriodUnit(periodUnit);
        request.setPrivateIpAddress(privateIpAddress);
        request.setRamRoleName(ramRoleName);
        request.setSecurityEnhancementStrategy(securityEnhancementStrategy);
        request.setSecurityGroupId(securityGroupId);
        request.setSpotPriceLimit(spotPriceLimit);
        request.setSpotStrategy(spotStrategy);
        request.setSystemDiskCategory(systemDiskCategory);
        request.setSystemDiskDescription(systemDiskDescription);
        request.setSystemDiskDiskName(systemDiskName);
        request.setSystemDiskSize(systemDiskSize);
        request.setUserData(userData);
        request.setVSwitchId(vSwitchId);
        request.setZoneId(zoneId);
        setRequestTags(request, tagsKeys, tagsValues);

        // Initiate the request and handle the response or exceptions
        CreateInstanceResponse response;
        try {
            response = client.getAcsResponse(request);
            return response.getInstanceId();
        } catch (ClientException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            ProxyUtil.clearProxy();
        }
    }

    private static void setRequestTags(CreateInstanceRequest request, List<String> tagsKeys, List<String> tagsValues) {
        if (isEmpty(tagsKeys.get(0))) {
            request.setTag1Key(tagsKeys.get(0));
            request.setTag1Value(tagsValues.get(0));
        }
        if (isEmpty(tagsKeys.get(1))) {
            request.setTag1Key(tagsKeys.get(1));
            request.setTag1Value(tagsValues.get(1));
        }
        if (isEmpty(tagsKeys.get(2))) {
            request.setTag1Key(tagsKeys.get(2));
            request.setTag1Value(tagsValues.get(2));
        }
        if (isEmpty(tagsKeys.get(3))) {
            request.setTag1Key(tagsKeys.get(3));
            request.setTag1Value(tagsValues.get(3));
        }
        if (isEmpty(tagsKeys.get(4))) {
            request.setTag1Key(tagsKeys.get(4));
            request.setTag1Value(tagsValues.get(4));
        }
    }
}
