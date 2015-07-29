/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.rft.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Date: 7/21/2015
 *
 * @author lesant
 */
public class Constants {
    public static final String EMPTY_STRING = "";
    public static final String NO_ACK_RECEIVED = "No ack received";
    public static final String STDOUT = "STDOUT";
    public static final String STDERR = "STDERR";

    // default values
    public static final int DEFAULT_PORT = 22;
    public static final int DEFAULT_TIMEOUT = 90000;
    public static final String DEFAULT_KNOWN_HOSTS_POLICY = "allow";
    public static final Path DEFAULT_KNOWN_HOSTS_PATH = Paths.get(System.getProperty("user.home"), ".ssh", "known_hosts");

    public static final class InputNames {
        public static final String SRC_HOST = "srcHost";
        public static final String SRC_PATH = "srcPath";
        public static final String SRC_PORT = "srcPort";
        public static final String SRC_PRIVATE_KEY_FILE = "srcPrivateKeyFile";
        public static final String SRC_USERNAME = "srcUsername";
        public static final String SRC_PASSWORD = "srcPassword";
        public static final String DEST_HOST = "destHost";
        public static final String DEST_PATH = "destPath";
        public static final String DEST_PORT = "destPort";
        public static final String DEST_PRIVATE_KEY_FILE = "destPrivateKeyFile";
        public static final String DEST_USERNAME = "destUsername";
        public static final String DEST_PASSWORD = "destPassword";
        public static final String KNOWN_HOSTS_POLICY = "knownHostsPolicy";
        public static final String KNOWN_HOSTS_PATH = "knownHostsPath";
        public static final String TIMEOUT = "timeout";

    }

    public static final class OutputNames {

        public static final String RETURN_RESULT = "returnResult";
        public static final String EXCEPTION = "exception";
        public static final String RETURN_CODE = "returnCode";
    }

    public static final class ReturnCodes {

        public static final String RETURN_CODE_FAILURE = "-1";
        public static final String RETURN_CODE_SUCCESS = "0";
    }

    public static final class ResponseNames {

        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

}