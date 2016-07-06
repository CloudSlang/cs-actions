package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.xml.entities.Constants;
import io.cloudslang.content.xml.utils.ValidateUtils;
import io.cloudslang.content.xml.utils.XmlOperationsUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.xml.utils.ValidateUtils.validateIsNotEmpty;

/**
 * Class used for creating @Action operation to edit xml documents.
 * User: turcm
 * Date: 10/16/13
 * Time: 10:57 AM
 */
public class EditXml {

    private String features;

    /**
     * @param xml             The XML (in the form of a String).
     * @param filePath        Absolute or remote path of the XML file.
     * @param action          The edit action to take place.
     *                        Valid values: delete, insert, append, createSubnode, move, rename, update.
     * @param xpath1          The XPath Query that is wanted to be run.
     *                        The changes take place at the resulting elements.
     * @param xpath2          The XPath Query that is wanted to be run.
     *                        For the move action the results of xpath1 are moved to the results of xpath2.
     * @param value           The new value.
     *                        Examples: <newNode>newNodeValue</newNode> ,
     *                        <newNode newAttribute="newAttributeValue">newNodeValue</newNode>, new value.
     * @param type            Defines on what should the changes take effect :
     *                        the element, the value of the element or the attributes of the element.
     *                        Valid values: elem, text, attr
     * @param name            The name of the attribute in case the selected type is 'attr' .
     * @param parsingFeatures The list of XML parsing features separated by new line (CRLF).
     *                        The feature name - value must be separated by empty space.
     *                        Setting specific features this field could be used to avoid XML security issues like
     *                        "XML Entity Expansion injection" and "XML External Entity injection".
     *                        To avoid aforementioned security issues we strongly recommend to set this input to the following values:
     *                        http://apache.org/xml/features/disallow-doctype-decl true
     *                        http://xml.org/sax/features/external-general-entities false
     *                        http://xml.org/sax/features/external-parameter-entities false
     *                        When the "http://apache.org/xml/features/disallow-doctype-decl" feature is set to "true"
     *                        the parser will throw a FATAL ERROR if the incoming document contains a DOCTYPE declaration.
     *                        When the "http://xml.org/sax/features/external-general-entities" feature is set to "false"
     *                        the parser will not include external general entities.
     *                        When the "http://xml.org/sax/features/external-parameter-entities" feature is set to "false"
     *                        the parser will not include external parameter entities or the external DTD subset.
     *                        If any of the validations fails, the operation will fail with an error message describing the problem.
     *                        Default value:
     *                        http://apache.org/xml/features/disallow-doctype-decl true
     *                        http://xml.org/sax/features/external-general-entities false
     *                        http://xml.org/sax/features/external-parameter-entities false
     * @return map of results containing success or failure text, a result message, and the value selected
     */
    @Action(name = "Edit XML",
            outputs = {
                    @Output(Constants.Outputs.RETURN_RESULT),
                    @Output(Constants.Outputs.RETURN_CODE),
                    @Output(Constants.Outputs.EXCEPTION)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.ReturnCodes.SUCCESS, value = "0"),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.ReturnCodes.FAILURE, value = "-1")})
    public Map<String, String> xPathReplaceNode(
            @Param(value = Constants.Inputs.XML) String xml,
            @Param(value = Constants.Inputs.FILE_PATH) String filePath,
            @Param(value = Constants.Inputs.ACTION, required = true) String action,
            @Param(value = Constants.Inputs.XPATH1, required = true) String xpath1,
            @Param(value = Constants.Inputs.XPATH2) String xpath2,
            @Param(value = Constants.Inputs.VALUE) String value,
            @Param(value = Constants.Inputs.TYPE) String type,
            @Param(value = Constants.Inputs.TYPE_NAME) String name,
            @Param(value = Constants.Inputs.FEATURES) String parsingFeatures) {
        Map<String, String> result = new HashMap<>();
        try {
            validateInputs(xml, filePath, action.toLowerCase(), xpath1, xpath2, type, name, parsingFeatures);
            ActionsEnum myAction = getAction(action);

            switch (myAction) {
                case delete:
                    result.put(Constants.Outputs.RETURN_RESULT, XmlOperationsUtils.delete(xml, filePath, xpath1, type, name, features));
                    break;
                case insert:
                    result.put(Constants.Outputs.RETURN_RESULT, XmlOperationsUtils.insert(xml, filePath, xpath1, value, type, name, features));
                    break;
                case append:
                    result.put(Constants.Outputs.RETURN_RESULT, XmlOperationsUtils.append(xml, filePath, xpath1, value, type, name, features));
                    break;
                case subnode:
                    result.put(Constants.Outputs.RETURN_RESULT, XmlOperationsUtils.createSubnode(xml, filePath, xpath1, value, features));
                    break;
                case move:
                    result.put(Constants.Outputs.RETURN_RESULT, XmlOperationsUtils.move(xml, filePath, xpath1, xpath2, features));
                    break;
                case rename:
                    result.put(Constants.Outputs.RETURN_RESULT, XmlOperationsUtils.rename(xml, filePath, xpath1, name, type, value, features));
                    break;
                case update:
                    result.put(Constants.Outputs.RETURN_RESULT, XmlOperationsUtils.update(xml, filePath, xpath1, value, type, name, features));
                    break;
            }
            result.put(Constants.Outputs.RETURN_CODE, String.valueOf(Constants.ReturnCodes.SUCCESS));
        } catch (IllegalArgumentException e) {
            result.put(Constants.Outputs.EXCEPTION, "Invalid action " + action);
            result.put(Constants.Outputs.RETURN_CODE, String.valueOf(Constants.ReturnCodes.FAILURE));
        } catch (Exception e) {
            result.put(Constants.Outputs.EXCEPTION, e.getMessage());
            result.put(Constants.Outputs.RETURN_CODE, String.valueOf(Constants.ReturnCodes.FAILURE));
        }
        return result;
    }

    private ActionsEnum getAction(String action) {
        return ActionsEnum.valueOf(action.toLowerCase());
    }

    /**
     * Validates the operation inputs.
     *
     * @param xml      the xml as String
     * @param filePath the path/remote path to the file
     * @param action   the edit action
     * @param xpath1   the first XPath
     * @param xpath2   the second XPath
     * @param type     the type on which the edit should occur
     * @param name     the name of the attribute
     * @throws Exception for invalid inputs
     */
    private void validateInputs(String xml, String filePath, String action, String xpath1, String xpath2, String type, String name, String features) throws Exception {
        ValidateUtils.validateXmlAndFilePathInputs(xml, filePath);
        validateIsNotEmpty(action, "action input is required.");
        validateIsNotEmpty(xpath1, "xpath1 input is required.");

        if (Constants.Inputs.MOVE_ACTION.equals(action)) {
            validateIsNotEmpty(xpath2, "xpath2 input is required for action 'move' ");
        }
        if (!Constants.Inputs.SUBNODE_ACTION.equals(action) && !Constants.Inputs.MOVE_ACTION.equals(action)) {
            validateIsNotEmpty(type, "type input is required for action '" + action + "'");
            if (!Constants.Inputs.TYPE_ELEM.equals(type) && !Constants.Inputs.TYPE_ATTR.equals(type) && !Constants.Inputs.TYPE_TEXT.equals(type)) {
                throw new Exception("Invalid type. Only supported : " + Constants.Inputs.TYPE_ELEM + ", " + Constants.Inputs.TYPE_ATTR + ", " + Constants.Inputs.TYPE_TEXT);
            }
            if (Constants.Inputs.TYPE_ATTR.equals(type)) {
                validateIsNotEmpty(name, "name input is required for type 'attr' ");
            }
        }

        setFeatures(features);
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    /**
     * Enum of all possible edit actions of the operation.
     */
    public enum ActionsEnum {
        delete(Constants.Inputs.DELETE_ACTION),
        insert(Constants.Inputs.INSERT_ACTION),
        append(Constants.Inputs.APPEND_ACTION),
        subnode(Constants.Inputs.SUBNODE_ACTION),
        move(Constants.Inputs.MOVE_ACTION),
        rename(Constants.Inputs.RENAME_ACTION),
        update(Constants.Inputs.UPDATE_ACTION);
        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}

