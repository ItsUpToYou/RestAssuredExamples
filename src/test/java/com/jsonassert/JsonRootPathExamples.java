package com.jsonassert;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.hamcrest.Matchers.*;


public class JsonRootPathExamples {
    public static RequestSpecification fbReqSpec;
    public static ResponseSpecification fbRespSpec;

    @BeforeAll
    public static void setup(){
        fbReqSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.football-data.org/")
                .setBasePath("/v4/")
                .addHeader("X-Auth-Token", "244f689001e64e93a0d1a84f34b50fb6")
                .addHeader("X-Response-Control", "minified")
                .build();

        fbRespSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        RestAssured.responseSpecification = fbRespSpec;
        RestAssured.requestSpecification = fbReqSpec;
    }

    @AfterAll
    public static void tearDown() {
        RestAssured.rootPath = "";
    }

    @Test
    public void rootPath(){
    RestAssured.
        given().
        when().
            get("competitions/PL/standings/").
        then().
            rootPath("standings.table.team[0]").
            body("standings.table.team[0].shortName",hasItem("Man City"),
                    "name",hasItem("Manchester City FC"),
                    "name",hasItem("Manchester City FC")).

            rootPath("standings.table[0]").
            body("playedGames", hasItem(38)).
            log().all().statusCode(200);
    }

}
