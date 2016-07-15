package io.cloudslang.content.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by giloan on 5/6/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(WSManUtils.class)
public class WSManUtilsTest {

    private static final String INVALID_UUID = "d2i37btr837t4r8347tr";
    @Rule
    private ExpectedException thrownException = ExpectedException.none();

    @Test
    public void testIsUUID() {
        assertTrue(WSManUtils.isUUID("fe99a244-9b0b-4929-8a43-2ef3080977d4"));
        assertFalse(WSManUtils.isUUID(INVALID_UUID));
        assertFalse(WSManUtils.isUUID(""));
        assertFalse(WSManUtils.isUUID(null));
    }

    @Test
    public void testValidateUUIDThrowsException() {
        String uuidValueOf = "shell";
        thrownException.expectMessage("The returned " + uuidValueOf + " is not a valid UUID value!");
        WSManUtils.validateUUID(INVALID_UUID, uuidValueOf);
    }
}
