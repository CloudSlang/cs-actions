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

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EQUAL;

/**
 * Created by moldovai on 20-Aug-18.
 */

public class ServiceCatalogUtil {
    private final static int SPLIT_LIMIT = 2;
    private final static int KEY_IDX = 0;
    private final static int VAL_IDX = 1;
    private final String lineOfText;
    public String object = "key = key \n value : value";


    public ServiceCatalogUtil(String line) {
        lineOfText = line;
    }

    public static List<ProvisioningParameter> toArrayOfProvisioningParameters(final String provisioningParameters) {
        if (StringUtils.isEmpty(provisioningParameters)) {
            return new ArrayList<>();
        }

        final List<ProvisioningParameter> param = new ArrayList<>();
        for (String line : provisioningParameters.split(StringUtils.LF)) {
            final ServiceCatalogUtil parameterLine = new ServiceCatalogUtil(line);
            if (parameterLine.isValid()) {
                final ProvisioningParameter parameter = new ProvisioningParameter();
                parameter.setKey(parameterLine.getKey());
                parameter.setValue(parameterLine.getValue());
                param.add(parameter);
            }
        }
        return param;

    }

    public static List<Tag> toArrayOfTags(final String provisioningTags) {
        if (StringUtils.isEmpty(provisioningTags)) {
            return new ArrayList<>();
        }

        final List<Tag> tags = new ArrayList<>();
        for (String line : provisioningTags.split(StringUtils.LF)) {
            final ServiceCatalogUtil tagLine = new ServiceCatalogUtil(line);
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

    public boolean isValid() {
        if (StringUtils.isEmpty(lineOfText)) {
            return false;
        }

        if (lineOfText.contains("=")) {

            String keyValueArr[] = lineOfText.split(EQUAL, SPLIT_LIMIT);

            return keyValueArr.length >= SPLIT_LIMIT &&
                    (!StringUtils.isEmpty(keyValueArr[KEY_IDX])) &&
                    (!StringUtils.isEmpty(keyValueArr[VAL_IDX]));
        }
        return false;
    }

    public String getKey() {
        if (StringUtils.isEmpty(lineOfText)) {
            return StringUtils.EMPTY;
        }
        return lineOfText.split(EQUAL, SPLIT_LIMIT)[KEY_IDX];
    }

    public String getValue() {
        if (StringUtils.isEmpty(lineOfText)) {
            return StringUtils.EMPTY;
        }

        return lineOfText.split(EQUAL, SPLIT_LIMIT)[VAL_IDX];
    }

}
