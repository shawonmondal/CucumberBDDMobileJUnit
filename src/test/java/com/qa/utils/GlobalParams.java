package com.qa.utils;

/**
 * The GlobalParams class manages thread-local storage for a set of global parameters
 * that are commonly used to configure platform-specific settings for devices. These parameters
 * include platform name, device identifiers, port numbers, and more, which are stored and retrieved
 * on a per-thread basis to ensure thread safety.
 *
 * The class provides getter and setter methods for each parameter, enabling users to easily manage configuration values.
 * Additionally, it includes a method to initialize these global parameters with default values or system properties.
 *
 * The parameters managed include:
 * - Platform Name: Specifies the operating system platform (e.g., Android or iOS).
 * - UDID: Unique device identifier.
 * - Device Name: Name of the device under test.
 * - System Port: Port for communication with the device in Android.
 * - Chrome Driver Port: Port for ChromeDriver in Android.
 * - WDA Local Port: Port for WebDriverAgent in iOS.
 * - Webkit Debug Proxy Port: Port for Webkit Debug Proxy in iOS.
 */
public class GlobalParams {
    private static ThreadLocal<String> platformName = new ThreadLocal<String>();
    private static ThreadLocal<String> udid = new ThreadLocal<String>();
    private static ThreadLocal<String> deviceName = new ThreadLocal<String>();
    private static ThreadLocal<String> systemPort = new ThreadLocal<String>();
    private static ThreadLocal<String> chromeDriverPort = new ThreadLocal<String>();
    private static ThreadLocal<String> wdaLocalPort = new ThreadLocal<String>();
    private static ThreadLocal<String> webkitDebugProxyPort = new ThreadLocal<String>();

    public void setPlatformName(String platformName1){
        platformName.set(platformName1);
    }

    public String getPlatformName(){
        return platformName.get();
    }

    public String getUDID() {
        return udid.get();
    }

    public void setUDID(String udid2) {
        udid.set(udid2);
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public void setDeviceName(String deviceName2) {
        deviceName.set(deviceName2);
    }

    public String getSystemPort() {
        return systemPort.get();
    }

    public void setSystemPort(String systemPort2) {
        systemPort.set(systemPort2);
    }

    public String getChromeDriverPort() {
        return chromeDriverPort.get();
    }

    public void setChromeDriverPort(String chromeDriverPort2) {
        chromeDriverPort.set(chromeDriverPort2);
    }

    public String getWdaLocalPort() {
        return wdaLocalPort.get();
    }

    public void setWdaLocalPort(String wdaLocalPort2) {
        wdaLocalPort.set(wdaLocalPort2);
    }

    public String getWebkitDebugProxyPort() {
        return webkitDebugProxyPort.get();
    }

    public void setWebkitDebugProxyPort(String webkitDebugProxyPort2) {
        webkitDebugProxyPort.set(webkitDebugProxyPort2);
    }

    /**
     * Initializes the global parameters required for the application to interact with
     * a mobile platform (e.g., Android or iOS). The method retrieves the values for
     * platform name, device ID, and device name from system properties or sets them
     * to default values if not provided. Additionally, platform-specific parameters
     * such as system port, ChromeDriver port for Android, or WDA local port and
     * WebKit debug proxy port for iOS are also configured.
     *
     * For Android:
     * - Sets the system port and ChromeDriver port.
     *
     * For iOS:
     * - Sets the WDA local port and WebKit debug proxy port.
     *
     * If an unsupported platform name is provided, the method throws an
     * {@code IllegalStateException}.
     */
    // This method initializes the global parameters
    public void initializeGlobalParams(){
        GlobalParams params = new GlobalParams();

        params.setPlatformName(System.getProperty("platformName", "Android"));        // The platformName is set to Android by default
        params.setUDID(System.getProperty("udid", "emulator-5554"));                 // The UDID is set to emulator-5554 by default
        params.setDeviceName(System.getProperty("deviceName", "Pixel_8_API_35"));   // The deviceName is set to Pixel_8_API_35 by default

        switch(params.getPlatformName()){
            case "Android":
                params.setSystemPort(System.getProperty("systemPort", "10000"));
                params.setChromeDriverPort(System.getProperty("chromeDriverPort", "11000"));
                break;

            case "iOS":
                params.setWdaLocalPort(System.getProperty("wdaLocalPort", "10001"));
                params.setWebkitDebugProxyPort(System.getProperty("webkitDebugProxyPort", "11001"));
                break;

            default:
                throw new IllegalStateException("Invalid Platform Name");
        }
    }

}
