package io.cloudslang.content.utilities.actions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.cloudslang.content.httpclient.HttpClientAction;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static io.cloudslang.content.microsoftAD.utils.Constants.UTF8;

public class TestUploadBig {

    public static HttpResponse uploadFile(String endpoint, String path, Map<String, String> headers, byte[] allBytes) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(endpoint);
        for (String headerType : headers.keySet()) {
            httpPut.setHeader(headerType, headers.get(headerType));
        }

        httpPut.setHeader("Content-Type",  ContentType.MULTIPART_FORM_DATA.toString());
        HttpEntity httpEntity = new ByteArrayEntity(allBytes, ContentType.MULTIPART_FORM_DATA);
        httpPut.setEntity(httpEntity);
        HttpResponse httpResponse = httpClient.execute(httpPut);
        return httpResponse;
    }


    public static void uploadChunks(String uploadedFilePath, String uploadUrl) throws IOException {

        Map<String, String> headers;
        final int BUFFER_SIZE = 192 * 320 * 1024; // 320*192KB
        Path pp = FileSystems.getDefault().getPath(uploadedFilePath);
        FileInputStream fis = new FileInputStream(pp.toFile());
        byte[] buffer = new byte[BUFFER_SIZE];
        int read = 0;
        int i=0;
        long fileSize = Files.size(Paths.get(uploadedFilePath));
        while( ( read = fis.read( buffer ) ) > 0 ){

            headers = new HashMap();
            headers.put("Content-Range","bytes " + i + "-"  + (i+read-1) + "/" +  fileSize );
            i += read;


            HttpResponse uploadedsuccessful = uploadFile(uploadUrl,uploadedFilePath , headers, Arrays.copyOf(buffer,read));
            System.out.println(EntityUtils.toString(uploadedsuccessful.getEntity(), UTF8));
            System.out.println(headers.get("Content-Range"));
            System.out.println(uploadedsuccessful.getStatusLine().getStatusCode());

        }

        fis.close();
    }

    public static void main(String[] args) {


        String token = "eyJ0eXAiOiJKV1QiLCJub25jZSI6InA0WXlxd3ZQOGFxQjF6WTFpdWdWd19vMjRxVkJJcmdFYWZ0ZVJrdWFWUmMiLCJhbGciOiJSUzI1NiIsIng1dCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCIsImtpZCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9iNGVlODRlNS0yNTA3LTRlOGYtYTVjMS1mZTY2M2YzZDJmODAvIiwiaWF0IjoxNjM1ODYwNzc0LCJuYmYiOjE2MzU4NjA3NzQsImV4cCI6MTYzNTg2NDkxNywiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkUyWmdZSkJmdFZxcGkvMk5ZMjl6bHQwbXB0bUdtd01UeEtzOEx0eldPYW5nV255ZzdROEEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6IlRlc3RfU2hhcmVQb2ludCIsImFwcGlkIjoiOWM5MTk4MGYtY2IxOS00YzE1LThlMmMtMjNkY2M5YTQ4NThhIiwiYXBwaWRhY3IiOiIwIiwiZmFtaWx5X25hbWUiOiJFbmVhIiwiZ2l2ZW5fbmFtZSI6Ik1hZGFsaW4iLCJpZHR5cCI6InVzZXIiLCJpcGFkZHIiOiIxODguMjQuOTguMTIzIiwibmFtZSI6Ik1hZGFsaW4gRW5lYSIsIm9pZCI6ImI3MmU0MmY1LTg2ZGMtNDM4Zi1iNDQwLTZmNWFiZTQ2ODBmZCIsInBsYXRmIjoiMTQiLCJwdWlkIjoiMTAwMzAwMDBBRUY1RDhFMiIsInB3ZF9leHAiOiI1NDcwMjUiLCJwd2RfdXJsIjoiaHR0cHM6Ly9wb3J0YWwubWljcm9zb2Z0b25saW5lLmNvbS9DaGFuZ2VQYXNzd29yZC5hc3B4IiwicmgiOiIwLkFTOEE1WVR1dEFjbGowNmx3ZjVtUHowdmdBLVlrWndaeXhWTWppd2ozTW1raFlvdkFCay4iLCJzY3AiOiJBbGxTaXRlcy5SZWFkIEFsbFNpdGVzLldyaXRlIE15RmlsZXMuUmVhZCBNeUZpbGVzLldyaXRlIFVzZXIuUmVhZCBwcm9maWxlIG9wZW5pZCBlbWFpbCIsInN1YiI6IjVkSU5NdmMyRkgwU2xWQWNZbWkyY1JsTmVCWERfSlB1Rnp0c1h2aUZiRmMiLCJ0ZW5hbnRfcmVnaW9uX3Njb3BlIjoiRVUiLCJ0aWQiOiJiNGVlODRlNS0yNTA3LTRlOGYtYTVjMS1mZTY2M2YzZDJmODAiLCJ1bmlxdWVfbmFtZSI6ImRlYWRwb29sQGl0b21jb250ZW50Lm9ubWljcm9zb2Z0LmNvbSIsInVwbiI6ImRlYWRwb29sQGl0b21jb250ZW50Lm9ubWljcm9zb2Z0LmNvbSIsInV0aSI6InN2Vm1OUkxlREVtYlFqWFFneXFzQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCIsImI3OWZiZjRkLTNlZjktNDY4OS04MTQzLTc2YjE5NGU4NTUwOSJdLCJ4bXNfc3QiOnsic3ViIjoiU0dyRG90bXVBWUlZQ2JkVnNJSE1ubVNjZU0tOTZyVEM1S0Y0bE5rWGFGayJ9LCJ4bXNfdGNkdCI6MTU0MTAwMDI4OH0.e4Sgo8d1__9RLKJo6oNhgCIAiV7gYClfZiffWt5t5Z5WDgVUcH2mppwGonBqF9zfpaMluIbw8Q8x_r1g4gNL6E3V2SciHvvSxEZqRI6p-7ba21nq-Di4MbNsPSPyV0D2Weo4l0eU2FjTPJEs36ih-xH_zrmp6-UO7yT-jNsc9Bq3QvttWBaFDBWiH0CAvLsJn-OxP5N0nQg9TxGQy6L3Y9atGq2-D22IsNuhKpUpQXNMDYv32lqYf04zGsC0oYUq1zILnyT1ZR82Wq7XrxI9b6e5WWMITldvWu0jSM3wjW0jbmRnw4Kie2VkopsPFh4kY9eSvy94K_7IExS0bUj-xw";
        String id = "01GLVAXZJT2W633FFCIFBJZ6T6MHPXQT2D";
        String uploadedFilePath = "C:\\Users\\ABenta\\Desktop\\downloadedd\\a.7z";
        HttpClientAction httpClient = new HttpClientAction();
        Map<String,String> createResponse = httpClient.execute("https://graph.microsoft.com/v1.0/sites/root/drive/items/root/children",
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
                "{\n" +
                        "  \"name\": \"a.7z\",\n" +
                        "  \"file\": { },\n" +
                        "  \"@microsoft.graph.conflictBehavior\": \"rename\"\n" +
                        "}",
                "application/json",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "POST",
                null,
                null

        );

        Gson gson = new Gson();
        JsonObject createdFileJson = gson.fromJson(createResponse.get("returnResult"), JsonObject.class);

        String idUpload = createdFileJson.get("id").toString().substring(1,createdFileJson.get("id").toString().length()-1);


        Map<String,String> session = httpClient.execute("https://graph.microsoft.com/v1.0/sites/root/drive/items/" + idUpload +"/createUploadSession",
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
                "{\n" +
                        "  \"@microsoft.graph.conflictBehavior\": \"fail (default) | replace | rename\",\n" +
                        "  \"description\": \"description\",\n" +
                        "  \"name\": \"myFile.mp4\"\n" +
                        "}",
                "application/json",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "POST",
                null,
                null

        );

        if( Integer.parseInt(session.get("statusCode")) == 200)
        {
            JsonObject createSesssionResponse = gson.fromJson(session.get("returnResult"), JsonObject.class);
            String uploadUrl = createSesssionResponse.get("uploadUrl").toString().substring(1,createSesssionResponse.get("uploadUrl").toString().length()-1);

            try {
                uploadChunks(uploadedFilePath,uploadUrl);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }





    }
}
