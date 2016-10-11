package io.cloudslang.content.azure.services;

import io.cloudslang.content.azure.utils.DateUtilities;
import org.junit.Test;

import java.util.Date;

import static io.cloudslang.content.azure.services.AuthorizationTokenImpl.getToken;
import static org.junit.Assert.assertEquals;

/**
 * Created by victor on 29.09.2016.
 */
public class AuthorizationTokenImplTest {
    @Test
    public void getTokenTest() throws Exception {
        final Date date = DateUtilities.parseDate("08/04/2014 10:03 PM") ;
//        assertEquals(getToken("53dd860e1b72ff0467030003", "pXeTVcmdbU9XxH6fPcPlq8Y9D9G3Cdo5Eh2nMSgKj/DWqeSFFXDdmpz5Trv+L2hQNM+nGa704Rf8Z22W9O1jdQ==", date), "SharedAccessSignature uid=53dd860e1b72ff0467030003&ex=2014-08-04T22:03:00.0000000Z&sn=FWBrF2PBYVEZgpoyaPWNKIBIGLQxbJAw6A3Lu4EB78aooaZldHXZTZBkqyMWlLdrpK5/q3BFDgj9vqX1vrR0ww==");
//        assertEquals(getToken("123", "321", date), "SharedAccessSignature uid=123&ex=2014-08-04T22:03:00.0000000Z&sn=q6hTggdd9OCV+1sdVPDl88VzYyN9XZWegp/s3ApX1szHOI5eDuMkoqC43v25uL8xvNPfU0VtzrJQj/balAlxvw==");
    }

}