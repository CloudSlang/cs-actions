package io.cloudslang.content.vmware.connection;

import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.impl.BasicConnection;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.utils.InputUtils;
import io.cloudslang.content.vmware.connection.helpers.GetMOREF;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class ConnectionResources {

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

    public void setServiceInstance(ManagedObjectReference serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public ManagedObjectReference getMorRootFolder() {
        return morRootFolder;
    }

    public void setMorRootFolder(ManagedObjectReference morRootFolder) {
        this.morRootFolder = morRootFolder;
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

    public void setGetMOREF(GetMOREF getMOREF) {
        this.getMOREF = getMOREF;
    }

    public ManagedObjectReference getDataCenterMor() {
        return dataCenterMor;
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

    public void setVimPortType(VimPortType vimPortType) {
        this.vimPortType = vimPortType;
    }

    private Connection getVCenterConnection(HttpInputs httpInputs) throws Exception {
        String url = InputUtils.getUrlString(httpInputs);

        return basicConnection.connect(url, httpInputs.getUsername(), httpInputs.getPassword(), httpInputs.isTrustEveryone());
    }

    public ConnectionResources (HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        connection = getVCenterConnection(httpInputs);
        getMOREF = new GetMOREF(connection);
        morRootFolder = basicConnection.getServiceContent().getRootFolder();
        serviceInstance = basicConnection.getServiceInstanceReference();

        ManagedObjectReference dataCenterMor = null;
        if(StringUtils.isNotBlank(vmInputs.getDataCenterName())){
            dataCenterMor = getdataCenterMor(vmInputs.getDataCenterName(), morRootFolder, getMOREF);
        }

        ManagedObjectReference hostMor = null;
        if(dataCenterMor != null){
            hostMor = getHostMor(vmInputs.getHostname(), getMOREF, dataCenterMor);
        }

        ManagedObjectReference computeResourceMor = null;
        if(hostMor != null){
            computeResourceMor = getComputeResourceMor(getMOREF, hostMor);
        }

        ManagedObjectReference resourcePoolMor = null;
        if(computeResourceMor != null){
            resourcePoolMor = (ManagedObjectReference) getMOREF
                    .entityProps(computeResourceMor, new String[]{Constants.RESOURCE_POOL})
                    .get(Constants.RESOURCE_POOL);
        }

        ManagedObjectReference vmFolderMor = null;
        if(dataCenterMor != null){
            vmFolderMor = (ManagedObjectReference) getMOREF
                    .entityProps(dataCenterMor, new String[]{Constants.VM_FOLDER})
                    .get(Constants.VM_FOLDER);
        }

        this.setDataCenterMor(dataCenterMor);
        this.setHostMor(hostMor);
        this.setComputeResourceMor(computeResourceMor);
        this.setResourcePoolMor(resourcePoolMor);
        this.setVmFolderMor(vmFolderMor);
        this.setVimPortType(connection.getVimPort());
        this.setGetMOREF(getMOREF);
        this.setConnection(connection);
        this.setMorRootFolder(morRootFolder);
        this.setServiceInstance(serviceInstance);
    }

    private ManagedObjectReference getdataCenterMor(String dataCenterName, ManagedObjectReference mor, GetMOREF getMOREF)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference dataCenterMor = getMOREF.inContainerByType(mor, Constants.DATA_CENTER).get(dataCenterName);
        if (dataCenterMor == null) {
            throw new RuntimeException("Datacenter " + dataCenterName + " not found.");
        }

        return dataCenterMor;
    }

    private ManagedObjectReference getHostMor(String hostname, GetMOREF getMOREF, ManagedObjectReference dataCenterMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference hostMor = getMOREF.inContainerByType(dataCenterMor, Constants.HOST_SYSTEM).get(hostname);
        if (hostMor == null) {
            throw new RuntimeException("Host " + hostname + " not found");
        }

        return hostMor;
    }

    private ManagedObjectReference getComputeResourceMor(GetMOREF getMOREF, ManagedObjectReference hostMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference computeResourceMor = (ManagedObjectReference) getMOREF
                .entityProps(hostMor, new String[]{Constants.PARENT})
                .get(Constants.PARENT);

        if (computeResourceMor == null) {
            throw new RuntimeException(ErrorMessages.COMPUTE_RESOURCE_NOT_FOUND_ON_HOST);
        }

        return computeResourceMor;
    }
}
