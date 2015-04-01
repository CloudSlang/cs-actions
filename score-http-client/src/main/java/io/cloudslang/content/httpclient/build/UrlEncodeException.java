/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package io.cloudslang.content.httpclient.build;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 9/9/14
 */
public class UrlEncodeException extends IllegalArgumentException {

    public UrlEncodeException(String message, RuntimeException e) {
        super(message, e);
    }
}
