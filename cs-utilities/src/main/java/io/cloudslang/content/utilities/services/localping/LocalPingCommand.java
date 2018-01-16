package io.cloudslang.content.utilities.services.localping;

import io.cloudslang.content.utilities.entities.LocalPingInputs;

import java.util.Map;

/**
 * Created by pinteae on 1/11/2018.
 */
public interface LocalPingCommand {
    String createCommand(LocalPingInputs localPingInputs);
    Map<String, String> parseOutput(String output);
}
