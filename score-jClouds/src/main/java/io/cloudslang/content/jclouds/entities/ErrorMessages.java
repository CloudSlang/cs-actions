package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 4/27/2016.
 */
public class ErrorMessages {
    public static final String SERVER_NOT_FOUND = "Server not found.";

    public static final String DIFFERENT_KEYS_AND_VALUES_NUMBER = "The inputs: [tagKeys] and [tagValues] should " +
            "have same number of elements.";

    public static final String DIFFERENT_LENGTH = "The inputs: [processorsCores] and [processorsSpeed] should " +
            "have same number of elements.";

    public static final String UNCORRELATED_PROCESSOR_INPUTS_VALUES = "Uncorrelated input values: [processorsNumber], " +
            "[processorsCores] and [processorsSpeed].";
}