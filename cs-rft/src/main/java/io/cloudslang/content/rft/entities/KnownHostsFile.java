/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.rft.entities;

import java.nio.file.Path;

/**
 * Date: 7/21/2015
 *
 * @author lesant
 */
public class KnownHostsFile {
    private Path path;
    private String policy;

    public KnownHostsFile(Path path, String policy) {
        this.path = path;
        this.policy = policy;
    }

    public Path getPath() {
        return path;
    }

    public String getPolicy() {
        return policy;
    }

}

