package io.cloudslang.content.jclouds.entities.inputs;

/**
 * Created by Mihai Tusa.
 * 9/15/2016.
 */
public class IamInputs {
    private String iamInstanceProfileArn;
    private String iamInstanceProfileName;
    private String keyPairName;

    private IamInputs(IamInputs.IamInputsBuilder builder) {
        this.iamInstanceProfileArn = builder.iamInstanceProfileArn;
        this.iamInstanceProfileName = builder.iamInstanceProfileName;
        this.keyPairName = builder.keyPairName;
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

    public static class IamInputsBuilder {
        private String iamInstanceProfileArn;
        private String iamInstanceProfileName;
        private String keyPairName;

        public IamInputs build() {
            return new IamInputs(this);
        }

        public IamInputs.IamInputsBuilder withIamInstanceProfileArn(String inputValue) {
            iamInstanceProfileArn = inputValue;
            return this;
        }

        public IamInputs.IamInputsBuilder withIamInstanceProfileName(String inputValue) {
            iamInstanceProfileName = inputValue;
            return this;
        }

        public IamInputs.IamInputsBuilder withKeyPairName (String inputValue) {
            keyPairName = inputValue;
            return this;
        }
    }
}