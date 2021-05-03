/*
 * (c) Copyright 2021 Micro Focus, L.P.
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
package io.cloudslang.content.azure.utils;

import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class CommonOutputs {
        public static final String DOCUMENT = "document";
        public static final String AUTH_TOKEN = "authToken";

    }

    public static class CreateStreamingJobOutputs {
        public static final String PROVISIONING_STATE = "provisioningState";
        public static final String JOB_ID = "jobId";
        public static final String JOB_STATE = "jobState";

    }

    public static class CreateStreamingOutputJobOutputs {
        public static final String OUTPUT_NAME = "outputName";

    }

    public static class CreateStreamingInputJobOutputs {
        public static final String INPUT_NAME = "inputName";

    }

}

