# CucumberBDDMobileJUnit
Use : mvn test -Dsurefire.suiteXmlFile=src/test/resources/testng.xml to run TestNG tests in command prompt
Use : netstat -ano|findstr "PID :4723" (To find PID of the port 4723)
Proto  Local Address          Foreign Address        State           PID
TCP    0.0.0.0:4723           0.0.0.0:0              LISTENING       14668

C:\Users\shawo>taskkill /pid 14668 /f (To kill the process with PID 14668)
SUCCESS: The process with PID 14668 has been terminated.