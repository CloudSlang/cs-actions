package io.cloudslang.content.rft.actions;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        RemoteSecureCopyAction rsca = new RemoteSecureCopyAction();

        Map<String, String> result = rsca.execute(
                "16.32.6.16",
                "C:\\Users\\Administrator\\Desktop\\workspace\\test.txt",
                "",
                "Administrator",
                "B33f34t3r",
                "",
                "16.32.6.81",
                "C:\\Users\\Administrator\\Desktop\\workspace\\spike_lenuta_upgrade_jsch.txt",
                "",
                "Administrator",
                "B33f34t3r",
                "",
                "allow",
                "",
                "",
                "",
                "");

        for (String s : result.keySet()) {
            System.out.println(s + ": " + result.get(s));
        }

    }
}
