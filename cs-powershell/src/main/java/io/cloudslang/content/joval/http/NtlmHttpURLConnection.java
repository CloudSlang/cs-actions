/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
// Copyright (C) 2012 jOVAL.org.  All rights reserved.
// This software is licensed under the AGPL 3.0 license available at http://www.joval.org/agpl_v3.txt

package io.cloudslang.content.joval.http;

import io.cloudslang.content.joval.util.Base64;
import org.microsoft.security.ntlm.NtlmAuthenticator;
import org.microsoft.security.ntlm.NtlmAuthenticator.ConnectionType;
import org.microsoft.security.ntlm.NtlmAuthenticator.NtlmVersion;
import org.microsoft.security.ntlm.NtlmSession;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.Permission;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An HttpURLConnection implementation that can negotiate NTLM authentication with an HTTP proxy and/or
 * destination HTTP server, even when using streaming modes.
 *
 * @author David A. Solin
 * @version %I% %V%
 */
public class NtlmHttpURLConnection extends AbstractConnection {
    private static NtlmVersion NTLMV2	= NtlmVersion.ntlmv2;
    private static ConnectionType CO	= ConnectionType.connectionOriented;
    private static long MAX_KEEPALIVE = 120000L; // 2 minutes

//    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static Hashtable<String, Queue<NtlmHttpURLConnection>> pool;
    private static String host = null;
    static {
	pool = new Hashtable<String, Queue<NtlmHttpURLConnection>>();
	try {
	    host = InetAddress.getLocalHost().getHostName();
	} catch (UnknownHostException e) {
	}
    }

