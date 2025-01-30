package com.qa.stepDef;

import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.ServerManager;
import com.qa.utils.VideoManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.OutputType;

import java.io.IOException;

public class Hooks {

    /**
     * Initializes the testing environment before executing test cases.
     *
     * This method is annotated with the `@Before` annotation, which indicates that it will
     * be executed before each test method in the test framework. It performs the following tasks:
     *
     * 1. Creates and initializes an instance of the `GlobalParams` class to configure platform-specific
     *    settings such as platform name and device name. These values are used throughout the test execution.
     * 2. Sets a routing key in the `ThreadContext` for logging and identification purposes. The key is a combination
     *    of the platform name and the device name.
     * 3. Starts the Appium Server by invoking the `startServer` method of the `ServerManager` class.
     * 4. Initializes the driver used for interacting with mobile devices by invoking the `initializeDriver` method
     *    of the `DriverManager` class.
     *
     * @throws Exception if an error occurs during initialization, such as failure to initialize global parameters,
     *                   start the server, or initialize the driver.
     */
    @Before
    public void inititialize() throws Exception {
//        GlobalParams params = new GlobalParams();
//        params.initializeGlobalParams();
//
//        ThreadContext.put("ROUTINGKEY", params.getPlatformName() + "_" + params.getDeviceName());
//
//        new ServerManager().startServer();
//        new DriverManager().initializeDriver();
        new VideoManager().startRecording();

    }

    /**
     * Cleans up resources after each test execution, including stopping the driver and server if they are running.
     * Additionally, captures and attaches a screenshot to the test report if the test scenario has failed.
     *
     * @param scenario The scenario object representing the current test execution.
     *                 It provides context such as whether the test has failed,
     *                 allows handling outputs like attaching screenshots, and other test information.
     */
    @After
    public void quit(Scenario scenario) throws IOException {

        if(scenario.isFailed()){
            byte[] screenshot = new DriverManager().getDriver().getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }

        new VideoManager().stopRecording(scenario.getName());

//        // Here we are stopping the driver only if it is running
//        DriverManager driverManager = new DriverManager();
//        driverManager.quitDriver();
//
//        // Here we are stopping the server only if it is running
//        ServerManager serverManager = new ServerManager();
//        if(serverManager.getServer() != null){
//            serverManager.getServer().stop();
//        }
    }
}
