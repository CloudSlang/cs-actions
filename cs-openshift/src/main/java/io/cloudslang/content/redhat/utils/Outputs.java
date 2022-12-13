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

package io.cloudslang.content.redhat.utils;

import io.cloudslang.content.constants.OutputNames;

public final class Outputs extends OutputNames {

    public static class OutputNames {

        public static final String STATUS_CODE = "statusCode";
        public static final String EXCEPTION = "exception";
        public static final String AUTH_TOKEN = "authToken";

        //Get Deployment Status
        public static final String NAME_OUTPUT = "name";
        public static final String DOCUMENT_OUTPUT = "document";
        public static final String NAMESPACE_OUTPUT = "namespace";
        public static final String UID_OUTPUT = "uid";
        public static final String KIND_OUTPUT = "kind";
        public static final String OBSERVED_GENERATION_OUTPUT = "observedGeneration";
        public static final String REPLICAS_OUTPUT = "replicas";
        public static final String UPDATED_REPLICAS_OUTPUT = "updatedReplicas";
        public static final String UNAVAILABLE_REPLICAS_OUTPUT = "unavailableReplicas";
        public static final String CONDITIONS_OUTPUT = "conditions";

        //Get Pod List

        public static final String POD_LIST = "podList";
        public static final String POD_ARRAY = "podArray";

    }
}

