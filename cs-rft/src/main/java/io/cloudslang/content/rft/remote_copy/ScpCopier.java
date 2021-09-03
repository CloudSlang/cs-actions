/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.rft.remote_copy;
/**
 * </p> Copies a file to/from/between two remote hosts using SCP.
 */

import com.hp.oo.content.commons.util.Address;
import com.iconclude.dharma.commons.exception.DharmaException;
import com.iconclude.dharma.commons.security.ssh.DefaultSSHSessionCreator;
import com.iconclude.dharma.commons.security.ssh.SSHChannelProcessor;
import com.iconclude.dharma.commons.security.ssh.SSHOperation;
import com.iconclude.dharma.commons.security.ssh.SSHOperationResult;
import com.iconclude.dharma.commons.util.StringUtils;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ScpCopier extends SimpleCopier {

    String sourceFileName;
    private String host;
    private int port;
    private String username;
    private String password;
    private String privateKeyFile;

    //Collection<KerberosTicket> tickets;

    public ScpCopier() {

    }

    protected void getFile(String source, File getFile) throws Exception {

        SCPLocalOperationGet scpOp = new SCPLocalOperationGet(source, host, port, username, password, privateKeyFile, getFile, this, super.timeout);
        SSHOperationResult raw = scpOp.exec();  //contains a call to bindOperation()
        if (raw.isTimedOut()) {
            throw new Exception("SCP Operation timed out");
        }
        if (raw.getCodeInt() != 0) {
            throw new Exception("SCP Operation failed: " + raw.getError() + "\n" + raw.getException());
        }
        if (sourceFileName == null)
            sourceFileName = getSimpleFileName(source);
    }

    protected IReader getFile(String source) throws Exception {
        File getFile;
        getFile = File.createTempFile("SCPCopy", ".tmp");

        getFile(source, getFile);
        return new SimpleReader(sourceFileName, getFile);
    }

    protected void putFile(IReader sourceFile, String destination) throws Exception {
        LocalSCPOperationPut scpOp = new LocalSCPOperationPut(sourceFile.getFile(), destination, host, port,
                username, password, privateKeyFile, sourceFile.getFileName(), super.timeout);
        SSHOperationResult raw = scpOp.exec();  //contains a call to bindOperation()
        if (raw.isTimedOut()) {
            throw new Exception("SCP Operation timed out");
        }
        if (raw.getCodeInt() != 0)
            throw new Exception("SCP Operation failed: " + raw.getError() + "\n" + raw.getException());
    }

    @Override
    public void setCredentials(String host, int port, String username,
                               String password) throws UnsupportedOperationException {
        Address address = new Address(host, port);
        this.host = address.getBareHost();
        this.port = address.getPort();
        this.username = username;
        this.password = password;
    }

    @Override
    public void setCredentials(String host, int port, String username,
                               String password, String privateKeyFile)
            throws UnsupportedOperationException {
        setCredentials(host, port, username, password);
        this.privateKeyFile = privateKeyFile;
    }

    public void setCustomArgument(complexArgument name, Object value) {
        switch (name) {
            //case kerberosTickets: tickets = ActionRequestUtils.collectKerberosTicketsFromPassword((ActionRequest)value); break;
            default:
                throw new UnsupportedOperationException(getProtocolName() + " does not allow " + name.name() + " to be set");
        }
    }

    public String getProtocolName() {
        return CopierFactory.copiers.scp.name();
    }
}


class SCPLocalOperationGet extends SSHOperation<DefaultSSHSessionCreator, SCPLocalProcessor> {

    private String srcHost;
    private int port;
    private String srcUsername;
    private String srcPassword;
    private String srcPrivateKeyFile;
    private int timeout;

    public SCPLocalOperationGet(String srcPath, String srcHost, int port, String srcUsername, String srcPassword, String srcPrivateKeyFile, File dest, ScpCopier exec, int timeout) {
        this(new DefaultSSHSessionCreator(), new SCPLocalProcessor(srcPath, dest, exec, timeout));
        Address address = new Address(srcHost, port);
        this.srcHost = address.getBareHost();
        this.port = address.getPort();
        this.srcUsername = srcUsername;
        this.srcPassword = srcPassword;
        this.srcPrivateKeyFile = srcPrivateKeyFile;
        this.timeout = timeout;
    }

    protected SCPLocalOperationGet(DefaultSSHSessionCreator sessCreator, SCPLocalProcessor channelProc) {
        super(sessCreator, channelProc);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map bindOperation() {
        Map result = new HashMap();

        if (StringUtils.isEmpty(srcHost)) {
            throw new DharmaException(SSHOperation.HOST + " not specified");
        }
        result.put(SSHOperation.HOST, srcHost);
        result.put(Address.INTEGER_PORT, port);

        if (StringUtils.isBlank(srcUsername)) {
            throw new DharmaException("userName not specified");
        }
        result.put(SSHOperation.USERNAME, srcUsername);

        result.put(SSHOperation.PASSWORD, srcPassword);

        if (!StringUtils.isBlank(srcPrivateKeyFile)) {
            result.put(SSHOperation.PK_FILE, srcPrivateKeyFile);
        }

        result.put(SSHOperation.TIMEOUT, String.valueOf(timeout));

        return result;
    }
}

class SCPLocalProcessor extends SSHChannelProcessor {

