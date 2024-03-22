/*
 * Copyright 2024 Open Text
 * This program and the accompanying materials
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



package io.cloudslang.content.azure.entities;

import org.jetbrains.annotations.NotNull;

public class AzureCreateVMInputs {

    private final AzureComputeCommonInputs azureComputeCommonInputs;
    private final String diskType;
    private final String nicName;
    private final String availabilitySetName;
    private final String adminUsername;
    private final String adminPassword;
    private final String sshPublicKeyName;
    private final String vmSize;
    private final String imageVersion;
    private final String diskSizeInGB;
    private final String storageAccount;
    private final String storageAccountType;
    private final String publisher;
    private final String sku;
    private final String offer;
    private final String plan;
    private final String privateImageName;
    private final String dataDiskName;
    private final String osDiskName;
    private final String tagKeyList;
    private final String tagValueList;

    private final String nicResourceGroupName;

    @java.beans.ConstructorProperties({"azureComputeCommonInputs", "diskType", "nicName", "availabilitySetName", "adminUsername", "adminPassword", "sshPublicKeyName", "vmSize", "imageVersion", "diskSizeInGB", "storageAccount", "storageAccountType", "publisher", "sku", "offer", "plan", "privateImageName", "dataDiskName", "osDiskName", "tagKeyList", "tagValueList", "nicResourceGroupName"})
    public AzureCreateVMInputs(AzureComputeCommonInputs azureComputeCommonInputs, String diskType, String nicName, String availabilitySetName, String adminUsername, String adminPassword, String sshPublicKeyName, String vmSize, String imageVersion, String diskSizeInGB, String storageAccount, String storageAccountType, String publisher, String sku, String offer, String plan, String privateImageName, String dataDiskName, String osDiskName, String tagKeyList, String tagValueList, String nicResourceGroupName) {

        this.azureComputeCommonInputs = azureComputeCommonInputs;
        this.diskType = diskType;
        this.nicName = nicName;

        this.availabilitySetName = availabilitySetName;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.sshPublicKeyName = sshPublicKeyName;
        this.vmSize = vmSize;

        this.imageVersion = imageVersion;
        this.diskSizeInGB = diskSizeInGB;
        this.storageAccount = storageAccount;
        this.storageAccountType = storageAccountType;
        this.publisher = publisher;
        this.sku = sku;
        this.offer = offer;
        this.plan = plan;
        this.privateImageName = privateImageName;
        this.dataDiskName = dataDiskName;
        this.osDiskName = osDiskName;
        this.tagKeyList = tagKeyList;
        this.tagValueList = tagValueList;
        this.nicResourceGroupName = nicResourceGroupName;
    }

    @NotNull
    public static AzureCreateVMInputs.AzureCreateVMInputsBuilder builder() {
        return new AzureCreateVMInputs.AzureCreateVMInputsBuilder();
    }

    public AzureComputeCommonInputs getAzureComputeCommonInputs() {
        return azureComputeCommonInputs;
    }

    public String getDiskType() {
        return diskType;
    }

    public String getNicName() {
        return nicName;
    }


    public String getAvailabilitySetName() {
        return availabilitySetName;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getSshPublicKeyName() {
        return sshPublicKeyName;
    }

    public String getVmSize() {
        return vmSize;
    }


    public String getImageVersion() {
        return imageVersion;
    }

    public String getDiskSizeInGB() {
        return diskSizeInGB;
    }

    public String getStorageAccount() {
        return storageAccount;
    }

    public String getStorageAccountType() {
        return storageAccountType;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSku() {
        return sku;
    }

    public String getOffer() {
        return offer;
    }

    public String getPlan() {
        return plan;
    }

    public String getPrivateImageName() {
        return privateImageName;
    }

    public String getDataDiskName() {
        return dataDiskName;
    }

    public String getOsDiskName() {
        return osDiskName;
    }

    public String getTagKeyList() {
        return tagKeyList;
    }

    public String getTagValueList() {
        return tagValueList;
    }

    public String getNicResourceGroupName() {
        return nicResourceGroupName;
    }


    public static final class AzureCreateVMInputsBuilder {

        private AzureComputeCommonInputs azureComputeCommonInputs;
        private String diskType;
        private String nicName;
        private String availabilitySetName;
        private String adminUsername;
        private String adminPassword;
        private String sshPublicKeyName;
        private String vmSize;
        private String imageVersion;
        private String diskSizeInGB;
        private String storageAccount;
        private String storageAccountType;
        private String publisher;
        private String sku;
        private String offer;
        private String plan;
        private String privateImageName;
        private String dataDiskName;
        private String osDiskName;
        private String tagKeyList;
        private String tagValueList;

        private String nicResourceGroupName;

        private AzureCreateVMInputsBuilder() {
        }


        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder azureComputeCommonInputs(AzureComputeCommonInputs azureComputeCommonInputs) {
            this.azureComputeCommonInputs = azureComputeCommonInputs;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder diskType(String diskType) {
            this.diskType = diskType;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder nicName(String nicName) {
            this.nicName = nicName;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder availabilitySetName(String availabilitySetName) {
            this.availabilitySetName = availabilitySetName;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder adminUsername(String adminUsername) {
            this.adminUsername = adminUsername;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder adminPassword(String adminPassword) {
            this.adminPassword = adminPassword;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder sshPublicKeyName(String sshPublicKeyName) {
            this.sshPublicKeyName = sshPublicKeyName;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder vmSize(String vmSize) {
            this.vmSize = vmSize;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder imageVersion(String imageVersion) {
            this.imageVersion = imageVersion;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder diskSizeInGB(String diskSizeInGB) {
            this.diskSizeInGB = diskSizeInGB;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder storageAccount(String storageAccount) {
            this.storageAccount = storageAccount;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder storageAccountType(String storageAccountType) {
            this.storageAccountType = storageAccountType;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder sku(String sku) {
            this.sku = sku;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder offer(String offer) {
            this.offer = offer;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder plan(String plan) {
            this.plan = plan;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder privateImageName(String privateImageName) {
            this.privateImageName = privateImageName;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder dataDiskName(String dataDiskName) {
            this.dataDiskName = dataDiskName;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder osDiskName(String osDiskName) {
            this.osDiskName = osDiskName;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder tagKeyList(String tagKeyList) {
            this.tagKeyList = tagKeyList;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder tagValueList(String tagValueList) {
            this.tagValueList = tagValueList;
            return this;
        }

        @NotNull
        public AzureCreateVMInputs.AzureCreateVMInputsBuilder nicResourceGroupName(String nicResourceGroupName) {
            this.nicResourceGroupName = nicResourceGroupName;
            return this;
        }
        @NotNull
        public AzureCreateVMInputs build() {
            return new AzureCreateVMInputs(azureComputeCommonInputs, diskType, nicName, availabilitySetName, adminUsername, adminPassword, sshPublicKeyName, vmSize, imageVersion, diskSizeInGB, storageAccount, storageAccountType, publisher, sku, offer, plan, privateImageName, dataDiskName, osDiskName, tagKeyList, tagValueList,nicResourceGroupName);
        }
    }


}
