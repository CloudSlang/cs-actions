/*
 * Copyright 2023 Open Text
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




package io.cloudslang.content.azure.utils;

import io.cloudslang.content.constants.InputNames;

public class Inputs extends InputNames {

    public static class CommonInputs {
        public static final String AUTH_TOKEN = "authToken";
        public static final String TENANT_ID = "tenantId";
        public static final String CLIENT_SECRET = "clientSecret";
        public static final String JOB_NAME = "jobName";
        public static final String RESOURCE_GROUP_NAME = "resourceGroupName";
        public static final String ACCOUNT_NAME = "accountName";
        public static final String ACCOUNT_KEY = "accountKey";
        public static final String API_VERSION = "apiVersion";
        public static final String SUBSCRIPTION_ID = "subscriptionId";
    }
    public static class ComputeCommonInputs {

        public static final String AZURE_PROTOCOL = "azureProtocol";
        public static final String AZURE_HOST = "azureHost";
        public static final String LOCATION = "location";
    }

    public static class CreateVMInputs {

        public static final String AVAILABILITY_SET_NAME = "availabilitySetName";
        public static final String DISK_TYPE = "diskType";
        public static final String NIC_NAME = "nicName";
        public static final String ADMIN_USERNAME = "adminUsername";
        public static final String ADMIN_PASSWORD = "adminPassword";
        public static final String SSH_PUBLIC_KEY_NAME = "sshPublicKeyName";
        public static final String VM_SIZE = "vmSize";
        public static final String VM_NAME = "vmName";
        public static final String IMAGE_VERSION = "imageVersion";
        public static final String DISK_SIZE_IN_GB = "diskSizeInGB";
        public static final String STORAGE_ACCOUNT = "storageAccount";
        public static final String STORAGE_ACCOUNT_TYPE = "storageAccountType";
        public static final String PUBLISHER = "publisher";
        public static final String SKU = "sku";
        public static final String OFFER = "offer";
        public static final String PLAN = "plan";
        public static final String PRIVATE_IMAGE_NAME = "privateImageName";
        public static final String DATA_DISK_NAME = "dataDiskName";
        public static final String OS_DISK_NAME = "osDiskName";
        public static final String TAG_KEY_LIST = "tagKeyList";
        public static final String TAG_VALUE_LIST = "tagValueList";
    }

    public static class CreateStreamingJobInputs {
        public static final String JOB_NAME = "jobName";
        public static final String SKU_NAME = "skuName";
        public static final String EVENTS_OUT_OF_ORDER_POLICY = "eventsOutOfOrderPolicy";
        public static final String OUTPUT_ERROR_POLICY = "outputErrorPolicy";
        public static final String EVENTS_OUT_OF_ORDER_MAX_DELAY_IN_SECONDS = "eventsOutOfOrderMaxDelayInSeconds";
        public static final String EVENTS_LATE_ARRIVAL_MAX_DELAY_IN_SECONDS = "eventsLateArrivalMaxDelayInSeconds";
        public static final String DATA_LOCALE = "dataLocale";
        public static final String COMPATIBILITY_LEVEL = "compatibilityLevel";
        public static final String LOCATION = "location";
        public static final String TAGS = "tags";

    }

    public static class StartStreamingJobInputs {
        public static final String OUTPUT_START_MODE = "outputStartMode";
        public static final String OUTPUT_START_TIME = "outputStartTime";

    }

    public static class CreateStreamingInputsJob {
        public static final String CONTAINER_NAME_STREAM_INPUT = "containerNameStreamInput";
    }

    public static class CreateStreamingOutputJob {
        public static final String CONTAINER_NAME_STREAM_OUTPUT = "containerNameStreamOutput";
    }

    public static class GetStreamingJobInputs {
        public static final String EXPAND = "expand";

    }

    public static class CreateTransformationsInputs {
        public static final String TRANSFORMATION_NAME = "transformationName";
        public static final String TRANSFORMATION_QUERY = "query";
        public static final String STREAMING_UNITS = "streamingUnits";
    }
}
