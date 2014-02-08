package ru.confetqa;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners({TestNGTestBase.LogListener.class})
public class TestNGTestBase {

  private static Logger LOG = LoggerFactory.getLogger(TestNGTestBase.class);

  protected RemoteWebDriver driver;
  
  @BeforeMethod
  public void initDriver() {
    LOG.debug("Starting Firefox");
    driver = new FirefoxDriver();
    LOG.debug("Firefox started");
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @AfterMethod
  public void stopDriver() {
    driver.quit();
  }
  
  public static class LogListener implements IInvokedMethodListener {

    @Override
    public void afterInvocation(IInvokedMethod m, ITestResult res) {
      LOG.info("<<< @Test " + m);
    }

    @Override
    public void beforeInvocation(IInvokedMethod m, ITestResult res) {
      LOG.info(">>> @Test " + m);
    }

  }

}
