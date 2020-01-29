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
