package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.ConfigManager;
import utils.ExtentManager;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected static ExtentReports extent;
    protected static ExtentTest test;

    @BeforeSuite
    public void beforeSuite() {
        RestAssured.baseURI = ConfigManager.get("baseUrl");
        extent = ExtentManager.getInstance();
    }

    @AfterSuite
    public void afterSuite() {
        if (extent != null) {
            extent.flush();
        }
    }

    public Response sendRequestWithApiKey(String method, String endpoint, Map<String, String> params, Object body) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("appid", ConfigManager.get("apiKey"));

        RequestSpecification request = RestAssured
                .given()
                .log().all()
                .queryParams(params)
                .contentType(ContentType.JSON);

        if (body != null) {
            request.body(body);
        }

        switch (method.toUpperCase()) {
            case "GET":
                return request.when().get(endpoint).then().log().all().extract().response();
            case "POST":
                return request.when().post(endpoint).then().log().all().extract().response();
            case "PUT":
                return request.when().put(endpoint).then().log().all().extract().response();
            case "DELETE":
                return request.when().delete(endpoint).then().log().all().extract().response();
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }

    public String buildFullUrl(String endpoint, Map<String, String> params) {
        StringBuilder url = new StringBuilder(RestAssured.baseURI);
        if (!endpoint.startsWith("/")) {
            url.append("/");
        }
        url.append(endpoint);

        if (params != null && !params.isEmpty()) {
            url.append("?");
            params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
            url.setLength(url.length() - 1); // remove trailing &
        }

        return url.toString();
    }


}
