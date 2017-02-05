/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils;

import io.cloudslang.content.utils.BooleanUtilities;
import org.apache.commons.lang3.StringUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;

import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by victor on 13.01.2017.
 */
public class InputsProcessor {

    /**
     * init all the non-static variables.
     *
     * @param sqlInputs
     */
    public static void init(SQLInputs sqlInputs) throws Exception {
        if (sqlInputs != null) {
            sqlInputs.setStrDelim(",");
            sqlInputs.setStrColumns("");
            sqlInputs.setlRows(new ArrayList<String>());
            sqlInputs.setiUpdateCount(0);
            sqlInputs.setSqlCommand(null);
            sqlInputs.setDbServer(null);
            sqlInputs.setDbName(null);
            sqlInputs.setDbType(null);
            sqlInputs.setStrKeyCol(null);
            sqlInputs.setStrKeyFiles(null);
            sqlInputs.setStrKeyNames(null);
            sqlInputs.setStrKeySkip(null);
            sqlInputs.setUsername(null);
            sqlInputs.setPassword(null);
            sqlInputs.setAuthenticationType(null);
            sqlInputs.setDbUrl(null);
//            sqlInputs.setDbUrls(new ArrayList<String>(3));
            sqlInputs.setDbClass(null);
            sqlInputs.setTrimRowstat("true");
            sqlInputs.setNetcool(false);
            sqlInputs.setlRowsFiles(new ArrayList<List<String>>());
            sqlInputs.setlRowsNames(new ArrayList<List<String>>());
            sqlInputs.setSkip(0L);
            sqlInputs.setInstance(null);
            sqlInputs.setTimeout(0);
            sqlInputs.setKey(null);
            sqlInputs.setIgnoreCase(false);
            sqlInputs.setResultSetConcurrency(-1000000);
            sqlInputs.setResultSetType(-1000000);
            sqlInputs.setTrustAllRoots(false);
            sqlInputs.setTrustStore("");
        } else throw new Exception("Cannot init null Sql inputs!");
    }
}
