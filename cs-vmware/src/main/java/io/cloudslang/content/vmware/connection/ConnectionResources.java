package io.cloudslang.content.vmware.connection;

import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.helpers.MoRefHandler;
import io.cloudslang.content.vmware.connection.impl.BasicConnection;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.VmParameter;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class ConnectionResources {
    private static final String RESOURCE_POOL = "resourcePool";

    private BasicConnection basicConnection = new BasicConnection();

    private Connection connection;
    private MoRefHandler moRefHandler;
    private VimPortType vimPortType;
    private ManagedObjectReference serviceInstance;
    private ManagedObjectReference morRootFolder;
    private ManagedObjectReference dataCenterMor;
    private ManagedObjectReference hostMor;
    private ManagedObjectReference computeResourceMor;
    private ManagedObjectReference resourcePoolMor;
    private ManagedObjectReference vmFolderMor;

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

    public MoRefHandler getMoRefHandler() {
        return moRefHandler;
    }

    public ManagedObjectReference getHostMor() {
        return hostMor;
    }

    public ManagedObjectReference getComputeResourceMor() {
        return computeResourceMor;
    }

    public ManagedObjectReference getResourcePoolMor() {
        return resourcePoolMor;
    }

    public ManagedObjectReference getVmFolderMor() {
        return vmFolderMor;
    }

    public VimPortType getVimPortType() {
        return vimPortType;
    }

    private void setComputeResourceMor(ManagedObjectReference computeResourceMor) {
        this.computeResourceMor = computeResourceMor;
    }

    private void setResourcePoolMor(ManagedObjectReference resourcePoolMor) {
        this.resourcePoolMor = resourcePoolMor;
    }

    private void setDataCenterMor(ManagedObjectReference dataCenterMor) {
        this.dataCenterMor = dataCenterMor;
    }

    private void setVmFolderMor(ManagedObjectReference vmFolderMor) {
        this.vmFolderMor = vmFolderMor;
    }

    private void setHostMor(ManagedObjectReference hostMor) {
        this.hostMor = hostMor;
    }

    private Connection getVCenterConnection(HttpInputs httpInputs) throws Exception {
        String url = InputUtils.getUrlString(httpInputs);

        return basicConnection.connect(url, httpInputs.getUsername(), httpInputs.getPassword(), httpInputs.isTrustEveryone());
    }

    public ConnectionResources(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        this.connection = getVCenterConnection(httpInputs);
        this.moRefHandler = new MoRefHandler(connection);
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
            vmFolderMor = (ManagedObjectReference) moRefHandler.entityProps(dataCenterMor,
                    new String[]{VmParameter.VM_FOLDER.getValue()}).get(VmParameter.VM_FOLDER.getValue());
        }
        this.setVmFolderMor(vmFolderMor);
    }

    private void setResourcePoolMor() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference resourcePoolMor = null;
        if (computeResourceMor != null) {
            resourcePoolMor = (ManagedObjectReference) moRefHandler
                    .entityProps(computeResourceMor, new String[]{RESOURCE_POOL}).get(RESOURCE_POOL);
        }
        this.setResourcePoolMor(resourcePoolMor);
    }

    private void setComputeResourceMor() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference computeResourceMor = null;
        if (hostMor != null) {
            computeResourceMor = getComputeResourceMor(moRefHandler, hostMor);
        }
        this.setComputeResourceMor(computeResourceMor);
    }

    private void setHostMor(VmInputs vmInputs) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference hostMor = null;
        if (dataCenterMor != null) {
            hostMor = getHostMor(vmInputs.getHostname(), moRefHandler, dataCenterMor);
        }
        this.setHostMor(hostMor);
    }

    private void setDataCenterMor(VmInputs vmInputs) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference dataCenterMor = null;
        if (StringUtils.isNotBlank(vmInputs.getDataCenterName())) {
            dataCenterMor = getDataCenterMor(vmInputs.getDataCenterName(), morRootFolder, moRefHandler);
        }
        this.setDataCenterMor(dataCenterMor);
    }

    private ManagedObjectReference getDataCenterMor(String dataCenterName, ManagedObjectReference mor, MoRefHandler moRefHandler)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference dataCenterMor = moRefHandler.inContainerByType(mor, VmParameter.DATA_CENTER.getValue())
                .get(dataCenterName);
        if (dataCenterMor == null) {
            throw new RuntimeException("Datacenter [" + dataCenterName + "] not found.");
        }

        return dataCenterMor;
    }

    private ManagedObjectReference getHostMor(String hostname, MoRefHandler moRefHandler, ManagedObjectReference dataCenterMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference hostMor = moRefHandler.inContainerByType(dataCenterMor, VmParameter.HOST_SYSTEM.getValue())
                .get(hostname);
        if (hostMor == null) {
            throw new RuntimeException("Host [" + hostname + "] not found.");
        }

        return hostMor;
    }

    private ManagedObjectReference getComputeResourceMor(MoRefHandler moRefHandler, ManagedObjectReference hostMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference computeResourceMor = (ManagedObjectReference) moRefHandler
                .entityProps(hostMor, new String[]{VmParameter.PARENT.getValue()}).get(VmParameter.PARENT.getValue());

        if (computeResourceMor == null) {
            throw new RuntimeException(ErrorMessages.COMPUTE_RESOURCE_NOT_FOUND_ON_HOST);
        }

        return computeResourceMor;
    }
}
