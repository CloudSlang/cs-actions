package io.cloudslang.content.database.services.dbconnection;

import java.sql.SQLException;

/**
 * class for handling exception when total max pool size exceeded.
 *
 * @author ggu
 */
public class TotalMaxPoolSizeExceedException extends SQLException {
    private static final long serialVersionUID = 1L;

    /**
     * constructor
     *
     * @param aMsg a message
     */
    public TotalMaxPoolSizeExceedException(String aMsg) {
        super(aMsg);
    }

    /**
     * constructor
     *
     * @param aThrowable a Throwable
     */
    public TotalMaxPoolSizeExceedException(Throwable aThrowable) {
        super(aThrowable);
    }

    /**
     * constructor
     *
     * @param aMsg       a message
     * @param aThrowable a Throwable
     */
    public TotalMaxPoolSizeExceedException(String aMsg, Throwable aThrowable) {
        super(aMsg, aThrowable);
    }
}
