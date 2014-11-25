package org.eclipse.score.content.ssh.utils.simulator.visualization;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by vranau on 11/25/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ScreenEmulator.class})
public class LinearUpdateTaskTest {

    private LinearUpdateTask linearUpdateTask;
    @Mock
    private ScreenEmulator screenEmulator;

    @Test
    public void testProcess() {
        int length = 10;
        linearUpdateTask = new LinearUpdateTask(length) {
            @Override
            public byte process(int offset) {
                return 0;
            }
        };
        linearUpdateTask.process(1, 6, new ByteWrapper(screenEmulator));
        assertEquals(false, linearUpdateTask.completed());
        assertEquals(false, linearUpdateTask.nextLine());

        linearUpdateTask = new LinearUpdateTask(length) {
            @Override
            public byte process(int offset) {
                return 10;
            }
        };
        linearUpdateTask.process(1, 6, new ByteWrapper(screenEmulator));
        assertEquals(false, linearUpdateTask.completed());
        assertEquals(true, linearUpdateTask.nextLine());

        linearUpdateTask = new LinearUpdateTask(0) {
            @Override
            public byte process(int offset) {
                return 10;
            }
        };
        linearUpdateTask.process(1, 6, new ByteWrapper(screenEmulator));
        assertEquals(true, linearUpdateTask.completed());
        assertEquals(true, linearUpdateTask.nextLine());


        linearUpdateTask = new LinearUpdateTask(0) {
            @Override
            public byte process(int offset) {
                return 10;
            }
        };
        linearUpdateTask.process(-345435341, 5464436, new ByteWrapper(screenEmulator));
        assertEquals(true, linearUpdateTask.completed());
        assertEquals(true, linearUpdateTask.nextLine());

        linearUpdateTask = new LinearUpdateTask(0) {
            @Override
            public byte process(int offset) {
                return 10;
            }
        };
        linearUpdateTask.process(345435341, -5464436, new ByteWrapper(screenEmulator));
        assertEquals(true, linearUpdateTask.completed());
        assertEquals(true, linearUpdateTask.nextLine());
    }
}