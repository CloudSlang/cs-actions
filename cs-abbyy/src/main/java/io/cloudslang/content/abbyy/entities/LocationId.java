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

package io.cloudslang.content.abbyy.entities;

import io.cloudslang.content.abbyy.constants.Protocols;
import io.cloudslang.content.abbyy.constants.Urls;
import org.jetbrains.annotations.NotNull;

public enum LocationId {
    //real servers
    EU("cloud-eu", Protocols.HTTPS),
    WEST_US("cloud-westus", Protocols.HTTPS),
    //for testing purposes
    TA_XSD_FAIL(String.format(Urls.TA_HOST_TEMPLATE, "xsd-fail"), Protocols.HTTP),
    TA_TXT_LARGE(String.format(Urls.TA_HOST_TEMPLATE, "txt-large"), Protocols.HTTP),
    TA_XML_LARGE(String.format(Urls.TA_HOST_TEMPLATE, "xml-large"), Protocols.HTTP),
    TA_PDF_LARGE(String.format(Urls.TA_HOST_TEMPLATE, "pdf-large"), Protocols.HTTP),
    TA_PDF_INVALID(String.format(Urls.TA_HOST_TEMPLATE, "pdf-invalid"), Protocols.HTTP),
    TA_TIMEOUT(String.format(Urls.TA_HOST_TEMPLATE, "timeout"), Protocols.HTTP),
    TA_TASK_FAILED(String.format(Urls.TA_HOST_TEMPLATE, "task-failed"), Protocols.HTTP);

    private final String str;
    private final String protocol;


    LocationId(String str, String protocol) {
        this.str = str;
        this.protocol = protocol;
    }


    public @NotNull String getProtocol() {
        return protocol;
    }


    @Override
    public String toString() {
        return this.str;
    }


}
