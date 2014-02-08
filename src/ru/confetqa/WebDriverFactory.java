package ru.confetqa;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opera.core.systems.OperaDriver;

public class WebDriverFactory {

  private static Logger LOG = LoggerFactory.getLogger(WebDriverFactory.class);

  // Factory settings

  private static String defaultHub = null; // change to
                                           // "http://myserver:4444/wd/hub" to
                                           // use remote webdriver by default
  private static int restartFrequency = Integer.MAX_VALUE;

  public static void setDefaultHub(String newDefaultHub) {
    defaultHub = newDefaultHub;
  }

  public static void setRestartFrequency(int newRestartFrequency) {
    restartFrequency = newRestartFrequency;
  }

  // Factory

  private static String key = null;
  private static int count = 0;
  private static TracingWebDriver driver;

  public static WebDriver getDriver(String hub, Capabilities capabilities) {
    String newKey = capabilities.toString() + ":" + hub;
    LOG.debug("Requested driver: " + newKey);
    count++;
    // 1. WebDriver instance is not created yet
    if (driver == null) {
      LOG.debug("No previous driver found");
      return newWebDriver(hub, capabilities);
    }
    // 2. Different flavour of WebDriver is required
    if (!newKey.equals(key)) {
      LOG.debug("Replacing driver " + key + " with a new one " + newKey);
      dismissDriver();
      key = newKey;
      return newWebDriver(hub, capabilities);
    }
    // 3. Browser is dead
    try {
      driver.getCurrentUrl();
    } catch (Throwable t) {
      LOG.debug("Browser is dead, starting a new one");
      t.printStackTrace();
      return newWebDriver(hub, capabilities);
    }
    // 4. It's time to restart
    if (count >= restartFrequency) {
      LOG.debug("Restarting driver after " + count + " uses");
      dismissDriver();
      return newWebDriver(hub, capabilities);
    }
    // 5. Just use existing WebDriver instance
    return driver;
  }

  public static WebDriver getDriver(Capabilities capabilities) {
    return getDriver(defaultHub, capabilities);
  }

  public static void dismissDriver() {
    if (driver != null) {
      LOG.debug("Killing driver " + driver.getWrappedDriver());
      try {
        driver.quit();
      } catch (Throwable t) {
        LOG.warn("Driver killing error", t);
      } finally {
        driver = null;
        key = null;
      }
    }
  }

  // Factory internals

  private static WebDriver newWebDriver(String hub, Capabilities capabilities) {
    RemoteWebDriver internalDriver = (hub == null)
        ? createLocalDriver(capabilities)
        : createRemoteDriver(hub, capabilities);
    key = capabilities.toString() + ":" + hub;
    count = 0;
    LOG.debug("Driver started with capabilities " + internalDriver.getCapabilities()
        + ", session id = " + internalDriver.getSessionId());
    driver = new TracingWebDriver(internalDriver);
    return driver;
  }

  private static RemoteWebDriver createRemoteDriver(String hub,
      Capabilities capabilities) {
    try {
      return new RemoteWebDriver(new URL(hub), capabilities);
    } catch (MalformedURLException e) {
      LOG.error("Could not start remote driver", e);
      throw new Error("Could not start remote driver", e);
    }
  }

  private static RemoteWebDriver createLocalDriver(Capabilities capabilities) {
    String browserType = capabilities.getBrowserName();
    try {
      if (browserType.equals("firefox"))
        return new FirefoxDriver(capabilities);
      if (browserType.startsWith("internet explorer"))
        return new InternetExplorerDriver(capabilities);
      if (browserType.equals("chrome"))
        return new ChromeDriver(capabilities);
      if (browserType.equals("opera"))
        return new OperaDriver(capabilities);
    } catch (Throwable e) {
      LOG.error("Could not start local driver", e);
      throw new Error("Could not start local driver", e);
    }
    LOG.error("Unrecognized browser type: " + browserType);
    throw new Error("Unrecognized browser type: " + browserType);
  }

  static {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        dismissDriver();
      }
    });
  }
}
