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

package io.cloudslang.content.abbyy.exceptions;

import java.util.Map;

public class AbbyySdkException extends Exception {

    private Map<String, String> resultsMap;


    public AbbyySdkException(String msg) {
        super(msg);
    }


    public AbbyySdkException(Throwable innerEx) {
        super(innerEx);
    }


    public AbbyySdkException(String msg, Map<String, String> resultsMap) {
        super(msg);
        this.resultsMap = resultsMap;
    }


    public AbbyySdkException(Throwable innerEx, Map<String, String> resultsMap) {
        super(innerEx);
        this.resultsMap = resultsMap;
    }


    public Map<String, String> getResultsMap() {
        return resultsMap;
    }


    public void setResultsMap(Map<String, String> resultsMap) {
        this.resultsMap = resultsMap;
    }
}
