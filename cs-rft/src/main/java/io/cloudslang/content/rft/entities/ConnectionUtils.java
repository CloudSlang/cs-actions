/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.rft.entities;


import io.cloudslang.content.rft.utils.Constants;
import io.cloudslang.content.rft.utils.StringUtils;

import java.nio.file.Path;

/**
 * Date: 7/30/2015
 *
 * @author lesant
 */
public class ConnectionUtils {

    public static KeyFile getKeyFile(String privateKeyFile, String privateKeyPassPhrase) {
        KeyFile keyFile = null;
        if (privateKeyFile != null && !privateKeyFile.isEmpty()) {
            if (privateKeyPassPhrase != null && !privateKeyPassPhrase.isEmpty()) {
                keyFile = new KeyFile(privateKeyFile, privateKeyPassPhrase);
            } else {
                keyFile = new KeyFile(privateKeyFile);
            }
        }
        return keyFile;
    }

    public static KnownHostsFile resolveKnownHosts(String policy, String path) {
        String knownHostsPolicy = StringUtils.toNotEmptyString(policy, Constants.DEFAULT_KNOWN_HOSTS_POLICY);
        Path knownHostsPath = StringUtils.toPath(path, Constants.DEFAULT_KNOWN_HOSTS_PATH);

        return new KnownHostsFile(knownHostsPath, knownHostsPolicy);
    }
}
