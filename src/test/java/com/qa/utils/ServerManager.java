package com.qa.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.ThreadContext;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

/**
 * The ServerManager class is responsible for managing the Appium server lifecycle,
 * including server configuration, starting, and checking its status.
 * It uses Appium local services to facilitate communication between mobile devices
 * and testing frameworks.
 */
public class ServerManager {
    private static ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();
    TestUtils utils = new TestUtils();

    /**
     * Retrieves an instance of the AppiumDriverLocalService.
     *
     * @return the current AppiumDriverLocalService instance managed by the server.
     */
    public AppiumDriverLocalService getServer(){
        return server.get();
    }


    /**
     * Builds and retrieves the default Appium server instance.
     *
     * @return an instance of AppiumDriverLocalService constructed with the default configuration.
     */
    public AppiumDriverLocalService getAppiumServerDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    /**
     * Builds and starts an AppiumDriverLocalService specifically configured for Windows platform testing.
     * The service is set to use any available free port, overrides existing sessions, and logs server
     * activity to a file named based on the platform name and device name.
     *
     * @return an instance of AppiumDriverLocalService configured for the Windows platform.
     */
    public AppiumDriverLocalService WindowsGetAppiumService() {
        GlobalParams params = new GlobalParams();
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withLogFile(new File(params.getPlatformName() + "_"
                        + params.getDeviceName() + File.separator + "Server.log")));
    }

    /**
     * Configures and builds an AppiumDriverLocalService instance specifically for macOS platforms.
     * The service is designed to use any available free port, override existing sessions,
     * and log server activity to a file named based on the platform name and device name.
     *
     * The method sets up a customized environment for the server, including paths for
     * PATH, ANDROID_HOME, and JAVA_HOME. It also specifies the locations for the Node.js
     * executable and the main Appium script.
     *
     * @return an instance of AppiumDriverLocalService configured for macOS platform.
     */
    public AppiumDriverLocalService MacGetAppiumService() {
        GlobalParams params = new GlobalParams();
        HashMap<String, String> environment = new HashMap<String, String>();
        environment.put("PATH", "enter_your_path_here" + System.getenv("PATH"));
        environment.put("ANDROID_HOME", "enter_your_android_home_path");
        environment.put("JAVA_HOME", "enter_your_java_home_path");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("/usr/local/bin/node"))
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withEnvironment(environment)
                .withLogFile(new File(params.getPlatformName() + "_"
                        + params.getDeviceName() + File.separator + "Server.log")));
    }

    /**
     * Checks if an Appium server is running on the specified port.
     * This method attempts to create a server socket on the given port.
     * If the port is already in use, it catches an IOException and determines the port is occupied,
     * indicating that an Appium server may be running.
     *
     * @param port the port number to check for an active Appium server
     * @return true if an Appium server is running on the specified port, false otherwise
     * @throws Exception if any unexpected error occurs during the socket operation
     */
    public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
        boolean isAppiumServerRunning = false;
        ServerSocket socket;
        try {
            socket = new ServerSocket(port);
            socket.close();
        } catch (IOException e) {
            System.out.println("1");
            isAppiumServerRunning = true;
        } finally {
            socket = null;
        }
        return isAppiumServerRunning;
    }

    /**
     * Starts the Appium server if it is not already running on the default port.
     *
     * This method first checks if an Appium server is running on port 4723. If a server
     * is not detected on this port, it invokes the appropriate AppiumDriverLocalService
     * instance to start the server. Server logs will not be displayed in the console by default.
     * If the server is already running, an informational message is logged.
     *
     * @throws Exception if any error occurs during the server operation or port checking process
     */
    public void startServer() throws Exception {
        ThreadContext.put("ROUTINGKEY", "ServerLogs");
//		server = getAppiumService(); // -> If using Mac, uncomment this statement and comment below statement
        AppiumDriverLocalService server = getAppiumServerDefault(); // -> If using Windows, uncomment this statement and comment above statement
        if(!checkIfAppiumServerIsRunnning(4723)) {
            server.start();
            server.clearOutPutStreams(); // -> Comment this if you want to see server logs in the console
            utils.log().info("Appium server started");
        } else {
            utils.log().info("Appium server already running");
        }
    }

    /**
     * Starts the Appium server instance using the configured Windows Appium service.
     *
     * This method initializes and starts an AppiumDriverLocalService instance specifically
     * configured for Windows platform testing. It validates the successful start of the server
     * and assigns it to a shared "server" field in the class. If the server fails to start,
     * a fatal log message is recorded, and an exception is thrown to halt the execution.
     *
     * The method performs the following:
     * 1. Logs the initiation of the server startup process.
     * 2. Retrieves the Appium server instance using the WindowsGetAppiumService method.
     * 3. Starts the server and verifies if it is running.
     * 4. Logs a fatal error and throws an exception if the server is not successfully started.
     * 5. Assigns the running server instance to the class-level server field.
     * 6. Logs a confirmation of the successfully started server.
     *
     * Throws:
     * - AppiumServerHasNotBeenStartedLocallyException if the Appium server fails to start or is not running.
     */
    public void startServer1(){
        utils.log().info("starting appium server");
        AppiumDriverLocalService server = WindowsGetAppiumService();
        server.start();
        if(server == null || !server.isRunning()){
            utils.log().fatal("Appium server not started. ABORT!!!");
            throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
        }
//        server.clearOutPutStreams();
        this.server.set(server);
        utils.log().info("Appium server started");
    }

}
