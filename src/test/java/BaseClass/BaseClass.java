package BaseClass;

import com.aventstack.extentreports.*;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pom.LoginPage;
import utility.Driver;
import utility.Utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;

public class BaseClass extends Utility {
    String OS = "Android";
    String virtual_Real_device="emulator";


    @BeforeSuite
    public void setupExtentReport() throws IOException {
        System.out.println("Start Automation on: "+OS);
        if (OS.equalsIgnoreCase("Android")) {
            updateDesiredCapJson(virtual_Real_device, "Android");
        }else{
            updateDesiredCapJson("simulator", "iOS");
        }
        initializeExtentReport();
    }

    @BeforeClass
    public void login() throws InterruptedException, MalformedURLException {
        if (OS.equals("Android")) {
            launchAndroidApplication(virtual_Real_device);
        } else {
            launchIOSApplication(virtual_Real_device);
        }
        new LoginPage().login();
    }
    @BeforeMethod
    public void implicitWait() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @AfterClass
    public void tearDown() throws InterruptedException {
        quitDriver();
        // service.stop();
    }

    @AfterMethod
    public void attachScreenshot(ITestResult result) throws IOException, InterruptedException {
        ExtentTest test = Driver.createExtentTestLog(result);

        if (result.getStatus() == ITestResult.FAILURE) {
            Driver.attachScreenshotToReport(test);
            test.log(Status.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test case passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test case skipped");
        }
        Driver.report.flush();
    }



    @AfterSuite
    public void tearDownExtentReport() {
        // Add any final report closing actions here if needed
    }
}
