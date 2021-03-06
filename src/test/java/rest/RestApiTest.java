package rest;

import com.rest.RestAssuredClient;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;


public class RestApiTest {

    RestAssuredClient client;

    Map<String, String> headers = new HashMap<String, String>() {{
        put("Accept", "application/json");
    }};

    @BeforeMethod
    public void beforeMethod() {
        client = new RestAssuredClient();
        client.setBaseUrl("https://reqres.in");
        client.addHeaders(headers);
    }

    @Test
    public void invokeGetServiceTest() {
        client.addParam("pages", "2");
        client.doRequest("/api/users");
        Reporter.log("response : " + client.getResponseAsString(), true);
    }

    @Test
    public void checkSchema() {
        client.addParam("pages", "2");
        client.doRequest("/api/users");
        client.getResponse().then().assertThat().body(matchesJsonSchema(new File("src/test/resources/schema/schema.json")));
    }

    @Test
    public void checkPost() {
        String request = "{ \"name\":\"vijay\",\"job\":\"space-man\"}";
        client.doRequest("/api/users","POST",request);
        client.getResponse().then().statusCode(equalTo(201)).body("id",notNullValue());
    }


}
