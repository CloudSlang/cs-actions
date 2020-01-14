/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.hashicorp.terraform.entities;


import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.CommonInputs.ORGANIZATION_NAME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ListOAuthClientInputs {

    private TerraformCommonInputs commonInputs;


    @java.beans.ConstructorProperties({ORGANIZATION_NAME})
    public ListOAuthClientInputs(TerraformCommonInputs commonInputs) {
        this.commonInputs = commonInputs;

    }

    public static ListOAuthClientInputsBuilder builder() {
        return new ListOAuthClientInputsBuilder();
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class ListOAuthClientInputsBuilder {
        private String organizationName = EMPTY;
        private TerraformCommonInputs commonInputs;

        ListOAuthClientInputsBuilder() {
        }

        @NotNull
        public ListOAuthClientInputs.ListOAuthClientInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public ListOAuthClientInputs build() {
            return new ListOAuthClientInputs(commonInputs);
        }
    }


}
