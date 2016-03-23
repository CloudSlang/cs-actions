package io.cloudslang.content.vmware.constants;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class Inputs {
    // Http client specific inputs
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String PROTOCOL = "protocol";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TRUST_EVERYONE = "trustEveryone";

    // VM specific inputs
    public static final String DATA_CENTER_NAME = "dataCenterName";
    public static final String HOSTNAME = "hostname";
    public static final String VM_NAME = "virtualMachineName";
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

    // Guest specific inputs
    public static final String REBOOT_OPTION = "rebootOption";
    public static final String COMPUTER_NAME = "computerName";
    public static final String COMPUTER_PASSWORD = "computerPassword";
    public static final String OWNER_ORGANIZATION = "ownerOrganization";
    public static final String PRODUCT_KEY = "productKey";
    public static final String DOMAIN_PASSWORD = "adminPassword";
    public static final String DOMAIN_USERNAME = "domainUsername";
    public static final String DOMAIN = "domain";
    public static final String WORKGROUP = "workgroup";
    public static final String LICENSE_DATA_MODE = "licenseDataMode";
    public static final String AUTO_LOGON = "autoLogon";
    public static final String PLAIN_TEXT = "plainText";
    public static final String DELETE_ACCOUNTS = "deleteAccounts";
    public static final String CHANGE_SID = "changeSID";
    public static final String AUTO_LOGON_COUNT = "autoLogonCount";
    public static final String AUTO_USERS = "autoUsers";
    public static final String DNS_SERVER = "dnsServer";

    // Miscellaneous
    public static final String DELIMITER = "delimiter";
}
