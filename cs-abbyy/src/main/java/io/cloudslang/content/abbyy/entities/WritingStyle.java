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

public enum WritingStyle {
    DEFAULT,
    AMERICAN,
    GERMAN,
    RUSSIAN,
    POLISH,
    THAI,
    JAPANESE,
    ARABIC,
    BALTIC,
    BRITISH,
    BULGARIAN,
    CANADIAN,
    CZECH,
    CROATIAN,
    FRENCH,
    GREEK,
    HUNGARIAN,
    ITALIAN,
    ROMANIAN,
    SLOVAK,
    SPANISH,
    TURKISH,
    UKRAINIAN,
    COMMON,
    CHINESE,
    AZERBAIJAN,
    KAZAKH,
    KIRGIZ,
    LATVIAN;


    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
