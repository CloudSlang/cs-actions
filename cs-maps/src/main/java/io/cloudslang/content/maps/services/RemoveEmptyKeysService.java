package io.cloudslang.content.maps.services;
import io.cloudslang.content.maps.entities.RemoveEmptyKeysInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.utils.MapSerializer;
import io.cloudslang.content.maps.validators.RemoveEmptyKeysValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class RemoveEmptyKeysService {

    private final RemoveEmptyKeysValidator validator = new RemoveEmptyKeysValidator();


    public @NotNull Map<String, String> execute(@NotNull RemoveEmptyKeysInput input) throws Exception {
        ValidationException validationEx = validator.validate(input);
        if (validationEx != null) {
            throw validationEx;
        }

        MapSerializer serializer = new MapSerializer(
                input.getPairDelimiter(), input.getEntryDelimiter(),
                input.getMapStart(), input.getMapEnd(),
                input.getElementWrapper(), input.isStripWhitespaces(),false);

        Map<String, String> map = serializer.deserialize(input.getMap());

        Map<String, String> result = map.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String returnResult = serializer.serialize(result);

        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
