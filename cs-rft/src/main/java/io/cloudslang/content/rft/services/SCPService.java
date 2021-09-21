/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.rft.services;

import com.jcraft.jsch.*;
import io.cloudslang.content.rft.entities.scp.SCPCopyFileInputs;
import io.cloudslang.content.rft.entities.scp.SCPRemoteCopyFileInputs;
import io.cloudslang.content.rft.utils.Constants;
import io.cloudslang.content.rft.utils.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.rft.utils.Constants.*;

public class SCPService {
    private static final String EXEC_CHANNEL = "exec";
    private static final String KNOWN_HOSTS_ALLOW = "allow";
    private static final String KNOWN_HOSTS_STRICT = "strict";
    private static final String KNOWN_HOSTS_ADD = "add";

    public Map<String, String> executeSCPCopyFile(SCPCopyFileInputs inputs) {
        Map<String, String> results = new HashMap<>();
        boolean successfullyCopied = false;

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(inputs.getUsername(), inputs.getHost(), Integer.parseInt(inputs.getPort()));
            establishPasswordOrPrivateKeyFile(jsch, session, inputs.getPrivateKey(), inputs.getPassword());
            if (!StringUtils.isEmpty(inputs.getProxyHost()))
                session.setProxy(new ProxyHTTP(inputs.getProxyHost(),Integer.parseInt(inputs.getProxyPort())));
            establishKnownHostsConfiguration(inputs.getKnownHostsPolicy(), inputs.getKnownHostsPath(), jsch, session);
            session.connect(Integer.parseInt(inputs.getConnectionTimeout()) * 1000);
            switch (inputs.getCopyAction().toLowerCase()) {
                case "to":
                    try {
                        successfullyCopied = CompletableFuture.supplyAsync(() -> copyLocalToRemote(session, inputs.getLocalFile(), inputs.getRemoteFile()))
                                .get(Integer.parseInt(inputs.getConnectionTimeout()) * 1000L, TimeUnit.SECONDS);
                    } catch (TimeoutException e) {
                        results.put(EXCEPTION, EXCEPTION_EXECUTION_TIMED_OUT);
                        results.put(RETURN_RESULT, EXCEPTION_EXECUTION_TIMED_OUT);
                        results.put(RETURN_CODE, FAILURE_RETURN_CODE);
                        return results;
                    } catch (InterruptedException | ExecutionException e) {
                        results.put(EXCEPTION, String.valueOf(e));
                        results.put(RETURN_RESULT, e.getMessage());
                        results.put(RETURN_CODE, FAILURE_RETURN_CODE);
                        return results;
                    }
                    break;
                case "from":
                    try {
                        successfullyCopied = CompletableFuture.supplyAsync(() -> copyRemoteToLocal(session, inputs.getRemoteFile(), inputs.getLocalFile()))
                                .get(Integer.parseInt(inputs.getConnectionTimeout()) * 1000L, TimeUnit.SECONDS);
                    } catch (TimeoutException e) {
                        results.put(EXCEPTION, EXCEPTION_EXECUTION_TIMED_OUT);
                        results.put(RETURN_RESULT, EXCEPTION_EXECUTION_TIMED_OUT);
                        results.put(RETURN_CODE, FAILURE_RETURN_CODE);
                        return results;
                    } catch (InterruptedException | ExecutionException e) {
                        results.put(EXCEPTION, String.valueOf(e));
                        results.put(RETURN_RESULT, e.getMessage());
                        results.put(RETURN_CODE, FAILURE_RETURN_CODE);
                        return results;
                    }
                    break;
                default:
                    break;
            }

            if (successfullyCopied) {
                results.put(RETURN_RESULT, SUCCESS_RESULT);
                results.put(RETURN_CODE, SUCCESS_RETURN_CODE);
            } else {
                results.put(RETURN_RESULT, Constants.NO_ACK_RECEIVED);
                results.put(EXCEPTION, Constants.NO_ACK_RECEIVED);
                results.put(RETURN_CODE, FAILURE_RETURN_CODE);
            }
        } catch (Exception e) {
            results.put(EXCEPTION, String.valueOf(e));
            results.put(RETURN_RESULT, (e.getMessage()));
            results.put(RETURN_CODE, FAILURE_RETURN_CODE);
        }
        return results;
    }

    public Map<String, String> executeSCPRemoteCopyFile(SCPRemoteCopyFileInputs inputs) {
        Map<String, String> results = new HashMap<>();
        boolean successfullyCopied = false;
        File temporaryDestFile = null;

        try {
            temporaryDestFile = File.createTempFile("SCPCopy", ".tmp");
            String temporaryDestFilePath = temporaryDestFile.getCanonicalPath().replace("\\", "\\\\");

            JSch jschRemoteToLocal = new JSch();
            Session sessionRemoteToLocal = jschRemoteToLocal.getSession(inputs.getSourceUsername(), inputs.getSourceHost(), Integer.parseInt(inputs.getSourcePort()));
            if (!StringUtils.isEmpty(inputs.getProxyHost()))
                sessionRemoteToLocal.setProxy(new ProxyHTTP(inputs.getProxyHost(),Integer.parseInt(inputs.getProxyPort())));
            establishPasswordOrPrivateKeyFile(jschRemoteToLocal, sessionRemoteToLocal, inputs.getSourcePrivateKey(), inputs.getSourcePassword());
            establishKnownHostsConfiguration(inputs.getKnownHostsPolicy(), inputs.getKnownHostsPath(), jschRemoteToLocal, sessionRemoteToLocal);
            sessionRemoteToLocal.connect(Integer.parseInt(inputs.getConnectionTimeout())* 1000);

            JSch jschLocalToRemote = new JSch();
            Session sessionLocalToRemote = jschLocalToRemote.getSession(inputs.getSourceUsername(), inputs.getSourceHost(), Integer.parseInt(inputs.getSourcePort()));
            if (!StringUtils.isEmpty(inputs.getProxyHost()))
                sessionRemoteToLocal.setProxy(new ProxyHTTP(inputs.getProxyHost(),Integer.parseInt(inputs.getProxyPort())));
            establishPasswordOrPrivateKeyFile(jschLocalToRemote, sessionLocalToRemote, inputs.getDestinationPrivateKey(), inputs.getDestinationPassword());
            establishKnownHostsConfiguration(inputs.getKnownHostsPolicy(), inputs.getKnownHostsPath(), jschLocalToRemote, sessionLocalToRemote);
            sessionLocalToRemote.connect(Integer.parseInt(inputs.getConnectionTimeout())* 1000);

            try {
                successfullyCopied = CompletableFuture.supplyAsync(() -> copyRemoteToLocal(sessionRemoteToLocal, inputs.getSourcePath(), temporaryDestFilePath)
                        && copyLocalToRemote(sessionLocalToRemote, temporaryDestFilePath, inputs.getDestinationPath()))
                        .get(Integer.parseInt(inputs.getConnectionTimeout()) * 1000L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                results.put(EXCEPTION, EXCEPTION_EXECUTION_TIMED_OUT);
                results.put(RETURN_RESULT, EXCEPTION_EXECUTION_TIMED_OUT);
                results.put(RETURN_CODE, FAILURE_RETURN_CODE);
                return results;
            } catch (InterruptedException | ExecutionException e) {
                results.put(EXCEPTION, String.valueOf(e));
                results.put(RETURN_RESULT, e.getMessage());
                results.put(RETURN_CODE, FAILURE_RETURN_CODE);
                return results;
            }

            if (successfullyCopied) {
                results.put(RETURN_RESULT, SUCCESS_RESULT);
                results.put(RETURN_CODE, SUCCESS_RETURN_CODE);
            } else {
                results.put(RETURN_RESULT, Constants.NO_ACK_RECEIVED);
                results.put(EXCEPTION, Constants.NO_ACK_RECEIVED);
                results.put(RETURN_CODE, FAILURE_RETURN_CODE);
            }
        } catch (Exception e) {
            results.put(EXCEPTION, String.valueOf(e));
            results.put(RETURN_RESULT, e.getMessage());
            results.put(RETURN_CODE, FAILURE_RETURN_CODE);
        }finally {
            if(temporaryDestFile != null)
                temporaryDestFile.delete();
        }
        return results;
    }

    private static boolean copyRemoteToLocal(Session session, String srcPath, String destPath) {
        FileOutputStream fos = null;
        Channel channel = null;
        try {
            // exec 'scp -f rfile' remotely
            String command = "scp -f " + srcPath;
            channel = session.openChannel(EXEC_CHANNEL);
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] buf = new byte[1024];

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            while (true) {
                int c = checkAck(in);
                if (c != 'C') {
                    break;
                }

                // read '0644 '
                in.read(buf, 0, 5);

                long filesize = 0L;
                while (true) {
                    if (in.read(buf, 0, 1) < 0) {
                        // error
                        break;
                    }
                    if (buf[0] == ' ') break;
                    filesize = filesize * 10L + (long) (buf[0] - '0');
                }

                String file = null;
                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        file = new String(buf, 0, i);
                        break;
                    }
                }

                //System.out.println("file-size=" + filesize + ", file=" + file);

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                // read a content of lfile
                fos = new FileOutputStream(destPath);
                int foo;
                while (true) {
                    if (buf.length < filesize) foo = buf.length;
                    else foo = (int) filesize;
                    foo = in.read(buf, 0, foo);
                    if (foo < 0) {
                        // error
                        break;
                    }
                    fos.write(buf, 0, foo);
                    filesize -= foo;
                    if (filesize == 0L) break;
                }

                if (checkAck(in) != 0) {
                    return false;
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
            }

            return true;
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if(channel!=null)
                    channel.disconnect();
                session.disconnect();
            } catch (Exception ex) {
            }
        }
    }


    private static boolean copyLocalToRemote(Session session, String srcPath, String destPath) {
        FileInputStream fis = null;
        OutputStream out = null;
        boolean ptimestamp = true;
        Channel channel = null;

        try {
            // exec 'scp -t rfile' remotely
            String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + destPath;
            channel = session.openChannel(EXEC_CHANNEL);
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            if (checkAck(in) != 0) {
                return false;
            }

            File _lfile = new File(srcPath);

            if (ptimestamp) {
                command = "T" + (_lfile.lastModified() / 1000) + " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
                out.write(command.getBytes());
                out.flush();
                if (checkAck(in) != 0) {
                    return false;
                }
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = _lfile.length();
            command = "C0644 " + filesize + " ";
            if (srcPath.lastIndexOf('/') > 0) {
                command += srcPath.substring(srcPath.lastIndexOf('/') + 1);
            } else {
                command += srcPath;
            }

            command += "\n";
            out.write(command.getBytes());
            out.flush();

            if (checkAck(in) != 0) {
                return false;
            }

            // send a content of lfile
            fis = new FileInputStream(srcPath);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0) break;
                out.write(buf, 0, len); //out.flush();
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            if (checkAck(in) != 0) {
                return false;
            }

            channel.disconnect();
            session.disconnect();
            return true;
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (out != null)
                    out.close();
                if(channel!=null)
                    channel.disconnect();
                session.disconnect();
            } catch (IOException e) {
            }
        }
    }


    protected void establishKnownHostsConfiguration(String policy, String knownHostsPath, JSch jsch, Session session) throws JSchException, IOException {
        Path knownHostsFilePath = Paths.get(knownHostsPath);
        switch (policy.toLowerCase(Locale.ENGLISH)) {
            case KNOWN_HOSTS_ALLOW:
                session.setConfig("StrictHostKeyChecking", "no");
                break;
            case KNOWN_HOSTS_STRICT:
                jsch.setKnownHosts(knownHostsFilePath.toString());
                session.setConfig("StrictHostKeyChecking", "yes");
                break;
            case KNOWN_HOSTS_ADD:
                if (!knownHostsFilePath.isAbsolute()) {
                    throw new RuntimeException("The known_hosts file path should be absolute.");
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

    protected void establishPasswordOrPrivateKeyFile(JSch jsch, Session session, String keyFilePath, String password) throws JSchException {
        if (!keyFilePath.isEmpty()) {
            if (!password.isEmpty()) {
                jsch.addIdentity(keyFilePath, password);
            } else {
                jsch.addIdentity(keyFilePath);
            }
        } else {
            session.setPassword(password);
        }
    }


    public static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //         -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }
}


