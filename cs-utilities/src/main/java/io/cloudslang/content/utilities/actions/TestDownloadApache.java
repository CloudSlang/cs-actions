package io.cloudslang.content.utilities.actions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.cloudslang.content.httpclient.HttpClientAction;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


import java.io.*;
import java.util.Arrays;
import java.util.Map;

import static io.cloudslang.content.microsoftAD.utils.Constants.UTF8;


public class TestDownloadApache {

    public static void downloadFile2(String URL, String fileName, String token) throws IOException {

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);

        request.setHeader("Authorization","Bearer "+ token );

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();



        int responseCode = response.getStatusLine().getStatusCode();

        System.out.println("Request Url: " + request.getURI());
        System.out.println("Response Code: " + responseCode);
        InputStream is = entity.getContent();

        String filePath = fileName;
        FileOutputStream fos = new FileOutputStream(new File(filePath));

        int inByte;
        final int BUFFER_SIZE = 192 * 320 * 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((inByte = is.read(buffer)) != -1) {
            fos.write(Arrays.copyOf(buffer,inByte));
            System.out.println("Downloading!");
        }

        is.close();
        fos.close();

        client.close();
        System.out.println("File Download Completed!!!");

    }


    public static void main(String[] args) {

        String token = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IlJITURUdG5wN3RlSkhBRk00M3JlV19uVDhuR3FaV0RNajhoMmFhWEJSTmMiLCJhbGciOiJSUzI1NiIsIng1dCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCIsImtpZCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9iNGVlODRlNS0yNTA3LTRlOGYtYTVjMS1mZTY2M2YzZDJmODAvIiwiaWF0IjoxNjM1OTUzMzg2LCJuYmYiOjE2MzU5NTMzODYsImV4cCI6MTYzNTk1ODkyNSwiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkUyWmdZRGpHSUJIemtFK3dKUC9KT1QwUC9namgvQVc2TDg0eXI1c3ZmK3RxM05WWnNROEEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6IlRlc3RfU2hhcmVQb2ludCIsImFwcGlkIjoiOWM5MTk4MGYtY2IxOS00YzE1LThlMmMtMjNkY2M5YTQ4NThhIiwiYXBwaWRhY3IiOiIwIiwiZmFtaWx5X25hbWUiOiJFbmVhIiwiZ2l2ZW5fbmFtZSI6Ik1hZGFsaW4iLCJpZHR5cCI6InVzZXIiLCJpcGFkZHIiOiIxODguMjQuOTguMTIzIiwibmFtZSI6Ik1hZGFsaW4gRW5lYSIsIm9pZCI6ImI3MmU0MmY1LTg2ZGMtNDM4Zi1iNDQwLTZmNWFiZTQ2ODBmZCIsInBsYXRmIjoiMTQiLCJwdWlkIjoiMTAwMzAwMDBBRUY1RDhFMiIsInB3ZF9leHAiOiI0NTQ0MTMiLCJwd2RfdXJsIjoiaHR0cHM6Ly9wb3J0YWwubWljcm9zb2Z0b25saW5lLmNvbS9DaGFuZ2VQYXNzd29yZC5hc3B4IiwicmgiOiIwLkFTOEE1WVR1dEFjbGowNmx3ZjVtUHowdmdBLVlrWndaeXhWTWppd2ozTW1raFlvdkFCay4iLCJzY3AiOiJBbGxTaXRlcy5SZWFkIEFsbFNpdGVzLldyaXRlIE15RmlsZXMuUmVhZCBNeUZpbGVzLldyaXRlIFVzZXIuUmVhZCBwcm9maWxlIG9wZW5pZCBlbWFpbCIsInN1YiI6IjVkSU5NdmMyRkgwU2xWQWNZbWkyY1JsTmVCWERfSlB1Rnp0c1h2aUZiRmMiLCJ0ZW5hbnRfcmVnaW9uX3Njb3BlIjoiRVUiLCJ0aWQiOiJiNGVlODRlNS0yNTA3LTRlOGYtYTVjMS1mZTY2M2YzZDJmODAiLCJ1bmlxdWVfbmFtZSI6ImRlYWRwb29sQGl0b21jb250ZW50Lm9ubWljcm9zb2Z0LmNvbSIsInVwbiI6ImRlYWRwb29sQGl0b21jb250ZW50Lm9ubWljcm9zb2Z0LmNvbSIsInV0aSI6InBFb1lPbTR1VGtDR0U3b21mRklSQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCIsImI3OWZiZjRkLTNlZjktNDY4OS04MTQzLTc2YjE5NGU4NTUwOSJdLCJ4bXNfc3QiOnsic3ViIjoiU0dyRG90bXVBWUlZQ2JkVnNJSE1ubVNjZU0tOTZyVEM1S0Y0bE5rWGFGayJ9LCJ4bXNfdGNkdCI6MTU0MTAwMDI4OH0.b7tu9uiKoehyQI6l8HynLnN3FiLWRKEu5eN5jJ-aVvUQBPhxhwQDl_t6H-RTW-TGuf3-laxa4o8Z8d7EEYiB61uPrCP275Rw3o8rvMCmuOXj-2fMEpU8OGyo7waWBslSoqtFxLq0zNbHFfC0uRYeA_7FaH8Nn6mWHzn7PMIriM94uip6dULAKyc2xGHHBBB9BAtY9YYUZsAvOItPeQDDd22UqAnvgm6wnamGmsSSn2yB-dik_RB6bKnwwKAe6geoIrTYs_DDr4RNkhQaWSI402Amv4NZiAO4UoEa6QOikXmtABwj0mTF7BeefHCFygPpf5HuZOnXFV-dwM3SKJ4XtA";
        String id = "01GLVAXZJSSP4TVWBKPZG3H27J3BLL3KKL";

        try {
            downloadFile2("https://graph.microsoft.com/v1.0/sites/root/drive/items/" + id + "/content","C:\\Users\\ABenta\\Desktop\\downloadedd\\myFile.mp4", token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*HttpClientAction httpClient = new HttpClientAction();
        Map<String, String> responseFileDetails = httpClient.execute("https://graph.microsoft.com/v1.0/sites/root/drive/items/" + id + "/content",
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

        //Gson gson = new Gson();
        //JsonObject myFileName = gson.fromJson(responseFileDetails.get("returnResult"), JsonObject.class);*/

        //System.out.println(responseFileDetails.get("returnResult"));


    }
}
