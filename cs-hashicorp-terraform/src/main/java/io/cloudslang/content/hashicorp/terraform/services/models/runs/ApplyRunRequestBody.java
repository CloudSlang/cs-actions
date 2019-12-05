/*
 * (c) Copyright 2020 Micro Focus, L.P.
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


package io.cloudslang.content.hashicorp.terraform.services.models.runs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplyRunRequestBody {
    @JsonProperty("comment")
    private String runComment;

    public String getRunComment() {
        return runComment;
    }

    public void setRunComment(String runComment) {
        this.runComment = runComment;
    }
}
