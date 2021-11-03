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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TestUpload {

    public static HttpResponse uploadFile(String endpoint, String path, Map<String, String> headers) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(endpoint);
        for (String headerType : headers.keySet()) {
            httpPut.setHeader(headerType, headers.get(headerType));
        }

        httpPut.setHeader("Content-Type",  ContentType.MULTIPART_FORM_DATA.toString());
        if (null != path) {
            byte[] allBytes = Files.readAllBytes(Paths.get(path));

            HttpEntity httpEntity = new ByteArrayEntity(allBytes, ContentType.MULTIPART_FORM_DATA);
            httpPut.setEntity(httpEntity);
        }
        HttpResponse httpResponse = httpClient.execute(httpPut);
        return httpResponse;
    }

    public static void main(String[] args) {


        String token = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IkNDMnNrX0N2V08wZ1o0Z1dJdlc3OUhwaC1yeVpabC1mVXN5S0FmMEw1Z2ciLCJhbGciOiJSUzI1NiIsIng1dCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCIsImtpZCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9iNGVlODRlNS0yNTA3LTRlOGYtYTVjMS1mZTY2M2YzZDJmODAvIiwiaWF0IjoxNjM1Nzc5NDE0LCJuYmYiOjE2MzU3Nzk0MTQsImV4cCI6MTYzNTc4NDQ2MywiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkUyWmdZUEI4c05ORkt6amg5eWRMWmFkdVEzN2hocXZKNzVOMkw3Z2tITnpPczdMdTAySUEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6IlRlc3RfU2hhcmVQb2ludCIsImFwcGlkIjoiOWM5MTk4MGYtY2IxOS00YzE1LThlMmMtMjNkY2M5YTQ4NThhIiwiYXBwaWRhY3IiOiIwIiwiZmFtaWx5X25hbWUiOiJFbmVhIiwiZ2l2ZW5fbmFtZSI6Ik1hZGFsaW4iLCJpZHR5cCI6InVzZXIiLCJpcGFkZHIiOiIxODguMjQuOTguMTIzIiwibmFtZSI6Ik1hZGFsaW4gRW5lYSIsIm9pZCI6ImI3MmU0MmY1LTg2ZGMtNDM4Zi1iNDQwLTZmNWFiZTQ2ODBmZCIsInBsYXRmIjoiMTQiLCJwdWlkIjoiMTAwMzAwMDBBRUY1RDhFMiIsInB3ZF9leHAiOiI2MjgzODUiLCJwd2RfdXJsIjoiaHR0cHM6Ly9wb3J0YWwubWljcm9zb2Z0b25saW5lLmNvbS9DaGFuZ2VQYXNzd29yZC5hc3B4IiwicmgiOiIwLkFTOEE1WVR1dEFjbGowNmx3ZjVtUHowdmdBLVlrWndaeXhWTWppd2ozTW1raFlvdkFCay4iLCJzY3AiOiJBbGxTaXRlcy5SZWFkIEFsbFNpdGVzLldyaXRlIE15RmlsZXMuUmVhZCBNeUZpbGVzLldyaXRlIFVzZXIuUmVhZCBwcm9maWxlIG9wZW5pZCBlbWFpbCIsInN1YiI6IjVkSU5NdmMyRkgwU2xWQWNZbWkyY1JsTmVCWERfSlB1Rnp0c1h2aUZiRmMiLCJ0ZW5hbnRfcmVnaW9uX3Njb3BlIjoiRVUiLCJ0aWQiOiJiNGVlODRlNS0yNTA3LTRlOGYtYTVjMS1mZTY2M2YzZDJmODAiLCJ1bmlxdWVfbmFtZSI6ImRlYWRwb29sQGl0b21jb250ZW50Lm9ubWljcm9zb2Z0LmNvbSIsInVwbiI6ImRlYWRwb29sQGl0b21jb250ZW50Lm9ubWljcm9zb2Z0LmNvbSIsInV0aSI6IlQxdlZINTZkcmtPWVNlTzlfRXg1QUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCIsImI3OWZiZjRkLTNlZjktNDY4OS04MTQzLTc2YjE5NGU4NTUwOSJdLCJ4bXNfc3QiOnsic3ViIjoiU0dyRG90bXVBWUlZQ2JkVnNJSE1ubVNjZU0tOTZyVEM1S0Y0bE5rWGFGayJ9LCJ4bXNfdGNkdCI6MTU0MTAwMDI4OH0.lk_XD24n05PhiJpiHptpO6i_Dw9LUuV__sF_u_PnPdsqr21KCT9Ym6bPVsMqQeu2MRpLMFizmrp1pa2k3tKxbiVY6h6f-7-RtoJi9wRyean2gWLWRIthgWOlvzqS3zXNbx4-DaIw7KqLeeoP3kKL8iVb608YkTe36nge9i0wnEhxL9y8G4ysWkYT8Mn0RgbiZ1_8gkm-3eXoInqjpLtqArcUDolpr4CBHWTAmSfnPmneYbMdaYG11CNy360ERKPijbIa4KpWTNnSL1KAcWG1CDERJYCPXg9cGL-UEPeHuQxGK_hEPBdU9Ig0PeHei9uYNlYjgvjYCDUXnoma72ETIg";
        String id = "01GLVAXZJT2W633FFCIFBJZ6T6MHPXQT2D";
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
                        "  \"name\": \"myFile.mp4\",\n" +
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
                Map<String, String> headers = new HashMap();
                String uploadedFilePath = "C:\\Users\\ABenta\\Desktop\\downloadedd\\1975.mp4";
               // headers.put("Content-Length", String.valueOf(Files.size(Paths.get(uploadedFilePath)) ));
                headers.put("Content-Range","bytes " + 0 + "-"  + (Files.size(Paths.get(uploadedFilePath))-1) + "/" +  Files.size(Paths.get(uploadedFilePath)) );

                HttpResponse uploadedsuccessful = uploadFile(uploadUrl,uploadedFilePath , headers );
                System.out.println(uploadedsuccessful.getHeaders("statusCode"));
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }






    }
}
