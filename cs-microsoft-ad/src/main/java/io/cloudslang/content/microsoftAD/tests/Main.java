package io.cloudslang.content.microsoftAD.tests;

import io.cloudslang.content.microsoftAD.actions.userManagement.CreateUser;
import io.cloudslang.content.microsoftAD.actions.utils.GetAuthorizationToken;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Map<String , String> authTokenResults = (new GetAuthorizationToken()).execute(
                "API",
                "c49005ae-7d31-4135-b32d-23081ebe6011",
                "OvyJ5u/s4BfpGw8rpvnqbJNxXOT/k2lGjD6GxID/cX0=",
                "",
                "",
                "https://login.microsoftonline.com/itomcontent.onmicrosoft.com/oauth2/v2.0/token",
                "https://graph.microsoft.com",
                "",
                "",
                "",
                "");

        CreateUser createUser = new CreateUser();
        Map<String , String> results = createUser.execute(authTokenResults.get("authToken"), "true", "Savu", "true", "Savu", "false", "false", "parola123!", "savu@itomcontent.onmicrosoft.com");
        System.out.println(results.toString());
    }
}
