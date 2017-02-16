package io.cloudslang.content.amazon.entities.validators;

import io.cloudslang.content.amazon.entities.aws.VolumeAttachmentStatus;
import io.cloudslang.content.amazon.entities.aws.VolumeStatus;
import io.cloudslang.content.amazon.entities.aws.VolumeType;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.amazon.entities.aws.VolumeFilter.*;

/**
 * Created by sandorr
 * 2/16/2017.
 */
public class VolumesFilterValidator implements FilterValidator {
    @Override
    @NotNull
    public String getFilterValue(@NotNull final String filterName, @NotNull final String filterValue) {
        switch (filterName) {
            case ATTACHMENT_STATUS:
                return VolumeAttachmentStatus.getValue(filterValue);
            case STATUS:
                return VolumeStatus.getValue(filterValue);
            case VOLUME_TYPE:
                return VolumeType.getValue(filterValue);
            default:
                return filterValue;
        }
    }
}
