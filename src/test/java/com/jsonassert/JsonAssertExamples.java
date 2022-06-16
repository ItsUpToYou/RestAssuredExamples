package com.jsonassert;

import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;


public class JsonAssertExamples {

    static String jsonResponse;

    @BeforeAll
    public static void init(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8085;
        jsonResponse = given().when().get("/student/list").asString();
    }

    @DisplayName("Get the root element")
    @Test
    public void getRoot() {
       List<String> s =  JsonPath.read(jsonResponse, "$");
        System.out.println(s);
    }


    @DisplayName("Get the all data with emails")
    @Test
    public void getAllDataElements() throws JSONException {
        List<String> dataElements= JsonPath.read(jsonResponse, "$.[*].email");
        dataElements.stream().forEach(System.out::println);
    }

    @DisplayName("Get lastDataElement")
    @Test
    public void getLastDataElement(){
        String lastDataElement = JsonPath.read(jsonResponse, "$.[-1].firstName" );
        System.out.println("The last name in the list is:" + lastDataElement);
    }

    @DisplayName("Get All the courses")
    @Test
    public void getAllIds(){
        List<String> listIds = JsonPath.read(jsonResponse, "$..[*].courses[*]");
        listIds.stream().forEach(System.out::println);
    }

    @DisplayName("Get the first name of the person with programme Law")
    @Test
    public void getFirstNamesWithCourseLaw(){
        List<String> names = JsonPath.read(jsonResponse, "$.[*].[?(@.programme == \"Law\")].firstName");
        names.stream().forEach(System.out::println);
    }

    @Test
    public void hardAsserts(){
                given().
                when().
                    get("/student/list").
                then().
                    body("[0].firstName", equalTo("Vernon")).
                    body("[0].lastName", equalTo("Harper")).
                    body("[0].programme", equalTo("Financial Analysis")).
                log().all().statusCode(200);;
    }

    @Test
    public void softAsserts(){
            given().
            when().
                get("/student/list").
            then().
                body("[1].firstName", equalTo("Murphy"),
                "[0].lastName", equalTo("Harper"),
                        "[2].programme", equalTo("Computer Science")).
                log().all().statusCode(200);
    }




    @Test
    public void getStudentsCompareModeLenient() throws IOException, JSONException {
        String expectedValue = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/target/file.txt")));
        String actualValue = jsonResponse;

        JSONAssert.assertEquals(actualValue,expectedValue, JSONCompareMode.LENIENT);
    }


    @Test
    public void getStudentsCompareModeStrict() throws IOException, JSONException {
        String expectedValue = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/target/change.txt")));
        String actualValue = jsonResponse;

        JSONAssert.assertEquals(actualValue,expectedValue, JSONCompareMode.STRICT);
    }

    @Test
    public void getResponseTime() {
        long responseTime =
                given().
                when().
                    get("/student/list").
                    timeIn(TimeUnit.MILLISECONDS);

        System.out.println("Response time is: " +responseTime + "ms");


        given().
        when().
            proxy(5555).
            get("/student/list").
        then().
            time(lessThan(5L), TimeUnit.SECONDS);
    }
}
