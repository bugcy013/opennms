package org.opennms.smoketest;

import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.SeleneseTestBase;



public class MapPageTest extends SeleneseTestBase {
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capability = DesiredCapabilities.firefox();
        WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);        String baseUrl = "http://localhost:8980/";
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
        //selenium.start();
        selenium.open("/opennms/login.jsp");
        selenium.type("name=j_username", "admin");
        selenium.type("name=j_password", "admin");
        selenium.click("name=Login");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Map");
        selenium.waitForPageToLoad("30000");
    }

    @Test
    public void testMapPage() throws Exception {
        assertTrue(selenium.isElementPresent("id=mainSvgDocument"));
        assertTrue(selenium.isTextPresent("Network Topology Maps"));
        selenium.click("link=Log out");
        selenium.waitForPageToLoad("30000");
    }

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}