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



package io.cloudslang.content.azure.entities.models.compute;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateVMRequestBody {

    String id;
    String name;
    String type;
    String location;
    Map<String, String> tags;

    CreateVMRequestBody.Plan plan;

    CreateVMRequestBody.Properties Properties;

    @JsonCreator
    public CreateVMRequestBody() {
    }

    public CreateVMRequestBody.Properties getProperties() {
        return Properties;
    }

    public void setProperties(CreateVMRequestBody.Properties properties) {
        Properties = properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public static class Properties {
        CreateVMRequestBody.Properties.availabilitySet availabilitySet;
        CreateVMRequestBody.Properties.hardwareProfile hardwareProfile;
        CreateVMRequestBody.Properties.storageProfile storageProfile;
        CreateVMRequestBody.Properties.osProfile osProfile;
        CreateVMRequestBody.Properties.networkProfile networkProfile;

        public Properties.availabilitySet getAvailabilitySet() {
            return availabilitySet;
        }

        public void setAvailabilitySet(Properties.availabilitySet availabilitySet) {
            this.availabilitySet = availabilitySet;
        }

        public Properties.hardwareProfile getHardwareProfile() {
            return hardwareProfile;
        }

        public void setHardwareProfile(Properties.hardwareProfile hardwareProfile) {
            this.hardwareProfile = hardwareProfile;
        }

        public Properties.storageProfile getStorageProfile() {
            return storageProfile;
        }

        public void setStorageProfile(Properties.storageProfile storageProfile) {
            this.storageProfile = storageProfile;
        }

        public Properties.osProfile getOsProfile() {
            return osProfile;
        }

        public void setOsProfile(Properties.osProfile osProfile) {
            this.osProfile = osProfile;
        }

        public Properties.networkProfile getNetworkProfile() {
            return networkProfile;
        }

        public void setNetworkProfile(Properties.networkProfile networkProfile) {
            this.networkProfile = networkProfile;
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public static class osProfile {
            String computerName;
            String adminUsername;
            String adminPassword;
            CreateVMRequestBody.Properties.osProfile.linuxConfiguration linuxConfiguration;

            public String getComputerName() {
                return computerName;
            }

            public void setComputerName(String computerName) {
                this.computerName = computerName;
            }

            public String getAdminUsername() {
                return adminUsername;
            }

            public void setAdminUsername(String adminUsername) {
                this.adminUsername = adminUsername;
            }

            public String getAdminPassword() {
                return adminPassword;
            }

            public void setAdminPassword(String adminPassword) {
                this.adminPassword = adminPassword;
            }

            public Properties.osProfile.linuxConfiguration getLinuxConfiguration() {
                return linuxConfiguration;
            }

            public void setLinuxConfiguration(Properties.osProfile.linuxConfiguration linuxConfiguration) {
                this.linuxConfiguration = linuxConfiguration;
            }

            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            public static class linuxConfiguration {
                boolean disablePasswordAuthentication;
                CreateVMRequestBody.Properties.osProfile.linuxConfiguration.ssh ssh;

                public boolean getDisablePasswordAuthentication() {
                    return disablePasswordAuthentication;
                }

                public void setDisablePasswordAuthentication(boolean disablePasswordAuthentication) {
                    this.disablePasswordAuthentication = disablePasswordAuthentication;
                }

                public Properties.osProfile.linuxConfiguration.ssh getSsh() {
                    return ssh;
                }

                public void setSsh(Properties.osProfile.linuxConfiguration.ssh ssh) {
                    this.ssh = ssh;
                }

                @JsonInclude(JsonInclude.Include.NON_DEFAULT)
                public static class ssh {
                    CreateVMRequestBody.Properties.osProfile.linuxConfiguration.ssh.publicKeys publicKeys[];

                    public Properties.osProfile.linuxConfiguration.ssh.publicKeys[] getPublicKeys() {
                        return publicKeys;
                    }

                    public void setPublicKeys(Properties.osProfile.linuxConfiguration.ssh.publicKeys[] publicKeys) {
                        this.publicKeys = publicKeys;
                    }

                    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
                    public static class publicKeys {
                        String path;
                        String keyData;

                        public String getPath() {
                            return path;
                        }

                        public void setPath(String path) {
                            this.path = path;
                        }

                        public String getKeyData() {
                            return keyData;
                        }

                        public void setKeyData(String keyData) {
                            this.keyData = keyData;
                        }
                    }

                }

            }
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public static class networkProfile {
            CreateVMRequestBody.Properties.networkProfile.networkInterfaces[] networkInterfaces;

            public Properties.networkProfile.networkInterfaces[] getNetworkInterfaces() {
                return networkInterfaces;
            }

            public void setNetworkInterfaces(Properties.networkProfile.networkInterfaces[] networkInterfaces) {
                this.networkInterfaces = networkInterfaces;
            }

            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            public static class networkInterfaces {
                String id;
                CreateVMRequestBody.Properties.networkProfile.networkInterfaces.properties properties;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public Properties.networkProfile.networkInterfaces.properties getProperties() {
                    return properties;
                }

                public void setProperties(Properties.networkProfile.networkInterfaces.properties properties) {
                    this.properties = properties;
                }

                public static class properties {
                    boolean primary;

                    public boolean isPrimary() {
                        return primary;
                    }

                    public void setPrimary(boolean primary) {
                        this.primary = primary;
                    }
                }

            }

        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public static class availabilitySet {
            String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public static class hardwareProfile {
            String vmSize;

            public String getVmSize() {
                return vmSize;
            }

            public void setVmSize(String vmSize) {
                this.vmSize = vmSize;
            }
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public static class storageProfile {
            CreateVMRequestBody.Properties.storageProfile.imageReference imageReference;
            CreateVMRequestBody.Properties.storageProfile.osDisk osDisk;
            CreateVMRequestBody.Properties.storageProfile.dataDisks dataDisks[];

            public Properties.storageProfile.imageReference getImageReference() {
                return imageReference;
            }

            public void setImageReference(Properties.storageProfile.imageReference imageReference) {
                this.imageReference = imageReference;
            }

            public Properties.storageProfile.osDisk getOsDisk() {
                return osDisk;
            }

            public void setOsDisk(Properties.storageProfile.osDisk osDisk) {
                this.osDisk = osDisk;
            }

            public Properties.storageProfile.dataDisks[] getDataDisks() {
                return dataDisks;
            }

            public void setDataDisks(Properties.storageProfile.dataDisks[] dataDisks) {
                this.dataDisks = dataDisks;
            }

            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            public static class dataDisks {
                String name;
                int diskSizeGB;
                @JsonInclude()
                int lun;
                String createOption;
                CreateVMRequestBody.Properties.storageProfile.dataDisks.managedDisk managedDisk;
                CreateVMRequestBody.Properties.storageProfile.dataDisks.vhd vhd;

                public CreateVMRequestBody.Properties.storageProfile.dataDisks.vhd getVhd() {
                    return vhd;
                }

                public void setVhd(CreateVMRequestBody.Properties.storageProfile.dataDisks.vhd vhd) {
                    this.vhd = vhd;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getDiskSizeGB() {
                    return diskSizeGB;
                }

                public void setDiskSizeGB(int diskSizeGB) {
                    this.diskSizeGB = diskSizeGB;
                }

                public int getLun() {
                    return lun;
                }

                public void setLun(int lun) {
                    this.lun = lun;
                }

                public String getCreateOption() {
                    return createOption;
                }

                public void setCreateOption(String createOption) {
                    this.createOption = createOption;
                }

                public Properties.storageProfile.dataDisks.managedDisk getManagedDisk() {
                    return managedDisk;
                }

                public void setManagedDisk(Properties.storageProfile.dataDisks.managedDisk managedDisk) {
                    this.managedDisk = managedDisk;
                }

                @JsonInclude(JsonInclude.Include.NON_DEFAULT)
                public static class vhd {
                    String uri;

                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                    }
                }

                @JsonInclude(JsonInclude.Include.NON_DEFAULT)
                public static class managedDisk {
                    String storageAccountType;

                    public String getStorageAccountType() {
                        return storageAccountType;
                    }

                    public void setStorageAccountType(String storageAccountType) {
                        this.storageAccountType = storageAccountType;
                    }
                }
            }

            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            public static class imageReference {
                String publisher;
                String offer;
                String sku;
                String version;
                String id;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getPublisher() {
                    return publisher;
                }

                public void setPublisher(String publisher) {
                    this.publisher = publisher;
                }

                public String getOffer() {
                    return offer;
                }

                public void setOffer(String offer) {
                    this.offer = offer;
                }

                public String getSku() {
                    return sku;
                }

                public void setSku(String sku) {
                    this.sku = sku;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }
            }

            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            public static class osDisk {
                String caching;
                String name;
                String createOption;
                CreateVMRequestBody.Properties.storageProfile.osDisk.vhd vhd;

                public CreateVMRequestBody.Properties.storageProfile.osDisk.vhd getVhd() {
                    return vhd;
                }

                public void setVhd(CreateVMRequestBody.Properties.storageProfile.osDisk.vhd vhd) {
                    this.vhd = vhd;
                }

                CreateVMRequestBody.Properties.storageProfile.osDisk.managedDisk managedDisk;

                public String getCaching() {
                    return caching;
                }

                public void setCaching(String caching) {
                    this.caching = caching;
                }

                public Properties.storageProfile.osDisk.managedDisk getManagedDisk() {
                    return managedDisk;
                }

                public void setManagedDisk(Properties.storageProfile.osDisk.managedDisk managedDisk) {
                    this.managedDisk = managedDisk;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCreateOption() {
                    return createOption;
                }

                public void setCreateOption(String createOption) {
                    this.createOption = createOption;
                }

                @JsonInclude(JsonInclude.Include.NON_DEFAULT)
                public static class vhd {
                    String uri;

                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                    }
                }

                @JsonInclude(JsonInclude.Include.NON_DEFAULT)
                public static class managedDisk {
                    String storageAccountType;

                    public String getStorageAccountType() {
                        return storageAccountType;
                    }

                    public void setStorageAccountType(String storageAccountType) {
                        this.storageAccountType = storageAccountType;
                    }
                }

            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Plan {
        String name;
        String product;
        String publisher;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }
    }


}
