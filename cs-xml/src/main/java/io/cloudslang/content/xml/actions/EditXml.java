/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.xml.entities.ActionType;
import io.cloudslang.content.xml.entities.inputs.EditXmlInputs;
import io.cloudslang.content.xml.factory.OperationFactory;
import io.cloudslang.content.xml.services.OperationService;
import io.cloudslang.content.xml.utils.ValidateUtils;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.xml.utils.Constants.Inputs.ACTION;
import static io.cloudslang.content.xml.utils.Constants.Inputs.FEATURES;
import static io.cloudslang.content.xml.utils.Constants.Inputs.FILE_PATH;
import static io.cloudslang.content.xml.utils.Constants.Inputs.TYPE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.TYPE_NAME;
import static io.cloudslang.content.xml.utils.Constants.Inputs.VALUE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XML;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XPATH1;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XPATH2;

/**
 * Class used for creating @Action operation to edit xml documents.
 * User: turcm
 * Date: 10/16/13
 * Time: 10:57 AM
 */
public class EditXml {

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
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)},
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE)})
    public Map<String, String> xPathReplaceNode(
            @Param(value = XML) String xml,
            @Param(value = FILE_PATH) String filePath,
            @Param(value = ACTION, required = true) String action,
            @Param(value = XPATH1, required = true) String xpath1,
            @Param(value = XPATH2) String xpath2,
            @Param(value = VALUE) String value,
            @Param(value = TYPE) String type,
            @Param(value = TYPE_NAME) String name,
            @Param(value = FEATURES) String parsingFeatures) {

        try {
            final EditXmlInputs inputs = new EditXmlInputs.EditXmlInputsBuilder()
                    .withXml(xml)
                    .withFilePath(filePath)
                    .withAction(action)
                    .withXpath1(xpath1)
                    .withXpath2(xpath2)
                    .withName(name)
                    .withType(type)
                    .withValue(value)
                    .withParsingFeatures(parsingFeatures)
                    .build();
            ValidateUtils.validateInputs(inputs);
            final ActionType myAction = ActionType.valueOf(action.toLowerCase());
            final OperationService operationService = OperationFactory.getOperation(myAction);

            return getSuccessResultsMap(operationService.execute(inputs));
        } catch (IllegalArgumentException e) {
            return getFailureResultsMap("Invalid action " + e.getMessage());
        } catch (Exception e) {
            return getFailureResultsMap(e.getMessage());
        }
    }
}

