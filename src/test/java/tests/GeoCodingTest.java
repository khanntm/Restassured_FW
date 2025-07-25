package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.ConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Location;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


import static org.testng.Assert.*;

public class GeoCodingTest extends BaseTest {

    @Test
    //Call function sendGetWithApiKey() from BaseTest
    public void testSearchCityByName() {
        test = extent.createTest("GeoCodingAPI - Search City By Name");

        List<String> cities = ConfigManager.getCityList("src/test/resources/cities.csv");

        for (String city : cities) {
            //test.info("Testing city: " + city);
            Map<String, String> params = new HashMap<>();
            params.put("q", city);
            params.put("limit", "1");

            //Response response = sendGetWithApiKey("/geo/1.0/direct", params);
            Response response = sendRequestWithApiKey("GET", "/geo/1.0/direct", params, null);

            assertEquals(response.statusCode(), 200, "Status code should be 200");
            test.pass("Status code is 200 for city: " + city);

            double lat = response.jsonPath().getDouble("[0].lat");
            double lon = response.jsonPath().getDouble("[0].lon");

            //test.info("Latitude: " + lat);
            //test.info("Longitude: " + lon);

            assertTrue(lat != 0, "Latitude should not be 0");
            assertTrue(lon != 0, "Longitude should not be 0");
            test.pass("Latitude and Longitude are valid for city: " + city);

            /* Use jsonPath():
            String actualCityName = response.jsonPath().getString("[0].name");
            assertEquals(actualCityName.toLowerCase(), city.toLowerCase(), "City name does not match");
            //test.pass("City name matches expected value: " + city); */

            Location[] locations = response.as(Location[].class);  // convert JSON to array of POJO
            Location location = locations[0];

            System.out.println("City: " + location.name);
            System.out.println("Latitude: " + location.lat);
            System.out.println("In Japanese: " + location.local_names.get("ja"));


            test.pass("Verified using POJO: name, lat, lon for city: " + city);

            //Verify schema match with expected
            response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/geocoding-schema.json"));
            test.pass("Response matches expected JSON schema");

            String fullUrl = buildFullUrl("/geo/1.0/direct", params);
            test.info("**Full Request URL:**\n" + fullUrl);

        }
    }

    @Test
    public void testSearchByZipcode() {
        test = extent.createTest("GeoCodingAPI - Search City by Zipcode: E14,GB");

        Map<String, String> params = new HashMap<>();
        params.put("zip", "E14,GB");
        params.put("limit","1");

        //Send request API GET method
        Response response = sendRequestWithApiKey("GET", "/geo/1.0/zip", params, null);

        //Verify status Code
        assertEquals(response.statusCode(), 200, "Status code should be 200");
        test.pass("Status code is 200");

        // Use POJO for convert response
        Location location = response.as(Location.class);  // convert JSON to array of POJO

        System.out.println("City: " + location.name);
        System.out.println("Latitude: " + location.lat);
        System.out.println("Lon: " + location.lon);
        System.out.println("Country: " + location.country);

        //Assertions and Extent report
        assertTrue(location.lat != 0, "Latitude should not be 0");
        assertTrue(location.lon != 0, "Longitude should not be 0");
        test.pass("Lat and lon are valid");

        String expectedZipCountry = "E14,GB";
        String actualZipCountry = location.zip + "," + location.country;
        assertEquals(actualZipCountry, expectedZipCountry, "Zip and country are match");
        test.pass("Zip and country match expected value: " + expectedZipCountry);

        test.pass("Verified using POJO name, lat, lon for city: " + location.name);

        //Verify schema
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/geocoding-city.json"));
        test.pass("Response matches expected JSON schema");

        String fullUrl = buildFullUrl("/geo/1.0/zip", params);
        test.info("**Full Request URL:**\n" + fullUrl);

    }

    @Test
    public void testSearchCityWithoutApiKey() {
        test = extent.createTest("GeoCodingAPI - Search City Without API Key");

        Map<String, String> params = new HashMap<>();
        params.put("q", "London");
        params.put("limit", "1");

        // Send request without API Key
        Response response = io.restassured.RestAssured
                .given()
                .queryParams(params)
                .when()
                .get("/geo/1.0/direct")
                .then()
                .extract().response();

        //test.info("Sent request to /geo/1.0/direct without API key");

        int statusCode = response.statusCode();
        //test.info("Received status code: " + statusCode);

        // Check response status as 401
        assertTrue(statusCode == 401, "Expected 401 when API key is missing");
        test.pass("API returned expected error status when API key is missing");

        String fullUrl = buildFullUrl("/geo/1.0/direct", params);
        test.info("**Full Request URL:**\n" + fullUrl);
    }


}
