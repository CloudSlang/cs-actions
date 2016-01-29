package io.cloudslang.content.vmware.connection;

import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.helpers.GetMOREF;
import io.cloudslang.content.vmware.connection.impl.BasicConnection;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class ConnectionResources {
    private static final String DATA_CENTER = "Datacenter";
    private static final String HOST_SYSTEM = "HostSystem";
    private static final String RESOURCE_POOL = "resourcePool";
    private static final String PARENT = "parent";
    private static final String VM_FOLDER = "vmFolder";

    private BasicConnection basicConnection = new BasicConnection();

    private ManagedObjectReference serviceInstance;
    private ManagedObjectReference morRootFolder;
    private ManagedObjectReference dataCenterMor;
    private ManagedObjectReference hostMor;
    private ManagedObjectReference computeResourceMor;
    private ManagedObjectReference resourcePoolMor;
    private ManagedObjectReference vmFolderMor;
    private VimPortType vimPortType;
    private GetMOREF getMOREF;
    private Connection connection;

    public ManagedObjectReference getServiceInstance() {
        return serviceInstance;
    }

    public ManagedObjectReference getMorRootFolder() {
        return morRootFolder;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public GetMOREF getGetMOREF() {
        return getMOREF;
    }

    public void setDataCenterMor(ManagedObjectReference dataCenterMor) {
        this.dataCenterMor = dataCenterMor;
    }

    public ManagedObjectReference getHostMor() {
        return hostMor;
    }

    public void setHostMor(ManagedObjectReference hostMor) {
        this.hostMor = hostMor;
    }

    public ManagedObjectReference getComputeResourceMor() {
        return computeResourceMor;
    }

    public void setComputeResourceMor(ManagedObjectReference computeResourceMor) {
        this.computeResourceMor = computeResourceMor;
    }

    public ManagedObjectReference getResourcePoolMor() {
        return resourcePoolMor;
    }

    public void setResourcePoolMor(ManagedObjectReference resourcePoolMor) {
        this.resourcePoolMor = resourcePoolMor;
    }

    public ManagedObjectReference getVmFolderMor() {
        return vmFolderMor;
    }

    public void setVmFolderMor(ManagedObjectReference vmFolderMor) {
        this.vmFolderMor = vmFolderMor;
    }

    public VimPortType getVimPortType() {
        return vimPortType;
    }

    private Connection getVCenterConnection(HttpInputs httpInputs) throws Exception {
        String url = InputUtils.getUrlString(httpInputs);

        return basicConnection.connect(url, httpInputs.getUsername(), httpInputs.getPassword(), httpInputs.isTrustEveryone());
    }

    public ConnectionResources(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        this.connection = getVCenterConnection(httpInputs);
        this.getMOREF = new GetMOREF(connection);
        this.morRootFolder = basicConnection.getServiceContent().getRootFolder();
        this.serviceInstance = basicConnection.getServiceInstanceReference();

        setDataCenterMor(vmInputs);
        setHostMor(vmInputs);
        setComputeResourceMor();
        setResourcePoolMor();
        setVmFolderMor();

        this.vimPortType = connection.getVimPort();
    }

    private void setVmFolderMor() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference vmFolderMor = null;
        if (dataCenterMor != null) {
            vmFolderMor = (ManagedObjectReference) getMOREF.entityProps(dataCenterMor, new String[]{VM_FOLDER}).get(VM_FOLDER);
        }
        this.setVmFolderMor(vmFolderMor);
    }

    private void setResourcePoolMor() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference resourcePoolMor = null;
        if (computeResourceMor != null) {
            resourcePoolMor = (ManagedObjectReference) getMOREF
                    .entityProps(computeResourceMor, new String[]{RESOURCE_POOL}).get(RESOURCE_POOL);
        }
        this.setResourcePoolMor(resourcePoolMor);
    }

    private void setComputeResourceMor() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference computeResourceMor = null;
        if (hostMor != null) {
            computeResourceMor = getComputeResourceMor(getMOREF, hostMor);
        }
        this.setComputeResourceMor(computeResourceMor);
    }

    private void setHostMor(VmInputs vmInputs) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference hostMor = null;
        if (dataCenterMor != null) {
            hostMor = getHostMor(vmInputs.getHostname(), getMOREF, dataCenterMor);
        }
        this.setHostMor(hostMor);
    }

    private void setDataCenterMor(VmInputs vmInputs) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference dataCenterMor = null;
        if (StringUtils.isNotBlank(vmInputs.getDataCenterName())) {
            dataCenterMor = getDataCenterMor(vmInputs.getDataCenterName(), morRootFolder, getMOREF);
        }
        this.setDataCenterMor(dataCenterMor);
    }

    private ManagedObjectReference getDataCenterMor(String dataCenterName, ManagedObjectReference mor, GetMOREF getMOREF)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference dataCenterMor = getMOREF.inContainerByType(mor, DATA_CENTER).get(dataCenterName);
        if (dataCenterMor == null) {
            throw new RuntimeException("Datacenter [" + dataCenterName + "] not found.");
        }

        return dataCenterMor;
    }

    private ManagedObjectReference getHostMor(String hostname, GetMOREF getMOREF, ManagedObjectReference dataCenterMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference hostMor = getMOREF.inContainerByType(dataCenterMor, HOST_SYSTEM).get(hostname);
        if (hostMor == null) {
            throw new RuntimeException("Host [" + hostname + "] not found.");
        }

        return hostMor;
    }

    private ManagedObjectReference getComputeResourceMor(GetMOREF getMOREF, ManagedObjectReference hostMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference computeResourceMor = (ManagedObjectReference) getMOREF
                .entityProps(hostMor, new String[]{PARENT}).get(PARENT);

        if (computeResourceMor == null) {
            throw new RuntimeException(ErrorMessages.COMPUTE_RESOURCE_NOT_FOUND_ON_HOST);
        }

        return computeResourceMor;
    }
}
