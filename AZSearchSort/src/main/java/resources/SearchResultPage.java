package resources;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchResultPage extends CommonModules {
		WebElement chkBox, optionLink;
		Logger log = LogManager.getLogger(SearchResultPage.class.getName());
		public SearchResultPage (WebDriver driver) {
			this.driver = driver;
		}
	
		private By resultList = By.xpath("//span[@data-component-type='s-search-results']");
		private By resultItems = By.xpath("//div[contains(@data-component-type,'s-search-result')]");
		
		
		private String resultItemString = "//div[contains(@data-component-type,'s-search-result')]";
		private String resultItemNameString = "//span[@class='a-size-base-plus a-color-base a-text-normal']";
		private String resultItemPriceString = "//span[@class='a-offscreen']";
		private String resItemAvgCustRev ="//span[@class='a-icon-alt']";
		private String resItemDelDateString = "//span[contains(@aria-label,'Get it')]";
		private By resultItemName; 
		private By resultItemPrice; 
		private By resultItemDelDate;  
		
		
		private By deliveryOptions = By.id("deliveryRefinements");
		private By payodChkBox = By.xpath("//li[@aria-label='Eligible for Pay On Delivery'] //input[@type='checkbox'][1]");
		private By getItToday = By.xpath("//li[contains(@aria-label,'Get It Today')] //span[contains(text(),'Get It Today')]");
		private By getItTodayChkbox = By.xpath("//li[contains(@aria-label,'Get It Today')] //input[@type='checkbox']");
		private By getItTomorrow = By.xpath("//li[contains(@aria-label,'Get It by Tomorrow')] //span[contains(text(),'Get It by Tomorrow')]");
		private By getItTomorrowChkbox = By.xpath("//li[contains(@aria-label,'Get It by Tomorrow')] //input[@type='checkbox']");
		private By getIn2Days = By.xpath("//li[contains(@aria-label,'Get It in 2 Days')] //span[contains(text(),'Get It in 2 Days')]");
		private By getIn2DaysChkbox = By.xpath("//li[contains(@aria-label,'Get It in 2 Days')] //input[@type='checkbox']");
		private By clearDelOption = By.xpath("//span[@class='a-list-item'] //span[contains(text(),'Clear')]");
		private By minPrice = By.xpath("//input[@id='low-price']");
		private By maxPrice = By.xpath("//input[@id='high-price']");
		private By goButton = By.xpath("//input[@class='a-button-input']");
		
		private By sortSelect = By.id("s-result-sort-select");
		private By sortByList = By.xpath("//div[contains(@class,'a-popover a-dropdown') and @aria-hidden='false']");
		private By sortPriceLowToHigh = By.xpath("//div[contains(@class,'a-popover a-dropdown') and @aria-hidden='false'] //a[contains(@data-value,'price-asc-rank')]");
		private By sortPriceHighToLow = By.xpath("//div[contains(@class,'a-popover a-dropdown') and @aria-hidden='false'] //a[contains(@data-value,'price-desc-rank')]");
		private By sortAvgCustReview = By.xpath("//div[contains(@class,'a-popover a-dropdown') and @aria-hidden='false'] //a[contains(@data-value,'review-rank')]");
		private By sortByNewArrival = By.xpath("//div[contains(@class,'a-popover a-dropdown') and @aria-hidden='false'] //a[contains(@data-value,'date-desc-rank')]");
		
		private By pageSelect = By.className("a-pagination");
		private By numberOfPages = By.xpath("//ul[@class='a-pagination'] //*");
		private By currentPage = By.xpath(("//li[@class='a-selected']"));
		private By selectablePageText = By.xpath("//li[@class='a-normal'] //a");
		
	public WebElement resultList() {
			return driver.findElement(resultList);
	}
	//Returns the count of 'search result page' to check if it's is displayed 
	public Integer resultListCheckDisplay() {
		return driver.findElements(resultList).size();
	}
	//Returns the count of search result items 
	public List<WebElement> resultItems() {
		return driver.findElements(resultItems);
	}
	//Retrieve name of the search result item where its index is the parameter 
	public String resultItemName (Integer index) {
		String resultItemNameXPath =  resultItemString+"["+index+"]"+resultItemNameString;
		resultItemName= By.xpath(resultItemNameXPath);
		return driver.findElement(resultItemName).getText();
	}
	//Retrieve price of an search result item using index of the item in the page 
	public String resultItemPrice (Integer index) {
		String resultItemPriceXPath = "("+resultItemString+"["+index+"]"+resultItemPriceString+")[1]";
		resultItemPrice = By.xpath(resultItemPriceXPath);
		if(driver.findElements(resultItemPrice).size()>0) {
			return driver.findElement(resultItemPrice).getAttribute("textContent");
		} else {
			return "INVALID";
		}
	}
	
	public WebElement deliveryOptions() {
		return driver.findElement(deliveryOptions);
	}
	public Integer getItTodayIsPresent() {
		return driver.findElements(getItToday).size();
	}
	public Integer getItTomorrowIsPresent() {
		return driver.findElements(getItTomorrow).size();
	}
	public Integer getIn2DaysIsPresent() {
		return driver.findElements(getIn2Days).size();
	}
	public void setMinPriceValue(String price) {
		driver.findElement(minPrice).sendKeys(price);
	}
	public void setMaxPriceValue(String price) {
		 driver.findElement(maxPrice).sendKeys(price);;
	}
	public void clickGoButton() {
		 driver.findElement(goButton).click();
	}
	public WebElement sortSelect() {
		return driver.findElement(sortSelect);
	}
	//Click on the SortBy list and select options using Javascript Executor because the list items are unable to select by using 'Select' selenium commands 
	public void selectSortItem() {
		WebElement sortSelectElem = driver.findElement(sortSelect);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		for(int i=1; i<=2; i++) {
			js.executeScript("arguments[0].click();", sortSelectElem);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (driver.findElements(sortByList).size() > 0) {
				break;
			} 
		}
	}
	public void sortPriceLowToHigh() {
		selectSortItem();
		driver.findElement(sortPriceLowToHigh).click();
	}
	public void sortPriceHighToLow() {
		selectSortItem();
		driver.findElement(sortPriceHighToLow).click();
	}
	public void sortAvgCustReview() {
		selectSortItem();
		driver.findElement(sortAvgCustReview).click();
	}
	public void sortByNewArrival() {
		selectSortItem();
		driver.findElement(sortByNewArrival).click();
	}
	public Integer pageSelectCheck() {
		return driver.findElements(pageSelect).size();
	}
	//Find the no. of search result pages.
	public Integer findNoOfPages() {
		Integer noOfPages = 0;
		String pageNoText;
		try {
			//Find total no. of search result pages. 
			if(pageSelectCheck()>0) {
				log.info("Search results are multiple pages.Finding no. of pages");
				List<WebElement> pages = driver.findElements(numberOfPages);

				for (int i = pages.size() - 1; i >= 0; i--) {
					pageNoText = pages.get(i).getText();
					if (pageNoText.matches("[0-9]+")) {
						noOfPages = Integer.parseInt(pageNoText);
						break;
					}
				}
			}else {
				log.info("Page no. selection option is not available. Looks like search result is only one page.");
				noOfPages = 1;
			}
		} catch (Exception e ) {
			log.error("Unable to find no. of search result pages. Cannot continue. Moving to next test case."); 
		}
		return noOfPages;
	}
	//set Min and Max price values in the search options
	public void setMinMaxValues (String minValue, String maxValue) {
		setMinPriceValue(minValue);
		setMaxPriceValue(maxValue);
		clickGoButton();
		waitForPageLoaded();

	}
	//Get currently selected search result page no.
	public Integer getCurrentPageNo() {
		Integer currentPageNo = Integer.parseInt(driver.findElement(currentPage).getText());
		 return currentPageNo;
	}
	//Select a particular search result page by its page number.
	public Boolean selectPageNo (Integer pgNo) {
		List <WebElement> SelectablePages =  driver.findElements(selectablePageText);
		for(int i=0;i<=SelectablePages.size();i++) {
			if( Integer.parseInt(SelectablePages.get(i).getText()) == pgNo) {
				try {
					SelectablePages.get(i).click();
					log.info("Clicked page no.: "+pgNo);
				} catch(Exception e) {
					log.error("Unable to select page no.: "+pgNo);
				}
				return true;
			}
		}
		return false;
	}
	//Retrieve Avg. customer review displayed at every search result item
	public String getAvgCustReview(Integer index) {
		String custRevXpath =  resultItemString+"["+index+"]"+resItemAvgCustRev;
		By custRev = By.xpath(custRevXpath);
		String custRevString =	driver.findElement(custRev).getAttribute("innerHTML");
		String rating = custRevString.substring(0, 3);
		return rating;
	}
	//Select Pay On Delivery option
	public void selectpayodChkBox() {
		if(!(driver.findElement(payodChkBox).isSelected())) {
			driver.findElement(payodChkBox).click();
		}
	}
	//Check Clear -> under Delivery options is present.
	public Integer clearIsPresent() {
		return driver.findElements(clearDelOption).size();
	}
	//Select Clear -> under Delivery options.
	public void selectClearDelOption() {
		if(clearIsPresent()<=0) {
			log.error("Clear button is not present. Unable to clear selected delivery option. Moving to next case.");
			return;
		}
		driver.findElement(clearDelOption).click();
		waitForPageLoaded();
	}
	//Select delivery options checkbox
	public void selectChkBox(String option) {
		WebElement optionLink;
		Boolean status;
		switch(option) {
			case "today":
				status =  driver.findElement(getItTodayChkbox).isSelected();
				optionLink = driver.findElement(getItToday);
			break;
			case "tomorrow":
				status =  driver.findElement(getItTomorrowChkbox).isSelected();
				optionLink = driver.findElement(getItTomorrow);
			break;
			case "twoDays":
				status =  driver.findElement(getIn2DaysChkbox).isSelected();
				optionLink = driver.findElement(getIn2Days);
			break;
			default:
				log.info("Invalid entry. Cannot select checkBox.");
				return;
		}

		if(!status) {
			optionLink.click();
			waitForPageLoaded();
		} else {
			log.info("The checkBox is already selected. Cannot select now.");
		}
	}
	//Check if delivery Date is present in search result item 
	public Integer delDateIsPresent (Integer index) {
		String resultItemDelDateXPath =  resultItemString+"["+index+"]"+resItemDelDateString;
		resultItemDelDate= By.xpath(resultItemDelDateXPath);
		return driver.findElements(resultItemDelDate).size();
	}
	//Retrieve delivery date from search result item and return a Hashmap with key values day,date and month.
	public HashMap<String,String> resultItemDeliveryDate (Integer index) {
		String resultItemDelDateXPath =  resultItemString+"["+index+"]"+resItemDelDateString;
		resultItemDelDate= By.xpath(resultItemDelDateXPath);
		
			String delDateString =  driver.findElement(resultItemDelDate).getText();
			String[] delDateArr;
			if(delDateString.contains("Get it by")) {
				delDateArr = delDateString.split("Get it by");
			} else {
				delDateArr = delDateString.split("Get it");
			}
			String delDate = delDateArr[1].trim();
			
			
			HashMap<String,String> hmDate = getDay(delDate);
			return hmDate;
			
	}
	//Convert input date into a Hashmap with keys day,date and month
	public  HashMap<String,String> getDay(String str) {
		String day, date, month;
		if (str.contains("-")) {
			str = (str.split("-"))[1];
		}

		String[] arr = str.split(",");
		HashMap<String, String> hmDate = new HashMap<String, String>();
		hmDate.put("day", arr[0].trim());

		String[] arr1 = arr[1].trim().split(" ");
		hmDate.put("month", arr1[0].trim());
		hmDate.put("date", arr1[1].trim());
		return hmDate;
	}
	
}
