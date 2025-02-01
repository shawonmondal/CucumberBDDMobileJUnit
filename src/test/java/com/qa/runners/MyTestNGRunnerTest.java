package com.qa.runners;

import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.ServerManager;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.CucumberPropertiesProvider;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;

/**
 * An example of using TestNG when the test class does not inherit from
 * AbstractTestNGCucumberTests but still executes each scenario as a separate
 * TestNG test.
 */
@CucumberOptions(plugin = {"pretty"
        , "html:target/cucumber/cucumber.html"
        , "summary"
}
        , features = "src/test/resources/Features"
        , glue = {"com.qa.stepDef"}             // glue = {"com.qa.stepDef"} will tell the runner where to look for the step definitions
        , dryRun = false                        // dryRun = true will not run the test, it will only check if the steps are defined or not
        , monochrome = true                     // monochrome = true will make the console output readable
        , tags = "@Test"
)
public class MyTestNGRunnerTest {

    private TestNGCucumberRunner testNGCucumberRunner;

    @Parameters({"platformName", "udid", "deviceName", "systemPort",
            "chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeClass(alwaysRun = true)
    public void setUpClass(ITestContext context, String platformName, String udid, String deviceName,
                           @Optional("Android") String systemPort,
                           @Optional("Android") String chromeDriverPort,
                           @Optional("iOS") String wdaLocalPort,
                           @Optional("iOS") String webkitDebugProxyPort) throws Exception {
        XmlTest currentXmlTest = context.getCurrentXmlTest();
        CucumberPropertiesProvider properties = currentXmlTest::getParameter;

        ThreadContext.put("ROUTINGKEY", platformName + "_" + deviceName);

        GlobalParams params = new GlobalParams();
        params.setPlatformName(platformName);
        params.setUDID(udid);
        params.setDeviceName(deviceName);

        switch (platformName) {
            case "Android":
                params.setSystemPort(systemPort);
                params.setChromeDriverPort(chromeDriverPort);
                break;
            case "iOS":
                params.setWdaLocalPort(wdaLocalPort);
                params.setWebkitDebugProxyPort(webkitDebugProxyPort);
                break;
            default:
                throw new Exception("Invalid platformName: " + platformName);
        }

//        new ServerManager().startServer();
        ServerManager serverManager = new ServerManager();
        serverManager.startServer();

//        new DriverManager().initializeDriver();
        DriverManager driverManager = new DriverManager();
        driverManager.initializeDriver();

        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass(), properties);
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void scenario(PickleWrapper pickle, FeatureWrapper cucumberFeature) {
        testNGCucumberRunner.runScenario(pickle.getPickle());
    }

    @DataProvider
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        DriverManager driverManager = new DriverManager();
        if (driverManager.getDriver() != null) {
            driverManager.getDriver().quit();
            driverManager.setDriver(null);
        }
        ServerManager serverManager = new ServerManager();
        if (serverManager.getServer() != null) {
            serverManager.getServer().stop();
        }
        if (testNGCucumberRunner != null) {
            testNGCucumberRunner.finish();
        }
    }

}