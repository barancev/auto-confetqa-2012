package ru.confetqa.sample1;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class JUnitSampleTest1 {

  private RemoteWebDriver driver;
  
  @Before
  public void initDriver() {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Test
  public void sampleTest() {
    driver.get("http://localhost/php4dvd/");
    // login
    driver.findElement(By.id("username")).sendKeys("admin");
    driver.findElement(By.name("password")).sendKeys("admin");
    driver.findElement(By.name("submit")).click();
    // logout
    driver.findElement(By.linkText("Log out")).click();
    driver.switchTo().alert().accept();
  }

  @After
  public void stopDriver() {
    driver.quit();
  }
}
