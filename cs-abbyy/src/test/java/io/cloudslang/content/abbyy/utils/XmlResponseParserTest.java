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

package io.cloudslang.content.abbyy.utils;

import io.cloudslang.content.abbyy.constants.MiscConstants;
import io.cloudslang.content.abbyy.entities.AbbyyResponse;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.HashMap;
import java.util.Map;

@PrepareForTest({XmlResponseParser.class, AbbyyResponse.TaskStatus.class})
public class XmlResponseParserTest extends AbbyyResponseParserTest {
    @Override
    protected AbbyyResponseParser newSutInstance() throws Exception {
        return new XmlResponseParser();
    }


    @Test
    public void parseResponse_validSuccessResponse_responseObjectCreatedSuccessfully() throws Exception {
        //Arrange
        final String id = "id";
        final int credits = 2;
        final AbbyyResponse.TaskStatus taskStatus = AbbyyResponse.TaskStatus.QUEUED;
        final String errorMessage = "errMsg";
        final String resultUrl1 = "url1", resultUrl2 = "url2", resultUrl3 = "url3";
        final int estimatedProcessingTime = 5;
        final String xml = "<response><task " +
                "id='" + id + "' " +
                "credits='" + credits + "' " +
                "status='" + taskStatus + "' " +
                "error='" + errorMessage + "' " +
                "resultUrl='" + resultUrl1 + "' " +
                "resultUrl2='" + resultUrl2 + "' " +
                "resultUrl3='" + resultUrl3 + "' " +
                "estimatedProcessingTime='" + estimatedProcessingTime + "' " +
                "/></response>";

        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "200");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);

        PowerMockito.mockStatic(AbbyyResponse.TaskStatus.class);
        PowerMockito.when(AbbyyResponse.TaskStatus.fromString(Matchers.anyString())).thenReturn(taskStatus);

        AbbyyResponse.Builder builderMock = PowerMockito.mock(AbbyyResponse.Builder.class);
        PowerMockito.whenNew(AbbyyResponse.Builder.class).withAnyArguments().thenReturn(builderMock);

        //Act
        this.sut.parseResponse(response);

        //Assert
        Mockito.verify(builderMock).taskId(id);
        Mockito.verify(builderMock).credits(credits);
        Mockito.verify(builderMock).taskStatus(taskStatus);
        Mockito.verify(builderMock).errorMessage(errorMessage);
        Mockito.verify(builderMock).resultUrl(resultUrl1);
        Mockito.verify(builderMock).resultUrl2(resultUrl2);
        Mockito.verify(builderMock).resultUrl3(resultUrl3);
        Mockito.verify(builderMock).estimatedProcessingTime(estimatedProcessingTime);
    }
}