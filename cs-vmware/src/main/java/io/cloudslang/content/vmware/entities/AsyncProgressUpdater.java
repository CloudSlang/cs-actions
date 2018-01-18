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

import com.vmware.vim25.ManagedObjectReference;
import io.cloudslang.content.vmware.connection.ConnectionResources;

import static java.lang.Thread.sleep;

public class AsyncProgressUpdater extends ProgressUpdater {

    public AsyncProgressUpdater(long totalNoBytes, ManagedObjectReference httpNfcLease, ConnectionResources connectionResources) {
        super(totalNoBytes, httpNfcLease, connectionResources);
    }

    @Override
    public final synchronized void updateBytesSent(final long bytesSent) throws Exception {
        this.bytesSent += bytesSent;
    }

    @Override
    public void run() {
        int percentage = 0;
        try {
            while (bytesSent < totalNoBytes) {
                sleep(100);
                final int newPercentage = getFloorPercentage();
                if (newPercentage != percentage) {
                    updateLeaseProgress(newPercentage);
                    percentage = newPercentage;
                }
            }
            updateProgressCompleted();
        } catch (Exception e) {
            //TODO: log when a logging system is adopted for content
        }
    }
}
