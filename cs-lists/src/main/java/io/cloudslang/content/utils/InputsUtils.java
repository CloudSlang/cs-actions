/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Created by moldovas on 7/13/2016.
 */
public class InputsUtils {
    public static String getInputDefaultValue(String input, String defaultValue) {
        return (StringUtils.isEmpty(input)) ? defaultValue : input;
    }

    public static Boolean toBoolean(String value, Boolean defaultValue, String inputName) throws Exception {
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        switch (value.toLowerCase(Locale.ENGLISH)) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                throw new Exception(String.format(Constants.INPUT_NOT_BOOLEAN, inputName));
        }
    }
}
