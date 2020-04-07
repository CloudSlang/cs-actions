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
package io.cloudslang.content.abby.constants;

public final class MiscConstants {
    public static final String HTTP_STATUS_CODE_OUTPUT = "statusCode";
    public static final String HTTP_RETURN_RESULT_OUTPUT = "returnResult";
    public static final String HTTP_EXCEPTION_OUTPUT = "exception";
    public static final String DOCUMENT_PROCESSED_SUCCESSFULLY = "The document was processed successfully. " +
            "Results can be retrieved by accessing one of the URLs from " + OutputNames.RESULT_URL + " output.";


    private MiscConstants() {
    }
}
