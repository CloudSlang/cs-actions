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
