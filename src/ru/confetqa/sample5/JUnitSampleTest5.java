package ru.confetqa.sample5;


import org.junit.Test;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.confetqa.JUnitTestBase2;

public class JUnitSampleTest5 extends JUnitTestBase2 {
  
  private static Logger LOG = LoggerFactory.getLogger(JUnitSampleTest5.class);

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
