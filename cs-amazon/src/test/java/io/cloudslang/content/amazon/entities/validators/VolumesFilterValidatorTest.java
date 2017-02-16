package io.cloudslang.content.amazon.entities.validators;

import io.cloudslang.content.amazon.entities.aws.VolumeFilter;
import org.junit.Before;
import org.junit.Test;

import static io.cloudslang.content.amazon.entities.aws.VolumeAttachmentStatus.ATTACHED;
import static io.cloudslang.content.amazon.entities.aws.VolumeStatus.AVAILABLE;
import static io.cloudslang.content.amazon.entities.aws.VolumeType.STANDARD;
import static org.junit.Assert.assertEquals;

/**
 * Created by sandorr
 * 2/16/2017.
 */
public class VolumesFilterValidatorTest {

    private static final String NOT_GOOD_VALUE = "notGoodValue";
    private static final String SIZE_VALUE = "1024";
    private static final String ATTACHED_VALUE = "attached";
    private static final String AVAILABLE_VALUE = "available";
    private static final String STANDARD_VALUE = "standard";
    private VolumesFilterValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new VolumesFilterValidator();
    }

    @Test
    public void testGoodValues() {
        assertEquals(ATTACHED.name().toLowerCase(), validator.getFilterValue(VolumeFilter.ATTACHMENT_STATUS, ATTACHED_VALUE));
        assertEquals(AVAILABLE.getKey().toLowerCase(), validator.getFilterValue(VolumeFilter.STATUS, AVAILABLE_VALUE));
        assertEquals(STANDARD.name().toLowerCase(), validator.getFilterValue(VolumeFilter.VOLUME_TYPE, STANDARD_VALUE));
        assertEquals(SIZE_VALUE, validator.getFilterValue(VolumeFilter.SIZE, SIZE_VALUE));

    }

    @Test(expected = RuntimeException.class)
    public void testBadValues() {
        validator.getFilterValue(VolumeFilter.ATTACHMENT_STATUS, NOT_GOOD_VALUE);
    }
}
