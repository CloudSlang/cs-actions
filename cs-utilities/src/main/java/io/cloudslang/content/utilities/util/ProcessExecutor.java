package io.cloudslang.content.utilities.util;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.text.StrTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS_95;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS_98;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS_ME;

public class ProcessExecutor {
    private final List<String> arguments;

    public ProcessExecutor() {
        if (IS_OS_WINDOWS_95 || IS_OS_WINDOWS_98 || IS_OS_WINDOWS_ME) {
            arguments = ImmutableList.of("command.exe", "/C");
        } else if (IS_OS_WINDOWS) {
            arguments = ImmutableList.of("cmd.exe", "/C");
        } else {
            arguments = ImmutableList.of();
        }
    }

    private List<String> processCommand(String commandLine) {
        List<String> command = new ArrayList<>(arguments);
        if(!isEmpty(commandLine)) {
            command.addAll(new StrTokenizer(commandLine, ',', '"').getTokenList());
        }

        return command;
    }

    public ProcessResponseEntity execute(String commandLine, int timeout) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Process process = new ProcessBuilder().command(processCommand(commandLine)).start();

        ProcessStreamConsumer processStreamConsumer = new ProcessStreamConsumer(process);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<ProcessResponseEntity> futureResult = executor.submit(processStreamConsumer);

        try {
            return futureResult.get(timeout, MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            stopProcess(process, futureResult);
            throw e;
        } finally {
            executor.shutdownNow();
        }
    }

    private void stopProcess(Process process, Future<ProcessResponseEntity> futureResult) {
        futureResult.cancel(true);
        process.destroy();

        closeQuietly(process.getInputStream());
        closeQuietly(process.getErrorStream());
    }

}
