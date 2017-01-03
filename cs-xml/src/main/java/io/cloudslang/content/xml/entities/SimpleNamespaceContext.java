/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.xml.entities;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by moldovas on 6/22/2016.
 */
public class SimpleNamespaceContext implements NamespaceContext {

    private Map<String, String> PREF_MAP;

    public SimpleNamespaceContext(final Map<String, String> PREF_MAP) {
        this.PREF_MAP = PREF_MAP;
    }

    @Override
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix can't be null");
        }
        return PREF_MAP.get(prefix);
    }

    @Override
    public String getPrefix(String namespaceURI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {
        throw new UnsupportedOperationException();
    }
}
