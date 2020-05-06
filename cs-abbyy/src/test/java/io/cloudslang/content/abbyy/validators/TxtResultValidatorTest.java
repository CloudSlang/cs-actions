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
package io.cloudslang.content.abbyy.validators;

import io.cloudslang.content.abbyy.constants.Limits;
import io.cloudslang.content.abbyy.entities.ExportFormat;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import io.cloudslang.content.abbyy.http.AbbyyRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class TxtResultValidatorTest extends AbbyyResultValidatorTest {

    @Mock
    private AbbyyApi abbyyApiMock;


    @Test
    public void validateBeforeDownload_resultSizeIsTooBig_ValidationException() throws Exception {
        //Arrange
        final AbbyyRequest abbyyInitialRequest = mock(AbbyyRequest.class);
        final String url = "url";

        when(this.abbyyApiMock.getResultSize(eq(abbyyInitialRequest), eq(url), any(ExportFormat.class))).thenReturn(Limits.MAX_SIZE_OF_RESULT + 1);

        //Act
        ValidationException ex = this.sut.validateBeforeDownload(abbyyInitialRequest, url);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validateBeforeDownload_resultValid_nullReturned() throws Exception {
        //Arrange
        final AbbyyRequest abbyyInitialRequest = mock(AbbyyRequest.class);
        final String url = "url";

        when(this.abbyyApiMock.getResultSize(eq(abbyyInitialRequest), eq(url), any(ExportFormat.class)))
                .thenReturn(Limits.MAX_SIZE_OF_RESULT - 1);

        //Act
        ValidationException ex = this.sut.validateBeforeDownload(abbyyInitialRequest, url);

        //Assert
        assertNull(ex);
    }


    @Override
    AbbyyResultValidator newSutInstance() {
        return new TxtResultValidator(this.abbyyApiMock);
    }
}
