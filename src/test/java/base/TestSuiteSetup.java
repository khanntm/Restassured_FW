package base;
import com.aventstack.extentreports.ExtentReports;
import com.weatherapi.utils.ExtentManager;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class TestSuiteSetup extends BaseTest {

    @BeforeSuite
    public void beforeSuite() {
        extent = ExtentManager.createInstance("test-output/ExtentReport.html");
    }

    @AfterSuite
    public void afterSuite() {
        extent.flush();
    }
}

