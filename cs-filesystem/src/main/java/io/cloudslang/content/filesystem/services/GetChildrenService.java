package io.cloudslang.content.filesystem.services;

import io.cloudslang.content.filesystem.entities.GetChildrenInputs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.filesystem.constants.Constants.RETURN_CODE_SUCCESS;
import static io.cloudslang.content.filesystem.constants.ResultsName.COUNT;

public class GetChildrenService {

    public static Map<String, String> execute(GetChildrenInputs getChildrenInputs) throws IOException {
        Map<String, String> result = new HashMap<>();

        File[] children;
            File f = new java.io.File(getChildrenInputs.getSource());
            if (!f.exists())
                throw new RuntimeException(getChildrenInputs.getSource() + ": The specified file or folder does not exist.");
            else if (!f.isDirectory())
                throw new RuntimeException(getChildrenInputs.getSource() + ": The specified path is a file, this operation only supports directories.");
            children = f.listFiles();

        if (children == null)
            throw new RuntimeException("The specified path does not have any children, or it could not be read.");

        String paths = "";
        for (int count = 0; count < children.length; count++) {
            if (!paths.isEmpty())
                paths += getChildrenInputs.getDelimiter();
            paths += children[count].getCanonicalPath();
        }
        result.put(RETURN_CODE, RETURN_CODE_SUCCESS);
        result.put(RETURN_RESULT, paths);
        result.put(COUNT, String.valueOf(children.length));

        return result;
    }
}
