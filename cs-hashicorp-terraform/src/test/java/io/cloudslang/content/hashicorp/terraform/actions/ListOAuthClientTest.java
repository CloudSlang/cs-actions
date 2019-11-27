package io.cloudslang.content.hashicorp.terraform.actions;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class ListOAuthClientTest extends MockitoJUnit {

   @Mock
   private ListOAuthClient clientTest;
    @Before
    public void setup(){
        MockitoAnnotations.initMocks("ListOAuthClientTest");
        clientTest=new ListOAuthClient();
    }


    @Test
    public void listOAuthClientTest()throws ClientProtocolException, IOException {
      Map<String,String> map= clientTest.execute("","","","","","","","","","","","","","","","");
        String statusCode=map.get("statusCode");
        String oauthTokenId=map.get("oauthTokenId");
        System.out.println(statusCode);
        System.out.println(oauthTokenId);
      assertEquals("200",statusCode);
    }
}
