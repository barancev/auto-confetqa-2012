package ru.confetqa.sample2;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JUnitSampleTest2 {
  
  private static Logger LOG = LoggerFactory.getLogger(JUnitSampleTest2.class);

  private RemoteWebDriver driver;
  
  @Before
  public void initDriver() {
    LOG.debug("Starting Firefox");
    driver = new FirefoxDriver();
    LOG.debug("Firefox started");
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Test
  public void sampleTest() {
    LOG.info(">>> @Test sampleTest");
    LOG.info("Go to main page");
    driver.get("http://localhost/php4dvd/");
    // login
    LOG.info("Login as admin/admin");
    driver.findElement(By.id("username")).sendKeys("admin");
    driver.findElement(By.name("password")).sendKeys("admin");
    driver.findElement(By.name("submit")).click();
    // logout
    LOG.info("Logout");
    driver.findElement(By.linkText("Log out")).click();
    driver.switchTo().alert().accept();
    LOG.info("<<< @Test sampleTest");
  }

  @After
  public void stopDriver() {
    driver.quit();
  }
}
