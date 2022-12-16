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

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import Utilities.SetUp;
import io.github.bonigarcia.wdm.WebDriverManager;

public class zapScript extends SetUp {
	static final String ZAP_PROXY_ADDRESS = "localhost";
	static final int ZAP_PROXY_PORT = 8080;
	static final String ZAP_API_KEY = "3aqi9b6rvvt2vlp2dtnem02avr";
	public WebDriver driver;
	public ClientApi api;

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

	@Test
	public void amazonSecurityTest() {

		driver.get("https://www.flipkart.com");
		/*
		 * WebElement username = driver.findElement(By.id("userName")); WebElement pwd =
		 * driver.findElement(By.id("password")); WebElement loginbtn =
		 * driver.findElement(By.id("submit"));
		 * username.sendKeys(prop.getProperty("username"));
		 * pwd.sendKeys(prop.getProperty("password")); loginbtn.click();
		 * 
		 */

		// Assert.assertTrue(driver.getTitle().contains("Flipkart"));

	}

	@AfterTest
	public void tearDown() throws ClientApiException {
		if (api != null) {
			System.out.println("Testing api");
			String title = "Amazon ZAP Securiy Report";
			String template = "traditional -html";
			String description = "This is amazon ZAP Testing Report";
			String reportfilename = "amazon-zap-report.html";
			String TARGET = System.getProperty("user.dir");
			try {
				api.pscan.enableAllScanners();
				ApiResponse response = api.pscan.recordsToScan();
				while (!response.toString().equals("0")) {
					response = api.pscan.recordsToScan();
				}
			} catch (ClientApiException e1) {
				e1.printStackTrace();
			}
			System.out.println("---Passive scan completed!----");

			/*************************************************************
			                         Active Scan
			 *************************************************************/

			String application_base_url = "https://www.flipkart.com";
			System.out.println("Active scan : " + application_base_url);
			try {

				ApiResponse resp = api.ascan.scan(application_base_url, "True", "False", null, null, null);

				int progress;

				String scanid = ((ApiResponseElement) resp).getValue();

				while (true) {
					Thread.sleep(5000);
					progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanid)).getValue());
					System.out.println("Active Scan progress : " + progress + "%");
					if (progress >= 100) {
						break;
					}
				}
				System.out.println("Active Scan complete");
			} catch (Exception e) {
				e.printStackTrace();
			}

			/************************************************************
			                      Spider scan
			 ****************************************************************/
			System.out.println("Spider : " + application_base_url);
			try {

				ApiResponse resp = api.spider.scan(TARGET, null, null, null, null);
				int progress;
				String scanid = ((ApiResponseElement) resp).getValue();
				while (true) {
					Thread.sleep(1000);
					progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanid)).getValue());
					System.out.println("Spider progress : " + progress + "%");
					if (progress >= 100) {
						break;
					}
				}
				System.out.println("Spider complete");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		/***********************************************************************
		                         Report Generation
		 ***********************************************************************/
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
	}

}
