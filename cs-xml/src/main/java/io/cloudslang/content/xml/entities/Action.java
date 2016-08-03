package io.cloudslang.content.xml.entities;

import io.cloudslang.content.xml.utils.Constants;

/**
 * Created by moldovas on 7/8/2016.
 */
public enum Action {
    delete(Constants.Inputs.DELETE_ACTION),
    insert(Constants.Inputs.INSERT_ACTION),
    append(Constants.Inputs.APPEND_ACTION),
    subnode(Constants.Inputs.SUBNODE_ACTION),
    move(Constants.Inputs.MOVE_ACTION),
    rename(Constants.Inputs.RENAME_ACTION),
    update(Constants.Inputs.UPDATE_ACTION);
    private String value;

    Action(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
