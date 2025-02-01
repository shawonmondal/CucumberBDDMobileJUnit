package com.qa.runners;

import com.qa.utils.DriverManager;
import com.qa.utils.GlobalParams;
import com.qa.utils.ServerManager;
import io.cucumber.testng.*;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

/**
 * An example of using TestNG when the test class does not inherit from
 * AbstractTestNGCucumberTests but still executes each scenario as a separate
 * TestNG test.
 */
@CucumberOptions(plugin = {"pretty"
        , "html:target/cucumber/Pixel8/cucumber.html"
        , "summary"
}
        , features = "src/test/resources/Features"
        , glue = {"com.qa.stepDef"}
        , dryRun = false
        , monochrome = true
        , tags = "@Test"
)
public class MyPixel8TestNGRunnerTest extends RunnerBase {


}