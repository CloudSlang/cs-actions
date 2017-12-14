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


package io.cloudslang.content.utilities;

import io.cloudslang.content.utilities.actions.FindTextInPdf;
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

/**
 * Created by marisca on 7/11/2017.
 */
public class FindTextInPdfTest {
    private final FindTextInPdf findTextInPdf = new FindTextInPdf();

    @Test
    public void samplePdfSuccessfulTest() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("This", "false", file.toString(), "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("1", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfSuccessfulTestIgnoreCase() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("This", "true", file.toString(), "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("3", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfSuccessfulTestParagraph() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("Most PDF readers have the ability to link", "false", file.toString(), "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("1", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfSuccessfulTestNotFound() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("This content will not be found", "false", file.toString(), "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("0", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfFailureTest() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("This", "false", file.toString() + ".wrong", "");
        assertEquals(FAILURE, result.get(RETURN_CODE));
    }

    @Test
    public void samplePdfTestVerticalText() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("Vertical text, such as this, should still be recognized", "false", file.toString(), "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("1", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfTestFootnoteText() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("Even if the text isn't in the main body of the page, such as a footnote", "false", file.toString(), "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("1", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfSuccessfulTestOverImage() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-2.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("CloudSlang logo", "false", file.toString(), "");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("1", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfSuccessfulTestPasswordProtected() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-3.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("sample", "false", file.toString(), "a1s2d3f4g5h");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("1", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfFailureTestInvalidPassword() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-3.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("sample", "false", file.toString(), "invalid-pass");
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals("Cannot decrypt PDF, the password is incorrect", result.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfFailureTestInvalidIgnoreCase() throws URISyntaxException {
        final URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-3.pdf");
        assertNotNull(resource);
        final File file = new File(resource.toURI());
        final Map<String, String> result = findTextInPdf.execute("sample", "invalid", file.toString(), "a1s2d3f4g5h");
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals("Invalid boolean value for ignoreCase parameter: invalid", result.get(RETURN_RESULT));
    }
}
