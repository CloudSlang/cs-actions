package io.cloudslang.content.mail.constants;

public final class ExceptionMsgs {
    public static final String UNRECOGNIZED_SSL_MESSAGE = "Unrecognized SSL message";
    public static final String UNRECOGNIZED_SSL_MESSAGE_PLAINTEXT_CONNECTION = "Unrecognized SSL message, plaintext " +
            "connection?";
    public static final String MESSAGES_ARE_NUMBERED_STARTING_AT_1 = "Messages are numbered starting at 1 through " +
            "the total number of messages in the folder!";
    public static final String MESSAGE_NUMBER_NOT_SPECIFIED = "The required messageNumber input is not specified!";
    public static final String SPECIFY_PORT_OR_PROTOCOL_OR_BOTH = "Please specify the port, the protocol, or both.";
    public static final String SPECIFY_PROTOCOL_FOR_GIVEN_PORT = "Please specify the protocol for the indicated port.";
    public static final String SPECIFY_PORT_FOR_PROTOCOL = "Please specify the port for the indicated protocol.";
    public static final String THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER = "The specified folder does " +
            "not exist on the remote server.";
    public static final String PRIVATE_KEY_ERROR_MESSAGE = "Can't find a private key!";
    public static final String PUBLIC_KEY_ERROR_MESSAGE = "Can't find a public key!";
    public static final String HOST_NOT_SPECIFIED = "The required host input is not specified!";
    public static final String USERNAME_NOT_SPECIFIED = "The required username input is not specified!";
    public static final String FOLDER_NOT_SPECIFIED = "The required folder input is not specified!";
    public static final String COUNT_MESSAGES_IN_FOLDER_ERROR_MESSAGE = " messages in folder";
    public static final String INVALID_DELIMITERS = "The columnDelimiter and rowDelimiter inputs have the " +
            "same value. They need to be different.";
    public static final String INVALID_ROW_DELIMITER = "The rowDelimiter can't be a substring of the columnDelimiter!";
    public static final String ROW_WITH_MULTIPLE_COLUMN_DELIMITERS_IN_HEADERS_INPUT = "Row #%d in the 'headers' " +
            "input has more than one column delimiter.";
    public static final String ROW_WITH_EMPTY_HEADERS_INPUT = "Row #%d in the 'headers' input does not contain " +
            "any values.";
    public static final String ROW_WITH_MISSING_VALUE_FOR_HEADER = "Row #%d in the 'headers' input is missing one " +
            "of the header values.";
    public static final String TIMEOUT_MUST_BE_POSITIVE = "timeout value must be a positive number";
    public static final String INVALID_PORT_NUMBER = "Invalid port number";
}
