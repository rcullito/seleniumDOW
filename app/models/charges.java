package models;

import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;



public class Charges {
  private WebDriver driver;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  
    
  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void launch(String url, String prefix, String firstname, String lastname, String email, String address1, String city, String state, String zip) throws Exception {
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
	    // need to find a better way around this option
	    new Select(driver.findElement(By.id("1428_34940_5_42561"))).selectByVisibleText("No");
	    driver.findElement(By.id("send")).click(); 
  }

  
  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
