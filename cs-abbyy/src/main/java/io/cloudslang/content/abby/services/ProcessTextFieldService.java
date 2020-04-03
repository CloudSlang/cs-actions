package io.cloudslang.content.abby.services;

import io.cloudslang.content.abby.entities.ProcessTextFieldInput;
import io.cloudslang.content.abby.utils.ResultUtils;

import java.util.Map;

public class ProcessTextFieldService {

    private ProcessTextFieldInput input;


    public Map<String, String> execute(ProcessTextFieldInput processTextFieldInput) {
        Map<String, String> results = ResultUtils.createNewEmptyMap();
        this.input = processTextFieldInput;
        //TODO
        return results;
    }
}
