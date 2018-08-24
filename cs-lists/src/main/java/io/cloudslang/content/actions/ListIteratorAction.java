package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.services.ListService;

import java.util.Map;

public class ListIteratorAction {

    /**
     * This operation is used to iterate a list of values with the help of GlobalSessionObject in order to keep track of the last index. It is not recommended to modify the value of the "list" and "separator" inputs during the iteration process.
     *
     * @param list      The list to iterate through.
     * @param separator A delimiter separating the list elements. This may be single character, multi-characters or special characters.
     *                  Default value: ‘,’ (comma).
     * @return resultString The current list element (if the response is "has more").
     */
    @Action(name = "List Iterator",
            outputs = {
                    @Output("resultString")},
            responses = {
                    @Response(text = "has more", field = "result", value = "has more", matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "no more", field = "result", value = "no more", matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "failure", field = "result", value = "failure", matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(@Param("list") String list,
                                       @Param("separator") String separator,
                                       @Param("globalSessionObject") GlobalSessionObject<Map<String, Object>> globalSessionObject) {
        return ListService.iterate(list, separator, globalSessionObject);
    }
}
