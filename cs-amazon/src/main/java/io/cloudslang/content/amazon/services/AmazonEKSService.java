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
/*
 * (c) Copyright 2023 Micro Focus, L.P.
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
package io.cloudslang.content.amazon.services;

import com.amazonaws.services.eks.AmazonEKS;
import com.amazonaws.services.eks.model.*;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.ListRolesRequest;
import com.amazonaws.services.identitymanagement.model.ListRolesResult;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AmazonEKSService {

    public static DescribeClusterResult getEKSClusterDetails(
            final String eksClusterName,
            final AmazonEKS amazonEKSClient) {
        DescribeClusterRequest describeEKSClusterRequest = new DescribeClusterRequest();
        describeEKSClusterRequest.setName(eksClusterName);
        return amazonEKSClient.describeCluster(describeEKSClusterRequest);
    }

    public static TagResourceResult tagResource(
            final String resourceArn,
            final Map<String, String> tags,
            final AmazonEKS amazonEKSClient) {
        TagResourceRequest tagResourceRequest = new TagResourceRequest();
        tagResourceRequest.setResourceArn(resourceArn);
        tagResourceRequest.setTags(tags);

        return amazonEKSClient.tagResource(tagResourceRequest);
    }

    public static CreateClusterResult createEKSCluster(
            final String eksClusterName,
            final String roleArn,
            final List<String> subnetIds,
            final List<String> securityGroupIds,
            final Boolean endpointPublicAccess,
            final Boolean endpointPrivateAccess,
            final String version,
            final String serviceIpv4Cidr,
            final Map<String, String> tags,
            final AmazonEKS amazonEKSClient
            ) {
        CreateClusterRequest createEKSClusterRequest = new CreateClusterRequest();
        createEKSClusterRequest.setName(eksClusterName);
        createEKSClusterRequest.setRoleArn(roleArn);

        VpcConfigRequest vpcConfigRequest = new VpcConfigRequest();
            vpcConfigRequest.setSubnetIds(subnetIds);
            vpcConfigRequest.setSecurityGroupIds(securityGroupIds);
            vpcConfigRequest.setEndpointPrivateAccess(endpointPublicAccess);
            vpcConfigRequest.setEndpointPublicAccess(endpointPrivateAccess);
        createEKSClusterRequest.setResourcesVpcConfig(vpcConfigRequest);

        if (!isEmpty(version))
            createEKSClusterRequest.setVersion(version);

        if (!isEmpty(serviceIpv4Cidr)) {
            KubernetesNetworkConfigRequest kubernetesNetworkConfigRequest = new KubernetesNetworkConfigRequest();
            kubernetesNetworkConfigRequest.setServiceIpv4Cidr(serviceIpv4Cidr);
            createEKSClusterRequest.setKubernetesNetworkConfig(kubernetesNetworkConfigRequest);
        }
        createEKSClusterRequest.setTags(tags);

        return amazonEKSClient.createCluster(createEKSClusterRequest);
    }


    public static DeleteClusterResult deleteEKSCluster (
            final String eksClusterName,
            final AmazonEKS amazonEKSClient) {
        DeleteClusterRequest deleteClusterRequest = new DeleteClusterRequest();
        deleteClusterRequest.setName(eksClusterName);

        return amazonEKSClient.deleteCluster(deleteClusterRequest);

    }

    public static ListRolesResult listRoles (
            final AmazonIdentityManagement amazonIdentityManagementClient
            ){
        ListRolesRequest listRolesRequest = new ListRolesRequest();

        //listRolesRequest.setPathPrefix("/aws-service-role/eks.amazonaws.com/");
        return amazonIdentityManagementClient.listRoles(listRolesRequest);
    }
}
