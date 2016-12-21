package io.cloudslang.content.amazon.entities.inputs;

/**
 * Created by TusaM
 * 12/21/2016.
 */
public class StorageInputs {
    private final String bucketName;

    public String getBucketName() {
        return bucketName;
    }

    private StorageInputs(StorageInputs.Builder builder) {
        this.bucketName = builder.bucketName;
    }

    public static class Builder {
        private String bucketName;

        public StorageInputs build() {
            return new StorageInputs(this);
        }

        public StorageInputs.Builder withBucketName(String inputValue) {
            bucketName = inputValue;
            return this;
        }
    }
}