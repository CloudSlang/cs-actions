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
     * This operation is used to retrieve a value from a list. When the index of an element from a list is known,
     * this operation can be used to extract the element.
     *
     * @param list      The list to get the value from.
     * @param delimiter The delimiter that separates values in the list.
     * @param index     The index of the value (starting with 0) to retrieve from the list.
     * @return It returns the value found at the specified index in the list, if the value specified for
     * the @index parameter is positive and less than the size of the list. Otherwise, it returns
     * the value specified for @index.
     */
    @Action(name = "List Iterator",
            outputs = {
                    @Output("resultString"),
                    @Output("result")},
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
