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

public class ParametersLine {
    private String parametersLine;

    public ParametersLine(String line) {
        parametersLine = line;
    }

    public boolean isValid() {
        if (parametersLine == null || parametersLine.isEmpty()) {
            return false;
        }

        String keyValueArr[] = parametersLine.split("=", 2);
        if ((keyValueArr == null) ||                 //can't split
                (keyValueArr.length < 2) ||          //can't split
                (keyValueArr[0].trim().isEmpty()) || //empty key
                (keyValueArr[1].trim().isEmpty()) ){ //empty value
            return false;
        }
        return true;
    }

    public String getKey() {
        return parametersLine.split("=", 2)[0];
    }

    public String getValue() {
        return parametersLine.split("=", 2)[1];
    }
}
