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
package io.cloudslang.content.utilities.entities;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class Base64EncoderInputs {

    private final String filePath;

    @java.beans.ConstructorProperties({"setFilePath"})
    public Base64EncoderInputs(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Get method for filePath
     *
     * @return - filePath
     */
    public String getFilePath() {
        return filePath;
    }

    public static class Base64EncoderInputsBuilder {
        public String filePath = StringUtils.EMPTY;

        /**
         * This method makes the instance of the builder to be accessible
         *
         * @param builderFunction - object of type Base64EncoderInputsBuilder which is passed to the accept method
         * @return - instance
         */
        public Base64EncoderInputsBuilder with(Consumer<Base64EncoderInputsBuilder> builderFunction) {
            builderFunction.accept(this);
            return this;
        }

        /**
         * This method builds the required param value
         *
         * @return - the inputs value
         */
        public Base64EncoderInputs buildInputs() {
            return new Base64EncoderInputs(filePath);
        }
    }
}
