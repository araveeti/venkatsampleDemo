package com.scotiabank.fwk.pageobjects;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

/**
 * Page Object for RBC CUrrency COnversion Page
 *
 */
public class RbcCurrencyConverterPage 
{
	private WebDriver driver;
	public static String URL = "";

	@FindBy(how = How.ID, using = "currency-have-unit")
	@CacheLookup
	private WebElement dropDownFromCurrency;
	
	@FindBy(how = How.ID, using = "currency-have-amount")
	@CacheLookup
	private WebElement inputCurrencyHave;
	
	
	
	@FindBy(xpath="//button[@class='cash-switch']")
	private WebElement btnChangeCurrency;
	
	@FindBy(xpath="//input[@id='currency-want-amount']")
	private WebElement labelOutPutCurrency;
	
	@FindBy(how = How.ID, using = "noncash-currencyHaveRate")
	private WebElement labelOriginalValue;
	
	
	
	public  final String dummyValue = "//div[@class='menu transition visible']/div[@data-value='DUMMY']";
	
	
	//Constructor to initialize driver instance

	public RbcCurrencyConverterPage(WebDriver driver) {
		this.driver = driver;
	}
	
	

	/**
	 * Sets the value of the quoteCurrency input box. Once text is entered into
	 * the box, the narrowed down result is clicked on.
	 * 
	 * @param currency
	 * @throws InterruptedException 
	 */
	public void setCurrencyIHave(String currency) throws InterruptedException {
		WebElement quoteCurrencyDropdown = driver.findElement(By.id("currency-have-unit"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", quoteCurrencyDropdown);		
		By ActualLocator = By.xpath(dummyValue.replaceAll("DUMMY", currency));
		waitForElementAndClick(ActualLocator);	
			}
	
	public void waitForElementAndClick(By aBy) {
		try {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement element = wait.until(
		ExpectedConditions.visibilityOfElementLocated(aBy));
		ExpectedConditions.elementToBeClickable(aBy);	
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
	
		}
		catch(Exception e)  {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the value of the baseCurrency input box. Once text is entered into
	 * the box, the narrowed down result is clicked on.
	 * 
	 * @param currency
	 */
	public void setCurrencyIWant(String currency) {
		
		WebElement quoteCurrencyDropdown = driver.findElement(By.id("currency-want-unit"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", quoteCurrencyDropdown);		
		By ActualLocator = By.xpath(dummyValue.replaceAll("DUMMY", currency));
		waitForElementAndClick(ActualLocator);			
	}

	public void setInputAmount(String input) throws InterruptedException {
		System.out.println("Input is::" +input);
		((JavascriptExecutor)driver).executeScript("return document.getElementById('currency-have-amount').value="+input);
		Thread.sleep(2000);
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", btnChangeCurrency);
		Thread.sleep(2000);

	}
	
	

	/**
	 * Validate the Conversion rates
	 * 
	 * @param input
	 */
	public void setInputAmountToSell(String input) {
		
	}

	/**
	 * Gets the info details
	 * 
	 * @return
	 */
	public String getValueAfterConversion() {
		return (String) ((JavascriptExecutor)driver).executeScript("return document.getElementById('currency-want-amount').value");
	}

	/**
	 * Get the Original Value
	 * 
	 * @return
	 */
	public String getOriginalValue() {
		System.out.println("reddy" +labelOriginalValue.getText());
		return labelOriginalValue.getText();
	}
	
	public boolean  calculateAmount(String amountFrom) {
		boolean isResult  = false;
		System.out.println("original"+amountFrom);
		float charge = Float.parseFloat(getOriginalValue());
		float amount = Float.parseFloat(amountFrom);
		
				String text = getValueAfterConversion().replaceAll("[^\\d.]+|\\.(?!\\d)", "");
				float textValue = Float.parseFloat(text);
		
		 final float THRESHOLD = (float) 0.005;
		 		
		float f =  amount*charge;
		
		System.out.println("Actual Result" +f);
		
		System.out.println("Expected Result" +textValue);

		
		if (Math.abs(textValue - f ) < THRESHOLD) {
			System.out.println("Equal");
			isResult = true;
					}
		else {
			isResult = false;
		}
		return isResult;
		}
		

	


}
