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

public enum LocationId {
    //real servers
    EU("cloud-eu", Protocols.HTTPS),
    WEST_US("cloud-westus", Protocols.HTTPS),
    //for testing purposes
    TA_XSD_FAIL("abbyy-ta.ros.swinfra.net:3000/xsd-fail", Protocols.HTTP),
    TA_TXT_LARGE("abbyy-ta.ros.swinfra.net:3000/txt-large", Protocols.HTTP),
    TA_XML_LARGE("abbyy-ta.ros.swinfra.net:3000/xml-large", Protocols.HTTP),
    TA_PDF_LARGE("abbyy-ta.ros.swinfra.net:3000/pdf-large", Protocols.HTTP),
    TA_PDF_INVALID("abbyy-ta.ros.swinfra.net:3000/pdf-invalid", Protocols.HTTP),
    TA_TIMEOUT("abbyy-ta.ros.swinfra.net:3000/timeout", Protocols.HTTP),
    TA_TASK_FAILED("abbyy-ta.ros.swinfra.net:3000/task-failed", Protocols.HTTP);

    private final String str;
    private final String protocol;


    LocationId(String str, String protocol) {
        this.str = str;
        this.protocol = protocol;
    }


    public String getProtocol() {
        return protocol;
    }


    @Override
    public String toString() {
        return this.str;
    }


}
