package io.cloudslang.content.filesystem.services;

import io.cloudslang.content.filesystem.entities.IsDirectoryInputs;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import static io.cloudslang.content.filesystem.constants.ExceptionMsgs.INVALID_PATH;
import static io.cloudslang.content.filesystem.constants.ExceptionMsgs.NOT_A_DIRECTORY;

public class IsDirectoryService {

    public @NotNull Map<String,String> execute (@NotNull IsDirectoryInputs inputs) throws Exception{
        File source = new File(inputs.getSource().trim());
        if(!source.isAbsolute() || !source.exists())
            throw new FileNotFoundException(INVALID_PATH);
        if(!source.isDirectory())
            throw new Exception(NOT_A_DIRECTORY);

        return OutputUtilities.getSuccessResultsMap("Source is a directory");
    }
}
