TODO

1. how to run selenium outside of a testing environment
2. how to run selenium with phantomjs?


driver = new FirefoxDriver();
//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

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