/*
 * Copyright 2020-2024 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.filesystem.entities;

import static io.cloudslang.content.filesystem.utils.InputBuilderUtils.buildSource;
import static io.cloudslang.content.filesystem.utils.InputBuilderUtils.buildThreshold;

public class GetSizeInputs {

    private String source;
    private long threshold;

    private GetSizeInputs(String source, long threshold) {
        this.source = source;
        this.threshold = threshold;
    }

    public GetSizeInputs() { }

    public String getSource() {
        return source;
    }

    public long getThreshold() {
        return threshold;
    }

    public static class Builder {
        private String source;
        private String threshold;

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder threshold(String threshold) {
            this.threshold = threshold;
            return this;
        }

        public GetSizeInputs build() throws Exception {
            return new GetSizeInputs(buildSource(source),buildThreshold(threshold));
        }
    }
}
