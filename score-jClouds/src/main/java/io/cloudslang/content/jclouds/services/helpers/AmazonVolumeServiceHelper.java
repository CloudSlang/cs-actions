package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.ec2.options.CreateVolumeOptions;
import org.jclouds.ec2.options.DetachVolumeOptions;

/**
 * Created by Mihai Tusa.
 * 6/22/2016.
 */
public class AmazonVolumeServiceHelper {
    private static final String GP2 = "gp2";
    private static final String IO1 = "io1";
    private static final String ST1 = "st1";
    private static final String SC1 = "sc1";

    private static final int FOUR = 4;
    private static final int FIVE_HUNDRED = 500;
    private static final int TEN_THOUSANDS = 10000;

    public CreateVolumeOptions getCreateVolumeOptions(String snapshotId, String volumeType, String size, String iops,
                                                      boolean encrypted) {
        CreateVolumeOptions options = CreateVolumeOptions.Builder.isEncrypted(encrypted);
        setCreateVolumeOptions(options, volumeType, size, iops);

        if (StringUtils.isNotBlank(snapshotId)) {
            options.fromSnapshotId(snapshotId);
        }

        return options;
    }

    public DetachVolumeOptions getDetachVolumeOptions(String instanceId, String device) {
        if (StringUtils.isBlank(instanceId) && StringUtils.isBlank(device)) {
            return null;
        }

        DetachVolumeOptions detachVolumeOptions = new DetachVolumeOptions();
        if (StringUtils.isNotBlank(instanceId)) {
            detachVolumeOptions = DetachVolumeOptions.Builder.fromInstance(instanceId);
        }
        if (StringUtils.isNotBlank(device)) {
            detachVolumeOptions = DetachVolumeOptions.Builder.fromDevice(device);
        }

        return detachVolumeOptions;
    }

    public int getSize(String volumeType, int min, int max, String sizeInput) {
        if (StringUtils.isBlank(sizeInput)) {
            return Constants.ValidationValues.ONE;
        }

        String errorMessage = "The size [" + sizeInput + "] provided for [" + volumeType + "] volumeType " +
                "should be greater or equal than [" + String.valueOf(min) + "] GiBs value and smaller or equal " +
                "than [" + String.valueOf(max) + "] GiBs value.";

        return InputsUtil.getVolumeValidInt(sizeInput, min, max, errorMessage);
    }

    private int getIops(String input, int min, int max, String iopsInput) {

        String errorMessage = "The iops [" + iopsInput + "] provided for [" + input + "] volumeType " +
                "should be greater or equal than [" + String.valueOf(min) + "] IOPS value and smaller or equal " +
                "than [" + String.valueOf(max) + "] IOPS value.";

        return InputsUtil.getVolumeValidInt(iopsInput, min, max, errorMessage);
    }

    private void setCreateVolumeOptions(CreateVolumeOptions options, String volumeType, String size, String iops) {
        switch (volumeType) {
            case GP2:
                options.volumeType(GP2);
                options.withSize(getSize(GP2, Constants.ValidationValues.ONE, Constants.ValidationValues.COMMON_LARGE_VALUE, size));
                if (!Constants.Miscellaneous.NOT_RELEVANT.equals(iops)) {
                    options.withIops(getIops(GP2, Constants.ValidationValues.ONE_HUNDRED, TEN_THOUSANDS, iops));
                }
                break;
            case IO1:
                options.volumeType(IO1);
                options.withSize(getSize(IO1, FOUR, Constants.ValidationValues.COMMON_LARGE_VALUE, size));
                if (!Constants.Miscellaneous.NOT_RELEVANT.equals(iops)) {
                    options.withIops(getIops(IO1, Constants.ValidationValues.ONE_HUNDRED,
                            Constants.ValidationValues.TWENTY_THOUSANDS, iops));
                }
                break;
            case ST1:
                options.volumeType(ST1);
                options.withSize(getSize(ST1, FIVE_HUNDRED, Constants.ValidationValues.COMMON_LARGE_VALUE, size));
                break;
            case SC1:
                options.volumeType(SC1);
                getSize(SC1, FIVE_HUNDRED, Constants.ValidationValues.COMMON_LARGE_VALUE, size);
                break;
            case Constants.Miscellaneous.STANDARD:
                options.withSize(getSize(Constants.Miscellaneous.STANDARD, Constants.ValidationValues.ONE,
                        Constants.ValidationValues.ONE_THOUSAND, size));
                break;
            default:
                break;
        }
    }
}