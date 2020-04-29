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

import io.cloudslang.content.utilities.actions.GetTextFromPdf;
import io.cloudslang.content.utilities.actions.MergePdfFiles;
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

public class MergePdfTest {
    private final MergePdfFiles mergePdfFiles = new MergePdfFiles();
    private final GetTextFromPdf getTextFromPdf = new GetTextFromPdf();

    @Test
    public void samplePdfSuccessfulTestWithOneFile() throws URISyntaxException {
        final URL resource = MergePdfFiles.class.getClassLoader().getResource("pdf/sample-pdf-2.pdf");
        final URL resourceDestination = GetTextFromPdf.class.getClassLoader().getResource("pdf/merge.pdf");
        assertNotNull(resource);
        assertNotNull(resourceDestination);
        final File file = new File(resource.toURI());
        final File fileResource = new File(resourceDestination.toURI());
        final Map<String, String> result = mergePdfFiles.execute(file.toString(), fileResource.toString());
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(fileResource.toString(), result.get(RETURN_RESULT));
        final Map<String, String> text = getTextFromPdf.execute(fileResource.toString(), "");
        assertEquals("This is some text written on top of CloudSlang logo. \r\n", text.get(RETURN_RESULT));

    }

    @Test
    public void samplePdfSuccessfulTestWithMultipleFiles() throws URISyntaxException {
        final URL resource = MergePdfFiles.class.getClassLoader().getResource("pdf/sample-pdf-2.pdf");
        final URL resourceDestination = GetTextFromPdf.class.getClassLoader().getResource("pdf/merge.pdf");
        assertNotNull(resource);
        assertNotNull(resourceDestination);
        final File file = new File(resource.toURI());
        final File fileResource = new File(resourceDestination.toURI());
        final Map<String, String> result = mergePdfFiles.execute(file.toString() + "," + file.toString() +
                "," + file.toString(), fileResource.toString());
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(fileResource.toString(), result.get(RETURN_RESULT));
        final Map<String, String> text = getTextFromPdf.execute(fileResource.toString(), "");
        assertEquals("This is some text written on top of CloudSlang logo. \r\nThis is some text written on top " +
                "of CloudSlang logo. \r\nThis is some text written on top of CloudSlang logo. \r\n", text.get(RETURN_RESULT));

    }

    @Test
    public void samplePdfFailureTestWithInvalidFilesPath() throws URISyntaxException {
        final URL resource = MergePdfFiles.class.getClassLoader().getResource("pdf/sample-pdf-2.pdf");
        final URL resourceDestination = MergePdfFiles.class.getClassLoader().getResource("pdf/merge.pdf");
        assertNotNull(resource);
        assertNotNull(resourceDestination);
        final File file = new File(resource.toURI());
        final File fileResource = new File(resourceDestination.toURI());
        final Map<String, String> result = mergePdfFiles.execute(file.toString() + ".wrong", fileResource.toString());
        assertEquals(FAILURE, result.get(RETURN_CODE));

    }

    @Test
    public void samplePdfSuccessTestWithCommaPathEnding() throws URISyntaxException {
        final URL resource = MergePdfFiles.class.getClassLoader().getResource("pdf/sample-pdf-2.pdf");
        final URL resourceDestination = MergePdfFiles.class.getClassLoader().getResource("pdf/merge.pdf");
        assertNotNull(resource);
        assertNotNull(resourceDestination);
        final File file = new File(resource.toURI());
        final File fileResource = new File(resourceDestination.toURI());
        final Map<String, String> result = mergePdfFiles.execute(file.toString() + ",", fileResource.toString());
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(fileResource.toString(), result.get(RETURN_RESULT));
        final Map<String, String> text = getTextFromPdf.execute(fileResource.toString(), "");
        assertEquals("This is some text written on top of CloudSlang logo. \r\n", text.get(RETURN_RESULT));
    }

    @Test
    public void samplePdfSuccessTestWithInvalidExtension() throws URISyntaxException {
        final URL resource = MergePdfFiles.class.getClassLoader().getResource("pdf/sample-pdf-2.pdf");
        final URL resourceDestination = MergePdfFiles.class.getClassLoader().getResource("pdf/merge.pdf");
        assertNotNull(resource);
        assertNotNull(resourceDestination);
        final File file = new File(resource.toURI());
        final File fileResource = new File(resourceDestination.toURI());
        final Map<String, String> result = mergePdfFiles.execute(file.toString(), fileResource.toString() + ".wrong");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(fileResource.toString() + ".wrong", result.get(RETURN_RESULT));
        final Map<String, String> text = getTextFromPdf.execute(fileResource.toString() + ".wrong", "");
        assertEquals("This is some text written on top of CloudSlang logo. \r\n", text.get(RETURN_RESULT));
    }

}