    public static final String CHANNELTYPE = "exec";
    protected ChannelExec channel;
    protected long timeout;
    OutputStream out;
    InputStream in;
    ScpCopier parent;
    private String srcPath;
    private File dest;

    public SCPLocalProcessor(String srcPath, File dest, ScpCopier parent, long timeout) {
        this.srcPath = srcPath;
        this.dest = dest;
        this.parent = parent;
        this.timeout = timeout;
        // small hack to handle the input name change from privateKey to privateKeyFile
    }

    @SuppressWarnings("unchecked")
    @Override
    public void createChannel(Session session, Map bindings)
            throws JSchException, IOException {
        channel = (ChannelExec) session.openChannel(CHANNELTYPE);
    }

    private String getSourceFile() {
        return srcPath;
    }

    private File getDestinationFile() {
        return dest;
    }

    @Override
    public SSHOperationResult process() throws JSchException {
        try {
            try {
                in = channel.getInputStream();
                out = channel.getOutputStream();
            } catch (Exception e) {
                return fail("Unable to read SSH session's IO streams.\n" + e.toString());
            }

            channel.setCommand("scp -f " + getSourceFile());

            // connect channel to host
            channel.connect((int) timeout);
            // run the shell simulator (this basically is sending a batch of commands to the ssh server)

            return copyFrom();
        } finally {
            if (channel.isConnected())
                channel.disconnect();
        }
    }

    private SSHOperationResult copyFrom() {

        FileOutputStream fos = null;
        try {

            byte[] buf = new byte[1024];

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            boolean processed = false;
            File destFile = getDestinationFile();
            while (true) {
                int c = checkAck(in);
                if (c != 'C') {
                    if (!processed)
                        return fail("Received incorrect ack response for secure copy command");
                    else
                        break;
                }
                processed = true;
                // read '0644 '
                in.read(buf, 0, 5);

                long filesize = 0L;
                while (true) {
                    if (in.read(buf, 0, 1) < 0) {
                        return fail("Read of file stream from remote host failed.");
                    }
                    if (buf[0] == ' ')
                        break;
                    filesize = filesize * 10L + (long) (buf[0] - '0');
                }

                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        parent.sourceFileName = new String(buf, 0, i);
                        break;
                    }
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                // read a content of lfile
                fos = new FileOutputStream(destFile);
                int foo;
                while (true) {
                    if (buf.length < filesize)
                        foo = buf.length;
                    else
                        foo = (int) filesize;
                    foo = in.read(buf, 0, foo);
                    if (foo < 0) {
                        // error
                        break;
                    }
                    fos.write(buf, 0, foo);
                    filesize -= foo;
                    if (filesize == 0L)
                        break;
                }
                fos.close();
                fos = null;
                if (checkAck(in) != 0) {
                    return fail("No ack received");
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
            }

            SSHOperationResult result = new SSHOperationResult();
            result.setOutput(destFile.getAbsolutePath());
            result.setCodeInt(0);
            result.setException("");
            result.setError("");
            return result;

        } catch (Exception e) {
            System.out.println(e);
            try {
                if (fos != null)
                    fos.close();
                return fail(e.toString());
            } catch (Exception ee) {
                return fail(ee.toString() + "\n" + e.toString());
            }
        }

    }

    private SSHOperationResult fail(String reason) {
        SSHOperationResult result = new SSHOperationResult();
        result.setOutput(reason);
        result.setCodeInt(-1);
        result.setException(reason);
        result.setError(reason);
        return result;
    }

    private int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        // 1 for error,
        // 2 for fatal error,
        // -1
        if (b == 0)
            return b;
        if (b == -1)
            return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            } while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
                throw new IOException("Error: " + sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
                throw new IOException("Fatal Error: " + sb.toString());
            }
        }
        return b;
    }
}

class LocalSCPProcessor extends SSHChannelProcessor {

    public static final String CHANNELTYPE = "exec";
    protected ChannelExec channel;
    protected long timeout;
    OutputStream out;
    InputStream in;
    String sourceFileName;
    private File tmp;
    private String destPath;

