/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.entities.validators.VolumesFilterValidator;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.*;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.VolumeInputs.VOLUME_IDS_STRING;
import static io.cloudslang.content.amazon.factory.helpers.FilterUtils.getFiltersQueryMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.*;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class VolumeUtils {
    private static final String AVAILABILITY_ZONE = "AvailabilityZone";
    private static final String DEVICE = "Device";
    private static final String KMS_KEY_ID = "KmsKeyId";
    private static final String SIZE = "Size";
    private static final String NEXT_TOKEN = "NextToken";
    private static final String MAX_RESULTS = "MaxResults";
    public static final String VOLUME_ID_FORMAT = "VolumeId.%s";

    public Map<String, String> getAttachVolumeQueryParamsMap(InputsWrapper wrapper) {
        return getAttachOrDetachVolumeCommonQueryParamsMap(wrapper);
    }

    public Map<String, String> getCreateVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone());

        String volumeType = NOT_RELEVANT.equals(wrapper.getCustomInputs().getVolumeType()) ? STANDARD : wrapper.getCustomInputs().getVolumeType();
        queryParamsMap.put(VOLUME_TYPE, volumeType);

        setOptionalMapEntry(queryParamsMap, KMS_KEY_ID, wrapper.getCustomInputs().getKmsKeyId(), isNotBlank(wrapper.getCustomInputs().getKmsKeyId()));
        setOptionalMapEntry(queryParamsMap, SIZE, wrapper.getVolumeInputs().getSize(), isNotBlank(wrapper.getVolumeInputs().getSize()));
        setOptionalMapEntry(queryParamsMap, SNAPSHOT_ID, wrapper.getVolumeInputs().getSnapshotId(), isNotBlank(wrapper.getVolumeInputs().getSnapshotId()));
        setOptionalMapEntry(queryParamsMap, ENCRYPTED, String.valueOf(ONE), wrapper.getVolumeInputs().isEncrypted());
        setOptionalMapEntry(queryParamsMap, IOPS, wrapper.getVolumeInputs().getIops(), !NOT_RELEVANT.equals(wrapper.getVolumeInputs().getIops()));

        return queryParamsMap;
    }

    public Map<String, String> getDetachVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = getAttachOrDetachVolumeCommonQueryParamsMap(wrapper);
        setOptionalMapEntry(queryParamsMap, FORCE, String.valueOf(wrapper.getVolumeInputs().isForce()), wrapper.getVolumeInputs().isForce());

        return queryParamsMap;
    }

    public Map<String, String> getDeleteVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(VOLUME_ID, wrapper.getCustomInputs().getVolumeId());

        return queryParamsMap;
    }

    @NotNull
    public Map<String, String> getDescribeVolumesQueryParamsMap(@NotNull final InputsWrapper wrapper) {
        final Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        setOptionalMapEntry(queryParamsMap, MAX_RESULTS, wrapper.getVolumeInputs().getMaxResults(),
                !NOT_RELEVANT.equalsIgnoreCase(wrapper.getVolumeInputs().getMaxResults()));
        setOptionalMapEntry(queryParamsMap, NEXT_TOKEN, wrapper.getVolumeInputs().getNextToken(),
                isNotBlank(wrapper.getVolumeInputs().getNextToken()));

        final VolumesFilterValidator volumesFilterValidator = new VolumesFilterValidator();

        final Map<String, String> filterQueryMap = getFiltersQueryMap(wrapper.getFilterInputs(), volumesFilterValidator);
        queryParamsMap.putAll(filterQueryMap);

        final Map<String, String> volumedIdsQueryMap = getVolumedIdsQueryMap(wrapper);
        queryParamsMap.putAll(volumedIdsQueryMap);

        return queryParamsMap;
    }

    @NotNull
    private Map<String, String> getVolumedIdsQueryMap(@NotNull final InputsWrapper wrapper) {
        final String[] volumeIds = getArrayWithoutDuplicateEntries(wrapper.getVolumeInputs().getVolumeIdsString(),
                VOLUME_IDS_STRING, wrapper.getCommonInputs().getDelimiter());
        final Map<String, String> volumeIdsQueryMap = new HashMap<>();
        if (isNotEmpty(volumeIds)) {
            final List<String> volumeIdsList = Arrays.asList(volumeIds);
            for (final String id : volumeIdsList) {
                final String key = String.format(VOLUME_ID_FORMAT, volumeIdsList.indexOf(id) + 1);
                setOptionalMapEntry(volumeIdsQueryMap, key, id, StringUtils.isNotEmpty(id));
            }
        }
        return volumeIdsQueryMap;
    }

    private Map<String, String> getAttachOrDetachVolumeCommonQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DEVICE, wrapper.getVolumeInputs().getDeviceName());
        queryParamsMap.put(INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(VOLUME_ID, wrapper.getCustomInputs().getVolumeId());

        return queryParamsMap;
    }

}
