package io.cloudslang.content.amazon.services;

import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;

import java.util.Set;

/**
 * Created by persdana on 5/27/2015.
 */
public interface ComputeService {
    Set<String> describeInstancesInRegion(CommonInputs commonInputs, InstanceInputs instanceInputs) throws Exception;
}