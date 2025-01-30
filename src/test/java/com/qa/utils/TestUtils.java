package com.qa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * The TestUtils class provides utility methods commonly used for testing,
 * such as parsing XML files, generating the current date and time in a specific format,
 * and obtaining logger instances for logging purposes.
 */
public class TestUtils {
    public static  final long WAIT = 10;

    /**
     * Parses the provided XML input stream and extracts key-value pairs where
     * the key is the value of the "name" attribute, and the value is the text content
     * of each "string" element in the XML.
     *
     * @param file the InputStream representing the XML file to be parsed
     * @return a HashMap containing the key-value pairs extracted from the XML
     * @throws Exception if any error occurs during XML parsing
     */
    public HashMap<String, String> parseStringXML(InputStream file) throws Exception{
        HashMap<String, String> stringMap = new HashMap<String, String>();
        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Build Document
        Document document = builder.parse(file);

        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

        //Here comes the root node
        Element root = document.getDocumentElement();

        //Get all elements
        NodeList nList = document.getElementsByTagName("string");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                // Store each element key value in map
                stringMap.put(eElement.getAttribute("name"), eElement.getTextContent());
            }
        }
        return stringMap;
    }

    /**
     * Generates the current date and time in the format "yyyy-MM-dd-HH-mm-ss".
     * This method logs the exact date and time at the moment of invocation
     * and returns the formatted string representation.
     *
     * @return a string representing the current date and time in the specified format.
     */
    public String dateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        log().info("Exact Date & Time: "+ dateFormat.format(date));
        return dateFormat.format(date);
    }

    /**
     * Retrieves a logger instance using log4j2.
     *
     * This method dynamically determines the class name from the call stack of the
     * calling thread and initializes a logger for that class using LogManager from
     * the log4j2 library.
     *
     * @return a Logger instance specific to the class that invoked this method.
     */
    // This method is used to get the logger using log4j2
    public Logger log() {
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }

}
