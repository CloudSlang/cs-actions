/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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
/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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



package io.cloudslang.content.amazon.utils;

import software.amazon.awssdk.services.cloudformation.model.Stack;
import software.amazon.awssdk.services.servicecatalog.model.ProvisioningParameter;
import software.amazon.awssdk.services.servicecatalog.model.Tag;
import software.amazon.awssdk.services.servicecatalog.model.UpdateProvisioningParameter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moldovai on 20-Aug-18.
 */

public class ServiceCatalogUtil {

    public static List<Tag> toArrayOfTags(final String provisioningTags, final String delimiter) {
        if (StringUtils.isEmpty(provisioningTags)) {
            return new ArrayList<>();
        }
        final List<Tag> tags = new ArrayList<>();
        for (String line : provisioningTags.split(delimiter)) {
            final ParametersLine tagLine = new ParametersLine(line);
            if (tagLine.isValid()) {
                tags.add(Tag.builder().key(tagLine.getKey()).value(tagLine.getValue()).build());
            }
        }
        return tags;
    }

    public static List<ProvisioningParameter> toArrayOfParameters(final String parameters, final String delimiter) {
        if (StringUtils.isEmpty(parameters)) {
            return new ArrayList<>();
        }

        final List<ProvisioningParameter> parametersList = new ArrayList<>();
        for (String line : parameters.split(delimiter)) {
            final ParametersLine paramLine = new ParametersLine(line);
            if (paramLine.isValid()) {
                parametersList.add(ProvisioningParameter.builder()
                        .key(paramLine.getKey()).value(paramLine.getValue()).build());
            }
        }
        return parametersList;
    }

    public static List<UpdateProvisioningParameter> toArrayOfUpdateParameters(final String updateParameters, final String delimiter, final boolean usePreviousValue) {
        if (StringUtils.isEmpty(updateParameters)) {
            return new ArrayList<>();
        }
        final List<UpdateProvisioningParameter> updateParametersList = new ArrayList<>();
        for (String line : updateParameters.split(delimiter)) {
            final ParametersLine paramLine = new ParametersLine(line);
            if (paramLine.isValid()) {
                updateParametersList.add(UpdateProvisioningParameter.builder()
                        .key(paramLine.getKey()).value(paramLine.getValue())
                        .usePreviousValue(usePreviousValue).build());
            }
        }
        return updateParametersList;
    }

    public static Stack getStack(List<Stack> stacks) {
        return stacks.get(0);
    }
}