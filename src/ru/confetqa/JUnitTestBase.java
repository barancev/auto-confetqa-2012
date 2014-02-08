package ru.confetqa;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JUnitTestBase {

  private static Logger LOG = LoggerFactory.getLogger(JUnitTestBase.class);
  
  protected WebDriver driver;

  @Before
  public void initDriver() {
    LOG.debug("Starting Firefox");
    //driver = new FirefoxDriver();
    driver = new TracingWebDriver(new FirefoxDriver());
    //driver.setLogLevel(Level.INFO);
    LOG.debug("Firefox started");
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @After
  public void stopDriver() {
    driver.quit();
  }

  @Rule
  public TestRule logRule = new TestWatcher() {
    
    @Override
    protected void starting(Description description) {
      super.starting(description);
      LOG.info(">>> @Test " + description);
    }

    @Override
    protected void finished(Description description) {
      super.finished(description);
      LOG.info("<<< @Test " + description);
    }

    @Override
    protected void succeeded(Description description) {
      super.succeeded(description);
    }

    @Override
    protected void failed(Throwable e, Description description) {
      super.failed(e, description);
      LOG.error("!!! @Test failed" + description, e);
    }
  };
}
