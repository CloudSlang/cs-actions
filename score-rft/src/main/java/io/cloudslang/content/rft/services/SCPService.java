/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.rft.services;

import com.jcraft.jsch.JSchException;
import java.io.IOException;

/**
 * Date: 7/1`/2015
 *
 * @author lesant
 */
public interface SCPService {
    public Boolean copy(String sourcePath, String destinationPath) throws JSchException, IOException;

}
