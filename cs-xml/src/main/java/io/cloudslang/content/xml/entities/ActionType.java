/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.xml.entities;

import io.cloudslang.content.xml.utils.Constants;

/**
 * Created by moldovas on 7/8/2016.
 */
public enum ActionType {
    delete(Constants.Inputs.DELETE_ACTION),
    insert(Constants.Inputs.INSERT_ACTION),
    append(Constants.Inputs.APPEND_ACTION),
    subnode(Constants.Inputs.SUBNODE_ACTION),
    move(Constants.Inputs.MOVE_ACTION),
    rename(Constants.Inputs.RENAME_ACTION),
    update(Constants.Inputs.UPDATE_ACTION);
    private String value;

    ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
