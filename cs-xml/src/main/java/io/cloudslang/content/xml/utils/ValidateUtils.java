package io.cloudslang.content.xml.utils;

import io.cloudslang.content.xml.entities.inputs.EditXmlInputs;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by moldovas on 7/6/2016.
 */
public class ValidateUtils {

    public static void validateIsNotEmpty(String action, String message) throws Exception {
        if (action == null || StringUtils.isEmpty(action)) {
            throw new Exception(message);
        }
    }

    public static void validateXmlAndFilePathInputs(String xml, String filePath) throws Exception {
        if ((StringUtils.isBlank(filePath)) && (StringUtils.isBlank(xml))) {
            throw new Exception("Supplied parameters: file path and xml is missing when one is required");
        }
        if ((!StringUtils.isEmpty(filePath)) && (!StringUtils.isEmpty(xml))) {
            throw new Exception("Supplied parameters: file path and xml when only one is required");
        }
    }

    /**
     * Validates the operation inputs.
     *
     * @param inputs@throws Exception for invalid inputs
     */
    public static void validateInputs(EditXmlInputs inputs) throws Exception {
        validateXmlAndFilePathInputs(inputs.getXml(), inputs.getFilePath());

        if (Constants.Inputs.MOVE_ACTION.equals(inputs.getAction())) {
            validateIsNotEmpty(inputs.getXpath2(), "xpath2 input is required for action 'move' ");
        }
        if (!Constants.Inputs.SUBNODE_ACTION.equals(inputs.getAction()) && !Constants.Inputs.MOVE_ACTION.equals(inputs.getAction())) {
            validateIsNotEmpty(inputs.getType(), "type input is required for action '" + inputs.getAction() + "'");
            if (!Constants.Inputs.TYPE_ELEM.equals(inputs.getType()) && !Constants.Inputs.TYPE_ATTR.equals(inputs.getType()) && !Constants.Inputs.TYPE_TEXT.equals(inputs.getType())) {
                throw new Exception("Invalid type. Only supported : " + Constants.Inputs.TYPE_ELEM + ", " + Constants.Inputs.TYPE_ATTR + ", " + Constants.Inputs.TYPE_TEXT);
            }
            if (Constants.Inputs.TYPE_ATTR.equals(inputs.getType())) {
                validateIsNotEmpty(inputs.getName(), "name input is required for type 'attr' ");
            }
        }
    }

    public static void validateInputs(String prettyPrint, String showXmlDeclaration) throws Exception {
        InputUtils.validateBoolean(prettyPrint);
        InputUtils.validateBoolean(showXmlDeclaration);
    }

    public static void validateInputs(String includeRootElement, String includeAttributes, String prettyPrint) throws Exception {
        InputUtils.validateBoolean(includeRootElement);
        InputUtils.validateBoolean(includeAttributes);
        InputUtils.validateBoolean(prettyPrint);
    }

}
