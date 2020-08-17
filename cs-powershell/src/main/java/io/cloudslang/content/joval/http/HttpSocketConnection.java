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

import io.cloudslang.content.joval.util.RFC822;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.security.Permission;
import java.util.*;

/**
 * An HTTP 1.1 connection implementation that re-uses a single socket connection.  This is useful when a single TCP connection
 * is needed to communicate repeatedly with a particular URL, for example, when performing NTLM authentication negotiation.
 *
 * Thanks to James Marshall for his concise discussion of HTTP/1.1:
 * @see http://www.jmarshall.com/easy/http/#http1.1c2
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class HttpSocketConnection extends AbstractConnection {
    private static int defaultChunkLength = 512;

    private boolean secure, tunnelFailure;
    private Socket socket;
    private Proxy proxy;
    private String host; // host:port
    private boolean gotResponse;
    private HSOutputStream stream;

    /**
     * Create a direct connection.
     */
    public HttpSocketConnection(URL url) {
	this(url, null);
    }

    /**
     * Create a connection through a proxy.
     */
    public HttpSocketConnection(URL url, Proxy proxy) throws IllegalArgumentException {
	super(url);
	if (url.getProtocol().equalsIgnoreCase("HTTPS")) {
	    secure = true;
	} else if (url.getProtocol().equalsIgnoreCase("HTTP")) {
	    secure = false;
	} else {
	    throw new IllegalArgumentException("Unsupported protocol: " + url.getProtocol());
	}
	setProxy(proxy);
	StringBuffer sb = new StringBuffer(url.getHost());
	if (url.getPort() == -1) {
	    if (secure) {
		sb.append(":443");
	    } else {
		sb.append(":80");
	    }
	} else {
	    sb.append(":").append(Integer.toString(url.getPort()));
	}
	host = sb.toString();
	reset();
    }

    // Overrides for HttpURLConnection

    @Override
    public Permission getPermission() throws IOException {
	return new java.net.SocketPermission(host, "connect");
    }

    @Override
    public boolean usingProxy() {
	return proxy == null ? false : proxy.type() != Proxy.Type.DIRECT;
    }

    /**
     * If a fixed content length has not been set, this method causes the connection to use chunked encoding.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
	if (doOutput) {
	    if (stream == null) {
		switch(fixedContentLength) {
		  case -1:
		    stream = new HSChunkedOutputStream(chunkLength);
		    break;
		  default:
		    stream = new HSOutputStream(fixedContentLength);
		    break;
		}
	    }
	    connect();
	    return stream;
	} else {
	    throw new IllegalStateException("Output not allowed");
	}
    }

    @Override
    public void setRequestProperty(String key, String value) {
	if (connected) {
	    throw new IllegalStateException("Already connected");
	}
	setMapProperty(key, value, requestProperties);
    }

    @Override
    public void addRequestProperty(String key, String value) {
	if (connected) {
	    throw new IllegalStateException("Already connected");
	}
	addMapProperty(key, value, requestProperties);
    }

    @Override
    public String getRequestProperty(String key) {
	for (Map.Entry<String, List<String>> entry : requestProperties.entrySet()) {
	    if (key.equalsIgnoreCase(entry.getKey())) {
		return entry.getValue().get(0);
	    }
	}
	return null;
    }

    @Override
    public Map<String, List<String>> getRequestProperties() {
	Map<String, List<String>> map = new HashMap<String, List<String>>();
	for (Map.Entry<String, List<String>> entry : requestProperties.entrySet()) {
	    map.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
	}
	return Collections.unmodifiableMap(map);
    }

    @Override
    public void disconnect() {
	if (connected) {
	    try {
		socket.close();
	    } catch (IOException e) {
	    }
	    connected = false;
	}
    }

    @Override
    public void connect() throws IOException {
	if (connected) {
	    return;
	}
	if (socket == null || socket.isClosed()) {
	    switch(proxy.type()) {
	      case SOCKS:
		socket = new Socket(proxy);
		socket.connect(new InetSocketAddress(url.getHost(), url.getPort()));
		break;
	      case HTTP:
		socket = new Socket();
		socket.connect(proxy.address());
		break;
	      case DIRECT:
	      default:
		socket = new Socket(url.getHost(), url.getPort());
		break;
	    }
	    socket.setSoTimeout(TIMEOUT);
	}
	if (secure && proxy.type() == Proxy.Type.HTTP) {
	    //
	    // Establish a tunnel through the proxy
	    //
	    write(new StringBuffer("CONNECT ").append(host).append(" HTTP/1.1").toString());
	    write(CRLF);
	    String temp = getRequestProperty("Proxy-Authorization");
	    if (temp != null) {
		write(new KVP("Proxy-Authorization", temp));
	    }
	    temp = getRequestProperty("User-Agent");
	    if (temp != null) {
		write(new KVP("User-Agent", temp));
	    }
	    if (method.equalsIgnoreCase("GET") && ifModifiedSince > 0) {
		write(new KVP("If-Modified-Since", RFC822.toString(ifModifiedSince)));
	    }
	    write(new KVP("Connection", "Keep-Alive"));
	    write(CRLF);

	    InputStream in = socket.getInputStream();
	    Map<String, List<String>> map = new HashMap<String, List<String>>();
	    KVP pair = null;
	    while((pair = readKVP(in)) != null) {
		if (pair.key().length() == 0) {
		    parseResponse(pair.value());
		}
		if (responseCode != HTTP_OK) {
		    if (orderedHeaderFields.size() > 0) {
			addMapProperties(pair, map);
		    }
		    orderedHeaderFields.add(pair);
		}
	    }
	    if (responseCode == HTTP_OK) {
		//
		// Establish a socket tunnel
		//
		int port = Integer.parseInt(host.substring(host.indexOf(":") + 1));
		socket = ((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(socket, url.getHost(), port, true);
	    } else {
		stream = new HSDevNull();
		headerFields = Collections.unmodifiableMap(map);
		gotResponse = true;
	    }
	} else {
	    StringBuffer req = new StringBuffer(getRequestMethod()).append(" ");
	    switch(proxy.type()) {
	      case SOCKS:
	      case DIRECT:
		String path = url.getPath();
		if (!path.startsWith("/")) {
		    req.append("/");
		}
		req.append(path);
		break;
	      case HTTP:
		req.append(url.toString());
		break;
	    }
	    req.append(" HTTP/1.1");
	    setRequestProperty("Connection", "Keep-Alive");
	    setRequestProperty("Host", host);
	    if (doOutput) {
		if (fixedContentLength != -1) {
		    setRequestProperty("Content-Length", Integer.toString(fixedContentLength));
		} else {
		    if (chunkLength == -1) {
			chunkLength = defaultChunkLength;
		    }
		    setRequestProperty("Transfer-Encoding", "chunked");
		}
	    }
	    write(req.toString());
	    write(CRLF);
	    for (Map.Entry<String, List<String>> entry : requestProperties.entrySet()) {
		KVP pair = new KVP(entry);
		write(pair);
	    }
	    write(CRLF);
	}
	connected = true;
    }

    // Internal

    /**
     * Check to determine whether the socket is connected.
     */
    boolean connected() {
	return socket != null && socket.isConnected();
    }

    /**
     * Set a proxy.
     */
    void setProxy(Proxy proxy) {
	if (proxy == null) {
	    proxy = Proxy.NO_PROXY;
	}
	if (connected()) {
	    if (!proxy.equals(this.proxy)) {
		throw new IllegalStateException("Cannot change proxies when connected");
	    }
	} else {
	    if (proxy == null) {
		this.proxy = Proxy.NO_PROXY;
	    } else {
		this.proxy = proxy;
	    }
	}
    }

    /**
     * Reset the connection to a pristine state.
     */
    void reset() {
	initialize();
	setRequestProperty("User-Agent", "jWSMV HTTP Client");
	if (stream != null) {
	    try {
		stream.close();
	    } catch (IOException e) {
		disconnect();
	    }
	    stream = null;
	}
	responseData = null;
	gotResponse = false;
    }

    /**
     * Read the response over the socket.
     */
    @Override
    void getResponse() throws IOException {
	if (gotResponse) return;
	connect();
	try {
	    if (stream == null) {
		switch(fixedContentLength) {
		  case -1:
		    //
		    // connect() would have assumed chunked transfer-encoding, so write a final 0-length chunk.
		    //
		    write("0");
		    write(CRLF);
		    write(CRLF);
		    // fall-thru
		  case 0:
		    break;
		  default:
		    throw new IllegalStateException("You promised to write " + fixedContentLength + " bytes!");
		}
	    } else if (!stream.complete()) {
		throw new IllegalStateException("You must write " + stream.remaining() + " more bytes!");
	    } else {
		stream.close();
	    }

	    orderedHeaderFields = new ArrayList<KVP>();
	    Map<String, List<String>> map = new HashMap<String, List<String>>();

	    boolean chunked = false;
	    InputStream in = socket.getInputStream();
	    KVP pair = null;
	    while((pair = readKVP(in)) != null) {
		if (orderedHeaderFields.size() == 0) {
		    parseResponse(pair.value());
		} else {
		    addMapProperties(pair, map);
		}
		orderedHeaderFields.add(pair);
		if ("Content-Length".equalsIgnoreCase(pair.key())) {
		    contentLength = Integer.parseInt(pair.value());
		} else if ("Content-Type".equalsIgnoreCase(pair.key())) {
		    contentType = pair.value();
		} else if ("Content-Encoding".equalsIgnoreCase(pair.key())) {
		    contentEncoding = pair.value();
		} else if ("Transfer-Encoding".equalsIgnoreCase(pair.key())) {
		    chunked = pair.value().equalsIgnoreCase("chunked");
		} else if ("Date".equalsIgnoreCase(pair.key())) {
		    try {
			date = RFC822.valueOf(pair.value());
		    } catch (IllegalArgumentException e) {
		    }
		} else if ("Last-Modified".equalsIgnoreCase(pair.key())) {
		    try {
			lastModified = RFC822.valueOf(pair.value());
		    } catch (IllegalArgumentException e) {
		    }
		} else if ("Expires".equalsIgnoreCase(pair.key())) {
		    try {
			expiration = RFC822.valueOf(pair.value());
		    } catch (IllegalArgumentException e) {
		    }
		}
	    }

	    if (chunked) {
		HSBufferedOutputStream buffer = new HSBufferedOutputStream();
		int len = 0;
		while((len = readChunkLength(in)) > 0) {
		    byte[] bytes = new byte[len];
		    assume(len == in.read(bytes, 0, len));
		    buffer.write(bytes);
		    assume(in.read() == '\r');
		    assume(in.read() == '\n');
		}
		responseData = new HSBufferedInputStream(buffer);
		contentLength = ((HSBufferedInputStream)responseData).size();

		//
		// Read footers (if any)
		//
		while((pair = readKVP(in)) != null) {
		    orderedHeaderFields.add(pair);
		    addMapProperties(pair, map);
		}
	    } else {
		byte[] bytes = new byte[contentLength];
		for (int offset=0; offset < contentLength; ) {
		    offset += in.read(bytes, offset, contentLength - offset);
		}
		responseData = new HSBufferedInputStream(bytes);
	    }

	    headerFields = Collections.unmodifiableMap(map);
	} finally {
	    gotResponse = true;
	    if (headerFields == null || "Close".equalsIgnoreCase(getHeaderField("Connection"))) {
		disconnect();
	    }
	}
    }

    // Private

    private void write(KVP header) throws IOException {
	write(header.toString());
	write(CRLF);
    }

    private void write(String s) throws IOException {
	write(s.getBytes("US-ASCII"));
    }

    private void write(int ch) throws IOException {
	socket.getOutputStream().write(ch);
    }

    private void write(byte[] bytes) throws IOException {
	write(bytes, 0, bytes.length);
    }

    private void write(byte[] bytes, int offset, int len) throws IOException {
	socket.getOutputStream().write(bytes, offset, len);
	socket.getOutputStream().flush();
    }

    /**
     * Parse the HTTP response line.
     */
    private void parseResponse(String line) throws IllegalArgumentException {
	StringTokenizer tok = new StringTokenizer(line, " ");
	if (tok.countTokens() < 2) {
	    throw new IllegalArgumentException(line);
	}
	String httpVersion = tok.nextToken();
	responseCode = Integer.parseInt(tok.nextToken());
	if (tok.hasMoreTokens()) {
	    responseMessage = tok.nextToken("\r\n");
	}
    }

    /**
     * Read a line ending in CRLF that indicates the length of the next chunk.
     */
    private int readChunkLength(InputStream in) throws IOException {
	StringBuffer sb = new StringBuffer();
	boolean done = false;
	boolean cr = false;
	while(!done) {
	    int ch = in.read();
	    switch(ch) {
	      case -1:
		throw new IOException("Connection was closed!");
	      case '\r':
		if (sb.length() == 0) {
		    cr = true;
		}
		break;
	      case '\n':
		if (cr) {
		    done = true;
		}
		break;
	      default:
		sb.append((char)ch);
		break;
	    }
	}

	String line = sb.toString();
	int ptr = line.indexOf(";");
	if (ptr > 0) {
	    return Integer.parseInt(line.substring(0,ptr), 16);
	} else {
	    return Integer.parseInt(line, 16);
	}
    }

    /**
     * Like assert, but always enabled.
     */
    private void assume(boolean test) throws AssertionError {
	if (!test) throw new AssertionError();
    }

    /**
     * ByteArrayOutputStream that provides access to the underlying memory buffer.
     */
    class HSBufferedOutputStream extends ByteArrayOutputStream {
	HSBufferedOutputStream() {
	    super();
	}

	/**
	 * Access the underlying buffer -- NOT A COPY.
	 */
	byte[] getBuf() {
	    return buf;
	}
    }

    /**
     * InputStream that resets this connection when closed.
     */
    class HSBufferedInputStream extends ByteArrayInputStream {
	private boolean closed;

	HSBufferedInputStream(HSBufferedOutputStream out) {
	    super(out.getBuf(), 0, out.size());
	    closed = false;
	}

	HSBufferedInputStream(byte[] buffer) {
	    super(buffer);
	    closed = false;
	}

	int size() {
	    return count;
	}

	@Override
	public void close() throws IOException {
	    if (!closed) {
		super.close();
		reset();
		closed = true;
	    }
	}
    }

    /**
     * An OutputStream for a fixed-length stream.
     */
    class HSOutputStream extends OutputStream {
	private int size;

	int ptr;
	boolean closed;

	HSOutputStream(int size) {
	    this.size = size;
	    ptr = 0;
	    closed = false;
	}

	boolean complete() {
	    return remaining() == 0;
	}

	int remaining() {
	    return size - ptr;
	}

	// InputStream overrides

	@Override
	public void write(int ch) throws IOException {
	    if (closed) throw new IOException("stream closed");
	    ptr++;
	    if (ptr > size) {
		throw new IOException("Buffer overflow " + ptr);
	    }
	    HttpSocketConnection.this.write(ch);
	}

	@Override
	public void write(byte[] b) throws IOException {
	    if (closed) throw new IOException("stream closed");
	    write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
	    if (closed) throw new IOException("stream closed");
	    ptr = ptr + len;
	    if (ptr > size) {
		throw new IOException("Buffer overflow " + ptr + ", size=" + size);
	    }
	    HttpSocketConnection.this.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
	    if (closed) throw new IOException("stream closed");
	    socket.getOutputStream().flush();
	}

	@Override
	public void close() throws IOException {
	    if (!closed) {
		if (complete()) {
		    flush();
		    closed = true;
		} else {
		    throw new IOException("You need to write " + remaining() + " more bytes!");
		}
	    }
	}
    }

    /**
     * A safe place (i.e., nowhere) to write output in the event of a failure to establish a CONNECT tunnel through
     * an HTTP proxy.
     */
    class HSDevNull extends HSOutputStream {
	HSDevNull() {
	    super(0);
	}

	@Override
	public void write(int ch) {}

	@Override
	public void write(byte[] b) {}

	@Override
	public void write(byte[] b, int offset, int len) {}

	@Override
	public void flush() {}

	@Override
	public void close() {}
    }

    /**
     * An OutputStream for chunked stream encoding.
     */
    class HSChunkedOutputStream extends HSOutputStream {
	private byte[] buffer;

	HSChunkedOutputStream(int chunkSize) {
	    super(chunkSize);
	    buffer = new byte[chunkSize];
	}

	@Override
	boolean complete() {
	    return ptr == 0;
	}

	@Override
	public void write(int ch) throws IOException {
	    if (closed) throw new IOException("stream closed");
	    int end = ptr + 1;
	    if (end <= buffer.length) {
		buffer[ptr++] = (byte)(ch & 0xFF);
		if (end == buffer.length) {
		    flush();
		}
	    } else {
		throw new IOException("Buffer overrun " + ptr);
	    }
	}

	@Override
	public void write(byte[] buff) throws IOException {
	    if (closed) throw new IOException("stream closed");
	    write(buff, 0, buff.length);
	}

	@Override
	public void write(byte[] buff, int offset, int len) throws IOException {
	    if (closed) throw new IOException("stream closed");
	    len = Math.min(buff.length - offset, len);
	    int end = ptr + len;
	    if (end <= buffer.length) {
		System.arraycopy(buff, offset, buffer, ptr, len);
		ptr = end;
		if (end == buffer.length) {
		    flush();
		}
	    } else {
		int remainder = buffer.length - ptr;
		write(buff, offset, remainder);
		write(buff, offset + remainder, len - remainder);
	    }
	}

	@Override
	public void flush() throws IOException {
	    if (closed) throw new IOException("stream closed");
	    if (ptr > 0) {
		HttpSocketConnection.this.write(Integer.toHexString(ptr));
		HttpSocketConnection.this.write(CRLF);
		HttpSocketConnection.this.write(buffer, 0, ptr);
		HttpSocketConnection.this.write(CRLF);
		buffer = new byte[buffer.length];
		ptr = 0;
	    }
	}

	@Override
	public void close() throws IOException {
	    if (!closed) {
		flush();
		HttpSocketConnection.this.write("0");
		HttpSocketConnection.this.write(CRLF);
		HttpSocketConnection.this.write(CRLF);
		closed = true;
	    }
	}
    }

    /**
     * An output stream to nowhere.
     */
    class DevNull extends OutputStream {
	DevNull() {}

	@Override
	public void write(int ch) {}
    }
}
