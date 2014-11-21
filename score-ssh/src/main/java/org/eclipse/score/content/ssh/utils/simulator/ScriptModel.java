package org.eclipse.score.content.ssh.utils.simulator;

import org.eclipse.score.content.ssh.utils.simulator.elements.AlwaysOn;
import org.eclipse.score.content.ssh.utils.simulator.elements.Expect;
import org.eclipse.score.content.ssh.utils.simulator.elements.ScriptElement;
import org.eclipse.score.content.ssh.utils.simulator.elements.Send;

import java.util.ArrayList;

public class ScriptModel {

    private ArrayList<ScriptElement> commands;
    private ArrayList<AlwaysOn> continuousCommands;

    public ScriptModel(String script, char[] newLineCharacter) throws Exception {
        commands = new ArrayList<>();
        continuousCommands = new ArrayList<>();
        if (script.length() > 0)
            parseScript(new ScriptLines(script), newLineCharacter);
    }

    public AlwaysOn checkAlwaysHandlers(String current, long matchTimeout) throws Exception {
        for (AlwaysOn curr : continuousCommands) {
            if (curr.match(current.trim(), matchTimeout)) {
                return curr;
            }
        }
        return null;
    }

    public Expect IsExceptAllowed(String current, ScriptRunner s, long matchTimeout) throws Exception {
        if (commands.size() > 0 && commands.get(0) instanceof Expect) {
            Expect e = (Expect) commands.get(0);
            if (e.waitIfNeeded())
                s.setReadTime();
            if (e.match(current.trim(), matchTimeout)) {
                commands.remove(0);
                return e;
            }
        }
        return null;
    }

    public Send IsSendAllowed() throws Exception {
        if (commands.size() > 0 && commands.get(0) instanceof Send) {
            return (Send) commands.remove(0);
        } else
            return null;
    }

    public int getCommandsLeft() {
        return commands.size();
    }

    private void parseScript(ScriptLines script, char[] newLineCharacter) throws Exception {
        for (boolean running = true; running; running = script.nextLine()) {
            String line = script.getCurrentLine();
            if (line.toLowerCase().startsWith("send")) {
                commands.add(Send.getInstance(line, newLineCharacter));
            } else if (line.toLowerCase().startsWith("expect")) {
                Expect curr = Expect.getInstance(line);
                commands.add(curr);
                String next = script.peekNext();
                if (next != null && next.toLowerCase().startsWith("erroron")) {
                    running = script.nextLine();
                    curr.setError(Expect.getInstance(next.substring(next.indexOf(' '))));
                }
            } else if (line.toLowerCase().startsWith("always"))
                continuousCommands.add(AlwaysOn.getInstance(script, newLineCharacter));
            else if (line.toLowerCase().startsWith("wait")) {
                parseWaitScript(script, newLineCharacter);
            } else
                throw new Exception("unsupported operation: " + script);
        }
    }

    private void parseWaitScript(ScriptLines script, char[] newLineCharacter) throws Exception {
        String line = script.getCurrentLine();
        String on = "";
        if (line.toLowerCase().startsWith("wait")) {
            String[] segments = line.split(" ");
            long time;
            try {
                time = Long.parseLong(segments[1]);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Unable to process wait command. An exception occurred while parsing wait length." + e.getMessage());
            }
            for (int count = 2; count < segments.length; count++)
                if (count == 2)
                    on = segments[count];
                else
                    on += " " + segments[count];
            if (on.trim().length() == 0) {
                if (!script.nextLine())
                    throw new Exception("Wait found at end of script. Wait must be followed by a send or expect");
                else
                    on = script.getCurrentLine();
            }
            if (on.startsWith("send")) {
                Send op = Send.getInstance(on, newLineCharacter);
                commands.add(op);
                op.setWaitTime(time);
            } else if (on.startsWith("expect")) {
                Expect op = Expect.getInstance(on);
                commands.add(op);
                op.setWaitTime(time);
            } else throw new Exception("unsupported operation: " + script);
        } else throw new Exception("unsupported wait operation: " + script);
    }
}
