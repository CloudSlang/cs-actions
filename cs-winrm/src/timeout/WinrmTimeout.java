package timeout;

import io.cloudsoft.winrm4j.client.WinRmClientContext;
import io.cloudsoft.winrm4j.winrm.WinRmTool;
import io.cloudsoft.winrm4j.winrm.WinRmToolResponse;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.client.config.AuthSchemes;

public class WinrmTimeout {

    public static void main(String[] args) throws InterruptedException {

        WinRmClientContext context = WinRmClientContext.newInstance();

        final WinRmTool tool = WinRmTool.Builder.builder("ps7-win.ros.swinfra.net", "Administrator", "B33f34t3r")
                .authenticationScheme(AuthSchemes.NTLM)
                .disableCertificateChecks(true)
                .port(5985)
                .useHttps(false)
                .context(context)
                .build();

        long userTimeout = 10l;

        StopWatch watch = new StopWatch();
        watch.start();
        WinRmToolResponse res = tool.executePs("sleep 10");
        watch.stop();
        long result = watch.getTime();

        if (result > userTimeout) {

            context.shutdown();
            //Thread.currentThread().stop();
            Thread.currentThread().interrupt();
            System.out.println("Is Thread interrupted ? " + Thread.currentThread().isInterrupted());

            throw new RuntimeException("timeout exception");

        } else
            context.shutdown();

        System.out.println("StatusCode: " + res.getStatusCode());
        System.out.println("StdErr: " + res.getStdErr());
        System.out.println("Stdout: " + res.getStdOut());
    }

}
