/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public enum ExportFormat {
    TXT("txt"),
    TXT_UNSTRUCTURED("txtUnstructured"),
    RTF("rtf"),
    DOCX("docx"),
    XLSX("xlsx"),
    PPTX("pptx"),
    PDF_SEARCHABLE("pdfSearchable"),
    PDF_TEXT_AND_IMAGES("pdfTextAndImages"),
    PDF_A("pdfa"),
    XML("xml"),
    XML_FOR_CORRECTED_IMAGE("xmlForCorrectedImage"),
    ALTO("alto");

    private final String str;


    ExportFormat(String str) {
        this.str = str;
    }


    public static ExportFormat fromString(String str) throws Exception {
        for (ExportFormat ef : ExportFormat.values()) {
            if (ef.str.equals(str)) {
                return ef;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_EXPORT_FORMAT, str));
    }


    @Override
    public String toString() {
        return this.str;
    }
}
