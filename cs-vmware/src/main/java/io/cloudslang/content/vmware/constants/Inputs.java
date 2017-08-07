/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.constants;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class Inputs {
    public static final String VMWARE_GLOBAL_SESSION_OBJECT = "VMWareGlobalSessionObject";

    // Http client specific inputs
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String PROTOCOL = "protocol";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TRUST_EVERYONE = "trustEveryone";
    public static final String CLOSE_SESSION = "closeSession";

    // VM specific inputs
    public static final String DATA_CENTER_NAME = "dataCenterName";
    public static final String HOSTNAME = "hostname";
    public static final String HOST_GROUP_NAME = "hostGroupName";
    public static final String HOST_LIST = "hostList";
    public static final String VM_NAME = "virtualMachineName";
    public static final String VM_ID = "virtualMachineId";
    public static final String VM_GROUP_NAME = "vmGroupName";
    public static final String VM_LIST = "vmList";
    public static final String VM_DESCRIPTION = "description";
    public static final String DATA_STORE = "dataStore";
    public static final String VM_CPU_COUNT = "numCPUs";
    public static final String VM_DISK_SIZE = "vmDiskSize";
    public static final String VM_MEMORY_SIZE = "vmMemorySize";
    public static final String GUEST_OS_ID = "guestOsId";
    public static final String DEVICE = "device";
    public static final String OPERATION = "operation";
    public static final String UPDATE_VALUE = "updateValue";
    public static final String VM_DISK_MODE = "vmDiskMode";
    public static final String FOLDER_NAME = "folderName";
    public static final String RESOURCE_POOL = "resourcePool";
    public static final String CLONE_NAME = "cloneName";
    public static final String THICK_PROVISION = "thickProvision";
    public static final String IS_TEMPLATE = "isTemplate";
    public static final String CPU_NUM = "cpuNum";
    public static final String CORES_PER_SOCKET = "coresPerSocket";
    public static final String MEMORY = "memory";
    public static final String CLONE_DESCRIPTION = "cloneDescription";
    public static final String CLONE_RESOURCE_POOL = "cloneResourcePool";
    public static final String CLONE_HOST = "cloneHost";
    public static final String CLONE_DATA_STORE = "cloneDataStore";
    public static final String RULE_NAME = "ruleName";
    public static final String AFFINE_HOST_GROUP_NAME = "affineHostGroupName";
    public static final String ANTI_AFFINE_HOST_GROUP_NAME = "antiAffineHostGroupName";
    public static final String SNAPSHOT_NAME = "snapshotName";
    public static final String SNAPSHOT_DESCRIPTION = "snapshotDescription";
    public static final String MEMORY_DUMP = "memoryDump";
    public static final String QUIESCE = "quiesce";

    // Guest specific inputs
    public static final String REBOOT_OPTION = "rebootOption";
    public static final String COMPUTER_NAME = "computerName";
    public static final String COMPUTER_PASSWORD = "computerPassword";
    public static final String OWNER_NAME = "ownerName";
    public static final String OWNER_ORGANIZATION = "ownerOrganization";
    public static final String PRODUCT_KEY = "productKey";
    public static final String DOMAIN_PASSWORD = "adminPassword";
    public static final String DOMAIN_USERNAME = "domainUsername";
    public static final String DOMAIN = "domain";
    public static final String WORKGROUP = "workgroup";
    public static final String LICENSE_DATA_MODE = "licenseDataMode";
    public static final String AUTO_LOGON = "autoLogon";
    public static final String DELETE_ACCOUNTS = "deleteAccounts";
    public static final String CHANGE_SID = "changeSID";
    public static final String AUTO_LOGON_COUNT = "autoLogonCount";
    public static final String AUTO_USERS = "autoUsers";
    public static final String DNS_SERVER = "dnsServer";
    public static final String IP_ADDRESS = "ipAddress";
    public static final String SUBNET_MASK = "subnetMask";
    public static final String DEFAULT_GATEWAY = "defaultGateway";
    public static final String MAC_ADDRESS = "macAddress";
    public static final String TIME_ZONE = "timeZone";
    public static final String UTC_CLOCK = "hwClockUTC";

    //Cluster specific inputs
    public static final String CLUSTER_NAME = "clusterName";
    public static final String RESTART_PRIORITY = "restartPriority";

    // Miscellaneous
    public static final String DELIMITER = "delimiter";

    //template upload inputs
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String DATACENTER = "datacenter";
    public static final String VM_FOLDER = "vmFolder";
    public static final String DISK_PROVISIONING = "diskProvisioning";
    public static final String OVF_NETWORK_JS = "ovfNetworkJS";
    public static final String NET_PORT_GROUP_JS = "netPortGroupJS";
    public static final String OVF_PROP_KEY_JS = "ovfPropKeyJS";
    public static final String OVF_PROP_VALUE_JS = "ovfPropValueJS";
    public static final String IP_PROTOCOL = "ipProtocol";
    public static final String IP_ALLOC_SCHEME = "ipAllocScheme";
    public static final String LOCALE_LANG = "localeLang";
    public static final String LOCALE_COUNTRY = "localeCountry";
    public static final String PARALLEL = "parallel";
}
