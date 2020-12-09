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
package io.cloudslang.content.filesystem.entities;

import io.cloudslang.content.filesystem.constants.ExceptionMsgs;
import io.cloudslang.content.filesystem.constants.InputNames;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RenameInputs {
    private Path source;
    private String newName;
    private boolean overwrite;


    private RenameInputs() {

    }


    public final Path getSource() {
        return source;
    }


    public final String getNewName() {
        return newName;
    }


    public final boolean isOverwrite() {
        return overwrite;
    }


    public static final class Builder {
        private String source;
        private String newName;
        private String overwrite;


        public Builder source(String source) {
            this.source = source;
            return this;
        }


        public Builder newName(String newName) {
            this.newName = newName;
            return this;
        }


        public Builder overwrite(String overwrite) {
            this.overwrite = overwrite;
            return this;
        }


        public RenameInputs build() {
            RenameInputs inputs = new RenameInputs();
            inputs.source = StringUtils.isNotBlank(this.source) ? Paths.get(this.source) : null;
            inputs.newName = this.newName;
            if (StringUtils.isNotEmpty(this.overwrite)) {
                try {
                    inputs.overwrite = BooleanUtils.toBoolean(this.overwrite, String.valueOf(true), String.valueOf(false));
                } catch (IllegalArgumentException ex) {
                    String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, this.overwrite, InputNames.OVERWRITE);
                    throw new IllegalArgumentException(msg);
                }
            }
            return inputs;
        }
    }
}
