/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.dropbox.entities.constants;

/**
 * Created by TusaM
 * 5/26/2017.
 */
public class Constants {
    private Constants() {
        // prevent instantiation
    }

    public static class Api {
        public static final String FOLDERS = "folders";
        public static final String FILES = "files";
    }

    public static class ErrorMessages {
        public static final String CONSTRAINS_ERROR_MESSAGE = "The value doesn't meet conditions for general purpose usage. " +
                "See operation inputs description section for details.";
        public static final String UNKNOWN_BUILDER_TYPE = "Unknown builder type.";
        public static final String UNKNOWN_DROPBOX_HEADER = "Unknown Dropbox header.";
        public static final String UNSUPPORTED_DROPBOX_API = "Unsupported Dropbox API.";
    }

    public static class FolderActions {
        public static final String CREATE_FOLDER = "CreateFolder";
        public static final String DELETE_FILE_OR_FOLDER = "DeleteFileOrFolder";
    }

    public static class HttpClientInputsValues {
        public static final String ALLOW_ALL = "allow_all";
        public static final String APPLICATION_JSON = "application/json";
        public static final String AUTHORIZATION_HEADER_PREFIX = "Authorization:Bearer";
        public static final String BROWSER_COMPATIBLE = "browser_compatible";
        public static final String STRICT = "strict";

    }

    public static class Miscellaneous {
        public static final String BLANK_CHAR = " ";
        public static final String CREATE_FOLDER_PATH_REGEX = "(/(.|[\r\n])*)|(ns:[0-9]+(/.*)?)";
        public static final String DELETE_FOLDER_PATH_REGEX = "(/(.|[\r\n])*)|(ns:[0-9]+(/.*)?)|(id:.*)";
    }

    public static class Values {
        public static final int INIT_INDEX = 0;
        public static final int VERSION_2 = 2;
    }
}