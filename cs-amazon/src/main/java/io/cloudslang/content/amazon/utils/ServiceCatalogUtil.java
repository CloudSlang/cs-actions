/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.amazon.utils;

import com.amazonaws.services.cloudformation.model.Stack;
import com.amazonaws.services.servicecatalog.model.ProvisioningParameter;
import com.amazonaws.services.servicecatalog.model.Tag;
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
                final Tag tag = new Tag();
                tag.setKey(tagLine.getKey());
                tag.setValue(tagLine.getValue());
                tags.add(tag);
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
                final ProvisioningParameter parameter = new ProvisioningParameter();
                parameter.setKey(paramLine.getKey());
                parameter.setValue(paramLine.getValue());
                parametersList.add(parameter);
            }
        }
        return parametersList;
    }

    public static Stack getStack(List<Stack> stacks) {
        return stacks.get(0);
    }
}