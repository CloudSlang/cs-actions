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
package io.cloudslang.content.amazon.utils;

import com.amazonaws.services.servicecatalog.model.ProvisioningParameter;
import com.amazonaws.services.servicecatalog.model.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.amazon.utils.ServiceCatalogUtil.setProvisionParameters;
import static io.cloudslang.content.amazon.utils.ServiceCatalogUtil.toArrayOfTags;
import static org.junit.Assert.assertEquals;

public class ServiceCatalogUtilTest {

    public static final String KEY_NAME = "keyName";
    public static final String SSH_LOCATION = "sshLocation";
    public static final String INSTANCE_TYPE = "instanceType";
    public static final String EMPTY_STRING = "";

    @Test
    public void toArrayOfTagsTest() {
        final String tags = "key1=value1\nkey2=value2";
        List<Tag> tagsList = toArrayOfTags(tags);
        assertEquals("key1", tagsList.get(0).getKey());
        assertEquals("value1", tagsList.get(0).getValue());
        assertEquals("key2", tagsList.get(1).getKey());
        assertEquals("value2", tagsList.get(1).getValue());
    }

    @Test
    public void toArrayOfTagsEmptyTest() {
        List<Tag> tagsList = toArrayOfTags(EMPTY_STRING);
        assertEquals(new ArrayList<>(), tagsList);
    }

    @Test
    public void toArrayOfTagsNullTest() {
        final String tags = null;
        List<Tag> tagsList = toArrayOfTags(tags);
        assertEquals(new ArrayList<>(), tagsList);
    }
    //This should be validated correctly

    @Test
    public void toArrayOfTagsInvalidStringTest() {
        final String tags = "myInvalidTag";
        List<Tag> tagsList = toArrayOfTags(tags);
        assertEquals(new ArrayList<>(), tagsList);
    }

    @Test
    public void setProvisionParametersTest() {
        List<ProvisioningParameter> parameters = setProvisionParameters(KEY_NAME, SSH_LOCATION, INSTANCE_TYPE);
        assertEquals("KeyName", parameters.get(0).getKey());
        assertEquals("SSHLocation", parameters.get(1).getKey());
        assertEquals("InstanceType", parameters.get(2).getKey());
        assertEquals(KEY_NAME, parameters.get(0).getValue());
        assertEquals(SSH_LOCATION, parameters.get(1).getValue());
        assertEquals(INSTANCE_TYPE, parameters.get(2).getValue());

    }

    @Test
    public void setProvisionParametersEmptyTest() {
        List<ProvisioningParameter> parameters = setProvisionParameters(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
        assertEquals(new ArrayList<>(), parameters);
    }

    @Test
    public void setProvisionParametersNullTest() {
        List<ProvisioningParameter> parameters = setProvisionParameters(null, null, null);
        assertEquals(new ArrayList<>(), parameters);
    }
}
