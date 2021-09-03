/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.rft.remote_copy;

import com.opsware.pas.content.commons.util.FSUtil;
import java.io.File;

public class LocalCopier extends SimpleCopier {

    private String overwrite = "true";

    public LocalCopier() {

    }

    protected IReader getFile(String source) throws Exception {
        return new SimpleReader(new File(source).getName(), new File(source));
    }

    protected void getFile(String source, File destFile) throws Exception {
        putFile(new SimpleReader(new File(source).getName(), new File(source)), destFile.getAbsolutePath());
    }

    protected void putFile(IReader sourceFile, String destination) throws Exception {
        File dest = new File(destination);
        if (dest.isDirectory()) {
            dest = new File(destination, sourceFile.getFileName());
        }
        if (overwrite != null && overwrite.equals("false") && (dest.exists())) {
            throw new Exception(destination + " exists, but overwrite is disabled");
        }
        FSUtil.recursiveCopy(sourceFile.getFile(), dest);
    }

    public void setCustomArgument(simpleArgument name, String value) {
        switch (name) {
            case overwrite:
                overwrite = value;
                break;
            default:
                throw new UnsupportedOperationException(getProtocolName() + " does not allow " + name.name() + " to be set");
        }
    }

    public String getCustomArgument(simpleArgument name) {
        switch (name) {
            case overwrite:
                return overwrite;
            default:
                return super.getCustomArgument(name);
        }
    }

    public String getProtocolName() {
        return CopierFactory.copiers.local.name();
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
