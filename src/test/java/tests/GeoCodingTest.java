package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.ConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class GeoCodingTest extends BaseTest {

    @Test
    //Call function sendGetWithApiKey() from BaseTest
    public void TC_01_testSearchCityByName() {
        test = extent.createTest("TC_01 - GeoCodingAPI - Search City By Name");

        List<String> cities = ConfigManager.getCityList("src/test/resources/cities.csv");

        for (String city : cities) {
            test.info("Testing city: " + city);


            Map<String, String> params = new HashMap<>();
            params.put("q", city);
            params.put("limit", "1");

            Response response = sendGetWithApiKey("/geo/1.0/direct", params);

            assertEquals(response.statusCode(), 200, "Status code should be 200");
            test.pass("Status code is 200 for city: " + city);

            double lat = response.jsonPath().getDouble("[0].lat");
            double lon = response.jsonPath().getDouble("[0].lon");

            test.info("Latitude: " + lat);
            test.info("Longitude: " + lon);

            assertTrue(lat != 0, "Latitude should not be 0");
            assertTrue(lon != 0, "Longitude should not be 0");
            test.pass("Latitude and Longitude are valid for city: " + city);

            String actualCityName = response.jsonPath().getString("[0].name");
            assertEquals(actualCityName.toLowerCase(), city.toLowerCase(), "City name does not match");
            test.pass("City name matches expected value: " + city);

        }
    }

    @Test
    public void TC_02_testSearchByZipcode() {
        test = extent.createTest("TC_02 - GeoCodingAPI - Search City by Zipcode: E14,GB");

        Map<String, String> params = new HashMap<>();
        params.put("zip", "E14,GB");
        params.put("limit","1");

        Response response = sendGetWithApiKey("/geo/1.0/zip",params);

        test.info("API called with zip = E14,GB");

        assertEquals(response.statusCode(), 200, "Status code should be 200");
        test.pass("Status code is 200");

        double lat = response.jsonPath().getDouble("lat");
        double lon = response.jsonPath().getDouble("lon");

        test.info("Latitude: " + lat);
        test.info("Longitude: " + lon);

        assertTrue(lat != 0, "Latitude should not be 0");
        assertTrue(lon != 0, "Longitude should not be 0");
        test.pass("Latitude and Longitude are valid");

        String expectedZipCountry = "E14,GB";
        String actualZipCountry = response.jsonPath().getString("zip") + "," + response.jsonPath().getString("country");

        assertEquals(actualZipCountry, expectedZipCountry, "Zip and country do not match");
        test.pass("Zip and country match expected value: " + expectedZipCountry);
    }

    @Test
    public void TC_03_testSearchCityWithoutApiKey() {
        test = extent.createTest("TC_03 - GeoCodingAPI - Search City Without API Key");

        Map<String, String> params = new HashMap<>();
        params.put("q", "London");
        params.put("limit", "1");

        // Gửi request mà không thêm API key
        Response response = io.restassured.RestAssured
                .given()
                .queryParams(params)
                .when()
                .get("/geo/1.0/direct")
                .then()
                .extract().response();

        test.info("Sent request to /geo/1.0/direct without API key");

        int statusCode = response.statusCode();
        test.info("Received status code: " + statusCode);

        // Kiểm tra mã lỗi (tùy vào API, có thể là 401 hoặc 400)
        assertTrue(statusCode == 401 || statusCode == 400, "Expected 401 or 400 when API key is missing");
        test.pass("API returned expected error status when API key is missing");
    }


}
