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
package io.cloudslang.content.mail.services;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

public class IMAPGetMailMessage {
    public static void receiveMail(String userName, String password) {
    try {
        String proxyIP = "124.124.124.14";
        String proxyPort = "4154";
        String proxyUser = "test";
        String proxyPassword = "test123";
        Properties prop = new Properties();
        prop.setProperty("mail.imaps.proxy.host", proxyIP);
        prop.setProperty("mail.imaps.proxy.port", proxyPort);
        prop.setProperty("mail.imaps.proxy.user", proxyUser);
        prop.setProperty("mail.imaps.proxy.password", proxyPassword);

        Session eSession = Session.getInstance(prop);

        Store eStore = eSession.getStore("imaps");
        eStore.connect("outlook.office365.com", userName, password);

        Folder eFolder = eStore.getFolder("Inbox");
        eFolder.open(Folder.READ_WRITE);
        Message messages[] = eFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        System.out.println(messages.length);
        for (int i = messages.length - 3; i < messages.length - 2; i++) {
            Message message = messages[i];
            System.out.println("Email Number::" + (i + 1));
            System.out.println("Subject::" + message.getSubject());
            System.out.println("From::" + message.getFrom()[0]);
            System.out.println("Date::" + message.getSentDate());

            try {
                Multipart multipart = (Multipart) message.getContent();

                for (int x = 0; x < multipart.getCount(); x++) {
                    BodyPart bodyPart = multipart.getBodyPart(x);

                    String disposition = bodyPart.getDisposition();

                    if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
                        System.out.println("Mail have some attachment : ");

                        DataHandler handler = bodyPart.getDataHandler();
                        System.out.println("file name : " + handler.getName());
                    } else {
                        System.out.println(bodyPart.getContent());
                    }

                }
            } catch (Exception e) {
                System.out.println("Content: " + message.getContent().toString());
            }

            message.setFlag(Flags.Flag.SEEN, true);
        }
        eFolder.close(true);
        eStore.close();

    } catch (NoSuchProviderException e) {
        e.printStackTrace();
    } catch (MessagingException e) {
        e.printStackTrace();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }


}

    public static void main(String[] args) {
        receiveMail("testnguser@itomcontent.onmicrosoft.com", "B33f34t3r#");
    }
}
