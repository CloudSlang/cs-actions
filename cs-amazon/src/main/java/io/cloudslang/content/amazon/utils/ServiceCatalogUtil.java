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
package io.cloudslang.content.amazon.utils;

import com.amazonaws.services.servicecatalog.model.ProvisioningParameter;
import com.amazonaws.services.servicecatalog.model.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moldovai on 20-Aug-18.
 */

public class ServiceCatalogUtil {

    public static List<Tag> toArrayOfTags(final String provisioningTags) {
        if (StringUtils.isEmpty(provisioningTags)) {
            return new ArrayList<>();
        }
        final List<Tag> tags = new ArrayList<>();
        for (String line : provisioningTags.split(StringUtils.LF)) {
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

    public static List<ProvisioningParameter> setProvisionParameters(String paramKeyName, String paramSshLocation, String paramInstanceType) {

        List<ProvisioningParameter> params = new ArrayList<>();
        if (!StringUtils.isEmpty(paramKeyName)) {
            params.add(setProvisionParameter("KeyName", paramKeyName));
        }
        if (!StringUtils.isEmpty(paramSshLocation)) {
            params.add(setProvisionParameter("SSHLocation", paramSshLocation));
        }
        if (!StringUtils.isEmpty(paramInstanceType)) {
            params.add(setProvisionParameter("InstanceType", paramInstanceType));
        }
        return params;
    }

    private static ProvisioningParameter setProvisionParameter(String key, String value) {
        ProvisioningParameter parameter = new ProvisioningParameter();
        parameter.setKey(key);
        parameter.setValue(value);
        return parameter;
    }
}