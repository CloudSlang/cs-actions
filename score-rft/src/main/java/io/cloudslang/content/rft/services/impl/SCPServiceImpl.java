/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.rft.services.impl;

import com.jcraft.jsch.*;

import io.cloudslang.content.rft.entities.ConnectionDetails;
import io.cloudslang.content.rft.entities.KeyFile;
import io.cloudslang.content.rft.entities.KnownHostsFile;
import io.cloudslang.content.rft.services.SCPService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Date: 4/10/2015
 *
 * @author lesant
 */
public class SCPServiceImpl implements SCPService {

    private static final String EXEC_CHANNEL = "exec";
    private static final String KNOWN_HOSTS_ALLOW = "allow";
    private static final String KNOWN_HOSTS_STRICT = "strict";
    private static final String KNOWN_HOSTS_ADD = "add";

    private Session session;

    public SCPServiceImpl(ConnectionDetails connectionDetails, KeyFile keyFile, KnownHostsFile knownHostsFile, int connectTimeout) {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(connectionDetails.getDestinationUsername(), connectionDetails.getDestinationHost(), connectionDetails.getDestinationPort());

            establishKnownHostsConfiguration(knownHostsFile, jsch);
            establishPrivateKeyFile(connectionDetails, keyFile, jsch);

            session.connect(connectTimeout);

        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void establishPrivateKeyFile(ConnectionDetails connectionDetails, KeyFile keyFile, JSch jsch) throws JSchException {
        if (keyFile == null) {
            session.setPassword(connectionDetails.getDestinationPassword());
        }
        else {
            String keyFilePath = keyFile.getKeyFilePath();
            String passPhrase = keyFile.getPassPhrase();
            if (passPhrase != null){
                jsch.addIdentity(keyFilePath, passPhrase);
            } else {
                jsch.addIdentity(keyFilePath);
            }
        }
    }

    private void establishKnownHostsConfiguration(KnownHostsFile knownHostsFile, JSch jsch) throws JSchException, IOException {
        String policy =  knownHostsFile.getPolicy();
        Path knownHostsFilePath = knownHostsFile.getPath();
        switch (policy.toLowerCase(Locale.ENGLISH)){
            case KNOWN_HOSTS_ALLOW:
                session.setConfig("StrictHostKeyChecking", "no");
                break;
            case KNOWN_HOSTS_STRICT:
                jsch.setKnownHosts(knownHostsFilePath.toString());
                session.setConfig("StrictHostKeyChecking", "yes");
                break;
            case KNOWN_HOSTS_ADD:
                if (!knownHostsFilePath.isAbsolute()){
                    throw new RuntimeException ("The known_hosts file path should be absolute.");
                }
                if (!Files.exists(knownHostsFilePath)) {
                    Files.createDirectories(knownHostsFilePath.getParent());
                    Files.createFile(knownHostsFilePath);
                }
                jsch.setKnownHosts(knownHostsFilePath.toString());
                session.setConfig("StrictHostKeyChecking", "no");
                break;
            default:
                throw new RuntimeException("Unknown known_hosts file policy.");
        }
    }


    @Override
    public Boolean copy(String sourcePath, String destinationPath) throws JSchException, IOException {

        boolean ptimestamp = true;
        FileInputStream fileInputStream = null;

        // exec 'scp -t destinationPath' remotely
        String command = "scp " + (ptimestamp ? "-p" :"") +" -t "+ destinationPath;
        Channel channel=session.openChannel(EXEC_CHANNEL);
        ((ChannelExec)channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if(checkAck(in) != 0){
            return false;
        }
        File sourcePathFile = new File(sourcePath);

        if(ptimestamp){
            command = "T " + (sourcePathFile.lastModified()/1000)+ " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " +(sourcePathFile.lastModified()/1000)+ " 0\n");
            out.write(command.getBytes());
            out.flush();
            if(checkAck(in) != 0){
                return false;
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = sourcePathFile.length();
        command = "C0644 " + filesize + " ";

        if(sourcePath.lastIndexOf('/')>0){
            command += sourcePath.substring(sourcePath.lastIndexOf('/') + 1);
        }
        else{
            command += sourcePath;
        }
        command += "\n";
        out.write(command.getBytes());
        out.flush();
        if(checkAck(in) != 0){
            return false;
        }

        // send a content of sourcePath
        fileInputStream = new FileInputStream(sourcePath);
        byte[] buf = new byte[1024];
        while (true){
            int len = fileInputStream.read(buf, 0, buf.length);
            if (len <= 0)
                break;
            out.write(buf, 0, len);
            out.flush();
        }
        fileInputStream.close();
        // send '\0'
        buf[0]=0;
        out.write(buf, 0, 1);
        out.flush();
        if(checkAck(in) != 0){
            return false;
        }
        out.close();

        channel.disconnect();
        session.disconnect();

        return true;
    }

    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if(b == 0) return b;
        if(b == -1) return b;

        if(b==1 || b==2){
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c=in.read();
                sb.append((char)c);
            }
            while(c!='\n');
            if(b==1){ // error
                System.out.print(sb.toString());
            }
            if(b==2){ // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

}
