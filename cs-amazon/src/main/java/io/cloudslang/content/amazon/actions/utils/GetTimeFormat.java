package io.cloudslang.content.amazon.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.amazon.services.SchedulerTimestampImpl;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.amazon.entities.constants.Constants.GetTimeFormatConstants.*;
import static io.cloudslang.content.amazon.entities.constants.Constants.SchedulerTimeConstants.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.Common.EXCEPTION_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.GetTimeFormat.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.ProvisionProductAction.FAILURE_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.ProvisionProductAction.SUCCESS_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.SchedulerTime.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.SchedulerTime.TIME_ZONE_DESC;
import static io.cloudslang.content.amazon.entities.validators.Validator.verifySchedulerInputs;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

public class GetTimeFormat {
    @Action(name = GET_TIME_FORMAT_OPERATION_NAME, description = GET_TIME_FORMAT_OPERATION_DESC,
            outputs = {

                    @Output(value = DATE_FORMAT, description = DATE_FORMAT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)},

            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = EPOCH_TIME, required = true, description = EPOCH_TIME_DESC) String epochTime,
            @Param(value = TIME_ZONE, required = true, description = TIME_ZONE_DESC) String timeZone
    ) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put(DATE_FORMAT,LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(epochTime)), ZoneId.of(timeZone.split("\\) ")[1]))+":00");
            return map;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }
}
