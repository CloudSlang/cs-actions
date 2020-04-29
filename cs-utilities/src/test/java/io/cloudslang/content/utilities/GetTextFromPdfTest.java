/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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




package io.cloudslang.content.utilities;

import io.cloudslang.content.utilities.actions.FindTextInPdf;
import io.cloudslang.content.utilities.actions.GetTextFromPdf;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class GetTextFromPdfTest {
    GetTextFromPdf getTextFromPdf = new GetTextFromPdf();

    @Test
    public void samplePdfSuccessfulTest() throws URISyntaxException {
        final URL resource = GetTextFromPdf.class.getClassLoader().getResource("pdf/sample-pdf-2.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = getTextFromPdf.execute(file.toString(), "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("This is some text written on top of CloudSlang logo. \r\n", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfSuccessfulTestPasswordProtected() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-3.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = getTextFromPdf.execute(file.toString(), "a1s2d3f4g5h");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("protected-pdf.txt[11/14/2017 11:08:28 AM]\r\nThis is a sample text inside a password protected PDF.\r\n", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfFailureTestInvalidPath() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-2.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = getTextFromPdf.execute(file.toString() + ".wrong", "");
        assertEquals(FAILURE, result.get(RETURN_CODE));
    }

    @Test
    public void samplePdfFailureTestInvalidPassword() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-3.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = getTextFromPdf.execute(file.toString(), "invalid-pass");
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals("Cannot decrypt PDF, the password is incorrect", result.get(RETURN_RESULT));
    }
}
