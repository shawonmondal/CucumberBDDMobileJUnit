package com.qa.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

/**
 * This class is responsible for managing the capabilities required to initialize
 * AppiumDriver instances for both Android and iOS platforms. It configures the
 * capabilities based on the device and platform details provided in the GlobalParams
 * class and loads properties from a configuration file.
 */
public class CapabilitiesManager {
    static TestUtils utils = new TestUtils();

    /**
     * Sets up and returns an AppiumDriver instance configured for the specified platform (Android or iOS).
     * This method loads platform-specific capabilities based on the properties from a configuration file
     * and global parameters, and initializes the driver accordingly.
     *
     * @return an AppiumDriver instance configured for the platform defined in the global parameters.
     * @throws IOException if there is an error loading the properties file or initializing the driver.
     */
    // The getCaps method is used to set the capabilities for the Android and iOS platforms
    public static AppiumDriver getCaps() throws IOException {
        AppiumDriver driver;
        String sessionId;
        GlobalParams params = new GlobalParams();
        URL url;

        // The try block is used to catch any exceptions that may occur during the execution of the code
        try {

            // It is important to note that the PropertyManager class is used to load the properties file
            Properties props = new PropertyManager().getProps();
            String appiumUrl = props.getProperty("appiumURL");
            utils.log().info("APPIUM URL: "+appiumUrl);
            if (appiumUrl == null || appiumUrl.isEmpty()) {
                throw new IllegalArgumentException("Appium URL is not specified in properties file.");
            }

            url = new URL(appiumUrl);
            utils.log().info("getting capabilities");

            // The switch statement is used to determine the platform name and set the appropriate capabilities using the options classes
            switch (params.getPlatformName()) {
                case "Android":
                    UiAutomator2Options androidOptions = new UiAutomator2Options();
                    androidOptions.setPlatformName(params.getPlatformName());                       // Setting the platformName and we are reading the value from the GlobalParams class
                    androidOptions.setUdid(params.getUDID());                                       // Setting the udid and we are reading the value from the GlobalParams class
                    androidOptions.setDeviceName(params.getDeviceName());                           // Setting the deviceName and we are reading the value from the GlobalParams class
                    androidOptions.setAutomationName(props.getProperty("androidAutomationName"));   // Setting the automationName and we are reading the value from the config.properties file
                    androidOptions.setNewCommandTimeout(Duration.ofSeconds(560));
                    androidOptions.setAppPackage(props.getProperty("androidAppPackage"));           // Setting the appPackage and we are reading the value from the config.properties file
                    androidOptions.setAppActivity(props.getProperty("androidAppActivity"));         // Setting the appActivity and we are reading the value from the config.properties file


                    androidOptions.setAvd(params.getDeviceName()).setAvdLaunchTimeout(Duration.ofSeconds(660));

                    androidOptions.setSystemPort(Integer.parseInt(params.getSystemPort()));                 // Setting the systemPort and we are reading the value from the GlobalParams class
                    androidOptions.setChromedriverPort(Integer.parseInt(params.getChromeDriverPort()));     // Setting the chromedriverPort and we are reading the value from the GlobalParams class

                    String appUrlAndroid = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" +
                            File.separator + "resources" + File.separator + "app" +
                            File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
                    utils.log().info("App Url is " + appUrlAndroid);                                        // Setting the app and we are getting the app location from the resources folder

                    androidOptions.setApp(appUrlAndroid);                                                   // Setting the app

                    // This becomes the local variable for this method
                    driver = new AndroidDriver(url, androidOptions);
                    sessionId = driver.getSessionId().toString();
                    utils.log().info("Session Id is " + sessionId);
                    break;


                case "iOS":
                    XCUITestOptions iOSOptions = new XCUITestOptions();
                    iOSOptions.setPlatformName(params.getPlatformName());
                    iOSOptions.setUdid(params.getUDID());
                    iOSOptions.setDeviceName(params.getDeviceName());
                    iOSOptions.setAutomationName(props.getProperty("iOSAutomationName"));
                    String iOSAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                            + File.separator + "resources" + File.separator + "app" + File.separator + "SwagLabsMobileApp.app";
                    utils.log().info("App Url is " + iOSAppUrl);
                    iOSOptions.setBundleId(props.getProperty("iOSBundleId"));
                    iOSOptions.setWdaLocalPort(Integer.parseInt(params.getWdaLocalPort()));
//                    iOSOptions.setWebkitDebugProxyPort(Integer.parseInt(params.getWebkitDebugProxyPort()));
                    iOSOptions.setApp(iOSAppUrl);

                    driver = new IOSDriver(url, iOSOptions);
                    sessionId = driver.getSessionId().toString();
                    break;
                default:
                    throw new IllegalStateException("Invalid Platform Name");
            }
//            return driver;


        } catch (Exception e) {
            e.printStackTrace();
            utils.log().fatal("Failed to load capabilities. ABORT!!" + e.toString());
            throw e;        // We are throwing the exception, as we want to stop the execution of the code if an exception occurs
        }
        return driver;
    }


}
