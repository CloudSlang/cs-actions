package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.inputs.ListServersInputs;
import io.cloudslang.content.jclouds.entities.inputs.ProvidersEnum;
import io.cloudslang.content.jclouds.execute.Inputs;
import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.impl.AmazonComputeService;
import io.cloudslang.content.jclouds.services.impl.ComputeServiceImpl;
import io.cloudslang.content.jclouds.services.impl.OpenstackComputeService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

/**
 * Created by persdana on 7/13/2015.
 */
public class ComputeFactoryTest {
    private static final String INVALID_PROVIDER = "Provider input is not specified! Valid values: openstack, amazon";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetComputeServiceWithOpenstack() throws Exception {
        ListServersInputs inputs = Inputs.getListServerInputsForAmazon();
        inputs.setProvider(ProvidersEnum.getProvider("openstack"));

        ComputeService result = ComputeFactory.getComputeService(inputs);

        assertTrue(result instanceof OpenstackComputeService);
    }

    @Test
    public void testGetComputeServiceWithAmazon() throws Exception {
        ListServersInputs inputs = Inputs.getListServerInputsForAmazon();
        inputs.setProvider(ProvidersEnum.getProvider("amazon"));

        ComputeService result = ComputeFactory.getComputeService(inputs);

        assertTrue(result instanceof AmazonComputeService);
    }

    @Test
    public void testGetComputeServiceWithEc2() throws Exception {
        ListServersInputs inputs = Inputs.getListServerInputsForAmazon();
        inputs.setProvider(ProvidersEnum.getProvider("ec2"));

        ComputeService result = ComputeFactory.getComputeService(inputs);

        assertTrue(result instanceof ComputeServiceImpl);
    }

    @Test
    public void testGetComputeServiceWithInvalidProvider() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(INVALID_PROVIDER);

        ListServersInputs inputs = Inputs.getListServerInputsForAmazon();
        inputs.setProvider(ProvidersEnum.getProvider(""));

        ComputeFactory.getComputeService(inputs);
    }
}
