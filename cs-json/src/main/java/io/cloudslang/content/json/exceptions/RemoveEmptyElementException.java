/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.json.exceptions;

/**
 * Created by Folea Ilie Cristian on 2/4/2016.
 */
public class RemoveEmptyElementException extends Exception {

    public RemoveEmptyElementException(String message) {
        super(message);
    }

    public RemoveEmptyElementException(Exception ex) {
        super(ex);
    }
}
