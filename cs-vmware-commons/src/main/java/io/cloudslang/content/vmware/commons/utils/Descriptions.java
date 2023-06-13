package io.cloudslang.content.vmware.commons.utils;

public class Descriptions {
    public static final String RETURN_CODE_DESC =  "The returnCode of the operation: 0 for success, -1 for failure.";
    public static final String RETURN_RESULT_DESC ="VM id resulted from the task in case of success or the error message in case of failure";
    public static final String EXCEPTION_DESC = "In case of failure response, this result contains the java stack trace of the runtime exception or fault details that the remote server generated throughout its communication with the client.";
    public static final String SUCCESS_DESC = "The operation completed successfully.";
    public static final String FAILURE_DESC ="Something went wrong.";
    public static final String HOST_DESC="VMware vCenter, ESX or ESXi host hostname or IP.";
    public static final String USERNAME_DESC ="VMware username.";
    public static final String PASSWORD_DESC ="VMware user's password.";
    public static final String VM_FOLDER_DESC ="The ID of the VIRTUAL_MACHINE folder to which you want to add your virtual machine.";
    public static final String VM_SOURCE_DESC ="Primary Virtual Machine identifier of the virtual machine to clone from";
    public static final String VM_RESOURCE_POOL_DESC ="The ID of the resource pool to which you want to add your virtual machine.";
    public static final String DATASTORE_DESC ="The ID of datastore or datastore cluster to store new virtual machine";
    public static final String CLUSTER_DESC ="The ID of the cluster to which you want to add your virtual machine.";
    public static final String VM_IDENTIFIER_TYPE_DESC ="";
    public static final String DESCRIPTION_DESC ="Description / annotation. To be able to clone the machine, even when no description input is given, the user needs to have the following permission to the virtual machine: Virtual machine -> Configuration -> Set annotation.";
}
