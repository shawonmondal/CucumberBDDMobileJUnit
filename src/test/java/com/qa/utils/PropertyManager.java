package com.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

    // This is a static variable that will hold the properties
    // We are not making it thread safe as we are not going to manipulate the properties, we will just use it for read only
    // Also these global properties are going to remain constant for all mobile device, thats why it is good to have a single instance and shared between the devices
    // Evem if you want to make this as thread safe, we can.
    private static Properties props = new Properties();

    // We are creating an instance of TestUtils class to log the messages
    TestUtils utils = new TestUtils();

    // This method will return the properties object and load the properties from the file
    public Properties getProps() throws IOException {
        InputStream is = null;        // InputStream object to read the properties file
        String propsFileName = "config.properties";     // Name of the properties file

        // If the properties object is empty, then load the properties from the file
        if(props.isEmpty()){
            try{
                utils.log().info("loading config properties");
                is = getClass().getClassLoader().getResourceAsStream(propsFileName);    // Get the input stream object
                props.load(is);                            // Load the properties from the input stream
            } catch (IOException e) {
                e.printStackTrace();
                // Log the error message and throw the exception
                utils.log().fatal("Failed to load config properties. ABORT!!" + e.toString());
                throw e;
            } finally {
                // Close the input stream if it is not null
                if(is != null){
                    is.close();     // Close the input stream (best practice)
                }
            }
        }
        return props;
    }
}
