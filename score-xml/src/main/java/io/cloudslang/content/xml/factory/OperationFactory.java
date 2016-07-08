package io.cloudslang.content.xml.factory;

import io.cloudslang.content.xml.entities.Action;
import io.cloudslang.content.xml.services.OperationService;
import io.cloudslang.content.xml.services.impl.*;

/**
 * Created by moldovas on 7/8/2016.
 */
public class OperationFactory {
    public static OperationService getOperation(Action action) {
        switch (action) {
            case delete:
                return new DeleteOperationServiceImpl();
            case insert:
                return new InsertOperationServiceImpl();
            case append:
                return new AppendOperationServiceImpl();
            case subnode:
                return new SubnodeOperationServiceImpl();
            case move:
                return new MoveOperationServiceImpl();
            case rename:
                return new RenameOperationServiceImpl();
            case update:
                return new UpdateOperationServiceImpl();
            default:
                throw new RuntimeException("Invalid/unsupported action provided as input.");
        }
    }
}
