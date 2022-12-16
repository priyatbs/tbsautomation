package Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.jdbc.core.JdbcTemplate;

public class SetUp {
	public static WebDriver driver;
	public static Properties prop;
	public static JdbcTemplate jdbcTemplate;
	public static TakesScreenshot webdriver;
	public static String fileWithPath;
	

	public static void setProperties() {
		prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream("config.properties");
		try {
			prop.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}}