package ru.confetqa;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JUnitTestBase2 {

  private static Logger LOG = LoggerFactory.getLogger(JUnitTestBase2.class);
  
  protected WebDriver driver;
  
  @Rule
  public TestRule startRule = new TestWatcher() {

    @Override
    protected void starting(Description description) {
      super.starting(description);
      driver = WebDriverFactory.getDriver(DesiredCapabilities.firefox());
    }
  };
  
  @ClassRule
  public static TestRule stopDriverRule = new ExternalResource() {

    @Override
    protected void after() {
      super.after();
      WebDriverFactory.dismissDriver();
    }
  };
  
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
