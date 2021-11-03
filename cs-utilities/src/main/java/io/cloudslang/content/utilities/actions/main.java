package io.cloudslang.content.utilities.actions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.cloudslang.content.httpclient.HttpClientAction;
import org.apache.commons.io.FileUtils;


import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class main {

    public static void downloadFile2(String URL, String fileName) throws IOException {

        FileUtils.copyURLToFile(
                new URL(URL),
                new File(fileName),
                30000,
                30000);

    }

    public static void downloadFile(String initialString, String fileName) throws IOException {

        try (InputStream in = new ByteArrayInputStream(initialString.getBytes());
             BufferedInputStream bis = new BufferedInputStream(in);
             FileOutputStream fos = new FileOutputStream(fileName)) {

            byte[] data = new byte[1024];
            int count;
            while ((count = bis.read(data, 0, 1024)) != -1) {
                fos.write(data, 0, count);
            }
        }
    }



    public static void main(String[] args) {

        String token = "eyJ0eXAiOiJKV1QiLCJub25jZSI6InF4UUtLZTdRSlRqMnktVFpmU01KZDIzSFU5cXY3aWd3bFZHRWNYQXF6OVkiLCJhbGciOiJSUzI1NiIsIng1dCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCIsImtpZCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9iNGVlODRlNS0yNTA3LTRlOGYtYTVjMS1mZTY2M2YzZDJmODAvIiwiaWF0IjoxNjM1OTQwOTUyLCJuYmYiOjE2MzU5NDA5NTIsImV4cCI6MTYzNTk0NTc0NywiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkUyWmdZR2hheURiZGtsR1EyM0JLUjluci9VRU4xaS9kTzFsVWRqU1VYVmk1MmNrN1V3Z0EiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6IlRlc3RfU2hhcmVQb2ludCIsImFwcGlkIjoiOWM5MTk4MGYtY2IxOS00YzE1LThlMmMtMjNkY2M5YTQ4NThhIiwiYXBwaWRhY3IiOiIwIiwiZmFtaWx5X25hbWUiOiJFbmVhIiwiZ2l2ZW5fbmFtZSI6Ik1hZGFsaW4iLCJpZHR5cCI6InVzZXIiLCJpcGFkZHIiOiIxODguMjQuOTguMTIzIiwibmFtZSI6Ik1hZGFsaW4gRW5lYSIsIm9pZCI6ImI3MmU0MmY1LTg2ZGMtNDM4Zi1iNDQwLTZmNWFiZTQ2ODBmZCIsInBsYXRmIjoiMTQiLCJwdWlkIjoiMTAwMzAwMDBBRUY1RDhFMiIsInB3ZF9leHAiOiI0NjY4NDciLCJwd2RfdXJsIjoiaHR0cHM6Ly9wb3J0YWwubWljcm9zb2Z0b25saW5lLmNvbS9DaGFuZ2VQYXNzd29yZC5hc3B4IiwicmgiOiIwLkFTOEE1WVR1dEFjbGowNmx3ZjVtUHowdmdBLVlrWndaeXhWTWppd2ozTW1raFlvdkFCay4iLCJzY3AiOiJBbGxTaXRlcy5SZWFkIEFsbFNpdGVzLldyaXRlIE15RmlsZXMuUmVhZCBNeUZpbGVzLldyaXRlIFVzZXIuUmVhZCBwcm9maWxlIG9wZW5pZCBlbWFpbCIsInN1YiI6IjVkSU5NdmMyRkgwU2xWQWNZbWkyY1JsTmVCWERfSlB1Rnp0c1h2aUZiRmMiLCJ0ZW5hbnRfcmVnaW9uX3Njb3BlIjoiRVUiLCJ0aWQiOiJiNGVlODRlNS0yNTA3LTRlOGYtYTVjMS1mZTY2M2YzZDJmODAiLCJ1bmlxdWVfbmFtZSI6ImRlYWRwb29sQGl0b21jb250ZW50Lm9ubWljcm9zb2Z0LmNvbSIsInVwbiI6ImRlYWRwb29sQGl0b21jb250ZW50Lm9ubWljcm9zb2Z0LmNvbSIsInV0aSI6IjFEQVBiY0RkUVVtMV9BWWRpS1VQQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCIsImI3OWZiZjRkLTNlZjktNDY4OS04MTQzLTc2YjE5NGU4NTUwOSJdLCJ4bXNfc3QiOnsic3ViIjoiU0dyRG90bXVBWUlZQ2JkVnNJSE1ubVNjZU0tOTZyVEM1S0Y0bE5rWGFGayJ9LCJ4bXNfdGNkdCI6MTU0MTAwMDI4OH0.YKcGYiuclpGfo26cQisHg1HwLd1ORzJNnbKeShtT2ZmHG_xNdZMTBd8qR5z--dkJzt3NP_1ZQ0B4_w1-zVyjnqB0GC5VPyQBlr_dgJZgEj-1ro1VfmhoxACfGvv9fuzH4oCQ6ZtuYxuTjF-g6T6ztM-h847qSUNqnmUbwic4GNls2cNg9O1fpmHGIqrqyTqKgrVxoK8qwrawo0RGDk_-ExIpwrmTo2xce9NfPejuf6dhL5GlaXvbLyNOzDor_0Jar-jvZfvXt_sZEwlADAdLSOZGfVr-1a8UKiZKwpCxp-YXKkCXvtD9quzjKbKS0bVQ6d8XwoVNNdrOuBTqZz0L_A";
        String id = "01GLVAXZPAXF34YWNK4VB2DVPRIARJPGE4";
        HttpClientAction httpClient = new HttpClientAction();
        Map<String,String> responseFileDetails = httpClient.execute("https://graph.microsoft.com/v1.0/sites/root/drive/items/" + id,
                "Anonymous",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "true",
                "allow_all",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "Authorization: Bearer  " + token,
                "UTF-8",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "GET",
                null,
                null

        );

        Gson gson = new Gson();
        JsonObject myFileName = gson.fromJson(responseFileDetails.get("returnResult"), JsonObject.class);

        String myUrl = myFileName.get("@microsoft.graph.downloadUrl").toString().substring(1,myFileName.get("@microsoft.graph.downloadUrl").toString().length()-1);
        String fileName = myFileName.get("name").toString().substring(1,myFileName.get("name").toString().length()-1);
    try {
        downloadFile2(myUrl, "C:\\Users\\ABenta\\Desktop\\downloadedd\\" + fileName);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
       /* final Map<String, String> result = HttpCommons.httpGet(AzureActiveDirectoryCommonInputs.builder()
                        .authToken(token)
                        .proxyHost("")
                        .proxyPort("")
                        .proxyUsername("")
                        .proxyPassword("")
                        .connectionsMaxTotal("")
                        .connectionsMaxPerRoute("")
                        .keepAlive("")
                        .connectTimeout("")
                        .socketTimeout("")
                        .trustAllRoots("")
                        .userId("")
                        .userPrincipalName("")
                        .x509HostnameVerifier("")
                        .trustKeystore("")
                        .trustPassword("")
                        .build(),
                "https://graph.microsoft.com/v1.0/sites/root/drive/items/01GLVAXZIEMTT4GXXOBBGZ6335CMQKJBUZ/content"
        );*/



        /*Map<String,String> response = httpClient.execute("https://graph.microsoft.com/v1.0/sites/root/drive/items/" + id  +"/content",
                "Anonymous",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "true",
                "allow_all",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "Authorization: Bearer " + token,
                "UTF-8",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "GET",
                null,
                null

        );*/

        /*try {
            downloadFile(response.get("returnResult"), "C:/Users/ABenta/Desktop/downloadedd/" + myFileName.get("name").toString().substring(1, myFileName.get("name").toString().length() - 1));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }*/


    }
}
