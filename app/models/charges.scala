package models

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit._;

import org.junit.Assert._
import org.hamcrest.CoreMatchers._

import org.openqa.selenium._;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.net.InetAddress; 
import java.net.UnknownHostException; 
 
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster; 
import com.datastax.driver.core.Session; 
import com.datastax.driver.core.querybuilder.QueryBuilder; 



object Charge {
  
  val driver: WebDriver = new FirefoxDriver();
  driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

  
  
  def launch(url: String, prefix: String, firstname: String, lastname: String, email: String, address1: String, city: String, state: String, zip: String) {
   
    
    driver.get(url);
    
    
  }
  

}