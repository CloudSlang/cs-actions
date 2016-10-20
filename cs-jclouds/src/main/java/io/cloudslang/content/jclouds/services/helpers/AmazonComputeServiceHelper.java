package io.cloudslang.content.jclouds.services.helpers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;

/**
 * Created by Mihai Tusa.
 * 2/23/2016.
 */
public class AmazonComputeServiceHelper {
    public Multimap<String, String> getInstanceFiltersMap(InstanceInputs instanceInputs, String delimiter) {
        Multimap<String, String> filtersMap = ArrayListMultimap.create();
        new FiltersHelper().updateInstanceFiltersMap(instanceInputs, filtersMap, delimiter);

        return filtersMap;
    }
}