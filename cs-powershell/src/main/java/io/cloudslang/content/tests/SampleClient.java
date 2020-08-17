/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.tests;

/*
 *
 * Copyright (c) 2001, 2002, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -Redistributions of source code must retain the above copyright
 * notice, this  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduct the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Oracle nor the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES  SUFFERED BY LICENSEE AS A RESULT OF  OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

import org.ietf.jgss.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A sample client application that uses JGSS to do mutual authentication
 * with a server using Kerberos as the underlying mechanism. It then
 * exchanges data securely with the server.
 *
 * Every message sent to the server includes a 4-byte application-level
 * header that contains the big-endian integer value for the number
 * of bytes that will follow as part of the JGSS token.
 *
 * The protocol is:
 *    1.  Context establishment loop:
 *         a. client sends init sec context token to server
 *         b. server sends accept sec context token to client
 *         ....
 *    2. client sends a wrap token to the server.
 *    3. server sends a MIC token to the client for the application
 *       message that was contained in the wrap token.
 */

public class SampleClient {

    public static void main(String[] args)
            throws IOException, GSSException  {

        // Obtain the command-line arguments and parse the port number

//        if (args.length < 3) {
//            System.err.println("Usage: java <options> Login SampleClient "
//                    + " <server> <hostName> <port>");
//            System.exit(-1);
//        }

        /*
        Important: In this command, you must replace <service_principal>, <host>, <port_number>, <your_realm>, and <your_kdc> with appropriate values (and note that the port number must be the same as the port number passed as an argument to SampleServer). These values need not be placed in quotes.

Here is the command:
java
-Djava.security.krb5.realm=TESTING924.ROSVC5.COM
 -Djava.security.krb5.kdc=win-av1p52vbbod.TESTING924.ROSVC5.COM
 -Djavax.security.auth.useSubjectCredsOnly=false
 -Djava.security.auth.login.config=C:\Users\omega\kerberos\login.conf
 SampleClient <service_principal> <host> <port_number>
        */


        String server = "HTTP/W2k12Christmas.TESTING924.ROSVC5.COM@TESTING924.ROSVC5.COM"; //The name of the Kerberos principal that represents SampleServer.
        String client = "omega";
        String hostName = "W2k12Christmas.TESTING924.ROSVC5.COM"; //server hostname
        int port = Integer.parseInt("5985");

        Socket socket = new Socket(hostName, port);
        DataInputStream inStream =
                new DataInputStream(socket.getInputStream());
        DataOutputStream outStream =
                new DataOutputStream(socket.getOutputStream());

        System.out.println("Connected to server "
                + socket.getInetAddress());

        /*
         * This Oid is used to represent the Kerberos version 5 GSS-API
         * mechanism. It is defined in RFC 1964. We will use this Oid
         * whenever we need to indicate to the GSS-API that it must
         * use Kerberos for some purpose.
         */
        Oid krb5Oid = new Oid("1.2.840.113554.1.2.2");
        Oid KRB5_PRINCIPAL_NAME_OID = new Oid("1.2.840.113554.1.2.2.1");

        GSSManager manager = GSSManager.getInstance();

        /*
         * Create a GSSName out of the server's name. The null
         * indicates that this application does not wish to make
         * any claims about the syntax of this name and that the
         * underlying mechanism should try to parse it as per whatever
         * default syntax it chooses.
         */
        GSSName serverName = manager.createName(server, null);
        GSSName clientName = manager.createName(client, KRB5_PRINCIPAL_NAME_OID);

        /*
         * Create a GSSContext for mutual authentication with the
         * server.
         *    - serverName is the GSSName that represents the server.
         *    - krb5Oid is the Oid that represents the mechanism to
         *      use. The client chooses the mechanism to use.
         *    - null is passed in for client credentials
         *    - DEFAULT_LIFETIME lets the mechanism decide how long the
         *      context can remain valid.
         * Note: Passing in null for the credentials asks GSS-API to
         * use the default credentials. This means that the mechanism
         * will look among the credentials stored in the current Subject
         * to find the right kind of credentials that it needs.
         */
        GSSCredential clientCred = manager.createCredential(clientName, 8 * 3600, krb5Oid, GSSCredential.INITIATE_ONLY);;
        GSSContext context = manager.createContext(serverName,
                krb5Oid,
                clientCred,
                GSSContext.DEFAULT_LIFETIME);

        // Set the desired optional features on the context. The client
        // chooses these options.

        context.requestMutualAuth(false);  // Mutual authentication
        context.requestConf(true);  // Will use confidentiality later
        context.requestInteg(true); // Will use integrity later

        // Do the context eastablishment loop

        byte[] token = new byte[0];

        while (!context.isEstablished()) {

            // token is ignored on the first call
            token = context.initSecContext(token, 0, token.length);

            // Send a token to the server if one was generated by
            // initSecContext
            if (token != null) {
                System.out.println("Will send token of size "
                        + token.length
                        + " from initSecContext.");
                outStream.writeInt(token.length);
                outStream.write(token);
                outStream.flush();
            }

            // If the client is done with context establishment
            // then there will be no more tokens to read in this loop
            if (!context.isEstablished()) {
                token = new byte[inStream.readInt()];
                System.out.println("Will read input token of size "
                        + token.length
                        + " for processing by initSecContext");
                inStream.readFully(token);
            }
        }

        System.out.println("Context Established! ");
        System.out.println("Client is " + context.getSrcName());
        System.out.println("Server is " + context.getTargName());

        /*
         * If mutual authentication did not take place, then only the
         * client was authenticated to the server. Otherwise, both
         * client and server were authenticated to each other.
         */
        if (context.getMutualAuthState())
            System.out.println("Mutual authentication took place!");

        byte[] messageBytes = "Hello There!\0".getBytes();

        /*
         * The first MessageProp argument is 0 to request
         * the default Quality-of-Protection.
         * The second argument is true to request
         * privacy (encryption of the message).
         */
        MessageProp prop =  new MessageProp(0, true);

        /*
         * Encrypt the data and send it across. Integrity protection
         * is always applied, irrespective of confidentiality
         * (i.e., encryption).
         * You can use the same token (byte array) as that used when
         * establishing the context.
         */

        token = context.wrap(messageBytes, 0, messageBytes.length, prop);
        System.out.println("Will send wrap token of size " + token.length);
        outStream.writeInt(token.length);
        outStream.write(token);
        outStream.flush();

        /*
         * Now we will allow the server to decrypt the message,
         * calculate a MIC on the decrypted message and send it back
         * to us for verification. This is unnecessary, but done here
         * for illustration.
         */

        token = new byte[inStream.readInt()];
        System.out.println("Will read token of size " + token.length);
        inStream.readFully(token);
        context.verifyMIC(token, 0, token.length,
                messageBytes, 0, messageBytes.length,
                prop);

        System.out.println("Verified received MIC for message.");

        System.out.println("Exiting...");
        context.dispose();
        socket.close();
    }
}
