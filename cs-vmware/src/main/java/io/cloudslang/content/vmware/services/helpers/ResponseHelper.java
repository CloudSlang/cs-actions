

package io.cloudslang.content.vmware.services.helpers;

import com.vmware.vim25.InvalidCollectorVersionFaultMsg;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TaskInfoState;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.WaitForValues;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.ManagedObjectType;
import io.cloudslang.content.vmware.utils.ResponseUtils;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 3/22/2016.
 */
public class ResponseHelper {
    private ConnectionResources connectionResources;
    private ManagedObjectReference task;

    public ResponseHelper(ConnectionResources connectionResources, ManagedObjectReference task) {
        this.connectionResources = connectionResources;
        this.task = task;
    }

    public Map<String, String> getResultsMap(String successMessage, String failureMessage)
            throws InvalidCollectorVersionFaultMsg, InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        return (getTaskResultAfterDone(connectionResources, task)) ?
                ResponseUtils.getResultsMap(successMessage, Outputs.RETURN_CODE_SUCCESS) :
                ResponseUtils.getResultsMap(failureMessage, Outputs.RETURN_CODE_FAILURE);
    }

    protected boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
        WaitForValues waitForValues = new WaitForValues(connectionResources.getConnection());
        Object[] result = waitForValues.wait(task, new String[]{ManagedObjectType.INFO_STATE.getValue(),
                        ManagedObjectType.INFO_ERROR.getValue()}, new String[]{ManagedObjectType.STATE.getValue()},
                new Object[][]{new Object[]{TaskInfoState.SUCCESS, TaskInfoState.ERROR}});

        if (result[1] instanceof LocalizedMethodFault) {
            throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
        }

        return result[0].equals(TaskInfoState.SUCCESS);
    }
}
