package io.cloudslang.content.excel.entities;

/**
 * Exception wrapper class for Excel
 *
 * @author shexiaoy
 */
public class ExcelOperationException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * constructor
     *
     * @param message
     */
    public ExcelOperationException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param aThrowable a Throwable
     */

    public ExcelOperationException(Throwable aThrowable) {
        super(aThrowable);
    }

    /**
     * constructor
     *
     * @param message    a message
     * @param aThrowable a Throwable
     */
    public ExcelOperationException(String message, Throwable aThrowable) {
        super(message, aThrowable);
    }

}
