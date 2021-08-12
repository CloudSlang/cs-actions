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
package io.cloudslang.content.rft.spike_rft.sftp;

import com.jcraft.jsch.SftpProgressMonitor;

import javax.swing.*;

public class MyProgressMonitor implements SftpProgressMonitor {
    ProgressMonitor monitor;
    long count = 0;
    long max = 0;
    private long percent = -1;

    public void init(int op, String src, String dest, long max) {
        this.max = max;
        monitor = new ProgressMonitor(null,
                ((op == SftpProgressMonitor.PUT) ?
                        "put" : "get") + ": " + src,
                "", 0, (int) max);
        count = 0;
        percent = -1;
        monitor.setProgress((int) this.count);
        monitor.setMillisToDecideToPopup(1000);
    }

    public boolean count(long count) {
        this.count += count;

        if (percent >= this.count * 100 / max) {
            return true;
        }
        percent = this.count * 100 / max;

        monitor.setNote("Completed " + this.count + "(" + percent + "%) out of " + max + ".");
        monitor.setProgress((int) this.count);

        return !(monitor.isCanceled());
    }

    public void end() {
        monitor.close();
    }
}
