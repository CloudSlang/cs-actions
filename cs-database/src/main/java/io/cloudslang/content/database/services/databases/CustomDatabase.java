/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services.databases;


import org.apache.commons.lang3.StringUtils;

/**
 * Created by vranau on 12/10/2014.
 */
public class CustomDatabase {

    public void setUp(String dbClass) throws ClassNotFoundException {
        if (StringUtils.isNoneEmpty(dbClass)) {
            Class.forName(dbClass);
        } else {
            throw new ClassNotFoundException("No db class name provided");
        }
    }
}
