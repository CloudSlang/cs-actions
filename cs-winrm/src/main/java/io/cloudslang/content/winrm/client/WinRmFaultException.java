/*
 * Copyright 2026-2027 Open Text
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

package io.cloudslang.content.winrm.client;

/**
 * SOAP fault returned by the WinRM service.
 */
public class WinRmFaultException extends WinRmException {

    private final String faultCode;

    public WinRmFaultException(final String message, final String faultCode) {
        super(message);
        this.faultCode = faultCode;
    }

    /**
     * @return the {@code WSManFault} code, or {@code null} if the fault does not carry one
     */
    public String getFaultCode() {
        return faultCode;
    }
}
