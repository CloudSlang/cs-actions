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
package io.cloudslang.content.abbyy.http;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AbbyyResponseTest {

    @RunWith(PowerMockRunner.class)
    public static class BuilderTest {

        @Rule
        public ExpectedException exception = ExpectedException.none();


        @Test
        public void build_allFieldsSet_allFieldsSet() {
            //Arrange
            final String taskId = "task-id";
            final int credits = 1;
            final AbbyyResponse.TaskStatus taskStatus = AbbyyResponse.TaskStatus.DELETED;
            final String errorMessage = "error message";
            final String resultUrl = "resultUrl";
            final String resultUrl2 = "resultUrl2";
            final String resultUrl3 = "resultUrl3";
            final long estimatedProcessingTime = 1;

            //Act
            AbbyyResponse abbyyResponse = new AbbyyResponse.Builder()
                    .taskId(taskId)
                    .credits(String.valueOf(credits))
                    .taskStatus(taskStatus.toString())
                    .errorMessage(errorMessage)
                    .resultUrl(resultUrl)
                    .resultUrl2(resultUrl2)
                    .resultUrl3(resultUrl3)
                    .estimatedProcessingTime(String.valueOf(estimatedProcessingTime))
                    .build();

            //Assert
            assertEquals(taskId, abbyyResponse.getTaskId());
            assertEquals(credits, abbyyResponse.getCredits());
            assertEquals(taskStatus, abbyyResponse.getTaskStatus());
            assertEquals(errorMessage, abbyyResponse.getErrorMessage());
            assertEquals(3, abbyyResponse.getResultUrls().size());
            assertTrue(abbyyResponse.getResultUrls().contains(resultUrl));
            assertTrue(abbyyResponse.getResultUrls().contains(resultUrl2));
            assertTrue(abbyyResponse.getResultUrls().contains(resultUrl3));
            assertEquals(estimatedProcessingTime, abbyyResponse.getEstimatedProcessingTime());
        }


        @Test
        public void build_resultUrl3NotSet_resultUrl3NotPresent() {
            //Arrange
            final int credits = 1;
            final AbbyyResponse.TaskStatus taskStatus = AbbyyResponse.TaskStatus.DELETED;
            final String resultUrl = "resultUrl";
            final String resultUrl2 = "resultUrl2";
            final long estimatedProcessingTime = 1;

            //Act
            AbbyyResponse abbyyResponse = new AbbyyResponse.Builder()
                    .credits(String.valueOf(credits))
                    .taskStatus(taskStatus.toString())
                    .resultUrl(resultUrl)
                    .resultUrl2(resultUrl2)
                    .estimatedProcessingTime(String.valueOf(estimatedProcessingTime))
                    .build();

            //Assert
            assertEquals(2, abbyyResponse.getResultUrls().size());
            assertTrue(abbyyResponse.getResultUrls().contains(resultUrl));
            assertTrue(abbyyResponse.getResultUrls().contains(resultUrl2));
        }


        @Test
        public void build_creditsIsNotSet_IllegalArgumentException() {
            //Arrange
            final AbbyyResponse.TaskStatus taskStatus = AbbyyResponse.TaskStatus.DELETED;
            final String resultUrl = "resultUrl";
            final String resultUrl2 = "resultUrl2";
            final long estimatedProcessingTime = 1;

            //Assert
            this.exception.expect(IllegalArgumentException.class);

            //Act
            new AbbyyResponse.Builder()
                    .taskStatus(taskStatus.toString())
                    .resultUrl(resultUrl)
                    .resultUrl2(resultUrl2)
                    .estimatedProcessingTime(String.valueOf(estimatedProcessingTime))
                    .build();
        }


        @Test
        public void build_creditsIsNaN_IllegalArgumentException() {
            //Arrange
            final String credits = "asd";
            final AbbyyResponse.TaskStatus taskStatus = AbbyyResponse.TaskStatus.DELETED;
            final String resultUrl = "resultUrl";
            final String resultUrl2 = "resultUrl2";
            final long estimatedProcessingTime = 1;

            //Assert
            this.exception.expect(IllegalArgumentException.class);

            //Act
            new AbbyyResponse.Builder()
                    .credits(credits)
                    .taskStatus(taskStatus.toString())
                    .resultUrl(resultUrl)
                    .resultUrl2(resultUrl2)
                    .estimatedProcessingTime(String.valueOf(estimatedProcessingTime))
                    .build();
        }


        @Test
        public void build_taskStatusIsNotSet_IllegalArgumentException() {
            //Arrange
            final int credits = 1;
            final String resultUrl = "resultUrl";
            final String resultUrl2 = "resultUrl2";
            final long estimatedProcessingTime = 1;

            //Assert
            this.exception.expect(IllegalArgumentException.class);

            //Act
            new AbbyyResponse.Builder()
                    .credits(String.valueOf(credits))
                    .resultUrl(resultUrl)
                    .resultUrl2(resultUrl2)
                    .estimatedProcessingTime(String.valueOf(estimatedProcessingTime))
                    .build();
        }
    }
}
