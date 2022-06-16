package paypaltest.base;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;


import static io.restassured.RestAssured.*;

public class BaseSetupPayPal {

    public static String accessToken;
    public static final String clientId = "AaFOIQWqA3Km76iBKifTNRwCCKDQkvIFqo5pB1CnqdE2GyA9zS_Ejj53PyRR_FBcNlgniJKwYmiqO25d";
    public static final String clientSecret = "EPqYEIAmtepVmoQwwjahEZO17fwVtGPTLAkkwnvvBaeKwVQwtLgAWQ3D23cxsrCpWlPJudb3l3GqC4CV";

    @BeforeAll
    public static void init()
    {
        RestAssured.baseURI="https://api-m.sandbox.paypal.com";
        RestAssured.basePath="/v1";

  accessToken =  given()
                .params("grant_type", "client_credentials")
                .auth()
                .preemptive()
                .basic(clientId,clientSecret).
            when()
                .post("/oauth2/token").
            then()
                .extract()
                .path("access_token");

        System.out.println("The token is: " + accessToken);
    }


}
