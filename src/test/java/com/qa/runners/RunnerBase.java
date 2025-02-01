package com.qa.runners;

import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.ServerManager;
import io.cucumber.testng.CucumberPropertiesProvider;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

public class RunnerBase {


    private static final ThreadLocal<TestNGCucumberRunner> testNGCucumberRunner = new ThreadLocal<>();

    public static TestNGCucumberRunner getRunner(){
        return testNGCucumberRunner.get();
    }

    private static void setRunner(TestNGCucumberRunner testNGCucumberRunner1){
        testNGCucumberRunner.set(testNGCucumberRunner1);
    }

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

//        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass(), properties);
        setRunner(new TestNGCucumberRunner(this.getClass(), properties));       // This way, we are setting the TestNGCucumberRunner instance to the ThreadLocal variable
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void scenario(PickleWrapper pickle, FeatureWrapper cucumberFeature) {
        getRunner().runScenario(pickle.getPickle());
    }

    @DataProvider
    public Object[][] scenarios() {
        return getRunner().provideScenarios();
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
            getRunner().finish();
        }
    }

}
