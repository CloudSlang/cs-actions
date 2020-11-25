package io.cloudslang.content.filesystem.utils;

import java.io.File;
import java.io.FileNotFoundException;

import static io.cloudslang.content.filesystem.constants.ExceptionMsgs.*;

public class Utils {

    public static void validateIsDirectory(File source) throws Exception{
        if(!source.isAbsolute())
            throw new IllegalArgumentException(INVALID_ABSOLUTE_PATH);
        if(!source.exists())
            throw new FileNotFoundException(DOES_NOT_EXIST);
        if(!source.isDirectory())
            throw new Exception(NOT_A_DIRECTORY);
    }
}
