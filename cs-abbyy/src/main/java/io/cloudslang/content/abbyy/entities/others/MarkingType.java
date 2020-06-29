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

package io.cloudslang.content.abbyy.entities.others;

public enum MarkingType {
    SIMPLE_TEXT("simpleText"),
    UNDERLINED_TEXT("underlinedText"),
    TEXT_IN_FRAME("textInFrame"),
    GREY_BOXES("greyBoxes"),
    CHAR_BOX_SERIES("charBoxSeries"),
    SIMPLE_COMB("simpleComb"),
    COMB_IN_FRAME("combInFrame"),
    PARTITIONED_FRAME("partitionedFrame");

    private final String str;


    MarkingType(String str) {
        this.str = str;
    }


    @Override
    public String toString() {
        return this.str;
    }
}
