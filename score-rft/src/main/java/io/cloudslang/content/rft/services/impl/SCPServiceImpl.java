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

    static final String EXEC_CHANNEL = "exec";
    static final String KNOWN_HOSTS_ALLOW = "allow";
    static final String KNOWN_HOSTS_STRICT = "strict";
    static final String KNOWN_HOSTS_ADD = "add";

    private SCPRemoteToLocal remoteToLocalCopier;
    private SCPLocalToRemote localToRemoteCopier;
    private ConnectionDetails connection;

    public SCPServiceImpl(ConnectionDetails connectionDetails, KeyFile srcKeyFile, KeyFile destKeyFile, KnownHostsFile knownHostsFile, int connectTimeout) {
        this.connection = connectionDetails;
        localToRemoteCopier = new SCPLocalToRemote(connectionDetails, destKeyFile, knownHostsFile, connectTimeout);
        if(connection.isRemoteToRemote()) {
            remoteToLocalCopier = new SCPRemoteToLocal(connectionDetails, srcKeyFile, knownHostsFile, connectTimeout);
        }
    }

    @Override
    public Boolean copy(String srcPath, String destPath) throws JSchException, IOException {
        boolean result;
        File temporaryDestFile = File.createTempFile("SCPCopy", ".tmp");
        String temporaryDestFilePath = temporaryDestFile.getCanonicalPath().replace("\\", "\\\\");

        if (!connection.isRemoteToRemote()){
            result = localToRemoteCopier.copyFromLocalToRemote(srcPath, destPath);
        }
        else{
            result = remoteToLocalCopier.copyFromRemoteToLocal(srcPath, temporaryDestFilePath) && localToRemoteCopier.copyFromLocalToRemote(temporaryDestFilePath, destPath);
            temporaryDestFile.delete();
        }
        return result;
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
    protected static void establishKnownHostsConfiguration(KnownHostsFile knownHostsFile, JSch jsch, Session session) throws JSchException, IOException {
        String policy =  knownHostsFile.getPolicy();
        Path knownHostsFilePath = knownHostsFile.getPath();
        switch (policy.toLowerCase(Locale.ENGLISH)){
            case SCPServiceImpl.KNOWN_HOSTS_ALLOW:
                session.setConfig("StrictHostKeyChecking", "no");
                break;
            case SCPServiceImpl.KNOWN_HOSTS_STRICT:
                jsch.setKnownHosts(knownHostsFilePath.toString());
                session.setConfig("StrictHostKeyChecking", "yes");
                break;
            case SCPServiceImpl.KNOWN_HOSTS_ADD:
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
    protected static void establishPrivateKeyFile(ConnectionDetails connectionDetails, KeyFile keyFile, JSch jsch, Session session, boolean usesSrcPrivateKeyFile) throws JSchException {
        if (keyFile == null) {
            if (usesSrcPrivateKeyFile){
                session.setPassword(connectionDetails.getSrcPassword());
            }
            else{
                session.setPassword(connectionDetails.getDestPassword());
            }
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

}

class SCPRemoteToLocal{

    private Session session;

    public SCPRemoteToLocal(ConnectionDetails connectionDetails, KeyFile srcKeyFile, KnownHostsFile knownHostsFile, int connectTimeout) {
        try {

            JSch jsch = new JSch();
            session = jsch.getSession(connectionDetails.getSrcUsername(), connectionDetails.getSrcHost(), connectionDetails.getSrcPort());

            SCPServiceImpl.establishKnownHostsConfiguration(knownHostsFile, jsch, session);
            SCPServiceImpl.establishPrivateKeyFile(connectionDetails, srcKeyFile, jsch, session, true);

            session.connect(connectTimeout);

        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean copyFromRemoteToLocal(String srcPath, String destPath) throws IOException, JSchException {

        FileOutputStream fileOutputStream = null;
        try {

            String command = "scp -f " + srcPath;
            Channel channel = session.openChannel(SCPServiceImpl.EXEC_CHANNEL);
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();
            byte[] buf = new byte[1024];

            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            while (true) {
                int c = SCPServiceImpl.checkAck(in);

                if (c == 1 || c == 2){
                    return false;
                }
                if (c != 'C') {
                    break;
                }

                in.read(buf, 0, 5);
                long filesize = 0L;
                while (true) {
                    if (in.read(buf, 0, 1) < 0) {
                        return false;
                    }
                    if (buf[0] == ' ') {
                        break;
                    }
                    filesize = filesize * 10L + (long) (buf[0] - '0');
                }
                //String file = null;
                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        //file = new String(buf, 0, i);
                        break;
                    }
                }
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                fileOutputStream = new FileOutputStream(destPath);

                int foo;
                while (true) {
                    if (buf.length < filesize) {
                        foo = buf.length;
                    } else {
                        foo = (int) filesize;
                    }
                    foo = in.read(buf, 0, foo);
                    if (foo < 0) {
                        return false;
                    }
                    fileOutputStream.write(buf, 0, foo);
                    filesize -= foo;
                    if (filesize == 0L) {
                        break;
                    }
                }
                fileOutputStream.close();
                fileOutputStream = null;
                if (SCPServiceImpl.checkAck(in) != 0) {
                    return false;
                }
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
            }
            channel.disconnect();
            session.disconnect();
            return true;

        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
            catch (Exception ee){

            }

        }

    }

}

class SCPLocalToRemote{

    private Session session;

    public SCPLocalToRemote(ConnectionDetails connectionDetails, KeyFile destinationKeyFile, KnownHostsFile knownHostsFile, int connectTimeout) {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(connectionDetails.getDestUsername(), connectionDetails.getDestHost(), connectionDetails.getDestPort());

            SCPServiceImpl.establishKnownHostsConfiguration(knownHostsFile, jsch, session);
            SCPServiceImpl.establishPrivateKeyFile(connectionDetails, destinationKeyFile, jsch, session, false);

            session.connect(connectTimeout);

        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean copyFromLocalToRemote(String srcPath, String destPath) throws JSchException, IOException {
        FileInputStream fileInputStream = null;

        try {
            // exec 'scp -t destPath' remotely
            String command = "scp " + "-p -t " + destPath;
            Channel channel = session.openChannel(SCPServiceImpl.EXEC_CHANNEL);
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            if (SCPServiceImpl.checkAck(in) != 0) {
                return false;
            }

            File srcFile = new File(srcPath);
            command = "T" + (srcFile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (srcFile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();

            if (SCPServiceImpl.checkAck(in) != 0) {
                return false;
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = srcFile.length();
            command = "C0644 " + filesize + " ";

            if (srcPath.lastIndexOf('/') > 0) {
                command += srcPath.substring(srcPath.lastIndexOf('/') + 1);
            } else {
                command += srcPath;
            }
            command += "\n";
            out.write(command.getBytes());
            out.flush();

            if (SCPServiceImpl.checkAck(in) != 0) {   // returns 1 if location where file is to be copied does not exist
                return false;
            }

            // send a content of srcPath
            fileInputStream = new FileInputStream(srcPath);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fileInputStream.read(buf, 0, buf.length);
                if (len <= 0)
                    break;
                out.write(buf, 0, len);
                out.flush();
            }
            fileInputStream.close();
            fileInputStream = null;
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            if (SCPServiceImpl.checkAck(in) != 0) {
                return false;
            }
            out.close();

            channel.disconnect();
            session.disconnect();
            return true;
        } finally {
            try{
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            }catch (Exception ee) {}
        }
    }

}
