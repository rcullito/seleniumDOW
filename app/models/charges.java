package models;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;



public class Charges {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  

  public void launch(String url, String prefix, String firstname, String lastname, String email, String address1, String city, String state, String zip) throws Exception {

      // Create a new instance of the Firefox driver
      // Notice that the remainder of the code relies on the interface, 
      // not the implementation.
      WebDriver driver = new FirefoxDriver();

      // And now use this to visit Google
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
      
      //Close the browser
      driver.quit();	

  }
  
  
  
}
