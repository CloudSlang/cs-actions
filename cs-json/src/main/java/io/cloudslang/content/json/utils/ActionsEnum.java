
package io.cloudslang.content.json.utils;

/**
 * Created by vranau
 * Date 2/17/2015.
 */
public enum ActionsEnum {
    get(Constants.EditJsonOperations.GET_ACTION, false),
    insert(Constants.EditJsonOperations.INSERT_ACTION, true),
    add(Constants.EditJsonOperations.ADD_ACTION, true),
    update(Constants.EditJsonOperations.UPDATE_ACTION, true),
    delete(Constants.EditJsonOperations.DELETE_ACTION, false);
    private final Boolean needValue;
    private String value;

    ActionsEnum(String value, Boolean needValue) {
        this.value = value;
        this.needValue = needValue;
    }

    public String getValue() {
        return value;
    }

    public Boolean getNeedValue() {
        return needValue;
    }
}
