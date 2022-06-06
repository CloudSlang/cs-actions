package io.cloudslang.content.google.javasdk.actions.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Lists;
import com.google.api.services.compute.Compute;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;


//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Map;
//
//import io.cloudslang.content.httpclient.entities.HttpClientInputs;
//import io.cloudslang.content.httpclient.services.HttpClientService;
//
public class GoogleComputeImpl {
//    @NotNull
//    public static Map<String, String> listinstance(@NotNull final GoogleCreateVMInputs googleCreateVMInputs)
//            throws Exception {
//        final HttpClientInputs httpClientInputs = new HttpClientInputs();
//        httpClientInputs.setUrl("https://compute.googleapis.com/compute/v1/projects/itom-gcpcontent-nonprod/zones/instances");
//        httpClientInputs.setMethod("GET");
//        return new HttpClientService().execute(httpClientInputs);
//
//    }

    public static Compute createComputeService(String token, String proxyHost, int proxyPort) throws IOException, GeneralSecurityException {

        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();


        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

        HttpTransport httpTransport1 = new NetHttpTransport.Builder().setProxy(proxy).build();


        ArrayList<String> arrayList = Lists.newArrayList();
        arrayList.add("https://www.googleapis.com/auth/cloud-platform");
        InputStream stream = new ByteArrayInputStream(token.getBytes(StandardCharsets.UTF_8));
        GoogleCredential credentials = GoogleCredential.fromStream(stream).createScoped(arrayList);

        return new Compute.Builder(httpTransport1, jsonFactory, credentials)
                .setApplicationName("Google-ComputeSample/0.1")
                .build();
    }
}
