

package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.entities.inputs.EditXmlInputs;

/**
 * Created by moldovas on 7/8/2016.
 */
public interface OperationService {
    String execute(EditXmlInputs inputs) throws Exception;
}
