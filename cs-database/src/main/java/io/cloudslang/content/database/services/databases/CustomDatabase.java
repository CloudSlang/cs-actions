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
