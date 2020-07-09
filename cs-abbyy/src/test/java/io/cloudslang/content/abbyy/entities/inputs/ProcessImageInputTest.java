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
package io.cloudslang.content.abbyy.entities.inputs;

import io.cloudslang.content.abbyy.entities.others.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProcessImageInputTest {
    @Test
    public void getUrl_allFieldsSet_correspondingUrlReturned() throws Exception {
        //Arrange
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processImage?language=English&profile=textExtraction&textType=" +
                "normal&imageSource=auto&correctOrientation=true&correctSkew=true&readBarcodes=false&" +
                "exportFormat=pdfSearchable%2Cxml%2Ctxt&description=dummy&pdfPassword=dummy&xml%3AwriteFormatting=true&" +
                "xml%3AwriteRecognitionVariants=true&pdf%3AwriteTags=auto";

        ProcessImageInput sut = (ProcessImageInput) new ProcessImageInput.Builder()
                .language("English")
                .textType(TextType.NORMAL.toString())
                .imageSource(ImageSource.AUTO.toString())
                .correctOrientation(String.valueOf(true))
                .correctSkew(String.valueOf(true))
                .exportFormat(
                        StringUtils.join(new String[]{ExportFormat.PDF_SEARCHABLE.toString(), ExportFormat.XML.toString(), ExportFormat.TXT.toString()}, ','))
                .description("dummy")
                .pdfPassword("dummy")
                .profile(Profile.TEXT_EXTRACTION.toString())
                .writeTags(WriteTags.AUTO.toString())
                .writeFormatting(String.valueOf(true))
                .writeRecognitionVariants(String.valueOf(true))
                .readBarcodes(String.valueOf(false))
                .locationId(LocationId.EU.toString())
                .build();

        //Act
        String url = sut.getUrl();

        //Assert
        assertEquals(expectedUrl, url);
    }


    @Test
    public void buildUrl_someFieldsSet_correspondingUrlReturned() throws Exception {
        //Arrange
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processImage?textType=" +
                "normal&imageSource=auto&correctOrientation=false&correctSkew=true&readBarcodes=false&" +
                "exportFormat=txt&description=dummy";

        ProcessImageInput sut = (ProcessImageInput) new ProcessImageInput.Builder()
                .textType(TextType.NORMAL.toString())
                .imageSource(ImageSource.AUTO.toString())
                .correctOrientation(String.valueOf(false))
                .correctSkew(String.valueOf(true))
                .exportFormat(ExportFormat.TXT.toString())
                .description("dummy")
                .writeTags(WriteTags.AUTO.toString())
                .writeFormatting(String.valueOf(true))
                .writeRecognitionVariants(String.valueOf(true))
                .readBarcodes(String.valueOf(false))
                .locationId(LocationId.EU.toString())
                .build();

        //Act
        String url = sut.getUrl();

        //Assert
        assertEquals(expectedUrl, url);
    }
}
