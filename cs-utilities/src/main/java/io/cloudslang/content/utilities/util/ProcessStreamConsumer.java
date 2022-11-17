/*
 * (c) Copyright 2022 EntIT Software LLC, a Micro Focus company, L.P.
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




package io.cloudslang.content.utilities.util;

import io.cloudslang.content.utilities.entities.ProcessResponseEntity;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.concurrent.Callable;

import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.closeQuietly;


public class ProcessStreamConsumer implements Callable<ProcessResponseEntity> {
    private static final int ERROR_CODE = -1;
    private final Process process;

    public ProcessStreamConsumer(Process process) {
        this.process = process;
    }

    @Override
    public ProcessResponseEntity call() throws Exception {
        String stdOut = "";
        String stdErr = "";
        int exitCode = ERROR_CODE;
        boolean timedOut = false;
        try {
            exitCode = process.waitFor();

            stdOut = IOUtils.toString(process.getInputStream(), UTF_8);
            stdErr = IOUtils.toString(process.getErrorStream(), UTF_8);
        } catch (InterruptedException ignored) {
            timedOut = true;
            currentThread().interrupt();
        } catch (IOException ignored) {
        } finally {
            closeQuietly(process.getInputStream());
            closeQuietly(process.getErrorStream());
        }

        return new ProcessResponseEntity(stdOut, stdErr, exitCode, timedOut);
    }
}