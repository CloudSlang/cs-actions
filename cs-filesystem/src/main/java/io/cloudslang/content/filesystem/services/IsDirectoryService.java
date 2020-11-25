package io.cloudslang.content.filesystem.services;

import io.cloudslang.content.filesystem.entities.IsDirectoryInputs;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import static io.cloudslang.content.filesystem.constants.ExceptionMsgs.*;
import static io.cloudslang.content.filesystem.utils.Utils.validateIsDirectory;

public class IsDirectoryService {

    public @NotNull Map<String,String> execute (@NotNull IsDirectoryInputs inputs) throws Exception{
        File source = new File(inputs.getSource().trim());
        validateIsDirectory(source);
        return OutputUtilities.getSuccessResultsMap("Source is a directory");
    }

//    public void validateSource(File source) throws Exception{
//        if(!source.isAbsolute())
//            throw new IllegalArgumentException(INVALID_ABSOLUTE_PATH);
//        if(!source.exists())
//            throw new FileNotFoundException(DOES_NOT_EXIST);
//        if(!source.isDirectory())
//            throw new Exception(NOT_A_DIRECTORY);
//    }
}
