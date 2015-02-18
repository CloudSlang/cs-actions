package org.openscore.content.json.utils;

/**
 * Created by vranau
 * Date 2/17/2015.
 */
public enum ActionsEnum {
    get(Constants.EditJsonOperations.GET_ACTION),
    insert(Constants.EditJsonOperations.INSERT_ACTION),
    add(Constants.EditJsonOperations.ADD_ACTION),
    update(Constants.EditJsonOperations.UPDATE_ACTION),
    delete(Constants.EditJsonOperations.DELETE_ACTION);
    private String value;

    private ActionsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
