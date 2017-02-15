/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.entities;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by giloan on 10/5/2016.
 */
public class CustomExecutor {

    private final ThreadPoolExecutor executor;
    private final boolean parallel;

    public CustomExecutor(boolean parallel) {
        this.parallel = parallel;
        this.executor = parallel ? (ThreadPoolExecutor) Executors.newCachedThreadPool() : null;
    }

    public void execute(final Runnable task) {
        if (parallel) {
            executor.execute(task);
        } else {
            task.run();
        }
    }

    public void shutdown() throws InterruptedException {
        if (parallel) {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        }
    }

    public boolean isParallel() {
        return parallel;
    }
}
