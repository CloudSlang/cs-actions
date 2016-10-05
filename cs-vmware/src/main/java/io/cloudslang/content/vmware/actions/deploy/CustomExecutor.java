package io.cloudslang.content.vmware.actions.deploy;

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
        if (parallel) {
            this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        } else {
            this.executor = null;
        }
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
