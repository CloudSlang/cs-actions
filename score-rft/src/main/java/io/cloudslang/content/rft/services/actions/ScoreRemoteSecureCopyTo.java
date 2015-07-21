/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.rft.services.actions;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import io.cloudslang.content.rft.entities.RemoteSecureCopyInputs;
import io.cloudslang.content.rft.utils.Constants;
import io.cloudslang.content.rft.utils.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 7/21/2015
 *
 * @author lesant
 */
public class ScoreRemoteSecureCopyTo {
    public Map<String, String> execute(RemoteSecureCopyInputs remoteSecureCopyInputs){
        Map<String, String> returnResult = new HashMap<>();

        FileInputStream fileInputStream = null;
        try{

            JSch jsch=new JSch();

            int portNumber = StringUtils.toInt(remoteSecureCopyInputs.getDestinatinPort(), Constants.DEFAULT_PORT);

            Session session=jsch.getSession(remoteSecureCopyInputs.getDestinationUsername(), remoteSecureCopyInputs.getDestinationHost(), portNumber);


            if (StringUtils.isEmpty(remoteSecureCopyInputs.getDestinationPrivateKeyFile())) {
                // use the destinationPassword
                session.setPassword(remoteSecureCopyInputs.getDestinationPassword());
            } else {
                if (remoteSecureCopyInputs.getDestinationPassword() != null) {
                    jsch.addIdentity(remoteSecureCopyInputs.getDestinationPrivateKeyFile(), remoteSecureCopyInputs.getDestinationPassword());
                } else {
                    jsch.addIdentity(remoteSecureCopyInputs.getDestinationPrivateKeyFile());
                }
            }

            session.setConfig("StrictHostKeyChecking", "no");
            //jsch.setKnownHosts(<knownhosts>) - better
            session.connect();

            boolean ptimestamp = true;

            // exec 'scp -t destinationPath' remotely
            String command = "scp " + (ptimestamp ? "-p" :"") +" -t "+ remoteSecureCopyInputs.getDestinationPath();
            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            if(checkAck(in) != 0){
                return failWithReason(returnResult);
            }
            String sourcePath = remoteSecureCopyInputs.getSourcePath();
            File sourcePathFile = new File(sourcePath);

            if(ptimestamp){
                command = "T " + (sourcePathFile.lastModified()/1000)+ " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += (" " +(sourcePathFile.lastModified()/1000)+ " 0\n");
                out.write(command.getBytes());
                out.flush();
                if(checkAck(in) != 0){
                    return failWithReason(returnResult);
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
                return failWithReason(returnResult);
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
            fileInputStream = null;
            // send '\0'
            buf[0]=0;
            out.write(buf, 0, 1);
            out.flush();
            if(checkAck(in) != 0){
                return failWithReason(returnResult);
            }
            out.close();

            channel.disconnect();
            session.disconnect();

        }
        catch(Exception e){

            try{
                if(fileInputStream != null)
                    fileInputStream.close();
            }catch(Exception ignored){

            }
            return failWithException(returnResult, e);
        }

        returnResult.put(Constants.OutputNames.RETURN_RESULT, "File " + remoteSecureCopyInputs.getSourcePath() + " successfully copied to path " +
                remoteSecureCopyInputs.getDestinationPath() + " on " + remoteSecureCopyInputs.getDestinationHost() );
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
        returnResult.put(Constants.OutputNames.EXCEPTION, Constants.EMPTY_STRING);

        return returnResult;
    }
    private Map<String, String> failWithReason(Map<String, String> returnResult) {
        returnResult.put(Constants.OutputNames.RETURN_RESULT, Constants.NO_ACK_RECEIVED);
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        returnResult.put(Constants.OutputNames.EXCEPTION, Constants.NO_ACK_RECEIVED);
        return returnResult;
    }

    private Map<String, String> failWithException(Map<String, String> returnResult, Exception e) {
        returnResult.put(Constants.OutputNames.RETURN_RESULT, e.getMessage());
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        returnResult.put(Constants.OutputNames.EXCEPTION, Arrays.toString(e.getStackTrace()));
        return returnResult;
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