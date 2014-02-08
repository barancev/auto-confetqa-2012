package ru.confetqa.sample4;


import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import ru.confetqa.TestNGTestBase;

public class TestNGSampleTest4 extends TestNGTestBase {
  
  private static Logger LOG = LoggerFactory.getLogger(TestNGSampleTest4.class);

  @Test
  public void sampleTest() {
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
  }

}
