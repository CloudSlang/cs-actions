package org.openscore.content.ssh.utils.simulator;

import java.util.ArrayList;

public class ScriptLines {
    String[] lines;
    int index;

    public ScriptLines(String script) {
        String[] currLines = script.trim().split("\n");
        ArrayList<String> lines = new ArrayList<>();
        for (String currLine : currLines)
            if (currLine.trim().length() > 0)
                lines.add(currLine.trim());
        this.lines = lines.toArray(new String[lines.size()]);
        index = 0;
    }

    public String getCurrentLine() {
        return lines[index];
    }

    public boolean nextLine() {
        if (hasNext()) {
            index++;
            return true;
        } else
            return false;
    }

    public boolean hasNext() {
        return index + 1 < lines.length;
    }

    public String peekNext() {
        if (hasNext())
            return lines[index + 1];
        else
            return null;
    }

    public String toString() {
        StringBuilder ret = new StringBuilder("Script:currently on line: " + (index + 1) + "\n");
        for (int count = 0; count < lines.length; count++) {
            ret.append("").append(count + 1).append(" ").append(lines[count]);
        }
        return ret.toString();
    }
}
