/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */

package io.cloudslang.content.utilities;

import io.cloudslang.content.utilities.actions.FindTextInPdf;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by marisca on 7/11/2017.
 */
public class FindTextInPdfTest {
    private final FindTextInPdf findTextInPdf = new FindTextInPdf();

    @Test
    public void samplePdfSuccessfulTest() {
        try {
            URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
            if (resource != null) {
                File file = new File(resource.toURI());
                final Map<String, String> result = findTextInPdf.execute("This", "false", file.toString(), "");
                assertEquals(SUCCESS, result.get(RETURN_CODE));
                assertEquals("1", result.get(RETURN_RESULT));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void samplePdfSuccessfulTestIgnoreCase() {
        try {
            URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
            if (resource != null) {
                File file = new File(resource.toURI());
                final Map<String, String> result = findTextInPdf.execute("This", "true", file.toString(), "");
                assertEquals(SUCCESS, result.get(RETURN_CODE));
                assertEquals("3", result.get(RETURN_RESULT));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void samplePdfSuccessfulTestParagraph() {
        try {
            URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
            if (resource != null) {
                File file = new File(resource.toURI());
                final Map<String, String> result = findTextInPdf.execute("Most PDF readers have the ability to link", "false", file.toString(), "");
                assertEquals(SUCCESS, result.get(RETURN_CODE));
                assertEquals("1", result.get(RETURN_RESULT));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void samplePdfSuccessfulTestNotFound() {
        try {
            URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
            if (resource != null) {
                File file = new File(resource.toURI());
                final Map<String, String> result = findTextInPdf.execute("This content will not be found", "false", file.toString(), "");
                assertEquals(SUCCESS, result.get(RETURN_CODE));
                assertEquals("0", result.get(RETURN_RESULT));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void samplePdfFailureTest() {
        try {
            URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
            if (resource != null) {
                File file = new File(resource.toURI());
                final Map<String, String> result = findTextInPdf.execute("This", "false", file.toString() + ".wrong", "");
                System.out.println(result);
                assertEquals(FAILURE, result.get(RETURN_CODE));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void samplePdfTestVerticalText() {
        try {
            URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
            if (resource != null) {
                File file = new File(resource.toURI());
                final Map<String, String> result = findTextInPdf.execute("Vertical text, such as this, should still be recognized", "false", file.toString(), "");
                assertEquals(SUCCESS, result.get(RETURN_CODE));
                assertEquals("1", result.get(RETURN_RESULT));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void samplePdfTestFootnoteText() {
        try {
            URL resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-1.pdf");
            if (resource != null) {
                File file = new File(resource.toURI());
                final Map<String, String> result = findTextInPdf.execute("Even if the text isn't in the main body of the page, such as a footnote", "false", file.toString(), "");
                assertEquals(SUCCESS, result.get(RETURN_CODE));
                assertEquals("1", result.get(RETURN_RESULT));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void samplePdfSuccessfulTestOverImage() {
        try {
            URI resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-2.pdf").toURI();
            File file = new File(resource);
            final Map<String, String> result = findTextInPdf.execute("CloudSlang logo", "false", file.toString(), "");
            assertEquals(SUCCESS, result.get(RETURN_CODE));
            assertEquals("1", result.get(RETURN_RESULT));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void samplePdfSuccessfulTestPasswordProtected() {
        try {
            URI resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-3.pdf").toURI();
            File file = new File(resource);
            final Map<String, String> result = findTextInPdf.execute("sample", "false", file.toString(), "a1s2d3f4g5h");
            assertEquals(SUCCESS, result.get(RETURN_CODE));
            assertEquals("1", result.get(RETURN_RESULT));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void samplePdfFailureTestInvalidPassword() {
        try {
            URI resource = FindTextInPdf.class.getClassLoader().getResource("pdf/sample-pdf-3.pdf").toURI();
            File file = new File(resource);
            final Map<String, String> result = findTextInPdf.execute("sample", "false", file.toString(), "invalid-pass");
            assertEquals(FAILURE, result.get(RETURN_CODE));
            assertEquals("Cannot decrypt PDF, the password is incorrect", result.get(RETURN_RESULT));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
