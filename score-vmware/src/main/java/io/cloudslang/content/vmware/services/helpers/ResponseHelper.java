package io.cloudslang.content.vmware.services.helpers;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.WaitForValues;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmParameter;
import io.cloudslang.content.vmware.utils.ResponseUtils;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 3/22/2016.
 */
public class ResponseHelper {
    public void setTaskResults(Map<String, String> results, ConnectionResources connectionResources,
                               ManagedObjectReference taskMor, String successMessage, String failureMessage)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {

        if (getTaskResultAfterDone(connectionResources, taskMor)) {
            ResponseUtils.setResults(results, successMessage, Outputs.RETURN_CODE_SUCCESS);
        } else {
            ResponseUtils.setResults(results, failureMessage, Outputs.RETURN_CODE_FAILURE);
        }
    }

    protected boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
        WaitForValues waitForValues = new WaitForValues(connectionResources.getConnection());
        Object[] result = waitForValues.wait(task, new String[]{VmParameter.INFO_STATE.getValue(),
                        VmParameter.INFO_ERROR.getValue()}, new String[]{VmParameter.STATE.getValue()},
                new Object[][]{new Object[]{TaskInfoState.SUCCESS, TaskInfoState.ERROR}});

        if (result[1] instanceof LocalizedMethodFault) {
            throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
        }

        return result[0].equals(TaskInfoState.SUCCESS);
    }
}