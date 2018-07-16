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
package io.cloudslang.content.alibaba.actions.instances;

import com.aliyuncs.IAcsClient;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.alibaba.utils.ClientUtil;
import io.cloudslang.content.alibaba.utils.Validator;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.alibaba.services.InstanceService.createInstance;
import static io.cloudslang.content.alibaba.utils.ListUtil.getDataDiskList;
import static io.cloudslang.content.alibaba.utils.constants.Commons.COMMA;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.*;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.CreateInstance.*;
import static io.cloudslang.content.alibaba.utils.constants.ExceptionMessages.CREATE_INSTANCE_EXCEPTION;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.*;
import static io.cloudslang.content.alibaba.utils.constants.Outputs.*;
import static io.cloudslang.content.alibaba.utils.constants.Outputs.INSTANCE_ID;
import static io.cloudslang.content.alibaba.utils.constants.SuccessMessages.CREATE_INSTANCE_SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.Boolean.valueOf;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CreateInstance {

    @Action(name = "Create Instance",
            description = CREATE_INSTANCE_DESC,
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = INSTANCE_ID, description = INSTANCE_ID_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            }
    )
    public Map<String, String> execute(@Param(value = ACCESS_KEY_ID, required = true, description = ACCESS_KEY_ID_DESC) final String accessKeyId,
                                       @Param(value = ACCESS_KEY_SECRET, required = true, encrypted = true, description = ACCESS_KEY_SECRET_ID_DESC) final String accessKeySecret,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) final String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) final String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) final String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) final String proxyPassword,
                                       @Param(value = REGION_ID, required = true, description = REGION_ID_DESC) final String regionId,
                                       @Param(value = IMAGE_ID, required = true, description = IMAGE_ID_DESC) final String imageId,
                                       @Param(value = INSTANCE_TYPE, required = true, description = INSTANCE_TYPE_DESC) final String instanceType,
                                       @Param(value = SECURITY_GROUP_ID, required = true, description = SECURITY_GROUP_ID_DESC) final String securityGroupId,
                                       @Param(value = ZONE_ID, description = ZONE_ID_DESC) final String zoneId,
                                       @Param(value = INSTANCE_NAME, description = INSTANCE_NAME_DESC) final String instanceName,
                                       @Param(value = DESCRIPTION, description = DESCRIPTION_DESC) final String description,
                                       @Param(value = INTERNET_CHARGE_TYPE, description = INTERNET_CHARGE_TYPE_DESC) final String internetChargeType,
                                       @Param(value = INTERNET_MAX_BANDWIDTH_IN, description = INTERNET_MAX_BANDWIDTH_IN_DESC) final String internetMaxBandwidthIn,
                                       @Param(value = INTERNET_MAX_BANDWIDTH_OUT, description = INTERNET_MAX_BANDWIDTH_OUT_DESC) final String internetMaxBandwidthOut,
                                       @Param(value = HOSTNAME, description = HOSTNAME_DESC) final String hostname,
                                       @Param(value = PASSWORD, encrypted = true, description = PASSWORD_DESC) final String password,
                                       @Param(value = PASSWORD_INHERIT, description = PASSWORD_INHERIT_DESC) final String passwordInherit,
                                       @Param(value = IS_OPTIMIZED, description = IS_OPTIMIZED_DESC) final String isOptimized,
                                       @Param(value = SYSTEM_DISK_CATEGORY, description = SYSTEM_DISK_CATEGORY_DESC) final String systemDiskCategory,
                                       @Param(value = SYSTEM_DISK_SIZE, description = SYSTEM_DISK_SIZE_DESC) final String systemDiskSize,
                                       @Param(value = SYSTEM_DISK_NAME, description = SYSTEM_DISK_NAME_DESC) final String systemDiskName,
                                       @Param(value = SYSTEM_DISK_DESCRIPTION, description = SYSTEM_DISK_DESCRIPTION_DESC) final String systemDiskDescription,
                                       @Param(value = DELIMITER, description = DELIMITER_DESC) final String delimiter,
                                       @Param(value = DATA_DISKS_SIZE_LIST, description = DATA_DISKS_SIZE_LIST_DESC) final String dataDisksSizeList,
                                       @Param(value = DATA_DISKS_CATEGORY_LIST, description = DATA_DISKS_CATEGORY_LIST_DESC) final String dataDisksCategoryList,
                                       @Param(value = DATA_DISKS_ENCRYPTED_LIST, description = DATA_DISKS_ENCRYPTED_LIST_DESC) final String dataDisksEncryptedList,
                                       @Param(value = DATA_DISKS_SNAPSHOT_ID_LIST, description = DATA_DISKS_SNAPSHOT_ID_LIST_DESC) final String dataDisksSnapshotIdList,
                                       @Param(value = DATA_DISKS_DISK_NAME_LIST, description = DATA_DISKS_DISK_NAME_LIST_DESC) final String dataDisksDiskNameList,
                                       @Param(value = DATA_DISKS_DESCRIPTION_LIST, description = DATA_DISKS_DESCRIPTION_LIST_DESC) final String dataDisksDescriptionList,
                                       @Param(value = DATA_DISKS_DELETE_WITH_INSTANCE_LIST, description = DATA_DISKS_DELETE_WITH_INSTANCE_LIST_DESC) final String dataDisksDeleteWithInstanceList,
                                       @Param(value = CLUSTER_ID, description = CLUSTER_ID_DESC) final String clusterId,
                                       @Param(value = HPC_CLUSTER_ID, description = HPC_CLUSTER_ID_DESC) final String hpcClusterId,
                                       @Param(value = V_SWITCH_ID, description = V_SWITCH_ID_DESC) final String vSwitchId,
                                       @Param(value = PRIVATE_IP_ADDRESS, description = PRIVATE_IP_ADDRESS_DESC) final String privateIpAddress,
                                       @Param(value = INSTANCE_CHARGE_TYPE, description = INSTANCE_CHARGE_TYPE_DESC) final String instanceChargeType,
                                       @Param(value = SPOT_STRATEGY, description = SPOT_STRATEGY_DESC) final String spotStrategy,
                                       @Param(value = SPOT_PRICE_LIMIT, description = SPOT_PRICE_LIMIT_DESC) final String spotPriceLimit,
                                       @Param(value = PERIOD, description = PERIOD_DESC) final String period,
                                       @Param(value = PERIOD_UNIT, description = PERIOD_UNIT_DESC) final String periodUnit,
                                       @Param(value = AUTO_RENEW, description = AUTO_RENEW_DESC) final String autoRenew,
                                       @Param(value = AUTO_RENEW_PERIOD, description = AUTO_RENEW_PERIOD_DESC) final String autoRenewPeriod,
                                       @Param(value = USER_DATA, description = USER_DATA_DESC) final String userData,
                                       @Param(value = CLIENT_TOKEN, description = CLIENT_TOKEN_DESC) final String clientToken,
                                       @Param(value = KEY_PAIR_NAME, description = KEY_PAIR_NAME_DESC) final String keyPairName,
                                       @Param(value = DEPLOYMENT_SET_ID, description = DEPLOYMENT_SET_ID_DESC) final String deploymentSetId,
                                       @Param(value = RAM_ROLE_NAME, description = RAM_ROLE_NAME_DESC) final String ramRoleName,
                                       @Param(value = SECURITY_ENHANCEMENT_STRATEGY, description = SECURITY_ENHANCEMENT_STRATEGY_DESC) final String securityEnhancementStrategy,
                                       @Param(value = TAGS_KEY_LIST, description = TAGS_KEY_LIST_DESC) final String tagsKeyList,
                                       @Param(value = TAGS_VALUE_LIST, description = TAGS_VALUE_LIST_DESC) final String tagsValueList) {
        //Get default values and String conversions
        final String proxyHostImp = defaultIfEmpty(proxyHost, EMPTY);
        final String proxyPortImp = defaultIfEmpty(proxyPort, EMPTY);
        final String proxyUsernameImp = defaultIfEmpty(proxyUsername, EMPTY);
        final String proxyPasswordImp = defaultIfEmpty(proxyPassword, EMPTY);
        final String delimiterImp = defaultIfEmpty(delimiter, COMMA);

        //Validate Inputs
        Validator validator = new Validator()
                .validatePort(proxyPort, PROXY_PORT)
                .validateBoolean(passwordInherit, PASSWORD_INHERIT)
                .validateBoolean(autoRenew, AUTO_RENEW)
                .validateInt(systemDiskSize, SYSTEM_DISK_SIZE)
                .validateIntList(dataDisksSizeList, DATA_DISKS_SIZE_LIST, delimiterImp)
                .validateBooleanList(dataDisksEncryptedList, DATA_DISKS_ENCRYPTED_LIST, delimiterImp)
                .validateBooleanList(dataDisksDeleteWithInstanceList, DATA_DISKS_DELETE_WITH_INSTANCE_LIST, delimiterImp)
                .validateInt(period, PERIOD)
                .validateInt(internetMaxBandwidthIn, INTERNET_MAX_BANDWIDTH_IN)
                .validateInt(internetMaxBandwidthOut, INTERNET_MAX_BANDWIDTH_OUT)
                .validateInt(systemDiskSize, SYSTEM_DISK_SIZE)
                .validateInt(autoRenewPeriod, AUTO_RENEW_PERIOD)
                .validateFloat(spotPriceLimit, SPOT_PRICE_LIMIT);

        final List<String> dataDisksSizeListImp = asList(dataDisksSizeList.split(delimiterImp));
        final List<String> dataDisksCategoryListImp = asList(dataDisksCategoryList.split(delimiterImp));
        final List<String> dataDisksSnapshotIdListImp = asList(dataDisksSnapshotIdList.split(delimiterImp));
        final List<String> dataDisksNameListImp = asList(dataDisksDiskNameList.split(delimiterImp));
        final List<String> dataDisksDescriptionListImp = asList(dataDisksDescriptionList.split(delimiterImp));
        final List<String> dataDisksDeleteWithInstanceListImp = asList(dataDisksDeleteWithInstanceList.split(delimiterImp));
        final List<String> dataDisksEncryptedListImp = asList(dataDisksEncryptedList.split(delimiterImp));
        final List<String> tagsKeyListImp = asList(tagsKeyList.split(delimiterImp));
        final List<String> tagsValueListImp = asList(tagsValueList.split(delimiterImp));

        validator
                .validateSameLengthSizeBound(16, dataDisksSizeListImp, dataDisksCategoryListImp, dataDisksSnapshotIdListImp, dataDisksNameListImp, dataDisksDescriptionListImp, dataDisksDeleteWithInstanceListImp, dataDisksEncryptedListImp)
                .validateSameLengthSizeBound(5, tagsKeyListImp, tagsValueListImp);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        //Other type conversions
        final Boolean passwordInheritImp = valueOf(passwordInherit);
        final Boolean autoRenewImp = valueOf(autoRenew);
        final Float spotPriceLimitImp = Float.valueOf(spotPriceLimit);
        final Integer internetMaxBandwidthInImp = Integer.valueOf(internetMaxBandwidthIn);
        final Integer internetMaxBandwidthOutImp = Integer.valueOf(internetMaxBandwidthOut);
        final Integer systemDiskSizeImp = Integer.valueOf(systemDiskSize);
        final Integer periodImp = Integer.valueOf(period);
        final Integer autoRenewPeriodImp = Integer.valueOf(autoRenewPeriod);

        try {
            final IAcsClient client = ClientUtil.getClient(regionId, accessKeyId, accessKeySecret);
            final String requestId = createInstance(proxyHostImp, proxyPortImp, proxyUsernameImp, proxyPasswordImp, regionId, imageId, instanceType, securityGroupId, zoneId, instanceName, description, internetChargeType, internetMaxBandwidthInImp, internetMaxBandwidthOutImp, hostname, password, passwordInheritImp, isOptimized, systemDiskCategory, systemDiskSizeImp, systemDiskName, systemDiskDescription, getDataDiskList(dataDisksSizeListImp, dataDisksCategoryListImp, dataDisksSnapshotIdListImp, dataDisksNameListImp, dataDisksDescriptionListImp, dataDisksDeleteWithInstanceListImp, dataDisksEncryptedListImp), clusterId, hpcClusterId, vSwitchId, privateIpAddress, instanceChargeType, spotStrategy, spotPriceLimitImp, periodImp, periodUnit, autoRenewImp, autoRenewPeriodImp, userData, clientToken, keyPairName, deploymentSetId, ramRoleName, securityEnhancementStrategy, tagsKeyListImp, tagsValueListImp, client);

            final Map<String, String> resultMap = getSuccessResultsMap(CREATE_INSTANCE_SUCCESS);
            resultMap.put(REQUEST_ID, requestId);

            return resultMap;
        } catch (Exception e) {
            return getFailureResultsMap(CREATE_INSTANCE_EXCEPTION, e);
        }
    }
}
