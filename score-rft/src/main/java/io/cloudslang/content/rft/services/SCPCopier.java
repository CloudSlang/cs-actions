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

import com.jcraft.jsch.*;
import io.cloudslang.content.rft.entities.*;
import io.cloudslang.content.rft.utils.Constants;
import io.cloudslang.content.rft.utils.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Date: 7/30/2015
 *
 * @author lesant
 */
public class SCPCopier {

    private static final String EXEC_CHANNEL = "exec";
    private static final String KNOWN_HOSTS_ALLOW = "allow";
    private static final String KNOWN_HOSTS_STRICT = "strict";
    private static final String KNOWN_HOSTS_ADD = "add";

    private Session session;
    private RemoteSecureCopyInputs remoteSecureCopyInputs;

    public SCPCopier(RemoteSecureCopyInputs remoteSecureCopyInputs) {
        this.remoteSecureCopyInputs = remoteSecureCopyInputs;
    }

    public boolean copyFromRemoteToRemote() throws IOException {
        boolean result;
        File temporaryDestFile = File.createTempFile("SCPCopy", ".tmp");
        String temporaryDestFilePath = temporaryDestFile.getCanonicalPath().replace("\\", "\\\\");

        result = copyFromRemoteToLocal(remoteSecureCopyInputs.getSrcPath(), temporaryDestFilePath) && copyFromLocalToRemote(temporaryDestFilePath, remoteSecureCopyInputs.getDestPath());
        temporaryDestFile.delete();

        return result;
    }

    public boolean copyFromLocalToRemote(String srcPath, String destPath){
        FileInputStream fileInputStream = null;

        try {

            JSch jsch = new JSch();
            session = jsch.getSession(remoteSecureCopyInputs.getDestUsername(), remoteSecureCopyInputs.getDestHost(), StringUtils.toInt(remoteSecureCopyInputs.getDestPort(), Constants.DEFAULT_PORT));

            establishKnownHostsConfiguration(ConnectionUtils.resolveKnownHosts(remoteSecureCopyInputs.getKnownHostsPolicy(), remoteSecureCopyInputs.getKnownHostsPath()), jsch, session);
            establishPrivateKeyFile(ConnectionUtils.getKeyFile(remoteSecureCopyInputs.getDestPrivateKeyFile(), remoteSecureCopyInputs.getDestPassword()), jsch, session, false);

            session.connect(StringUtils.toInt(remoteSecureCopyInputs.getTimeout(), Constants.DEFAULT_TIMEOUT));

            String command = "scp " + "-p -t " + destPath;
            Channel channel = session.openChannel(EXEC_CHANNEL);
            ((ChannelExec) channel).setCommand(command);

            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            if (checkAck(in) != 0) {
                return false;
            }

            File srcFile = new File(srcPath);
            command = "T" + (srcFile.lastModified() / 1000) + " 0";
            command += (" " + (srcFile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();

            if (checkAck(in) != 0) {
                return false;
            }

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

            if (checkAck(in) != 0) {   // returns 1 if location where file is to be copied does not exist
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
            if (checkAck(in) != 0) {
                return false;
            }
            out.close();

            channel.disconnect();
            session.disconnect();
            return true;
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        } finally{
            try{
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            }catch (IOException ioe) {}
        }
    }

    public boolean copyFromRemoteToLocal(String srcPath, String destPath){
        FileOutputStream fileOutputStream = null;

        try {

            JSch jsch = new JSch();
            session = jsch.getSession(remoteSecureCopyInputs.getSrcUsername(), remoteSecureCopyInputs.getSrcHost(), StringUtils.toInt(remoteSecureCopyInputs.getSrcPort(), Constants.DEFAULT_PORT));

            establishKnownHostsConfiguration(ConnectionUtils.resolveKnownHosts(remoteSecureCopyInputs.getKnownHostsPolicy(), remoteSecureCopyInputs.getKnownHostsPath()), jsch, session);
            establishPrivateKeyFile(ConnectionUtils.getKeyFile(remoteSecureCopyInputs.getSrcPrivateKeyFile(), remoteSecureCopyInputs.getSrcPassword()), jsch, session, true);

            session.connect(StringUtils.toInt(remoteSecureCopyInputs.getTimeout(), Constants.DEFAULT_TIMEOUT));

            String command = "scp -f " + srcPath;
            Channel channel = session.openChannel(EXEC_CHANNEL);
            ((ChannelExec) channel).setCommand(command);

            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();
            byte[] buf = new byte[1024];

            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            while (true) {
                int c = checkAck(in);

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

                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
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
                if (checkAck(in) != 0) {
                    return false;
                }
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
            }
            channel.disconnect();
            session.disconnect();
            return true;

        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
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

    private void establishKnownHostsConfiguration(KnownHostsFile knownHostsFile, JSch jsch, Session session) throws JSchException, IOException {
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
    private void establishPrivateKeyFile(KeyFile keyFile, JSch jsch, Session session, boolean usesSrcPrivateKeyFile) throws JSchException {
        if (keyFile == null) {
            if (usesSrcPrivateKeyFile){
                session.setPassword(remoteSecureCopyInputs.getSrcPassword());
            }
            else{
                session.setPassword(remoteSecureCopyInputs.getDestPassword());
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
    private int checkAck(InputStream in) throws IOException {
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
            } while(c!='\n');

            throw new RuntimeException(sb.toString());


        }
        return b;
    }
}
