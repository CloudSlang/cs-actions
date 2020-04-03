package io.cloudslang.content.abby.services;

import io.cloudslang.content.abby.entities.ProcessImageInput;
import io.cloudslang.content.abby.utils.ResultUtils;

import java.util.Map;

public class ProcessImageService {

    private ProcessImageInput input;


    public Map<String, String> execute(ProcessImageInput processImageInput) {
        Map<String, String> results = ResultUtils.createNewEmptyMap();
        this.input = processImageInput;
        //TODO
        return results;
    }
}
