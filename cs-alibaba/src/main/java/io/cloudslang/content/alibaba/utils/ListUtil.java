/*
 * (c) Copyright 2019 Micro Focus
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


package io.cloudslang.content.alibaba.utils;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.valueOf;

public class ListUtil {
    public static List<CreateInstanceRequest.DataDisk> getDataDiskList(@NotNull final List<String> dataDisksSizeListImp,
                                                                       @NotNull final List<String> dataDisksCategoryListImp,
                                                                       @NotNull final List<String> dataDisksSnapshotIdListImp,
                                                                       @NotNull final List<String> dataDisksNameListImp,
                                                                       @NotNull final List<String> dataDisksDescriptionListImp,
                                                                       @NotNull final List<String> dataDisksDeleteWithInstanceListImp,
                                                                       @NotNull final List<String> dataDisksEncryptedListImp) {
        final List<CreateInstanceRequest.DataDisk> dataDiskList = new ArrayList<>();

        for (int i = 0; i < dataDisksNameListImp.size(); i++) {
            CreateInstanceRequest.DataDisk dataDisk = new CreateInstanceRequest.DataDisk();

            dataDisk.setCategory(dataDisksCategoryListImp.get(i));
            dataDisk.setDeleteWithInstance(valueOf(dataDisksDeleteWithInstanceListImp.get(i)));
            dataDisk.setDescription(dataDisksDescriptionListImp.get(i));
            dataDisk.setDiskName(dataDisksNameListImp.get(i));
            dataDisk.setEncrypted(valueOf(dataDisksEncryptedListImp.get(i)));
            dataDisk.setSize(Integer.valueOf(dataDisksSizeListImp.get(i)));
            dataDisk.setSnapshotId(dataDisksSnapshotIdListImp.get(i));

            dataDiskList.add(dataDisk);
        }

        return dataDiskList;
    }
}
