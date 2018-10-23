/**
 * @author Venkat
 */
package com.scotiabank.fwk.tests;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.scotiabank.fwk.utils.ExcelUtils;
import com.scotiabank.fwk.pageobjects.RbcCurrencyConverterPage;

/**
 * 
 * @author Venkat
 *
 */
public class RbcCurrencyConverterTestDemo {
	private static final String PROPERTIES_FILE = "resources/Environment.properties";
	private static final String XLS_FILE = "resources/oanda.xls";
	private static WebDriver driver;
	private static String baseUrl;
	private static com.scotiabank.fwk.pageobjects.RbcCurrencyConverterPage currencyConverterPage;

	private static Vector<RbcCurrencyTestDataPojo> currencyConversionTestVector;
	

	// Define ExcelSheet Columns
	private enum CurrencyInfoColumns {
		INPUT_CURRENCY, WANTED_CURRENCY, INPUT_AMOUNT
	}

	@Parameters({"browser"})
	@BeforeClass
	public static void setUp(String aBrowser) throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream(PROPERTIES_FILE));
		System.out.println(findOS());
		
		if(findOS().toLowerCase().contains("mac")) {
			if(aBrowser.toLowerCase().contains("chrome")) {
		
		System.setProperty("webdriver.chrome.driver", getBaseDir()+File.separator+"drivers"+File.separator+"chromedriver 2");
		driver = new ChromeDriver();
		}
			
		else if(aBrowser.toLowerCase().contains("firefox")) {
				
				System.setProperty("webdriver.gecko.driver", getBaseDir()+File.separator+"drivers"+File.separator+"geckodriver");
				driver = new FirefoxDriver();
				}
			
		}
		else if(findOS().toLowerCase().contains("win")) {
			if(aBrowser.toLowerCase().contains("chrome")) {
		
		System.setProperty("webdriver.chrome.driver", getBaseDir()+File.separator+"drivers"+File.separator+"chromedriver.exe");
		driver = new ChromeDriver();

		}
		else if(aBrowser.toLowerCase().contains("firefox")) {
				
				System.setProperty("webdriver.gecko.driver", getBaseDir()+File.separator+"drivers"+File.separator+"geckodriver.exe");
				driver = new FirefoxDriver();

				}
			
		}
		
		baseUrl = props.getProperty("url");
		driver.manage().timeouts().implicitlyWait(65, TimeUnit.SECONDS);
		currencyConverterPage = PageFactory.initElements(driver,
				RbcCurrencyConverterPage.class);
		driver.navigate().to(baseUrl + RbcCurrencyConverterPage.URL);
		driver.manage().window().maximize();

	}
	
	public static String findOS() {
		return System.getProperty("os.name");
	}
	
	public static String getBaseDir() {
		return System.getProperty("user.dir");
	}

	/**
	 * Read all the test input data from the excel sheet.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void readTestData() throws Exception {
		currencyConversionTestVector = new Vector<RbcCurrencyTestDataPojo>();
		// Read Excel Test Data
		List<List<HSSFCell>> testData = ExcelUtils.readDataFromFile(XLS_FILE);
		HSSFCell cell;
		for (int row = 1; row < testData.size(); row++) {
			List<?> list = testData.get(row);
			cell = (HSSFCell) list.get(CurrencyInfoColumns.INPUT_CURRENCY
					.ordinal());
			String inputCurrency = cell.getStringCellValue();

			cell = (HSSFCell) list.get(CurrencyInfoColumns.WANTED_CURRENCY
					.ordinal());
			String wantedCurrency = cell.getStringCellValue();

			String inputAmountString = "";
			cell = (HSSFCell) list.get(CurrencyInfoColumns.INPUT_AMOUNT
					.ordinal());
			
			
			if (cell.getCellType() == CellType.NUMERIC) {
				inputAmountString = "" + cell.getNumericCellValue();
			} else if (cell.getCellType() == CellType.STRING) {
				inputAmountString = cell.getStringCellValue();
			}

			
			RbcCurrencyTestDataPojo currencyTestData = new RbcCurrencyTestDataPojo(
					inputCurrency, wantedCurrency, inputAmountString);
			currencyConversionTestVector.addElement(currencyTestData);
		}
 }

	@AfterClass
	public static void tearDown() throws Exception {
		System.out.println("tearDown");
		driver.quit();
	}

	@Test
	public void testRbcCurrencyConversions() throws InterruptedException,
			IOException {

		for (RbcCurrencyTestDataPojo t : currencyConversionTestVector) {
			currencyConverterPage.setCurrencyIHave(t.getInputCurrency());
			currencyConverterPage.setCurrencyIWant(t.getWantedCurrency());
			currencyConverterPage.setInputAmount(t.getInputAmountString());	
			Assert.assertTrue(currencyConverterPage.calculateAmount(t.getInputAmountString()));

		}
	}
	
}

/**
 * POJO  for getting and setting the Values.
 * 
 * @author Venkat
 *
 */
class RbcCurrencyTestDataPojo {
	private String inputCurrency;
	private String wantedCurrency;
	private String inputAmountString;

	public RbcCurrencyTestDataPojo(String inputCurrency, String wantedCurrency,
			String inputAmountWanted) {
		this.inputCurrency = inputCurrency;
		this.wantedCurrency = wantedCurrency;
		this.inputAmountString = inputAmountWanted;
	}

	// Setters and Getters
	public String getInputCurrency() {
		return inputCurrency;
	}

	

	public String getWantedCurrency() {
		return wantedCurrency;
	}

	

	public String getInputAmountString() {
		return inputAmountString;
	}

}
