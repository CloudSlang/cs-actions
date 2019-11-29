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
package io.cloudslang.content.utilities.util.randomPasswordGenerator;

import org.passay.CharacterData;

public class SpecialCharacters implements CharacterData {

    private String characters = "-=[];,.~!@#$%&*()_+{}|:<>?";

    public SpecialCharacters(String forbiddenCharacters) {
        for (char forbiddenCharacter : forbiddenCharacters.toCharArray()) {
            StringBuilder sb = new StringBuilder(characters);
            int index = sb.indexOf(Character.toString(forbiddenCharacter));
            if(index != -1){
                sb.deleteCharAt(index);
                characters = sb.toString();
            }
        }
    }

    public String getErrorCode() {
        return "INSUFFICIENT_SPECIAL";
    }

    public String getCharacters() {
        return this.characters;
    }

}