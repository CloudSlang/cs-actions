package io.cloudslang.content.jclouds.execute.tags;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.TagFactory;
import io.cloudslang.content.jclouds.services.TagService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 7/21/2016.
 */
public class ApplyToResourcesExecutor {
    private static final String APPLIED_TO_RESOURCES_SUCCESSFULLY = "Apply tags to resources process started successfully.";

    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs) throws Exception {
        TagService tagService = TagFactory.getTagService(commonInputs);
        tagService.applyToResources(commonInputs, customInputs);

        return OutputsUtil.getResultsMap(APPLIED_TO_RESOURCES_SUCCESSFULLY);
    }
}