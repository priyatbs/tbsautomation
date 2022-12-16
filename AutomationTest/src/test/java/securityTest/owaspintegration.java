/*
 * ScriptName : ZAP Integration
 * Description : Security Testing using selenium
 * Created By : Priyadarshini
 * Created On : 14/12/2022
 * Version : 0.1
*/
package securityTest;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import io.github.bonigarcia.wdm.WebDriverManager;

public class owaspintegration {

	static final String ZAP_PROXY_ADDRESS = "localhost";
	static final int ZAP_PROXY_PORT = 8080;
	static final String ZAP_API_KEY = "3aqi9b6rvvt2vlp2dtnem02avr";

	private WebDriver driver;
	private ClientApi api;
/***************************************************Setting up proxy*************************************************/
	@BeforeMethod
	public void setup() {
		String proxyServerUrl = ZAP_PROXY_ADDRESS + ":" + ZAP_PROXY_PORT;
		Proxy proxy = new Proxy();
		proxy.setHttpProxy(proxyServerUrl);
		proxy.setSslProxy(proxyServerUrl);
		ChromeOptions co = new ChromeOptions();
		co.setAcceptInsecureCerts(true);
		co.setProxy(proxy);
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(co);
		api = new ClientApi(ZAP_PROXY_ADDRESS, ZAP_PROXY_PORT, ZAP_API_KEY);

	}
/*************************************************Launching url*******************************************************/
	@Test
	public void launchUrl() {
		driver.get("https://www.flipkart.com");
		/*
		 * WebElement username = driver.findElement(By.id("userName")); WebElement pwd =
		 * driver.findElement(By.id("password")); WebElement loginbtn =
		 * driver.findElement(By.id("submit"));
		 * username.sendKeys(prop.getProperty("username"));
		 * pwd.sendKeys(prop.getProperty("password")); loginbtn.click();
		 * 
		 */
}

/***************************************************Report Generation*********************************************************/
	@AfterMethod

	public void tearDown() {
		if (api != null) {
			String title = "Flipkart ZAP Security Report";
			String template = "traditional-html";
			String description = " This is flipkart zap security test";
			String reportfilename = "flipkart-zap-report.html";
			String targetFolder = System.getProperty("user.dir");
			try {

				String application_base_url = "https://www.flipkart.com";
				byte[] bytes = api.core.htmlreport();
				ApiResponse messages = api.core.messages(application_base_url, "0", "99999999");
				System.out.println(messages);
				String str = new String(bytes, StandardCharsets.UTF_8);
				File newTextFile = new File("report.html");
				FileWriter fw = new FileWriter(newTextFile);
				fw.write(str);
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			driver.quit();
		}

	}
}
