package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.AirQualityEvaluator;
import utils.ConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class AirPollutionTest extends BaseTest {

    private final double DEFAULT_LAT = 10.762622; // Ho Chi Minh City
    private final double DEFAULT_LON = 106.660172;

    @Test
    public void testGetAirPollutionByCity() {
        test = extent.createTest("AirPollutionAPI - Air Pollution by City");
        List<String> cities = ConfigManager.getCityList("src/test/resources/cities.csv");

        for (String city : cities) {
            //test.info("Testing air pollution for city: " + city);

            Map<String, String> geoParams = new HashMap<>();
            geoParams.put("q", city);
            geoParams.put("limit", "1");

            //Response geoResponse = sendGetWithApiKey("/geo/1.0/direct", geoParams);
            Response geoResponse = sendRequestWithApiKey("GET", "/geo/1.0/direct", geoParams, null);
            double lat = DEFAULT_LAT;
            double lon = DEFAULT_LON;

            if (geoResponse.statusCode() == 200 && geoResponse.jsonPath().getList("$").size() > 0) {
                lat = geoResponse.jsonPath().getDouble("[0].lat");
                lon = geoResponse.jsonPath().getDouble("[0].lon");
                //test.info("Retrieved lat/lon: " + lat + ", " + lon);
            } else {
                test.warning("Could not retrieve lat/lon for city: " + city + ". Using default values.");
            }

            Map<String, String> pollutionParams = new HashMap<>();
            pollutionParams.put("lat", String.valueOf(lat));
            pollutionParams.put("lon", String.valueOf(lon));

            //Response pollutionResponse = sendGetWithApiKey("/data/2.5/air_pollution", pollutionParams);
            Response pollutionResponse = sendRequestWithApiKey("GET", "/data/2.5/air_pollution", pollutionParams, null);

            assertEquals(pollutionResponse.statusCode(), 200, "Status code should be 200");
            test.pass("Air pollution data retrieved successfully for city: " + city);

            double aqi = pollutionResponse.jsonPath().getDouble("list[0].main.aqi");
            //test.info("Air Quality Index (AQI) for " + city + ": " + aqi);

            assertTrue(aqi >= 1 && aqi <= 5, "AQI should be between 1 and 5");

            //verify lon & lat in response
            double actualLat = pollutionResponse.jsonPath().getDouble("coord.lat");
            double actualLon = pollutionResponse.jsonPath().getDouble("coord.lon");

            int expectedLatInt = (int) lat;
            int expectedLonInt = (int) lon;

            int actualLatInt = (int) pollutionResponse.jsonPath().getDouble("coord.lat");
            int actualLonInt = (int) pollutionResponse.jsonPath().getDouble("coord.lon");

            assertEquals(actualLatInt, expectedLatInt, "Lat does not match with expected");
            test.pass("Lon matches expected value: " + lat);

            assertEquals(actualLonInt, expectedLonInt, "Lon does not match with expected");
            test.pass("Lon matches expected value: " + lon);

            String fullUrl = buildFullUrl("/geo/1.0/direct", geoParams);
            test.info("**Full Request URL:**\n" + fullUrl);

        }
    }

    public void checkAirQualityEvaluatorLogic() {
        test = extent.createTest("Test AirQualityEvaluator Logic");

        // Case 1: All values in "Good" range
        String result1 = AirQualityEvaluator.evaluateAirQuality(10, 20, 10, 5, 30, 1000);
        assertEquals(result1, "Good", "Expected 'Good' quality");
        test.pass("Case 1 passed: Good");

        // Case 2: One value in "Fair" range
        String result2 = AirQualityEvaluator.evaluateAirQuality(25, 20, 10, 5, 30, 1000);
        assertEquals(result2, "Fair", "Expected 'Fair' quality");
        test.pass("Case 2 passed: Fair");

        // Case 3: One value in "Moderate" range
        String result3 = AirQualityEvaluator.evaluateAirQuality(90, 20, 10, 5, 30, 1000);
        assertEquals(result3, "Moderate", "Expected 'Moderate' quality");
        test.pass("Case 3 passed: Moderate");

        // Case 4: One value in "Poor" range
        String result4 = AirQualityEvaluator.evaluateAirQuality(260, 20, 10, 5, 30, 1000);
        assertEquals(result4, "Poor", "Expected 'Poor' quality");
        test.pass("Case 4 passed: Poor");

        // Case 5: One value in "Very Poor" range
        String result5 = AirQualityEvaluator.evaluateAirQuality(400, 20, 10, 5, 30, 1000);
        assertEquals(result5, "Very Poor", "Expected 'Very Poor' quality");
        test.pass("Case 5 passed: Very Poor");
    }

    @Test
    public void testAirQualityByCity() {
        test = extent.createTest("AirPollutionAPI - Test Air Quality by City");

        List<String> cities = ConfigManager.getCityList("src/test/resources/cities.csv");

        for (String city : cities) {
            //test.info("Testing air pollution for city: " + city);

            Map<String, String> geoParams = new HashMap<>();
            geoParams.put("q", city);
            geoParams.put("limit", "1");

            //Response geoResponse = sendGetWithApiKey("/geo/1.0/direct", geoParams);
            Response geoResponse = sendRequestWithApiKey("GET", "/geo/1.0/direct", geoParams, null);

            double lat = DEFAULT_LAT;
            double lon = DEFAULT_LON;

            if (geoResponse.statusCode() == 200 && geoResponse.jsonPath().getList("$").size() > 0) {
                lat = geoResponse.jsonPath().getDouble("[0].lat");
                lon = geoResponse.jsonPath().getDouble("[0].lon");
                //test.info("Retrieved lat/lon: " + lat + ", " + lon);
            } else {
                test.warning("Could not retrieve lat/lon for city: " + city + ". Using default values.");
            }

            Map<String, String> pollutionParams = new HashMap<>();
            pollutionParams.put("lat", String.valueOf(lat));
            pollutionParams.put("lon", String.valueOf(lon));

            //Response pollutionResponse = sendGetWithApiKey("/data/2.5/air_pollution", pollutionParams);
            Response pollutionResponse = sendRequestWithApiKey("GET", "/data/2.5/air_pollution", pollutionParams, null);


            assertEquals(pollutionResponse.statusCode(), 200, "Status code should be 200");
            test.pass("Air pollution data retrieved successfully for city: " + city);

            // Extract pollutant values
            double so2 = pollutionResponse.jsonPath().getDouble("list[0].components.so2");
            double no2 = pollutionResponse.jsonPath().getDouble("list[0].components.no2");
            double pm10 = pollutionResponse.jsonPath().getDouble("list[0].components.pm10");
            double pm25 = pollutionResponse.jsonPath().getDouble("list[0].components.pm2_5");
            double o3 = pollutionResponse.jsonPath().getDouble("list[0].components.o3");
            double co = pollutionResponse.jsonPath().getDouble("list[0].components.co");

            test.info("Pollutant levels - SO2: " + so2 + ", NO2: " + no2 + ", PM10: " + pm10 + ", PM2.5: " + pm25 + ", O3: " + o3 + ", CO: " + co);

            // Evaluate air quality
            String quality = AirQualityEvaluator.evaluateAirQuality(so2, no2, pm10, pm25, o3, co);
            //test.info("Evaluated Air Quality for " + city + ": " + quality);

            String fullUrl = buildFullUrl("/geo/1.0/direct", geoParams);
            test.info("**Full Request URL:**\n" + fullUrl);
        }

    }
}
