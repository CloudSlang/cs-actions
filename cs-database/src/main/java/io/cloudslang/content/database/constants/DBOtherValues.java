/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.constants;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pinteae on 1/17/2017.
 */
public class DBOtherValues {
    public static final String ZERO = "0";

    public final static String SET_NOCOUNT_ON = "SET NOCOUNT ON";

    public static final String CONCUR_READ_ONLY = "CONCUR_READ_ONLY";
    public static final String CONCUR_UPDATABLE = "CONCUR_UPDATABLE";

    public static final String TYPE_FORWARD_ONLY = "TYPE_FORWARD_ONLY";
    public static final String TYPE_SCROLL_INSENSITIVE = "TYPE_SCROLL_INSENSITIVE";
    public static final String TYPE_SCROLL_SENSITIVE = "TYPE_SCROLL_SENSITIVE";

    public static final Map<String, Integer> CONCUR_VALUES = createConcurValues();
    public static final Map<String, Integer> TYPE_VALUES = createTypeValues();

    private static Map<String, Integer> createConcurValues() {
        final Map<String, Integer> concurValues = new HashMap<>();
        concurValues.put(CONCUR_READ_ONLY, ResultSet.CONCUR_READ_ONLY);
        concurValues.put(CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
        return concurValues;
    }

    private static Map<String, Integer> createTypeValues() {
        final Map<String, Integer> typeValues = new HashMap<>();
        typeValues.put(TYPE_FORWARD_ONLY, ResultSet.TYPE_FORWARD_ONLY);
        typeValues.put(TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_INSENSITIVE);
        typeValues.put(TYPE_SCROLL_SENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE);
        return typeValues;
    }
//    NO_RESULT_SET(-1000000, "NO_RESULT_SET");
}
