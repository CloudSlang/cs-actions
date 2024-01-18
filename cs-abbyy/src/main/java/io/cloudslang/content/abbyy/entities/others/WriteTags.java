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



package io.cloudslang.content.abbyy.entities.others;

public enum WriteTags {
    AUTO("auto"),
    WRITE("write"),
    DONT_WRITE("dontWrite");

    private final String str;


    WriteTags(String str) {
        this.str = str;
    }


    @Override
    public String toString() {
        return this.str;
    }
}
