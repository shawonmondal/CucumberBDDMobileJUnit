package com.qa.runners;

import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.ServerManager;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.apache.logging.log4j.ThreadContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;

/**
 * This is the test runner class for executing Cucumber tests using JUnit.
 * It integrates with Cucumber and uses the specified options to configure the test execution.
 * The class is annotated with the {@code @RunWith(Cucumber.class)} annotation,
 * which indicates it should be run with the Cucumber test runner.
 * <p>
 * The class includes:
 * <p>
 * - Cucumber options:
 * - `plugin`: Specifies the plugins used for report generation, including a pretty console output,
 * an HTML report, and a summary.
 * - `features`: Location of the feature files.
 * - `glue`: Specifies the location of the step definitions.
 * - `snippets`: Defines the code snippet format for undefined steps as camel case.
 * - `dryRun`: If set to true, checks that step definitions are defined without actually running the steps.
 * - `monochrome`: Enables readable console output.
 * - `tags`: Used to select scenarios to execute based on the specified tag.
 * <p>
 * - A {@code @BeforeClass} annotated method:
 * - Initializes the global parameters for the application using the {@code GlobalParams} class,
 * including platform-specific parameters for Android or iOS.
 * - Sets a thread context routing key for logging purposes based on platform and device name.
 * - Starts the appium server and initializes the driver for test execution.
 * <p>
 * - A {@code @AfterClass} annotated method:
 * - Terminates the driver if it is running.
 * - Stops the appium server if it is running.
 * <p>
 * This class facilitates the execution of Cucumber feature scenarios with the specified configuration
 * for test environments.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"
                , "html:target/cucumber/cucumber.html"
                , "summary"
        }
        , features = "src/test/resources/Features"
        , glue = {"com.qa.stepDef"}
        , snippets = CAMELCASE
        , dryRun = false
        , monochrome = true
        , tags = "@Test"
//           , strict=true             // This is depriaated

)

public class MyRunnerTest {

    /**
     * Initializes the testing environment and prepares the necessary resources
     * before test execution. This method is executed once before any test cases are run.
     * <p>
     * The method performs the following actions:
     * 1. Initializes global parameters for the application using the GlobalParams class.
     * This includes setting platform-specific configurations such as platform name,
     * device name, and other related parameters.
     * 2. Sets a thread context routing key for organized logging,
     * formatted as "PlatformName_DeviceName".
     * 3. Starts the Appium server to enable interaction with the device under test.
     * 4. Initializes the driver using the DriverManager class to facilitate
     * communication with the device and perform test automation actions.
     *
     * @throws Exception if an error occurs during initialization of the global
     *                   parameters, server, or driver.
     */
    @BeforeClass
    public static void initialize() throws Exception {
        GlobalParams params = new GlobalParams();
        params.initializeGlobalParams();

        ThreadContext.put("ROUTINGKEY", params.getPlatformName() + "_" + params.getDeviceName());

        new ServerManager().startServer();
        new DriverManager().initializeDriver();
    }

    /**
     * Cleans up resources after all tests have been executed.
     * <p>
     * This method is executed after all test cases are completed to ensure proper teardown
     * of the server and driver instances. It performs the following actions:
     * <p>
     * 1. Stops the Appium driver by invoking the quitDriver method in the DriverManager class,
     * which ensures the AppiumDriver instance is terminated and removed.
     * 2. Stops the Appium server using the ServerManager if it is currently running.
     * <p>
     * This method ensures that all resources initialized during the tests are properly released
     * to prevent memory leaks or unwanted resource usage. It also ensures proper cleanup in
     * multi-threaded environments.
     */
    @AfterClass
    public static void quit() {
        // Here we are stopping the driver only if it is running
        DriverManager driverManager = new DriverManager();
        driverManager.quitDriver();

        // Here we are stopping the server only if it is running
        ServerManager serverManager = new ServerManager();
        if (serverManager.getServer() != null) {
            serverManager.getServer().stop();
        }
    }
}
