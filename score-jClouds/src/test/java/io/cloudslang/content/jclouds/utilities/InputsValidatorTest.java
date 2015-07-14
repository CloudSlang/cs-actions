package io.cloudslang.content.jclouds.utilities;

import io.cloudslang.content.jclouds.entities.inputs.ListRegionsInputs;
import io.cloudslang.content.jclouds.entities.inputs.ProvidersEnum;
import io.cloudslang.content.jclouds.entities.inputs.ServerIdentificationInputs;
import io.cloudslang.content.jclouds.execute.Inputs;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by persdana on 7/13/2015.
 */
public class InputsValidatorTest {
    public static final String SERVER_ID_NOT_SPECIFIED = "The required serverId input is not specified!";
    public static final String ENDPOINT_NOT_SPECIFIED   = "Endpoint input is not specified!";
    public static final String PROVIDER_NOT_SPECIFIED   = "Provider input is not specified! Valid values: openstack, amazon";
    private static final String REGION = "us-east-1";
    private static final String SERVER_ID = "i-578dde87";
    private static final String DELIMITER = ";;";
    private static final String DEFAULT_DELIMITER  = ";" + System.lineSeparator();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Test validateServerIdentificationInputs method. Positive scenario.
     * @throws Exception
     */
    @Test
    public void testValidateServerIdentificationInputs() throws Exception {
        ServerIdentificationInputs inputs = Inputs.getServerIdentificationInputsForAmazon();
        inputs.setServerId(SERVER_ID + " ");
        InputsValidator.validateServerIdentificationInputs(inputs);

        assertEquals(REGION, inputs.getRegion());
        assertEquals(SERVER_ID.trim(), inputs.getServerId());
    }

    /**
     * Test ValidateServerIdentificationInputs method with null server id.
     * @throws Exception
     */
    @Test
    public void testValidateServerIdentificationInputsWithNullServerId() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(SERVER_ID_NOT_SPECIFIED);

        ServerIdentificationInputs inputs = Inputs.getServerIdentificationInputsForAmazon();
        inputs.setServerId(null);
        InputsValidator.validateServerIdentificationInputs(inputs);
    }

    /**
     * Test validateListRegionsInputs method. Positive scenario.
     * @throws Exception
     */
    @Test
    public void testValidateListRegionsInputs() throws Exception {
        ListRegionsInputs inputs = Inputs.getListRegionsInputsForAmazon();
        inputs.setDelimiter(DELIMITER);
        InputsValidator.validateListRegionsInputs(inputs);

        assertEquals(DELIMITER, inputs.getDelimiter());
    }

    /**
     * Test ValidateListRegionsInputs method with null server id.
     * Default delimiter should be set.
     * @throws Exception
     */
    @Test
    public void testValidateListRegionsInputsWithNullServerId() throws Exception {
        ListRegionsInputs inputs = Inputs.getListRegionsInputsForAmazon();
        inputs.setDelimiter(null);
        InputsValidator.validateListRegionsInputs(inputs);

        assertEquals(DEFAULT_DELIMITER, inputs.getDelimiter());
    }

    /**
     * Test ValidateCommonInputs method. Positive scenario.
     * @throws Exception
     */
    @Test
    public void testValidateCommonInputs() throws Exception {
        ListRegionsInputs inputs = Inputs.getListServerInputsForAmazon();
        inputs.setProvider(ProvidersEnum.getProvider("testprov"));
        InputsValidator.validateCommonInputs(inputs);

        assertEquals("testprov", inputs.getProvider().getProviderStr());
    }

    /**
     * Test ValidateCommonInputs method with null Provider.
     * @throws Exception
     */
    @Test
    public void testValidateCommonInputsWithNullProvider() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(PROVIDER_NOT_SPECIFIED);

        ListRegionsInputs inputs = Inputs.getListServerInputsForAmazon();
        inputs.setProvider(ProvidersEnum.getProvider(""));
        InputsValidator.validateCommonInputs(inputs);
    }

    /**
     * Test ValidateCommonInputs method with null Endpoint.
     * @throws Exception
     */
    @Test
    public void testValidateCommonInputsWithNullEndpoint() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ENDPOINT_NOT_SPECIFIED);

        ListRegionsInputs inputs = Inputs.getListServerInputsForAmazon();
        inputs.setEndpoint(null);
        InputsValidator.validateCommonInputs(inputs);
    }
}
