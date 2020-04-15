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
package io.cloudslang.content.abbyy.entities;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;

public class Region {

    public static final Region DEFAULT_VALUE = new Region(-1,-1,-1,-1);

    private final int left;
    private final int top;
    private final int right;
    private final int bottom;


    public Region(int left, int top, int right, int bottom) {
        if ((left > right || top > bottom) ||
                (left < 0 && left != -1) ||
                (top < 0 && top != -1) ||
                (right < 0 && right != -1) ||
                (bottom < 0 && bottom != -1)) {
            throw new IllegalArgumentException(ExceptionMsgs.INVALID_REGION);
        }

        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }


    public int getLeft() {
        return left;
    }


    public int getTop() {
        return top;
    }


    public int getRight() {
        return right;
    }


    public int getBottom() {
        return bottom;
    }


    @Override
    public String toString() {
        return left + "," + top + "," + right + "," + bottom;
    }
}
