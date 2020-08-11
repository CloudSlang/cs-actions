package io.cloudslang.content.maps.services;

import io.cloudslang.content.maps.entities.MergeMapsInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.serialization.MapDeserializer;
import io.cloudslang.content.maps.serialization.MapSerializer;
import io.cloudslang.content.maps.validators.MergeMapsInputValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MergeMapsService {

    private final MergeMapsInputValidator validator = new MergeMapsInputValidator();

    public @NotNull Map<String,String> execute(@NotNull MergeMapsInput input) throws Exception{
        ValidationException validationException = validator.validate(input);
        if(validationException != null){
            throw validationException;
        }

        MapSerializer serializer = new MapSerializer(input.getPairDelimiter(), input.getEntryDelimiter(),
                input.getMapStart(), input.getMapEnd());
        MapDeserializer deserializer = new MapDeserializer(input.getPairDelimiter(), input.getEntryDelimiter(),
                input.getMapStart(), input.getMapEnd());

        Map<String, String> map1 = deserializer.deserialize(input.getMap1());
        Map<String, String> map2 = deserializer.deserialize(input.getMap2());

        for(Map.Entry<String,String> entry : map2.entrySet()){
            map1.put(entry.getKey(),entry.getValue());
        }

        String returnResult = serializer.serialize(map1);

        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
