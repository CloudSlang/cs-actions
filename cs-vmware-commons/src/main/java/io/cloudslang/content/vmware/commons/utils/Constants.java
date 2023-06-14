/*
 * (c) Copyright 2023 Open Text
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
package io.cloudslang.content.vmware.commons.utils;

public class Constants {
    public static final String EXCEPTION_INVALID_VM_IDENTIFIER_TYPE = "%s for %s input is not a valid vmIdentifierType value. The valid values are: 'name', 'uuid', 'vmid'.";
    public static final String NEW_LINE = "\n";
    public static final String DEFAULT_VM_IDENTIFIER_TYPE = "name";
    public static final String NAME = "name";
    public static final String VMID = "vmid";
    public static final String UUID = "uuid";
    public static final String DEFAULT_TIMEOUT = "90";
    public static final String ZERO = "0";

    //exceptions
    public static final String EXCEPTION_RESOURCE_POOL_NOT_FOUND = "Resource pool not found: ";
    public static final String EXCEPTION_DATASTORE_NOT_FOUND = "Datastore not found: ";
    public static final String EXCEPTION_CLUSTER_NOT_FOUND ="Cluster not found:";
    public static final String EXCEPTION_VM_FOLDER_NOT_FOUND ="Folder not found: ";
    public static final String EXCEPTION_LIBRARY_ITEM_NOT_FOUND ="The item was not found in the provided Content Library: ";
    public static final String EXCEPTION_VM_NOT_FOUND = "Virtual machine or template not found: ";
    public static final String EXCEPTION_LIBRARY_NOT_FOUND = "Library not found: ";
    public static final String EXCEPTION_HOST_NOT_FOUND = "Host not found: ";
    public static final String EXCEPTION_TIMED_OUT= "The execution timed out after ";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
    static final String EXCEPTION_NEGATIVE_INDEX = "The value '%s' for %s input cannot be a negative number.";


}
