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


package io.cloudslang.content.vmware.utils;

import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Mihai Tusa.
 * 1/11/2016.
 */
public class InputsUtilsTest {
    @Test
    public void getIntInput() {
        int testInt = InputUtils.getIntInput("4096", 1024);
        assertEquals(4096, testInt);
    }

    @Test
    public void getIntInputDefault() {
        int testInt = InputUtils.getIntInput("", 1024);
        assertEquals(1024, testInt);
    }

    @Test
    public void getIntInputException() throws RuntimeException {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> InputUtils.getIntInput("Doesn't work in this way", -1));
        assertEquals("The input value must be 0 or positive number.", exception.getMessage());
    }

    @Test
    public void getLongInput() {
        long testLong = InputUtils.getLongInput("4096", 1024L);
        assertEquals(4096L, testLong);
    }

    @Test
    public void getLongInputDefault() {
        long testLong = InputUtils.getLongInput("", 1024L);
        assertEquals(1024L, testLong);
    }

    @Test
    public void getLongInputException() throws RuntimeException {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> InputUtils.getLongInput("Still doesn't work in this way", 0));
        assertEquals("The input value must be 0 or positive number.", exception.getMessage());
    }

    @Test
    public void getUrlStringSuccess() throws Exception {
        HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                .withHost("vc6.subdomain.example.com")
                .withPort("443")
                .withProtocol("")
                .withUsername("")
                .withPassword("")
                .withTrustEveryone("false").build();
        String testUrl = InputUtils.getUrlString(httpInputs);

        assertEquals("https://vc6.subdomain.example.com:443/sdk", testUrl);
    }

    @Test
    public void getUrlStringException() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost("")
                    .withPort("8080")
                    .withProtocol("myProtocol")
                    .withUsername("")
                    .withPassword("")
                    .withTrustEveryone("true").build();
            InputUtils.getUrlString(httpInputs);
        });
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", exception.getMessage());
    }

    @Test
    public void getSuccessfullyDefaultDelimiter() {
        String testDelimiter = InputUtils.getDefaultDelimiter("", ",");
        assertEquals(",", testDelimiter);
    }

    @Test
    public void getDiskFileNameString() {
        String testDiskFileNameString = InputUtils.getDiskFileNameString("someDataStore", "testVM", "Renamed");
        assertEquals("[someDataStore] testVM/Renamed.vmdk", testDiskFileNameString);
    }

    @Test
    public void checkValidOperation() throws Exception {
        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withDevice("disk").withOperation("update").build();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> InputUtils.checkValidOperation(vmInputs, "disk"));
        assertEquals("Invalid operation specified for disk device. The disk device can be only added or removed.", exception.getMessage());
    }

    @Test
    public void isUpdateOperation() throws Exception {
        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withOperation("add").build();
        assertFalse(InputUtils.isUpdateOperation(vmInputs));
    }

    @Test
    public void isIntFalse() {
        assertFalse(InputUtils.isInt("2147483648"));
    }

    @Test
    public void isIntTrue() {
        assertTrue(InputUtils.isInt("2147483647"));
    }

    @Test
    public void validateDiskInputsAdd() throws Exception {
        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("add")
                .withDevice("disk")
                .withLongVmDiskSize("0")
                .withDiskMode("persistent")
                .build();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> InputUtils.validateDiskInputs(vmInputs));
        assertEquals("The disk size must be positive long.", exception.getMessage());
    }

    @Test
    public void validateDiskInputsRemove() throws Exception {
        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("remove")
                .withDevice("disk")
                .withUpdateValue("")
                .build();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> InputUtils.validateDiskInputs(vmInputs));
        assertEquals("The [] is not a valid disk label.", exception.getMessage());
    }

    @Test
    public void getByteInputSuccess() {
        byte test = 0;
        InputUtils.getByteInput("-128", test);

        assertEquals(0, test);
    }

    @Test
    public void getByteInputNotByte() {
        byte test = 0;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> InputUtils.getByteInput("128", test));
        assertEquals("The input value must be a positive number between 0 and 127 values range.", exception.getMessage());
    }

    @Test
    public void getByteInputDefault() {
        byte test = 0;
        InputUtils.getByteInput("", test);

        assertEquals(0, test);
    }

    @Test
    public void getBooleanInputFalse() {
        assertFalse(InputUtils.getBooleanInput("anything", true));
    }

    @Test
    public void getBooleanInputDefault() {
        assertTrue(InputUtils.getBooleanInput("", true));
    }

    @Test
    public void getBooleanInputTrue() {
        assertTrue(InputUtils.getBooleanInput("TrUe", false));
    }
}
