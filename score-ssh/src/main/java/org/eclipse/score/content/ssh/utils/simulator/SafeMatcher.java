package org.eclipse.score.content.ssh.utils.simulator;

public class SafeMatcher extends Thread {
    String toTest;
    String regex;
    boolean match;

    private SafeMatcher(String toTest, String regex) {
        this.toTest = toTest;
        this.regex = regex;
        match = false;
    }

    public void run() {
        match = toTest.matches(regex);
    }

    @SuppressWarnings("deprecation")
    public static boolean match(String toTest, String regex, long timeOut) throws Exception {
        SafeMatcher t = new SafeMatcher(toTest, regex);
        long startTime = System.currentTimeMillis();
        t.start();
        while (t.isAlive() && (System.currentTimeMillis() - startTime) < timeOut) {
            Thread.sleep(50);
        }
        if (t.isAlive()) {
            t.stop();
            throw new Exception("match timed out after: " + (System.currentTimeMillis() - startTime) + "ms");
        }
        return t.match;
    }
}
