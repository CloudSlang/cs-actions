/*
 * Copyright 2019-2024 Open Text
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



package io.cloudslang.content.couchbase.entities.constants;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */
public class Constants {
    private Constants() {
        // prevent instantiation
    }

    public static class Api {
        public static final String BUCKETS = "buckets";
        public static final String CLUSTER = "cluster";
        public static final String NODES = "nodes";
        public static final String VIEWS = "views";
    }

    public static class HttpClientInputsValues {
        public static final String ALL_TYPE_HEADER = "Accept:application/json, text/plain, */*";
        public static final String ALLOW_ALL = "allow_all";
        public static final String BROWSER_COMPATIBLE = "browser_compatible";
        public static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded; charset=UTF-8";
        public static final String STRICT = "strict";
        public static final String X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1 = "X-memcachekv-Store-Client-Specification-Version:0.1";
    }

    public static class BucketActions {
        public static final String CREATE_OR_EDIT_BUCKET = "CreateOrEditBucket";
        public static final String DELETE_BUCKET = "DeleteBucket";
        public static final String FLUSH_BUCKET = "FlushBucket";
        public static final String GET_ALL_BUCKETS = "GetAllBuckets";
        public static final String GET_BUCKET = "GetBucket";
        public static final String GET_BUCKET_STATISTICS = "GetBucketStatistics";
    }

    public static class ClusterActions {
        public static final String GET_AUTO_FAILOVER_SETTINGS = "GetAutoFailOverSettings";
        public static final String GET_CLUSTER_DETAILS = "GetClusterDetails";
        public static final String GET_CLUSTER_INFO = "GetClusterInfo";
        public static final String GET_DESTINATION_CLUSTER_REFERENCE = "GetDestinationClusterReference";
        public static final String REBALANCING_NODES = "RebalancingNodes";
    }

    public static class NodeActions {
        public static final String FAIL_OVER_NODE = "FailOverNode";
        public static final String GRACEFUL_FAIL_OVER_NODE = "GracefulFailOverNode";
        public static final String SET_RECOVERY_TYPE = "SetRecoveryType";
    }

    public static class ViewsActions {
        public static final String GET_DESIGN_DOCS_INFO = "GetDesignDocsInfo";
    }

    public static class ErrorMessages {
        public static final String CONSTRAINS_ERROR_MESSAGE = "The value doesn't meet conditions for general purpose usage. " +
                "See operation inputs description section for details.";
        public static final String UNKNOWN_BUILDER_TYPE = "Unknown builder type.";
        public static final String UNKNOWN_COUCHBASE_HEADER = "Unknown Couchbase header.";
        public static final String UNSUPPORTED_COUCHBASE_API = "Unsupported Couchbase API.";
        public static final String INPUTS_COMBINATION_ERROR_MESSAGE = "The combination of values supplied for inputs: " +
                "authType, proxyPort and/or saslPassword doesn't meet conditions for general purpose usage.";
    }

    public static class Miscellaneous {
        public static final String AMPERSAND = "&";
        public static final String AT = "@";
        public static final String BLANK_SPACE = " ";
        public static final String COMMA = ",";
        public static final String EQUAL = "=";
        public static final String PORT_REGEX = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        public static final String SLASH = "/";
    }

    public static class Values {
        public static final int INIT_INDEX = 0;
        public static final int COUCHBASE_DEFAULT_PROXY_PORT = 11215;
        public static final int DEFAULT_REPLICA_NUMBER = 1;
        public static final int DEFAULT_THREADS_NUMBER = 2;
        public static final int MAXIMUM_REPLICA_NUMBER = 3;
        public static final int MAXIMUM_THREADS_NUMBER = 8;
        public static final int MINIMUM_RAM_QUOTA_AMOUNT = 100;
        public static final int MINIMUM_THREADS_NUMBER = 2;
    }
}