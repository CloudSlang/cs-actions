/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.vmware.entities;

import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;

import java.io.OutputStream;

public class TransferVmdkTask implements Runnable {

    private final ITransferVmdkFrom source;
    private final TransferVmdkToUrl destination;
    private final ProgressUpdater progressUpdater;

    public TransferVmdkTask(final ITransferVmdkFrom fromFile, final TransferVmdkToUrl toUrl,
                            ProgressUpdater progressUpdater) throws RuntimeFaultFaultMsg, TimedoutFaultMsg {
        this.source = fromFile;
        this.destination = toUrl;
        this.progressUpdater = progressUpdater;
    }

    @Override
    public void run() {
        try (OutputStream output = destination.getOutputStream()) {
            source.uploadTo(output, progressUpdater);
        } catch (Exception e) {
            //TODO: log when a logging system is adopted for content
        }
    }
}
