/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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

package io.cloudslang.content.vmware.services.helpers;

import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import io.cloudslang.content.vmware.connection.ConnectionResources;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 3/22/2016.
 */
public class MorObjectHandler {
    public ManagedObjectReference getMor(ConnectionResources connectionResources, String filter, String parameter) throws Exception {
        ManagedObjectReference reference = connectionResources.getMorRootFolder();
        return getSpecificMor(connectionResources, reference, filter, parameter);
    }

    public ManagedObjectReference getMorById(final ConnectionResources connectionResources, final String filter, final String id) throws Exception {
        final ManagedObjectReference reference = connectionResources.getMorRootFolder();
        final RetrieveOptions retrieveOptions = new RetrieveOptions();
        return connectionResources.getMoRefHandler().findManagedObjectReferenceByTypeAndId(reference, filter, retrieveOptions, id);
    }

    public Map<String, ManagedObjectReference> getSpecificObjectsMap(ConnectionResources connectionResources, String objectType)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return connectionResources.getMoRefHandler().inContainerByType(connectionResources.getMorRootFolder(), objectType);
    }

    public ManagedObjectReference getEnvironmentBrowser(ConnectionResources connectionResources, String filter)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference reference = connectionResources.getComputeResourceMor();
        return getProperty(connectionResources, reference, filter, filter);
    }

    public ManagedObjectReference getSpecificMor(ConnectionResources connectionResources, ManagedObjectReference reference,
                                                 String filter, String parameter) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return connectionResources.getMoRefHandler().inContainerByType(reference, filter, new RetrieveOptions()).get(parameter);
    }

    public Object getObjectProperties(ConnectionResources connectionResources, ManagedObjectReference reference, String filter)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return connectionResources.getMoRefHandler().entityProps(reference, new String[]{filter}).get(filter);
    }

    private ManagedObjectReference getProperty(ConnectionResources connectionResources, ManagedObjectReference reference,
                                               String filter, String parameter) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return (ManagedObjectReference) connectionResources.getMoRefHandler().entityProps(reference, new String[]{filter}).get(parameter);
    }
}
