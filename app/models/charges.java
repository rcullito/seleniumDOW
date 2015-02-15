package models;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;



public class Charges {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  
  
  public WebDriver selectDriver(Boolean ghostProtocol) {
	  if (ghostProtocol) {
		  DesiredCapabilities caps = new DesiredCapabilities();
	      caps.setJavascriptEnabled(true);
	      caps.setCapability("takesScreenshot", true);
	      caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"/usr/local/bin/phantomjs");
	      return new PhantomJSDriver(caps);
	  } else {
		  return new FirefoxDriver();
	  } 
  }
  
  public String launch(Boolean ghostProtocol, String url, String prefix, String firstname, String lastname, String email, String address1, String city, String state, String zip) throws Exception {

	  WebDriver driver = selectDriver(ghostProtocol);
      driver.get(url);      
      new Select(driver.findElement(By.id("title"))).selectByVisibleText(prefix);
      driver.findElement(By.id("fname")).clear();
      driver.findElement(By.id("fname")).sendKeys(firstname);    
      driver.findElement(By.id("lname")).clear();
      driver.findElement(By.id("lname")).sendKeys(lastname);
      driver.findElement(By.id("street1")).clear();
      driver.findElement(By.id("email")).sendKeys(email);
      driver.findElement(By.id("street1")).clear();
      driver.findElement(By.id("street1")).sendKeys(address1);
      driver.findElement(By.id("city")).clear();
      driver.findElement(By.id("city")).sendKeys(city);
      new Select(driver.findElement(By.id("state"))).selectByVisibleText(state);    
      driver.findElement(By.id("zip")).clear();
      driver.findElement(By.id("zip")).sendKeys(zip); 
      driver.findElement(By.id("send")).click(); 

      Thread.sleep(5000);
      
      // check what the page title is if it is thanks we can quit if it is asking for more info then give it more info!
      
      String endPageTitle = "Page title is: " + driver.getTitle();
      
      //Close the browser
      driver.quit();	
      
      return endPageTitle;

  }
  
  
  
}
