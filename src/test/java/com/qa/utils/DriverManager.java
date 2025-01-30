package com.qa.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.IOException;

/**
 * The DriverManager class is responsible for managing AppiumDriver instances associated with threads using ThreadLocal.
 * It provides methods to initialize, retrieve, and quit the AppiumDriver for the current thread.
 */
public class DriverManager {

    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    TestUtils utils = new TestUtils();

    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driver2) {
        driver.set(driver2);
    }

    /**
     * Initializes an AppiumDriver instance for use in the current thread.
     *
     * If the driver instance is null, it attempts to create and assign a new AppiumDriver
     * by invoking the CapabilitiesManager's capabilities. The initialization process includes
     * setting the driver to the current thread's context using the ThreadLocal object in the DriverManager class.
     *
     * This method also logs relevant information during the process of initializing the driver.
     * If an issue occurs during initialization, such as the driver being null or an IOException happening
     * while setting up the driver, the method logs an error and throws the respective exception.
     *
     * @throws Exception if the driver could not be initialized or if an IOException occurs during the setup process.
     */
    public void initializeDriver() throws Exception {
        GlobalParams params = new GlobalParams();
        PropertyManager props = new PropertyManager();

        AppiumDriver driver = null;

        if (driver == null) {
            try {
                utils.log().info("Initializing appium driver");
                driver = CapabilitiesManager.getCaps();
                if (driver == null) {
                    throw new Exception("Driver is null");
                }
                utils.log().info("Driver is initialized");
                this.driver.set(driver);
            } catch (IOException e) {
                e.printStackTrace();
                utils.log().fatal("Driver initialization failure");
                throw e;
            }
        }

    }

    /**
     * Quits the Appium driver instance associated with the current thread and
     * removes it from the ThreadLocal storage to ensure cleanup.
     *
     * This method ensures that if a driver instance is present for the current thread,
     * it will be properly terminated, and the reference will be removed from the
     * ThreadLocal to prevent memory leaks and ensure safe multithreading practices.
     *
     * The method logs the process of quitting the driver and handles any exceptions
     * that may occur during the driver termination to avoid application crashes.
     * Regardless of success or failure, the driver instance is removed from the
     * ThreadLocal at the end of the operation.
     */
    // Quits the driver and removes it from the ThreadLocal
    public void quitDriver() {
        if (this.driver.get() != null) {
            utils.log().info("Quitting Appium driver for current thread");
            try {
                this.driver.get().quit(); // Quit the driver
            } catch (Exception e) {
                utils.log().error("Error while quitting Appium driver: " + e.getMessage(), e);
            } finally {
                this.driver.remove(); // Remove the driver from ThreadLocal
                utils.log().info("Appium driver removed from ThreadLocal.");
            }
        }
    }
}