    public LocalSCPProcessor(File tmp, String destPath, String sourceFileName, long timeout) {
        this.tmp = tmp;
        this.destPath = destPath;
        this.sourceFileName = sourceFileName;
        this.timeout = timeout;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void createChannel(Session session, Map bindings)
            throws JSchException, IOException {
        channel = (ChannelExec) session.openChannel(CHANNELTYPE);
    }

    private String getDestinationName() {
        if (destPath.lastIndexOf('/') == destPath.length() - 1)
            return sourceFileName;
        else if (destPath.contains("/"))
            return destPath.substring(destPath.lastIndexOf("/") + 1);
        else return destPath;
    }

    private String getDestinationPath() {
        if (destPath.lastIndexOf('/') == destPath.length() - 1)
            return destPath + sourceFileName;
        else
            return destPath;
    }

    @Override
    public SSHOperationResult process() throws JSchException {
        try {
            try {
                in = channel.getInputStream();
                out = channel.getOutputStream();
            } catch (Exception e) {
                return fail("Unable to read SSH session's IO streams.\n" + e.toString());
            }

            channel.setCommand("scp -p -t " + getDestinationPath());

            // connect channel to host
            channel.connect((int) timeout);
            // run the shell simulator (this basically is sending a batch of commands to the ssh server)

            return copyTo();
        } finally {
            if (channel.isConnected())
                channel.disconnect();
        }
    }

    private SSHOperationResult copyTo() {
        try {
            SSHOperationResult result = new SSHOperationResult();

            if (checkAck(in) != 0) {
                return fail("No ack received");
            }

            // send "C0644 filesize filename", where filename should not
            // include '/'
            long filesize = (tmp).length();
            String command = "C0644 " + filesize + " ";
            command += getDestinationName();
            command += "\n";
            out.write(command.getBytes());
            out.flush();

            if (checkAck(in) != 0) {
                return fail("No ack received");
            }

            // send a content of lfile
            FileInputStream fis = new FileInputStream(tmp);
            byte[] buf = new byte[1024];
            try {
                while (true) {
                    int len = fis.read(buf, 0, buf.length);
                    if (len <= 0)
                        break;
                    out.write(buf, 0, len);
                    out.flush();
                }
            } finally {
                fis.close();
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            if (checkAck(in) != 0) {
                return fail("No ack received");
            }
            result.setOutput(destPath);
            result.setCodeInt(0);
            result.setException("");
            result.setError("");
            return result;
        } catch (Exception e) {
            return fail(e.toString());
        }
    }

    private SSHOperationResult fail(String reason) {
        SSHOperationResult result = new SSHOperationResult();
        result.setOutput(reason);
        result.setCodeInt(-1);
        result.setException(reason);
        result.setError(reason);
        return result;
    }

    private int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        // 1 for error,
        // 2 for fatal error,
        // -1
        if (b == 0)
            return b;
        if (b == -1)
            return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            } while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
                throw new IOException("Error: " + sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
                throw new IOException("Fatal Error: " + sb.toString());
            }
        }
        return b;
    }
}

class LocalSCPOperationPut extends SSHOperation<DefaultSSHSessionCreator, LocalSCPProcessor> {

    private String destHost;
    private int port;
    private String destUsername;
    private String destPassword;
    private String destPrivateKeyFile;
    private int timeout;

    public LocalSCPOperationPut(File tmp, String destPath, String destHost, int port, String destUsername, String destPassword, String destPrivateKeyFile, String sourceFileName, int timeout) {
        this(new DefaultSSHSessionCreator(), new LocalSCPProcessor(tmp, destPath, sourceFileName, timeout));
        Address address = new Address(destHost, port);
        this.destHost = address.getBareHost();
        this.port = address.getPort();
        this.destUsername = destUsername;
        this.destPassword = destPassword;
        this.destPrivateKeyFile = destPrivateKeyFile;
        this.timeout = timeout;
    }

    protected LocalSCPOperationPut(DefaultSSHSessionCreator sessCreator, LocalSCPProcessor channelProc) {
        super(sessCreator, channelProc);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map bindOperation() {
        Map result = new HashMap();

        if (StringUtils.isEmpty(destHost)) {
            throw new DharmaException(SSHOperation.HOST + " not specified");
        }
        result.put(SSHOperation.HOST, destHost);
        result.put(Address.INTEGER_PORT, port);

        if (StringUtils.isBlank(destUsername)) {
            throw new DharmaException("userName not specified");
        }
        result.put(SSHOperation.USERNAME, destUsername);

        result.put(SSHOperation.PASSWORD, destPassword);

        if (!StringUtils.isBlank(destPrivateKeyFile)) {
            result.put(SSHOperation.PK_FILE, destPrivateKeyFile);
        }

        result.put(SSHOperation.TIMEOUT, String.valueOf(timeout));

        // Kerberos support: deserialize tickets and place them in the binding map
        /*if (!CollectionUtils.isEmpty(tickets)) {
            result.put(SSHOperation.KRBTICKETS, tickets);
        }*/
        return result;
    }
}