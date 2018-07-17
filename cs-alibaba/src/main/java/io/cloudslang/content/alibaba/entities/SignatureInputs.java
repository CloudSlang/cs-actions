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
package io.cloudslang.content.alibaba.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SignatureInputs {



    private  final String signatureNonce;
    private  final String timestamp;
    private  final String signatureMethod;
    private  final String signatureVersion;
    private  final String signature;


    @java.beans.ConstructorProperties({"signatureNonce", "timestamp", "signatureMethod", "signatureVersion", "signature", })
    private SignatureInputs(final String signatureNonce,final String timestamp,final String signatureMethod,  final String signatureVersion, final String signature){
        this.signatureNonce=signatureNonce;
        this.timestamp=timestamp;
        this.signatureMethod=signatureMethod;
        this.signatureVersion=signatureVersion;
        this.signature=signature;

    }
    @NotNull
    public static SignatureInputs.SignatureInputsBuilder builder() {
        return new SignatureInputs.SignatureInputsBuilder();
    }
    @NotNull
    public String getSignatureNonce() {
        return signatureNonce;
    }
    @NotNull
    public String getTimestamp() {
        return timestamp;
    }
    @NotNull
    public String getSignatureMethod() {
        return signatureMethod;
    }
    @NotNull
    public String getSignatureVersion() {
        return signatureVersion;
    }
    @NotNull
    public String getSignature() {
        return signature;
    }


    public static class SignatureInputsBuilder {


        private String signatureNonce = EMPTY;
        private String timestamp = EMPTY;
        private String signatureMethod = EMPTY;
        private String signatureVersion = EMPTY;
        private String signature = EMPTY;

        SignatureInputsBuilder() {
        }

        @NotNull
        public SignatureInputs.SignatureInputsBuilder signatureNonce(@NotNull final String signatureNonce) {
            this.signatureNonce = signatureNonce;
            return this;
        }

        @NotNull
        public SignatureInputs.SignatureInputsBuilder timestamp(@NotNull final String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @NotNull
        public SignatureInputs.SignatureInputsBuilder signatureMethod(@NotNull final String signatureMethod) {
            this.signatureMethod = signatureMethod;
            return this;
        }

        @NotNull
        public SignatureInputs.SignatureInputsBuilder signatureVersion(@NotNull final String signatureVersion) {
            this.signatureVersion = signatureVersion;
            return this;
        }

        @NotNull
        public SignatureInputs.SignatureInputsBuilder signature(@NotNull final String signature) {
            this.signature = signature;
            return this;
        }


        @NotNull
        public SignatureInputs build() {
            return new SignatureInputs(signatureNonce, timestamp, signatureMethod, signatureVersion, signature);
        }

        @Override
        public String toString() {
            return String.format("SignatureInputsBuilder(signatureNonce=%s, timestamp=%s, signatureMethod=%s, signatureVersion=%s," +
                            " signature=%s)",
                    this.signatureNonce, this.timestamp, this.signatureMethod, this.signatureVersion, this.signature);

        }
    }





}
