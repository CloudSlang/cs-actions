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



package io.cloudslang.content.amazon.entities.inputs;

/**
 * Created by Mihai Tusa.
 * 9/15/2016.
 */
public class IamInputs {
    private final String iamInstanceProfileArn;
    private final String iamInstanceProfileName;
    private final String keyPairName;
    private final String securityGroupIdsString;
    private final String securityGroupNamesString;

    private IamInputs(Builder builder) {
        this.iamInstanceProfileArn = builder.iamInstanceProfileArn;
        this.iamInstanceProfileName = builder.iamInstanceProfileName;
        this.keyPairName = builder.keyPairName;
        this.securityGroupIdsString = builder.securityGroupIdsString;
        this.securityGroupNamesString = builder.securityGroupNamesString;
    }

    public String getIamInstanceProfileArn() {
        return iamInstanceProfileArn;
    }

    public String getIamInstanceProfileName() {
        return iamInstanceProfileName;
    }

    public String getKeyPairName() {
        return keyPairName;
    }

    public String getSecurityGroupIdsString() {
        return securityGroupIdsString;
    }

    public String getSecurityGroupNamesString() {
        return securityGroupNamesString;
    }

    public static class Builder {
        private String iamInstanceProfileArn;
        private String iamInstanceProfileName;
        private String keyPairName;
        private String securityGroupIdsString;
        private String securityGroupNamesString;

        public IamInputs build() {
            return new IamInputs(this);
        }

        public Builder withIamInstanceProfileArn(String inputValue) {
            iamInstanceProfileArn = inputValue;
            return this;
        }

        public Builder withIamInstanceProfileName(String inputValue) {
            iamInstanceProfileName = inputValue;
            return this;
        }

        public Builder withKeyPairName(String inputValue) {
            keyPairName = inputValue;
            return this;
        }

        public Builder withSecurityGroupIdsString(String inputValue) {
            securityGroupIdsString = inputValue;
            return this;
        }

        public Builder withSecurityGroupNamesString(String inputValue) {
            securityGroupNamesString = inputValue;
            return this;
        }
    }
}
