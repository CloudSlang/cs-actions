/*
 * (c) Copyright 2022 Micro Focus, L.P.
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

public class Descriptions {

    public static class Common {

        public static final String RETURN_CODE_DESC = "0 if success, -1 if failure.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for Openshift API request.";

        public static final String HOST_DESC = "The url of the service to which API calls are made.\n" +
                "Example: https://api.domain:6443";
        public static final String AUTH_TOKEN_DESC = "Token used to authenticate to the Openshift environment.";
        public static final String PROXY_HOST_DESC = "The proxy server used to access the web site.";
        public static final String PROXY_PORT_DESC = "The proxy server port." +
                "Default value: 8080.";
        public static final String PROXY_USERNAME_DESC = "The username used when connecting to the proxy.";
        public static final String PROXY_PASSWORD_DESC = "The proxy server password associated with the 'proxyUsername'" +
                " input value.";
        public static final String TLS_VERSION_DESC = "The version of TLS to use. The value of this input will be ignored if 'protocol'" +
                "is set to 'HTTP'. This capability is provided “as is”, please see product documentation for further information." +
                "Valid values: TLSv1, TLSv1.1, TLSv1.2. \n" +
                "Default value: TLSv1.2.  \n";
        public static final String ALLOWED_CIPHERS_DESC = "A list of ciphers to use. The value of this input will be ignored " +
                "if 'tlsVersion' does " +
                "not contain 'TLSv1.2'. This capability is provided “as is”, please see product documentation for further security considerations." +
                "In order to connect successfully to the target host, it should accept at least one of the following ciphers. If this is not the case, it is " +
                "the user's responsibility to configure the host accordingly or to update the list of allowed ciphers. \n" +
                "Default value: TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, " +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256, TLS_DHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384, " +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, " +
                "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_CBC_SHA256, " +
                "TLS_RSA_WITH_AES_128_CBC_SHA256.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it.";
        public static final String X509_HOSTNAME_VERIFIER_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\".";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. \n " +
                "Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String KEYSTORE_DESC = "The pathname of the Java KeyStore file. You only need this if " +
                "the server requires client authentication. If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String KEYSTORE_PASSWORD_DESC = "The password associated with the KeyStore file. If " +
                "trustAllRoots is false and keystore is empty, keystorePassword default will be supplied.";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout.";
        public static final String EXECUTION_TIMEOUT_DESC = "The amount of time (in seconds) to allow the client to complete the execution " +
                "of an API call. A value of '0' disables this feature. \n" +
                "Default: 60  \n";

    }

    public static class GetTokenAction {

        public static final String GET_TOKEN_NAME = "Get Token";
        public static final String GET_TOKEN_NAME_DESC = "Get Authorization Token";
        public static final String RETURN_RESULT_DESC = "The authorization token for Openshift.";
        public static final String AUTH_TOKEN_DESC = "Generated authentication token.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while generating the " +
                "token.";
        public static final String USERNAME_DESC = "The name of the user who is logging in to Openshift.";
        public static final String PASSWORD_DESC = "The password used by the user to log in to Openshift.";

        public static final String SUCCESS_DESC = "Token generated successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve token.";

    }

    public static class CreateDeploymentAction {
        public static final String CREATE_DEPLOYMENT = "Create Deployment";
        public static final String CREATE_DEPLOYMENT_DESCRIPTION = "This operation creates a deployment with the inputs from the user.";
        public static final String AUTH_TOKEN_DESCRIPTION = "Token used to authenticate to the openshift environment.";
        public static final String NAMESPACE_DESCRIPTION = "The namespace in which the deployment will be created.";
        public static final String DEFINITION_DESCRIPTION = "JSON definitions consisting of the deployment specifications.";
        public static final String RETURN_RESULT_DESC = "The created deployment in case of success or a comprehensive message in case of failure.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error";
        public static final String SUCCESS_DESC = "Deployment created successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to create the deployment.";
    }

    public static class DeleteDeployment {

        public static final String DELETE_DEPLOYMENT_NAME = "Delete Deployment";
        public static final String DELETE_DEPLOYMENT_DESC = "Deletes the deployment from namespace.";

        public static final String NAMESPACE_DESC = "Namespace to delete the deployment from.";
        public static final String DEPLOYMENT_DESC = "Name of the deployment to delete.";
        public static final String RETURN_RESULT_DESC = "The deployment was successfully deleted.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while deleting the deployment.";

        public static final String SUCCESS_DESC = "Deployment was successfully deleted.";
        public static final String FAILURE_DESC = "There was an error while trying to delete the deployment.";

    }

    public static class GetDeploymentStatus {

        public static final String GET_DEPLOYMENT_STATUS = "Get Deployment Status";
        public static final String GET_DEPLOYMENT_STATUS_DESC = "Read the status of the specified deployment.";

        public static final String NAME_DESC = "The name of the deployment.";
        public static final String NAMESPACE_DESC = "The object name and auth scope, such as for teams and projects.";

        //Outputs
        public static final String RETURN_RESULT_DESC = "A suggestive message both for the case of success and for the " +
                "case of failure.";
        public static final String RETURN_RESULT_MESSAGE_DESC = "The request was made successfully, please analyze the " +
                "entire response generated by the API call in the document output as a json format.";
        public static final String STATUS_CODE_DESC = "The status code of the request.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while reading the " +
                "deployment status.";

        public static final String SUCCESS_DESC = "The request to read the status of the specified deployment was made " +
                "successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to get the status of the deployment.";

        //Specific outputs

        //Get Deployment Status
        public static final String NAME_OUTPUT_DESC = "The deployment name.";
        public static final String NAMESPACE_OUTPUT_DESC = "The deployment namespace.";
        public static final String UID_OUTPUT_DESC = "The deployment uid.";
        public static final String KIND_OUTPUT_DESC = "The deployment kind.";
        public static final String OBSERVED_GENERATION_OUTPUT_DESC = "The observedGeneration status property of the deployment.";
        public static final String REPLICAS_OUTPUT_DESC = "The replicas status property of the deployment.";
        public static final String UPDATED_REPLICAS_OUTPUT_DESC = "The updatedReplicas status property of the deployment.";
        public static final String UNAVAILABLE_REPLICAS_OUTPUT_DESC = "The unavailableReplicas status property of the deployment.";
        public static final String CONDITIONS_OUTPUT_DESC = "The conditions status properties of the deployment in the json format.";

        public static final String DOCUMENT_OUTPUT_DESC = "All the information related to a specific deployment in the " +
                "json format.";

    }

    public static class GetPodList {
        public static final String GET_POD_LIST = "Get Pod List";
        public static final String GET_POD_LIST_DESC = "List objects of Pod type.";
        public static final String NAMESPACE_DESC = "The object name and auth scope, such as for teams and projects.";
        public static final String RETURN_RESULT_DESC = "A suggestive message both for the case of success and for the " +
                "case of failure.";
        public static final String STATUS_CODE_DESC = "The status code of the request.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while retrieving the Pod list.";
        public static final String SUCCESS_DESC = "The retrieval of the pod list was made successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the pod list.";
        public static final String POD_LIST_DESC = "The comma separated list of pod uids.";
        public static final String POD_ARRAY_DESC = "The list containing pairs of pod name and uids in JSON format.";
        public static final String DOCUMENT_OUTPUT_DESC = "All the information related to the pod list in json format.";
        public static final String SUCCESSFUL_RETURN_RESULT = "The pod list was returned successfully.";
        public static final String FAILURE_RETURN_RESULT = "An issue was encountered during your query. Please check the exception output for more information.";

    }

    public static class CreatePod {
        public static final String CREATE_POD = "Create Pod";
        public static final String CREATE_POD_DESCRIPTION = "This operation creates a new pod with the inputs from the user.";
        public static final String AUTH_TOKEN_DESCRIPTION = "Token used to authenticate to the openshift environment.";
        public static final String NAMESPACE_DESCRIPTION = "The namespace in which the pod will be created.";
        public static final String SPEC_DESCRIPTION = "The pod specification in JSON format.";
        public static final String RETURN_RESULT_DESC = "The created pod in case of success or a comprehensive message in case of failure.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while creating the pod.";
        public static final String SUCCESS_DESC = "The pod was created successfully.";
        public static final String FAILURE_DESC = "There was an error while creating the pod.";
    }

    public static class DeletePod {
        public static final String DELETE_POD = "Delete Pod";
        public static final String DELETE_POD_DESCRIPTION = "This operation deletes a pod from a namespace.";
        public static final String AUTH_TOKEN_DESCRIPTION = "Token used to authenticate to the openshift environment.";
        public static final String NAMESPACE_DESCRIPTION = "The namespace from which to delete the pod.";
        public static final String POD_NAME_DESCRIPTION = "Name of the pod to delete.";
        public static final String RETURN_RESULT_DESC = "A suggestive message in case of success or failure.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while deleting the pod.";
        public static final String SUCCESS_DESC = "The pod was deleted successfully.";
        public static final String FAILURE_DESC = "There was an error while deleting the pod.";
    }

    public static class GetRouteList {
        public static final String GET_ROUTE_LIST = "Get Route List";
        public static final String GET_ROUTE_LIST_DESC = "This operation returns a list of routes present in a given namespace.";
        public static final String NAMESPACE_DESC = "The object name and auth scope, such as for teams and projects.";
        public static final String RETURN_RESULT_DESC = "A suggestive message both for the case of success and for the " +
                "case of failure.";
        public static final String STATUS_CODE_DESC = "The status code of the request.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while retrieving the route list.";
        public static final String SUCCESS_DESC = "The retrieval of the route list was made successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the route list.";
        public static final String ROUTE_LIST_DESC = "The comma separated list of route uids.";
        public static final String ROUTE_ARRAY_DESC = "The list containing pairs of route name and uids in JSON format.";
        public static final String DOCUMENT_OUTPUT_DESC = "All the information related to the route list in json format.";
        public static final String SUCCESSFUL_RETURN_RESULT = "The route list was returned successfully.";

    }

    public static class GetPodTemplateList {
        public static final String GET_POD_TEMPLATE_LIST = "Get Pod Template List";
        public static final String GET_POD_TEMPLATE_LIST_DESC = "This operation returns a list of pod templates present in a given namespace.";
        public static final String NAMESPACE_DESC = "The object name and auth scope, such as for teams and projects.";
        public static final String RETURN_RESULT_DESC = "A suggestive message in case of success or failure.";
        public static final String STATUS_CODE_DESC = "The HTTP status code of the Openshift API request.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while retrieving the pod template list.";
        public static final String SUCCESS_DESC = "The retrieval of the pod template list was made successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the pod template list.";
        public static final String POD_TEMPLATE_LIST_DESC = "A comma separated list of pod template uids.";
        public static final String POD_TEMPLATE_ARRAY_DESC = "A list containing pairs of pod template name and uids in JSON format.";
        public static final String DOCUMENT_OUTPUT_DESC = "All the information related to the pod template list in json format.";
        public static final String SUCCESSFUL_RETURN_RESULT = "The pod template list was returned successfully.";
        public static final String FAILURE_RETURN_RESULT = "An issue was encountered during your query. Please check the exception output for more information.";

    }


    public static class GetTemplateList {
        public static final String GET_TEMPLATE_LIST = "Get Template List";
        public static final String GET_TEMPLATE_LIST_DESC = "This operation returns a list of templates present in a given namespace.";
        public static final String NAMESPACE_DESC = "The object name and auth scope, such as for teams and projects.";
        public static final String RETURN_RESULT_DESC = "A suggestive message in case of success or failure.";
        public static final String STATUS_CODE_DESC = "The HTTP status code of the Openshift API request.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while retrieving the template list.";
        public static final String SUCCESS_DESC = "The retrieval of the template list was made successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the template list.";
        public static final String TEMPLATE_LIST_DESC = "A comma separated list of template uids.";
        public static final String TEMPLATE_ARRAY_DESC = "A list containing pairs of template name and uids in JSON format.";
        public static final String DOCUMENT_OUTPUT_DESC = "All the information related to the template list in json format.";
        public static final String SUCCESSFUL_RETURN_RESULT = "The template list was returned successfully.";
        public static final String FAILURE_RETURN_RESULT = "An issue was encountered during your query. Please check the exception output for more information.";

    }

    public static class CreateBuild {
        public static final String CREATE_BUILD = "Create Build";
        public static final String CREATE_BUILD_DESC = "This operation starts a build from an already existing build config.";
        public static final String NAMESPACE_DESC = "The namespace where the build config is located.";
        public static final String BUILD_CONFIG_DESC = "The build config that will be used as a template for the build.";
        public static final String SPECIFICATION_DESC = "The build specification in JSON format.";
        public static final String RETURN_RESULT_DESC = "The created build in case of success or a comprehensive message in case of failure.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while creating the build.";
        public static final String SUCCESS_DESC = "The build was created successfully.";
        public static final String FAILURE_DESC = "There was an error while creating the build.";
    }


    public static class DeleteRoute {
        public static final String DELETE_ROUTE = "Delete Route";
        public static final String DELETE_ROUTE_DESCRIPTION = "This operation deletes a route from a namespace.";
        public static final String AUTH_TOKEN_DESCRIPTION = "Token used to authenticate to the openshift environment.";
        public static final String NAMESPACE_DESCRIPTION = "The namespace from which to delete the route.";
        public static final String ROUTE_NAME_DESCRIPTION = "Name of the route to delete.";
        public static final String RETURN_RESULT_DESC = "A suggestive message in case of success or failure.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while deleting the route.";
        public static final String SUCCESS_DESC = "The route was deleted successfully.";
        public static final String FAILURE_DESC = "There was an error while deleting the route.";
    }
}
