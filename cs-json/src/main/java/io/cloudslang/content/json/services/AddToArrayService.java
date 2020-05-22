package io.cloudslang.content.json.services;

import com.google.gson.JsonArray;
import io.cloudslang.content.json.entities.AddToArrayInput;
import io.cloudslang.content.json.validators.AddToArrayValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AddToArrayService {

    private final AddToArrayValidator validator = new AddToArrayValidator();


    public @NotNull Map<String, String> execute(@NotNull AddToArrayInput input) {
        List<RuntimeException> validationErrs = this.validator.validate(input);
        if (!validationErrs.isEmpty()) {
            throw validationErrs.get(0);
        }

        int index;
        if (input.getIndex() == null) {
            index = input.getArray().size();
        } else if (input.getIndex() < 0) {
            index = input.getArray().size() - input.getIndex();
        } else {
            index = input.getIndex();
        }

        JsonArray array = new JsonArray();
        for (int i = 0; i <= input.getArray().size(); i++) {
            if(i == index) {
                array.add(input.getElement());
            } else {
                array.add(input.getArray().get(i));
            }
        }

        String returnResult = array.toString();
        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
