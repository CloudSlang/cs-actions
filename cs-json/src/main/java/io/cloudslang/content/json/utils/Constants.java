
package io.cloudslang.content.json.utils;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public final class Constants {

    public static final class InputNames extends io.cloudslang.content.constants.InputNames {
        public static final String JSON_OBJECT = "jsonObject";
        public static final String NEW_PROPERTY_NAME = "newPropertyName";
        public static final String NEW_PROPERTY_VALUE = "newPropertyValue";
        public static final String OBJECT = "object";
        public static final String KEY = "key";
        public static final String ARRAY = "array";
        public static final String ACTION = "action";
        public static final String JSON_PATH = "jsonPath";
        public static final String NAME = "name";
        public static final String VALUE = "value";
        public static final String VALIDATE_VALUE = "validateValue";
    }

    static final class EditJsonOperations {
        static final String GET_ACTION = "get";
        static final String INSERT_ACTION = "insert";
        static final String UPDATE_ACTION = "update";
        static final String DELETE_ACTION = "delete";
        static final String ADD_ACTION = "add";
    }
}
