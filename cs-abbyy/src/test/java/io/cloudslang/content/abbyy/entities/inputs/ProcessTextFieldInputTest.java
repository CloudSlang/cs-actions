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

import io.cloudslang.content.abbyy.entities.others.LocationId;
import io.cloudslang.content.abbyy.entities.others.MarkingType;
import io.cloudslang.content.abbyy.entities.others.TextType;
import io.cloudslang.content.abbyy.entities.others.WritingStyle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProcessTextFieldInputTest {
    @Test
    public void buildUrl_allFieldsSet_correspondingUrlReturned() throws Exception {
        //Arrange
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processTextField?language=English&letterSet=dummy" +
                "&regExp=dummy&textType=normal&oneTextLine=true&oneWordPerTextLine=true" +
                "&markingType=simpleText&placeholdersCount=1&writingStyle=default&description=dummy&pdfPassword=dummy";

        ProcessTextFieldInput sut = (ProcessTextFieldInput) new ProcessTextFieldInput.Builder()
                .language("English")
                .letterSet("dummy")
                .regExp("dummy")
                .textType(TextType.NORMAL.toString())
                .oneTextLine(String.valueOf(true))
                .oneWordPerTextLine(String.valueOf(true))
                .markingType(MarkingType.SIMPLE_TEXT.toString())
                .placeholdersCount(String.valueOf(1))
                .description("dummy")
                .pdfPassword("dummy")
                .writingStyle(WritingStyle.DEFAULT.toString())
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
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processTextField?language=English&letterSet=dummy" +
                "&regExp=dummy&textType=normal&oneTextLine=true&oneWordPerTextLine=true" +
                "&markingType=simpleText&placeholdersCount=1";

        ProcessTextFieldInput sut = (ProcessTextFieldInput) new ProcessTextFieldInput.Builder()
                .language("English")
                .letterSet("dummy")
                .regExp("dummy")
                .textType(TextType.NORMAL.toString())
                .oneTextLine(String.valueOf(true))
                .oneWordPerTextLine(String.valueOf(true))
                .markingType(MarkingType.SIMPLE_TEXT.toString())
                .placeholdersCount(String.valueOf(1))
                .description(null)
                .locationId(LocationId.EU.toString())
                .build();

        //Act
        String url = sut.getUrl();

        //Assert
        assertEquals(expectedUrl, url);
    }
}
