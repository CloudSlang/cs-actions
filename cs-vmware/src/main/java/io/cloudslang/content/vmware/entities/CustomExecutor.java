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

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by giloan on 10/5/2016.
 */
public class CustomExecutor {

    private final ThreadPoolExecutor executor;
    private final boolean parallel;

    public CustomExecutor(boolean parallel) {
        this.parallel = parallel;
        this.executor = parallel ? (ThreadPoolExecutor) Executors.newCachedThreadPool() : null;
    }

    public void execute(final Runnable task) {
        if (parallel) {
            executor.execute(task);
        } else {
            task.run();
        }
    }

    public void shutdown() throws InterruptedException {
        if (parallel) {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        }
    }

    public boolean isParallel() {
        return parallel;
    }
}