    private static String createId(URL url) {
	try {
	    StringBuffer sb = new StringBuffer(new URL(url, "/").toString());
	    return Base64.encodeBytes(MessageDigest.getInstance("MD5").digest(sb.toString().getBytes()));
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    private String id;
    private HttpSocketConnection connection;
    private PasswordAuthentication cred;
    private NtlmSession session, proxySession;
    private String proxyAuth;
    private boolean encrypt;
    private boolean negotiated;
    private ByteArrayOutputStream cachedOutput;
    private NtlmPhase phase, proxyPhase;
    private long lastUsed;

    /**
     * Create a new connection, or recycle one from the pool, as appropriate.
     */
    public static NtlmHttpURLConnection openConnection(URL url, PasswordAuthentication cred, boolean encrypt) {
	synchronized(pool) {
	    String id = createId(url);
	    if (pool.containsKey(id)) {
		Queue<NtlmHttpURLConnection> connections = pool.get(id);
		while (connections.size() > 0) {
		    NtlmHttpURLConnection conn = connections.remove();
		    long dormant = System.currentTimeMillis() - conn.lastUsed;
		    if (conn.connection.connected()) {
			if (dormant > MAX_KEEPALIVE) {
			    //
			    // If it hasn't been used for longer than MAX_KEEPALIVE, kill the connection
			    //
			    conn.connection.disconnect();
			} else {
			    //
			    // Recycle the connection, but replace its credential first.
			    //
			    conn.cred = cred;
			    return conn;
			}
		    }
		}
	    } else {
		pool.put(id, new ConcurrentLinkedQueue<NtlmHttpURLConnection>());
	    }
	    //
	    // Create a new connection
	    //
	    return new NtlmHttpURLConnection(url, cred, encrypt);
	}
    }

    /**
     * This method will cause the connection to go through a proxy.
     */
    public void setProxy(Proxy proxy) {
	setProxy(proxy, null);
    }

    /**
     * Connect through a proxy using the specified credentials. If the username contains a back-slash character, the
     * connection will use NTLM to negotiate with the proxy.
     */
    public void setProxy(Proxy proxy, PasswordAuthentication proxyCred) {
	if (connected) {
	    throw new IllegalStateException("connected");
	} else {
	    connection.setProxy(proxy);
	    if (proxy != null && proxy.type() == Proxy.Type.HTTP && proxyCred != null) {
		if (proxyCred.getUserName().indexOf("\\") == -1) {
		    StringBuffer sb = new StringBuffer("Basic");
		    sb.append(proxyCred.getUserName());
		    sb.append(":");
		    sb.append(proxyCred.getPassword());
		    proxyAuth = sb.toString();
		    proxySession = null;
		    proxyPhase = null;
		} else {
		    proxyAuth = null;
		    proxySession = createSession(proxyCred, false);
		    proxyPhase = NtlmPhase.TYPE1;
		}
	    }
	}
    }

    // HttpURLConnection overrides

    @Override
    public void connect() throws IOException {
	if (connected) return;

	//
	// Seed the initial connection with negotiate headers
	//
	if (proxyPhase == NtlmPhase.TYPE1) {
	    connection.setRequestProperty("Proxy-Authorization", "Negotiate " +
		Base64.encodeBytes(proxySession.generateNegotiateMessage()));
	} else if (proxyAuth != null) {
	    connection.setRequestProperty("Proxy-Authorization", proxyAuth);
	}
	if (phase == NtlmPhase.TYPE1) {
	    connection.setRequestProperty("Authorization", "Negotiate " +
		Base64.encodeBytes(session.generateNegotiateMessage()));
	}

	if (sendOutput()) {
	    if (encrypt) {
		StringBuffer sb = new StringBuffer("OriginalContent: type=");
		sb.append(getRequestProperty("Content-Type"));
		sb.append(";Length=").append(Integer.toString(cachedOutput.size()));
		sb.append("\r\n");

		StringBuffer ctBuff = new StringBuffer("multipart/encrypted;");
		ctBuff.append("protocol=\"application/HTTP-SPNEGO-session-encrypted\";");
		ctBuff.append("boundary=\"Encrypted Boundary\"");
		setRequestProperty("Content-Type", ctBuff.toString());

		ByteArrayOutputStream temp = new ByteArrayOutputStream();
		temp.write("--Encrypted Boundary\r\n".getBytes("US-ASCII"));
		temp.write("Content-Type: application/HTTP-SPNEGO-session-encrypted\r\n".getBytes("US-ASCII"));
		temp.write(sb.toString().getBytes("US-ASCII"));
		temp.write("--Encrypted Boundary\r\n".getBytes("US-ASCII"));
		temp.write("Content-Type: application/octet-stream\r\n".getBytes("US-ASCII"));
		temp.write(session.seal(cachedOutput.toByteArray()));
		temp.write("--Encrypted Boundary--\r\n".getBytes("US-ASCII"));
		cachedOutput = temp;
	    }
	    setFixedLengthStreamingMode(cachedOutput.size());
	} else {
	    setFixedLengthStreamingMode(0);
	}

//        METHOD 1
//        Callable<String> callable = new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                if (!connection.connected()) {
//                    connection.connect();
//                }
//                return "connected!";
//            }
//        };
//        Future<String> future = executorService.submit(callable);
//        try {
//            future.get(5000, TimeUnit.MILLISECONDS);
//        } catch (Exception e) {
//            connection.disconnect();
//            future.cancel(true);
//            System.out.println(ExceptionUtils.getStackTrace(e));
//        }
//        executorService.shutdown();

//        METHOD 2
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (!connection.connected()) {
//                        connection.connect();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//        try {
//            System.out.println(thread.getId() + ": isAlives: " + thread.isAlive());
//            thread.join(5);
//            System.out.println(thread.getId() + ": isdeamon: " + thread.isDaemon());
//            thread.interrupt();
//            thread.stop();
//            thread.stop(new Exception("a"));
//            System.out.println(thread.getId() + ": afterstop isAlive: " + thread.isAlive());
//        } catch (Exception e) {
//            System.out.println("ceva");
//            connection.disconnect();
//            System.out.println(ExceptionUtils.getStackTrace(e));
//        }

        connection.connect();


	//
        // Send any cached output
        //
        if (sendOutput()) {
            OutputStream out = connection.getOutputStream();
            cachedOutput.writeTo(out);
            out.close();
        }

        connected = true;
    }

    @Override
    public void disconnect() {
	if (connection.connected()) {
	    synchronized(pool) {
		pool.get(id).add(this);
	    }
	}
	try {
	    reset();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public Object getContent() throws IOException {
	getResponse();
	return connection.getContent();
    }

    @Override
    public Object getContent(Class[] classes) throws IOException {
	getResponse();
	return connection.getContent(classes);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
	if (cachedOutput == null) {
	    cachedOutput = new ByteArrayOutputStream();
	}
	return cachedOutput;
    }

    @Override
    public String toString() {
	return connection.toString();
    }

    @Override
    public Permission getPermission() throws IOException {
	return connection.getPermission();
    }

    @Override
    public boolean usingProxy() {
	return connection.usingProxy();
    }

    @Override
    public boolean getDoInput() {
	return connection.getDoInput();
    }

    @Override
    public boolean getDoOutput() {
	return connection.getDoOutput();
    }

    @Override
    public boolean getAllowUserInteraction() {
	return connection.getAllowUserInteraction();
    }

    @Override
    public boolean getUseCaches() {
	return connection.getUseCaches();
    }

    @Override
    public boolean getInstanceFollowRedirects() {
	return connection.getInstanceFollowRedirects();
    }

    @Override
    public long getIfModifiedSince() {
	return connection.getIfModifiedSince();
    }

    @Override
    public String getHeaderField(int index) {
	return connection.getHeaderField(index);
    }

    @Override
    public String getHeaderFieldKey(int index) {
	return connection.getHeaderFieldKey(index);
    }

    @Override
    public String getRequestProperty(String key) {
	return connection.getRequestProperty(key);
    }

    @Override
    public Map<String, List<String>> getRequestProperties() {
	return connection.getRequestProperties();
    }

    @Override
    public void setRequestProperty(String key, String value) {
	connection.setRequestProperty(key, value);
    }

    @Override
    public void addRequestProperty(String key, String value) {
	connection.addRequestProperty(key, value);
    }

    @Override
    public void setDefaultUseCaches(boolean defaultUseCaches) {
	connection.setDefaultUseCaches(defaultUseCaches);
    }

    @Override
    public void setInstanceFollowRedirects(boolean instanceFollowRedirects) {
	connection.setInstanceFollowRedirects(instanceFollowRedirects);
    }

    @Override
    public void setChunkedStreamingMode(int chunkSize) {
	connection.setChunkedStreamingMode(chunkSize);
    }

    @Override
    public void setFixedLengthStreamingMode(int contentLength) {
	connection.setFixedLengthStreamingMode(contentLength);
    }

    @Override
    public void setDoInput(boolean doInput) {
	connection.setDoInput(doInput);
	this.doInput = doInput;
    }

    @Override
    public void setDoOutput(boolean doOutput) {
	connection.setDoOutput(doOutput);
	this.doOutput = doOutput;
    }

    @Override
    public void setAllowUserInteraction(boolean allowUserInteraction) {
	this.allowUserInteraction = allowUserInteraction;
	connection.setAllowUserInteraction(allowUserInteraction);
    }

    @Override
    public void setUseCaches(boolean useCaches) {
	this.useCaches = useCaches;
	connection.setUseCaches(useCaches);
    }

    @Override
    public void setIfModifiedSince(long ifModifiedSince) {
	this.ifModifiedSince = ifModifiedSince;
	connection.setIfModifiedSince(ifModifiedSince);
    }

    @Override
    public void setRequestMethod(String requestMethod) throws ProtocolException {
	this.method = requestMethod;
	connection.setRequestMethod(requestMethod);
    }

    // Internal

    /**
     * Perform NTLM negotiations.
     */
    void getResponse() throws IOException {
	if (negotiated) return;
	boolean close = false;
	try {
	    for (int attempt=1; attempt < 3; attempt++) {
		connect();
		responseCode = connection.getResponseCode();
		contentLength = connection.getContentLength();
		responseMessage = connection.getResponseMessage();
		contentType = connection.getContentType();
		contentEncoding = connection.getContentEncoding();
		expiration = connection.getExpiration();
		date = connection.getDate();
		lastModified = connection.getLastModified();
		headerFields = new HashMap<String, List<String>>();
		for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
		    headerFields.put(entry.getKey(), entry.getValue());
		}
		close = "close".equals(headerFields.get("Connection"));

		switch(responseCode) {
		  case HTTP_UNAUTHORIZED:
		    if (session == null) {
			return;
		    } else {
			AuthenticateData auth = new AuthenticateData(connection.getHeaderField("WWW-Authenticate"));
			if (auth.isNegotiate()) {
			    reset();
			    setRequestProperty("Authorization", auth.createAuthenticateHeader(session));
			    phase = NtlmPhase.TYPE3;
			} else {
			    close = true;
			    return;
			}
		    }
		    break;

		  case HTTP_PROXY_AUTH:
		    if (proxySession == null) {
			return;
		    } else {
			AuthenticateData auth = new AuthenticateData(connection.getHeaderField("Proxy-Authenticate"));
			if (auth.isNegotiate()) {
			    reset();
			    setRequestProperty("Proxy-Authorization", auth.createAuthenticateHeader(proxySession));
			    proxyPhase = NtlmPhase.TYPE3;
			} else {
			    close = true;
			    return;
			}
		    }
		    break;

		  default:
		    responseData = responseCode == HTTP_OK ? connection.getInputStream() : connection.getErrorStream();
		    if (encrypt && contentType != null) {
			responseData = new ByteArrayInputStream(decrypt(responseData));
		    }
		    return;
		}
	    }
	} catch (SignatureException e) {
	    throw new IOException(e);
	} finally {
	    if (close) {
		lastUsed = 0; // don't recycle this connection!
	    } else {
		lastUsed = System.currentTimeMillis();
	    }
	    cachedOutput = null;
	    negotiated = true;
	}
    }

    // Private

    /**
     * Create a connection to a URL, and use the specified credentials to negotiate with the destination server.
     */
    private NtlmHttpURLConnection(URL url, PasswordAuthentication cred, boolean encrypt) {
	super(url);
	this.cred = cred;
	id = createId(url);
	connection = new HttpSocketConnection(url);
	if (cred == null) {
	    phase = NtlmPhase.NA;
	} else {
	    session = createSession(cred, encrypt);
	    phase = NtlmPhase.TYPE1;
	    this.encrypt = encrypt;
	}
	proxyPhase = NtlmPhase.NA;
    }

    private byte[] decrypt(InputStream in) throws IOException, SignatureException {
	StringTokenizer tok = new StringTokenizer(contentType, ";");
	if ("multipart/encrypted".equals(tok.nextToken())) {
	    String boundary = null;
	    while(tok.hasMoreTokens()) {
		String token = tok.nextToken();
		int ptr = token.indexOf("=");
		if (ptr != -1) {
		    String key = token.substring(0,ptr);
		    String val = token.substring(ptr+1);
		    if (val.startsWith("\"") && val.endsWith("\"")) {
			val = val.substring(1, val.length()-1);
		    }
		    if ("protocol".equalsIgnoreCase(key) && !"application/HTTP-SPNEGO-session-encrypted".equals(val)) {
			throw new IOException("Unknown encryption protocol: " + val);
		    } else if ("boundary".equalsIgnoreCase(key)) {
			boundary = val;
		    }
		}
	    }
	    if (boundary == null) {
		throw new IOException("No boundary specified for multipart message");
	    }
	    String pattern = new StringBuffer("--").append(boundary).toString();
	    KVP pair = null;
	    String line = null;
	    boolean started = false;
	    while((pair = readKVP(in)) != null) {
		if (pair.value().equals(pattern)) {
		    if (started) {
			break;
		    } else {
			started = true;
		    }
		} else if ("Content-Type".equalsIgnoreCase(pair.key())) {
		    if (!"application/HTTP-SPNEGO-session-encrypted".equals(pair.value())) {
			throw new IOException("Unknown " + pair.toString());
		    }
		} else if ("OriginalContent".equalsIgnoreCase(pair.key())) {
		    tok = new StringTokenizer(pair.value(), ";");
		    while(tok.hasMoreTokens()) {
			String token = tok.nextToken();
			int ptr = token.indexOf("=");
			if (ptr != -1) {
			    String key = token.substring(0,ptr);
			    String val = token.substring(ptr+1);
			    if (val.startsWith("\"") && val.endsWith("\"")) {
				val = val.substring(1, val.length()-1);
			    }
			    if ("type".equalsIgnoreCase(key)) {
				contentType = val;
			    } else if ("charset".equalsIgnoreCase(key)) {
				contentEncoding = val;
			    } else if ("length".equalsIgnoreCase(key)) {
				contentLength = Integer.parseInt(val);
			    }
			}
		    }
		}
	    }
	    pair = readKVP(in);
	    if ("Content-Type".equalsIgnoreCase(pair.key()) && "application/octet-stream".equals(pair.value())) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[512];
		int len = 0;
		while((len = in.read(buff)) > 0) {
		    out.write(buff, 0, len);
		}
		byte[] data = out.toByteArray();
		byte[] end = new StringBuffer(pattern).append("--\r\n").toString().getBytes("US-ASCII");

		int offset = data.length - end.length;
		if (offset < 0) {
		    throw new IOException("Data shorter than barrier");
		}
		if (!Arrays.equals(end, Arrays.copyOfRange(data, offset, data.length))) {
		    throw new IOException("Data not terminated with barrier");
		}
		return session.unseal(Arrays.copyOfRange(data, 0, offset));
	    } else {
		throw new IOException("Unexpected line: " + pair.toString());
	    }
	} else {
	    throw new IOException("Not encrypted content: " + contentType);
	}
    }

    /**
     * Read as ASCII from the stream until encountering CRLF.
     */
    private String readLine(InputStream in) throws IOException {
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	int ch = -1;
	boolean cr = false;
	while((ch = in.read()) != -1) {
	    switch(ch) {
	      case '\r':
		cr = true;
		break;
	      case '\n':
		if (cr) {
		    return new String(buffer.toByteArray(), "US-ASCII");
		}
	      default:
		if (cr) {
		    buffer.write(0xFF & '\r');
		    cr = false;
		}
		buffer.write((byte)(0xFF & ch));
		break;
	    }
	}
	if (buffer.size() > 0) {
	    return new String(buffer.toByteArray(), "US-ASCII");
	} else {
	    return null;
	}
    }

    /**
     * Prepare the connection for another attempt.
     */
    private void reset() throws IOException {
	drain(connection.getErrorStream());
	if (session == null) {
	    phase = NtlmPhase.NA;
	} else {
	    session = createSession(cred, encrypt);
	    phase = NtlmPhase.TYPE1;
	}
	negotiated = false;

	Map<String, List<String>> map = connection.getRequestProperties();
	connection.reset();
	connected = false;

	//
	// Transfer all headers, except the authorization headers
	//
	for (Map.Entry<String, List<String>> entry : map.entrySet()) {
	    if (phase != NtlmPhase.NA && "Authorization".equalsIgnoreCase(entry.getKey())) {
		//
		// Don't copy the Authorization header if this class is managing target NTLM authentication
		//
	    } else if (proxyPhase != NtlmPhase.NA && "Proxy-Authorization".equalsIgnoreCase(entry.getKey())) {
		//
		// Don't copy the Proxy-Authorization header if this class is managing proxy NTLM authentication
		//
	    } else {
		List<String> values = entry.getValue();
		connection.setRequestProperty(entry.getKey(), values.get(0));
		for (int i=1; i < values.size(); i++) {
		    connection.addRequestProperty(entry.getKey(), values.get(i));
		}
	    }
	}
	connection.setRequestMethod(method);
	connection.setAllowUserInteraction(allowUserInteraction);
	connection.setDoInput(doInput);
	connection.setDoOutput(doOutput);
	connection.setIfModifiedSince(ifModifiedSince);
	connection.setUseCaches(useCaches);
    }

    /**
     * Create an NtlmSession using the supplied credentials.
     */
    private NtlmSession createSession(PasswordAuthentication cred, boolean encrypt) {
	if (cred == null) {
	    return null;
	}
	String domain = null;
	String user = cred.getUserName();
	int ptr = user.indexOf("\\");
	if (ptr != -1) {
	    domain = user.substring(0, ptr);
	    user = user.substring(ptr+1);
	}
	return NtlmAuthenticator.createSession(NTLMV2, CO, encrypt, host, domain, user, cred.getPassword());
    }

    /**
     * Returns true when there is output to send, and any negotiations are concluding.
     */
    private boolean sendOutput() {
	return	(proxyPhase == NtlmPhase.NA || proxyPhase == NtlmPhase.TYPE3) &&
		(phase == NtlmPhase.NA || phase == NtlmPhase.TYPE3) && cachedOutput != null;
    }

    /**
     * Read any available data from the InputStream.
     */
    private void drain(InputStream stream) throws IOException {
	if (stream != null && stream.available() != 0) {
	    int count;
	    byte[] buf = new byte[1024];
	    while ((count = stream.read(buf, 0, 1024)) != -1);
	    stream.close();
	}
    }

    /**
     * Read from in into buff until buff is full.
     */
    private void readFully(InputStream in, byte[] buff) throws IOException {
	int len=0, offset=0;
	do {
	    len = in.read(buff, offset, buff.length - offset);
	    if (len > 0) {
		offset += len;
	    } else {
		throw new EOFException(Integer.toString(len));
	    }
	} while (offset < buff.length);
    }

    /**
     * Read a 32-bit unsigned little-endian integer.
     */
    private int readUInt(InputStream in) throws IOException {
	byte[] buff = new byte[4];
	readFully(in, buff);
	return (  buff[0] & 0xFF)        |
		((buff[1] & 0xFF) << 8)  |
		((buff[2] & 0xFF) << 16) |
		((buff[3] & 0xFF) << 24);
    }

    /**
     * Write a 32-bit unsigned little-endian integer.
     */
    private void writeUInt(int i, OutputStream out) throws IOException {
	byte[] buff = new byte[4];
	buff[0] = (byte)(0xFF & i);
	buff[1] = (byte)(0xFF & (i >> 8));
	buff[2] = (byte)(0xFF & (i >> 16));
	buff[3] = (byte)(0xFF & (i >> 24));
	out.write(buff);
    }

    // Inner Classes

    enum NtlmPhase {
	NA, TYPE1, TYPE3;
    }

    class AuthenticateData {
	String type;
	byte[] type2;

	AuthenticateData(String header) throws IOException {
	    int ptr = header.indexOf(" ");
	    if (ptr == -1) {
		type = header;
		type2 = null;
	    } else {
		type = header.substring(0,ptr);
		type2 = Base64.decode(header.substring(ptr+1));
	    }
	}

	boolean isNegotiate() {
	    return type2 != null;
	}

	String getType() {
	    return type;
	}

	String createNegotiateHeader(NtlmSession session) {
	    StringBuffer header = new StringBuffer(type);
	    header.append(" ");
	    header.append(Base64.encodeBytes(session.generateNegotiateMessage()));
	    return header.toString();
	}

	String createAuthenticateHeader(NtlmSession session) {
	    if (type2 == null) {
		throw new IllegalStateException("no challenge");
	    }
	    session.processChallengeMessage(type2);
	    StringBuffer header = new StringBuffer(type);
	    header.append(" ");
	    header.append(Base64.encodeBytes(session.generateAuthenticateMessage()));
	    return header.toString();
	}
    }
}
