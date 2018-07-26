/*
 * (c) Copyright 2018 Micro Focus
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.alibaba.services;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest.DataDisk;
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
                                        final Boolean passwordInherit,
                                        final String isOptimized,
                                        final String systemDiskCategory,
                                        final Integer systemDiskSize,
                                        final String systemDiskName,
                                        final String systemDiskDescription,
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
                                        final IAcsClient client) throws RuntimeException {
        // Instantiate Create Instance request
        final CreateInstanceRequest createInstanceRequest = new CreateInstanceRequest();
        createInstanceRequest.setRegionId(regionId);
        createInstanceRequest.setAutoRenew(autoRenew);
        createInstanceRequest.setAutoRenewPeriod(autoRenewPeriod);
        createInstanceRequest.setClientToken(clientToken);
        createInstanceRequest.setClusterId(clusterId);
        createInstanceRequest.setDeploymentSetId(deploymentSetId);
        createInstanceRequest.setDescription(description);
        createInstanceRequest.setHostName(hostname);
        createInstanceRequest.setHpcClusterId(hpcClusterId);
        createInstanceRequest.setImageId(imageId);
        createInstanceRequest.setInstanceChargeType(instanceChargeType);
        createInstanceRequest.setInternetMaxBandwidthIn(internetMaxBandwidthIn);
        createInstanceRequest.setInternetMaxBandwidthOut(internetMaxBandwidthOut);
        createInstanceRequest.setInternetChargeType(internetChargeType);
        createInstanceRequest.setInstanceName(instanceName);
        createInstanceRequest.setInstanceType(instanceType);
        createInstanceRequest.setIoOptimized(isOptimized);
        createInstanceRequest.setKeyPairName(keyPairName);
        createInstanceRequest.setPassword(password);
        createInstanceRequest.setPasswordInherit(passwordInherit);
        createInstanceRequest.setPeriod(period);
        createInstanceRequest.setPeriodUnit(periodUnit);
        createInstanceRequest.setPrivateIpAddress(privateIpAddress);
        createInstanceRequest.setRamRoleName(ramRoleName);
        createInstanceRequest.setSecurityEnhancementStrategy(securityEnhancementStrategy);
        createInstanceRequest.setSecurityGroupId(securityGroupId);
        createInstanceRequest.setSpotPriceLimit(spotPriceLimit);
        createInstanceRequest.setSpotStrategy(spotStrategy);
        createInstanceRequest.setSystemDiskCategory(systemDiskCategory);
        createInstanceRequest.setSystemDiskDescription(systemDiskDescription);
        createInstanceRequest.setSystemDiskDiskName(systemDiskName);
        createInstanceRequest.setSystemDiskSize(systemDiskSize);
        createInstanceRequest.setUserData(userData);
        createInstanceRequest.setVSwitchId(vSwitchId);
        createInstanceRequest.setZoneId(zoneId);

        // Initiate the request and handle the response or exceptions
        return ((CreateInstanceResponse) getResponse(proxyHost, proxyPort, proxyUsername, proxyPassword,
                client, createInstanceRequest)).getInstanceId();
    }

    public static String deleteInstance(final String proxyHost,
                                        final String proxyPort,
                                        final String proxyUsername,
                                        final String proxyPassword,
                                        final String instanceId,
                                        final IAcsClient client) throws RuntimeException {
        // Instantiate Delete Instance request
        final DeleteInstanceRequest deleteInstanceRequest = new DeleteInstanceRequest();
        deleteInstanceRequest.setInstanceId(instanceId);

        // Initiate the request and handle the response or exceptions
        return ((DeleteInstanceResponse) getResponse(proxyHost, proxyPort, proxyUsername, proxyPassword,
                client, deleteInstanceRequest)).getRequestId();
    }

    public static String allocatePublicIpAddress(final String proxyHost,
                                                 final String proxyPort,
                                                 final String proxyUsername,
                                                 final String proxyPassword,
                                                 final String instanceId,
                                                 final IAcsClient client) throws RuntimeException {
        // Instantiate Allocate Public Ip Address request
        final AllocatePublicIpAddressRequest allocatePublicIpAddressRequest = new AllocatePublicIpAddressRequest();
        allocatePublicIpAddressRequest.setInstanceId(instanceId);

        // Initiate the request and handle the response or exceptions
        return ((AllocatePublicIpAddressResponse) getResponse(proxyHost, proxyPort, proxyUsername, proxyPassword,
                client, allocatePublicIpAddressRequest)).getIpAddress();
    }

    public static String getInstanceStatus(final String proxyHost,
                                           final String proxyPort,
                                           final String proxyUsername,
                                           final String proxyPassword,
                                           final String instanceId,
                                           final String regionId,
                                           final IAcsClient client) throws RuntimeException {
        // Instantiate getInstanceStatus request
        String status = "";
        final DescribeInstanceStatusRequest describeInstanceStatusRequest = new DescribeInstanceStatusRequest();
        describeInstanceStatusRequest.setRegionId(regionId);

        // Initiate the request and handle the response or exceptions
        List<DescribeInstanceStatusResponse.InstanceStatus> instanceStatuses = ((DescribeInstanceStatusResponse) getResponse(proxyHost, proxyPort, proxyUsername, proxyPassword,
                client, describeInstanceStatusRequest)).getInstanceStatuses();
        for (DescribeInstanceStatusResponse.InstanceStatus instanceStatus : instanceStatuses) {
            if ((instanceStatus.getInstanceId()).equalsIgnoreCase(instanceId)) {
                status = instanceStatus.getStatus();
                break;
            }
        }

        return status;
    }

    public static String startInstance(final String proxyHost,
                                       final String proxyPort,
                                       final String proxyUsername,
                                       final String proxyPassword,
                                       final String instanceId,
                                       final Boolean initLocalDisk,
                                       final IAcsClient client) throws RuntimeException {
        // Instantiate Start Instance request
        final StartInstanceRequest startInstanceRequest = new StartInstanceRequest();
        startInstanceRequest.setInstanceId(instanceId);
        startInstanceRequest.setInitLocalDisk(initLocalDisk);

        // Initiate the request and handle the response or exceptions
        return ((StartInstanceResponse) getResponse(proxyHost, proxyPort, proxyUsername, proxyPassword,
                client, startInstanceRequest)).getRequestId();
    }

    public static String stopInstance(final String proxyHost,
                                      final String proxyPort,
                                      final String proxyUsername,
                                      final String proxyPassword,
                                      final String instanceId,
                                      final Boolean forceStop,
                                      final Boolean confirmStop,
                                      final String stoppedMode,
                                      final IAcsClient client) throws RuntimeException {
        // Instantiate Stop Instance request
        final StopInstanceRequest stopInstanceRequest = new StopInstanceRequest();
        stopInstanceRequest.setInstanceId(instanceId);
        stopInstanceRequest.setForceStop(forceStop);
        stopInstanceRequest.setConfirmStop(confirmStop);
        stopInstanceRequest.setStoppedMode(stoppedMode);


        // Initiate the request and handle the response or exceptions
        return ((StopInstanceResponse) getResponse(proxyHost, proxyPort, proxyUsername, proxyPassword,
                client, stopInstanceRequest)).getRequestId();
    }

    public static String restartInstance(final String proxyHost,
                                         final String proxyPort,
                                         final String proxyUsername,
                                         final String proxyPassword,
                                         final String instanceId,
                                         final Boolean forceStop,
                                         final IAcsClient client) throws RuntimeException {
        // Instantiate Restart Instance request
        final RebootInstanceRequest rebootInstanceRequest = new RebootInstanceRequest();
        rebootInstanceRequest.setInstanceId(instanceId);
        rebootInstanceRequest.setForceStop(forceStop);

        // Initiate the request and handle the response or exceptions
        return ((RebootInstanceResponse) getResponse(proxyHost, proxyPort, proxyUsername, proxyPassword,
                client, rebootInstanceRequest)).getRequestId();
    }

    private static AcsResponse getResponse(final String proxyHost,
                                           final String proxyPort,
                                           final String proxyUsername,
                                           final String proxyPassword,
                                           final IAcsClient client,
                                           final AcsRequest request) {


        try {
            if (!isEmpty(proxyHost)) {
                // Set JVM proxies during runtime
                ProxyUtil.setProxies(proxyHost, proxyPort, proxyUsername, proxyPassword);
                //Thread.sleep(30000);
            }
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (!isEmpty(proxyHost)) {
                //Clear proxies
                ProxyUtil.clearProxy();
            }
        }
    }

    private static void setRequestTags(CreateInstanceRequest request, List<String> tagsKeys, List<String> tagsValues) {
        if (isEmpty(tagsKeys.get(0))) {
            request.setTag1Key(tagsKeys.get(0));
            request.setTag1Value(tagsValues.get(0));
        }
        if (isEmpty(tagsKeys.get(1))) {
            request.setTag2Key(tagsKeys.get(1));
            request.setTag2Value(tagsValues.get(1));
        }
        if (isEmpty(tagsKeys.get(2))) {
            request.setTag3Key(tagsKeys.get(2));
            request.setTag3Value(tagsValues.get(2));
        }
        if (isEmpty(tagsKeys.get(3))) {
            request.setTag3Key(tagsKeys.get(3));
            request.setTag3Value(tagsValues.get(3));
        }
        if (isEmpty(tagsKeys.get(4))) {
            request.setTag4Key(tagsKeys.get(4));
            request.setTag4Value(tagsValues.get(4));
        }
    }
}
