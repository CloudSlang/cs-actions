/*
 * Copyright 2019-2024 Open Text
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


package io.cloudslang.content.vmware.connection;

import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.helpers.MoRefHandler;
import io.cloudslang.content.vmware.connection.impl.BasicConnection;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.ManagedObjectType;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import static io.cloudslang.content.vmware.utils.ConnectionUtils.computeConnectionContextKey;
import static io.cloudslang.content.vmware.utils.ConnectionUtils.getBasicConnectionFromContext;


/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class ConnectionResources {
    private static final String RESOURCE_POOL = "resourcePool";

    private final BasicConnection basicConnection;

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

    public ConnectionResources(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        this(httpInputs);

        setDataCenterMor(vmInputs);
        setHostMor(vmInputs);
        setComputeResourceMor();
        setResourcePoolMor();
        setVmFolderMor();
    }

    public ConnectionResources(HttpInputs httpInputs) throws Exception {
        final String connectionContextKey = computeConnectionContextKey(httpInputs.getProtocol(), httpInputs.getHost(), httpInputs.getPort(), httpInputs.getUsername());
        basicConnection = getBasicConnectionFromContext(httpInputs.getGlobalSessionObject(), connectionContextKey);

        this.connection = getVCenterConnection(httpInputs);
        this.moRefHandler = new MoRefHandler(connection);
        this.morRootFolder = basicConnection.getServiceContent().getRootFolder();
        this.serviceInstance = basicConnection.getServiceInstanceReference();
        this.vimPortType = connection.getVimPort();
    }


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

    public ManagedObjectReference getResourcePoolMor() {
        return resourcePoolMor;
    }

    private void setResourcePoolMor(ManagedObjectReference resourcePoolMor) {
        this.resourcePoolMor = resourcePoolMor;
    }

    public ManagedObjectReference getVmFolderMor() {
        return vmFolderMor;
    }

    private void setVmFolderMor(ManagedObjectReference vmFolderMor) {
        this.vmFolderMor = vmFolderMor;
    }

    public VimPortType getVimPortType() {
        return vimPortType;
    }

    private Connection getVCenterConnection(HttpInputs httpInputs) throws Exception {
        String url = InputUtils.getUrlString(httpInputs);
        if (basicConnection.isConnected()) {
            return basicConnection;
        }
        return basicConnection.connect(url, httpInputs.getUsername(), httpInputs.getPassword(), httpInputs.isTrustEveryone());
    }

    private void setVmFolderMor() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference vmFolderMor = null;
        if (dataCenterMor != null) {
            vmFolderMor = (ManagedObjectReference) moRefHandler.entityProps(dataCenterMor,
                    new String[]{ManagedObjectType.VM_FOLDER.getValue()}).get(ManagedObjectType.VM_FOLDER.getValue());
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

    private void setHostMor(ManagedObjectReference hostMor) {
        this.hostMor = hostMor;
    }

    private void setDataCenterMor(ManagedObjectReference dataCenterMor) {
        this.dataCenterMor = dataCenterMor;
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

        ManagedObjectReference dataCenterMor = moRefHandler.inContainerByType(mor, ManagedObjectType.DATA_CENTER.getValue())
                .get(dataCenterName);
        if (dataCenterMor == null) {
            throw new RuntimeException("Datacenter [" + dataCenterName + "] not found.");
        }

        return dataCenterMor;
    }

    public ManagedObjectReference getHostMor() {
        return hostMor;
    }

    private void setHostMor(VmInputs vmInputs) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference hostMor = null;
        if (dataCenterMor != null) {
            hostMor = getHostMor(vmInputs.getHostname(), moRefHandler, dataCenterMor);
        }
        this.setHostMor(hostMor);
    }

    private ManagedObjectReference getHostMor(String hostname, MoRefHandler moRefHandler, ManagedObjectReference dataCenterMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference hostMor = moRefHandler.inContainerByType(dataCenterMor, ManagedObjectType.HOST_SYSTEM.getValue())
                .get(hostname);
        if (hostMor == null) {
            throw new RuntimeException("Host [" + hostname + "] not found.");
        }

        return hostMor;
    }

    public ManagedObjectReference getComputeResourceMor() {
        return computeResourceMor;
    }

    private void setComputeResourceMor(ManagedObjectReference computeResourceMor) {
        this.computeResourceMor = computeResourceMor;
    }

    private ManagedObjectReference getComputeResourceMor(MoRefHandler moRefHandler, ManagedObjectReference hostMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference computeResourceMor = (ManagedObjectReference) moRefHandler
                .entityProps(hostMor, new String[]{ManagedObjectType.PARENT.getValue()}).get(ManagedObjectType.PARENT.getValue());

        if (computeResourceMor == null) {
            throw new RuntimeException(ErrorMessages.COMPUTE_RESOURCE_NOT_FOUND_ON_HOST);
        }

        return computeResourceMor;
    }
}
