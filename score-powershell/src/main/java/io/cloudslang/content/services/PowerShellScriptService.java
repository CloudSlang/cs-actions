package io.cloudslang.content.services;

import io.cloudslang.content.entities.PowerShellInputs;

import java.util.Map;

/**
 * User: bancl
 * Date: 10/9/2015
 */
public interface PowerShellScriptService {
    Map<String, String> execute(PowerShellInputs inputs);
}
