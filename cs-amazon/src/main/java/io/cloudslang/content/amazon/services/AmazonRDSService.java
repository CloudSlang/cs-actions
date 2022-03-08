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
package io.cloudslang.content.amazon.services;

import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.*;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AmazonRDSService {

    public static DescribeDBInstancesResult getDBDetails(
            final String dbInstanceIdentifier,
            final AmazonRDS amazonRDSClient) {
        DescribeDBInstancesRequest describeDBInstancesRequest = new DescribeDBInstancesRequest();
        describeDBInstancesRequest.setDBInstanceIdentifier(dbInstanceIdentifier);
        return amazonRDSClient.describeDBInstances(describeDBInstancesRequest);
    }

    public static DBInstance createRDSInstance(
            final String dbEngineName, final String dbEngineVersion, final String dbUsername,
            final String dbPassword, final String dbInstanceIdentifier, final String dbInstanceSize, final int dbStorageSize,
            final String licenseModel, final String availabilityZone, final AmazonRDS amazonRDSClient) {


        CreateDBInstanceRequest createDBInstanceRequest = new CreateDBInstanceRequest();
        createDBInstanceRequest.setDBInstanceIdentifier(dbInstanceIdentifier);
        createDBInstanceRequest.setEngine(dbEngineName);
        createDBInstanceRequest.setEngineVersion(dbEngineVersion);
        createDBInstanceRequest.setMasterUsername(dbUsername);
        createDBInstanceRequest.setMasterUserPassword(dbPassword);
        createDBInstanceRequest.setDBInstanceClass(dbInstanceSize);
        createDBInstanceRequest.setAllocatedStorage(dbStorageSize);
        if (!isEmpty(licenseModel))
            createDBInstanceRequest.setLicenseModel(licenseModel);
        if (!isEmpty(availabilityZone))
            createDBInstanceRequest.setAvailabilityZone(availabilityZone);


        return amazonRDSClient.createDBInstance(createDBInstanceRequest);
    }

    public static DBInstance deleteRDSInstance(final String dbInstanceIdentifier,
                                               final Boolean skipFinalSnapshot, final String finalDBSnapshotIdentifier, final Boolean deleteAutomatedBackups,
                                               final AmazonRDS amazonRDSClient) {

        DeleteDBInstanceRequest deleteDBInstanceRequest = new DeleteDBInstanceRequest();
        deleteDBInstanceRequest.setDBInstanceIdentifier(dbInstanceIdentifier);
        if (skipFinalSnapshot)
            deleteDBInstanceRequest.setSkipFinalSnapshot(skipFinalSnapshot);
        if (!isEmpty(finalDBSnapshotIdentifier))
            deleteDBInstanceRequest.setFinalDBSnapshotIdentifier(finalDBSnapshotIdentifier);
        if (deleteAutomatedBackups)
            deleteDBInstanceRequest.setDeleteAutomatedBackups(deleteAutomatedBackups);

        return amazonRDSClient.deleteDBInstance(deleteDBInstanceRequest);
    }
}
