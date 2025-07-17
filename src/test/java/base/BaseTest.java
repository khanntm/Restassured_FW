package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.weatherapi.utils.ExtentManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;
import utils.ConfigManager;

import java.util.Map;

public class BaseTest {



    protected static ExtentReports extent;
    protected static ExtentTest test;


    /* @BeforeSuite
    public void setupReport() {
        extent = ExtentManager.createInstance("test-output/ExtentReport.html");
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    } */


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigManager.get("baseUrl");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Finished tests in " + this.getClass().getSimpleName());
    }

    public Response sendGetWithApiKey(String endpoint, Map<String, String> params) {
        params.put("appid", ConfigManager.get("apiKey"));
        return RestAssured
                .given()
                .queryParams(params)
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }
}
