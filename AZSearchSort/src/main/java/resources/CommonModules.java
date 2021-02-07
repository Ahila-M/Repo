
package resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
//import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class CommonModules {
	
	private static Logger log= LogManager.getLogger(CommonModules.class.getName());
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected HomePage hp;
	protected SearchResultPage sr;
	protected static HashMap<String, String> inputValues;
	private FileInputStream fis;
	private HSSFWorkbook workbook;
	
	//Declare objects of GetDateDetails -a class to instantiate objects with date details 
	private GetDateDetails todayDet;
	private GetDateDetails tomorrowDet;
	private GetDateDetails twoDaysMoreDet;
	
	//return driver when requested 
	public WebDriver getDriver() {
		return driver;
	}
	//Method to open input data sheet and get test input values 
	@BeforeTest
	@Parameters("ResourcesPath")//Retrieve input files folder location from testng file 
	public void initialization(String ResourcesPath) {
		log.info("Going to read input values from excel sheet.");
		readExcelSheet(ResourcesPath);
	}
	//Method to open URL before executing a class of tests 
	@BeforeClass(alwaysRun=true)
	@Parameters({"ResourcesPath","browserName"})
	public void loadURL(String ResourcesPath,String browserName) {
		//Call openBrowser function to create and get driver details.
		openBrowser(ResourcesPath,browserName);
		//Calling page Object models for initialization of POM class files
		callingPOMs();
		//Open URL to be tested
		openURL();
	}
	//Closing drivers after executing a class of tests
	@AfterClass(alwaysRun=true)
	public void closedrivers() {
		driver.quit();
	}
	
	//get driverPath and browserName as input and open browser and get driver details
	public void openBrowser(String DriverPath,String browserName){		
		try {
			//for Chrome
			if (browserName.equalsIgnoreCase("Chrome")) {
				System.setProperty("webdriver.chrome.driver",DriverPath+"/chromedriver");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--no-sandbox");
				driver = new ChromeDriver(options);
			} 
			//For Firefox
			else if (browserName.equalsIgnoreCase("Firefox")) {
				System.setProperty("webdriver.gecko.driver",DriverPath+"/geckodriver");
				driver = new FirefoxDriver();
			} 
			//for IE
			else if (browserName.equalsIgnoreCase("IE")) {
				System.setProperty("webdriver.ie.driver",DriverPath+"/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			}
			else {//Invalid browser name input scenario
				log.fatal("Provided browser name doesnt match with existing browser drivers. Please check configuration.");
				log.fatal("Cannot continue execution");
				System.exit(1);
			}
			wait = new WebDriverWait(driver,60);
			driver.manage().window().maximize();
		} catch (Exception e) {
			log.catching(e);
			log.fatal("Exception while opening browser. Cannot continue testing. Exiting.");
			System.exit(1);
		}
	}
	//reads excel sheet with sheetpath as input 
	//and creates hashmap of columns InputParameter as Key and InputValues as value of the hashmap.  
	public void readExcelSheet(String ResourcesPath) {
		try {
			// Open the excel sheet for reading
			log.info("Opening the excel sheet for reading.");
			fis = new FileInputStream(ResourcesPath+"/DataSheet.xls");
			// Access Excel sheet
			workbook = new HSSFWorkbook(fis);
			log.info("Input excel sheet opened successfully.");
			
			log.info("In input excel sheet-> access sheet named 'input'.");
			// Open the sheet 'input' from input excel sheet
			HSSFSheet inputsheet = workbook.getSheet("input");
			if (inputsheet != null) {
				getParamValues(inputsheet);
			} else {
				log.debug("Please check if sheet named 'input' is present in the excel sheet. Returned null while opening.");
				closeExcelOnError();
			}
			//Closing excel sheet after reading all values 
			workbook.close();
			fis.close();
		  } 
		catch(FileNotFoundException e) {
			log.fatal("The input excel sheet is not found in the location or could not be opened.Cannot continue. Exiting.");
			log.catching(e);
			closeExcelOnError();
		}
		catch(IOException e)  
        {  
			log.fatal("The input excel sheet cannot be read successfully.Cannot continue. Exiting.");
			log.catching(e);
			closeExcelOnError();
        }      
        catch(Exception e)  
        {   log.debug("Exception during opening and reading input excel sheet. Cannot continue. Exiting.");
        	log.catching(e);
        	closeExcelOnError();
        }     
		
	}
	//Closeout input excel sheet when an error occurs
	public void closeExcelOnError() {
		try {
		log.debug("Unable to continue testing. Hence exiting.");
		workbook.close();
		fis.close();
		System.exit(1);
		} catch (Exception e) {
			log.debug("Exeption while closing input excel sheet.");
			log.catching(e);
		}
		
	}

	//Return the hashmap data from input excel sheet when requested 
	public HashMap<String,String> getexcelValues() {
		return inputValues;
	}
	//Calling PageObjectModel
	public void  callingPOMs() {
		//Calling Page Object Model HomePage of the website
		hp = new HomePage(getDriver());
		//Calling Page Object Model SearchResultPage of the website
		sr = new SearchResultPage(getDriver());
	}
	
	//Open URL to be tested
	public void openURL(){
		
		Boolean result;
		if (driver != null) {
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			try {
				log.info("Opening website");
				driver.get(inputValues.get("URL"));
				Thread.sleep(1000);
				waitForPageLoaded();
				
				String title = driver.getTitle();
				
				log.debug("Checking if the title of the webpage is as expected.");
				
				
				Boolean logodisp = hp.logo()>0;
				
				result = title.contains(inputValues.get("URL-brandName"))&& (logodisp);
				//System.out.println(result);
			}
			catch (Exception e) {
				log.catching(e);
				throw new SkipException("Exception during Opening URL "+inputValues.get("URL")+". Unable to continue the testcase.");
			}
			
			if(result == true){
				log.info("URL: "+inputValues.get("URL")+" opened successfully.");
			} else {
				throw new SkipException("Opening URL "+inputValues.get("URL")+" failed.May not be able to continue testing.");
				
			}
		} else {
			log.debug("driver value is null while executing mehod 'openURL'. Unable to proceed.");
			throw new SkipException("driver value is null while executing method 'openURL'. Unable to proceed.");
		}
	
	}
	//Method to get Screen shot 
	public String getScreenShot(String methodName,WebDriver driver) throws IOException {
		//Getting formatted times tamp
		String timestamp = new SimpleDateFormat("dd-MM-YYYY_HH-mm-ss").format(new Date());
		//Creating dest file name inside logReports folder with method name and time stamp
		String destFileName = System.getProperty("user.dir")+"/logReports/"+methodName+timestamp+".png";
		log.debug("Taking screenshot.");
		try {
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			File dest = new File(destFileName);
			FileUtils.copyFile(src, dest);
			
		} catch (Exception e) {
			log.debug("Exception while taking screenshot. ");
			log.catching(e);
		}
		return destFileName;
	}
	//Read parameter name and parameter values and make it into an HashMap from input excel sheet
	public void getParamValues (HSSFSheet inputsheet) {
		try {
		// Find first and last row
		int startRow = inputsheet.getFirstRowNum();
		int endRow = inputsheet.getLastRowNum();
		int paramColIndex, valueColIndex;
		inputValues = new HashMap<String, String>();

		if (startRow != -1 && endRow != -1) {
			Row firstRow = inputsheet.getRow(startRow);
			if (firstRow != null) {
				// Find column index of the input parameters and their values from the excel
				// sheet
				paramColIndex = getColIndex(inputsheet, firstRow, "InputParameter");
				valueColIndex = getColIndex(inputsheet, firstRow, "InputValues");

				// Forming HashMap with parameters and values retrieved from the excel sheet
				if (paramColIndex != -1 && valueColIndex != -1) {
					// Use parameters of column Index which to create Hashmap inputValues
					for (int r = startRow; r <= endRow; r++) {
						Cell pCell = inputsheet.getRow(r).getCell(paramColIndex);
						Cell vCell = inputsheet.getRow(r).getCell(valueColIndex);

						if (pCell != null && vCell != null) {
							String pData = getCellDataValue(pCell);
							String vData = getCellDataValue(vCell);
							if (pData != null && vData != null) {
								inputValues.put(pData, vData);
							} else {
								log.debug("Reading cell" + pCell + " and/or " + vCell
										+ " has returned null.Please Check.");
								closeExcelOnError();
							}
						} else {
							log.debug("Locating cell in input excel sheet by using row and column indeces returned null. Please check.");
							closeExcelOnError();
						}
					}

				} else {
					log.debug("'InputParameter' and 'InputValues' keys are not available in the first row of the excel sheet.");
					closeExcelOnError();
				}
			} else {
				log.debug("Starting row of the 'input' sheet could not be retrieved.");
				closeExcelOnError();
			}
		} else {
			log.debug("Please check if 'input' sheet contains content. First row returned invalid value. ");
			closeExcelOnError();
		}
		} catch (Exception e) {
			log.debug("Exception during reading values from Input excel sheet.");
			log.catching(e);
			closeExcelOnError();
		}
	}
	//Find if a particular String is present in a given row in the input excel sheet and return the column number  
	public int getColIndex(HSSFSheet inputsheet, Row row, String expValue) {
		int colIndex=-1;
		short maxColIndex = row.getLastCellNum();
		if(maxColIndex!= -1) {
			for (int columnNo = 0; columnNo < maxColIndex; columnNo++) {
				Cell cell = row.getCell(columnNo) ;
				if(cell!=null) {
					String cellValue = getCellDataValue(cell);
					if (cellValue.equalsIgnoreCase(expValue) ){
						colIndex = cell.getColumnIndex();
					}
				}
			}
		}else {
			log.debug("Couldn't retrieve column index. returned invalid value. Unable to continue.");
			closeExcelOnError();
		} 
		return colIndex;
	}
	//Read input data excel sheet data in the corresponding format and return as a String.
	public String getCellDataValue(Cell cell) {
		
		String value;
		switch (cell.getCellType()) {
        case STRING:
            value = cell.getStringCellValue();
            break;
        case NUMERIC:
        	String numValue = Double.toString(cell.getNumericCellValue());
        	value = numValue.split("\\.")[0];
            break;
		default:
			value = null;
			break;
		}
		return value;
	}
	//Reverse the case of String passed as a parameter
	public String reverseFirstCharCase(String string) {
		 
		 if(Character.isLowerCase(string.charAt(0))) {    
	           //Convert it into upper case using toUpperCase() function    
			 String newString = Character.toUpperCase(string.charAt(0))+string.substring(1, string.length());
			 return newString;
	     } else {
	    	 //Convert it into lower case using toUpperCase() function    
	    	   String newString = Character.toLowerCase(string.charAt(0))+string.substring(1, string.length());
	    	   return newString;
		}
	}
	//Wait for webpage to be reloaded completely before continuing with further test steps
	public void waitForPageLoaded() {
		try {
		log.debug("Waiting for page to be loaded.");
		WebDriverWait waittoReady = new WebDriverWait(driver,60);
		waittoReady.until(driver -> ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
		} catch (Exception e) {
			log.debug("Exception while waiting for Page Load Request to complete.");
			log.catching(e);
		}
	}
	//Get and Compare Delivery date displayed in the result item with the delivery option (Today,Tomorrow or TwoDays) you have chosen
	public boolean getCompDelDate(Integer noOfPages, String param) {
		Integer totalSearchItems = 0, matchCounter=0; //Counters to decide whether test case passed or failed
		Boolean resultflag=false;
		try {
			if (noOfPages != 0) {
				for (int n = 1; n <= noOfPages; n++) {
					Integer currPage = sr.getCurrentPageNo();
					if(currPage!=0) {
						if (n != currPage) {
							// select next page.
							if (sr.selectPageNo(n)) {
								log.info("Successfully found and clicked the page no:" + n);
							}
						}
					}else {
						log.debug("Current page number is returned as '0'. Couldn't find it correctly. Exiting testcase. ");
						Assert.fail();
					}
					log.info("Retrieve names of items displayed in the search result page: " + n);
					// Part1 get the list of items displayed in the search result page.
					List<WebElement> resultItems = sr.resultItems();
					
					//Add no.of search result items to find total no. of search items across all pages.
					totalSearchItems = totalSearchItems + resultItems.size();
					switch(param) {
					  case "TodayDelivery":
						  for (int i = 1; i <= resultItems.size(); i++) {
								if(sr.delDateIsPresent(i) > 0) {
									HashMap<String,String> hmDate = sr.resultItemDeliveryDate(i);
									if (compareDelDate(hmDate,"today")) {
										//log.info("Delivery date values match with selected "+param+" option for search result item."+ sr.resultItemName(i));
										matchCounter++;
									}else {
										log.error("Delivery date values does not match with selected option"+param+" option for search result item."+ sr.resultItemName(i));
									}
								} else {
									log.info("Delivery date details for search item " + sr.resultItemName(i) + "is not displayed.");
								}
							}
						  break;  
					  case "TwoDaysDelivery":
						for (int i = 1; i <= resultItems.size(); i++) {
							if(sr.delDateIsPresent(i) > 0) {
								HashMap<String,String> hmDate = sr.resultItemDeliveryDate(i);
								if (compareDelDate(hmDate,"twoDaysMore")) {
									//log.info("Delivery date values match with selected "+param+" option for search result item."+ sr.resultItemName(i));
									matchCounter++;
								}else {
									log.error("Delivery date values does not match with selected option"+param+" option for search result item."+ sr.resultItemName(i));
								}
							} else {
								log.info("Delivery date details for search item " + sr.resultItemName(i) + "is not displayed.");
							}
						}
					  break;
					  case "TomorrowDelivery":
						for (int i = 1; i <= resultItems.size(); i++) {
	
							if(sr.delDateIsPresent(i) > 0) {
								HashMap<String,String> hmDate = sr.resultItemDeliveryDate(i);
								
								if (compareDelDate(hmDate,"tomorrow")) {
									//log.info("Delivery date values match with selected "+param+" option for search result item."+ sr.resultItemName(i));
									matchCounter++;
									
								}else {
									log.error("Delivery date values does not match with selected "+param+"  option for search result item."+ sr.resultItemName(i));
								}
								
							} else {
								log.info("Delivery date details for search item " + sr.resultItemName(i) + "is not displayed.");
							}
						}
					  break;
					}
				}
			}
			log.info(matchCounter+" "+totalSearchItems);
			if(totalSearchItems>0) {
				Double dataMatchPercent = (double) ((matchCounter*100)/totalSearchItems);
				
				if(dataMatchPercent>=90) {
					log.info("Delivery date values match with selected option for "+dataMatchPercent+"% of search result items.");
					resultflag = true;
				} else {
					log.error("Delivery date values match with selected option only for "+dataMatchPercent+"% of search result items.");
					resultflag = false;
				}
			} else {
				log.error("zero search result items displayed in the search result page. Cannot check delivery date details.");
				resultflag = false;
			}
		} catch (Exception e) {
			log.debug("Exception while executing method getCompDelDate. Moving to next testcase.");
			log.catching(e);
			Assert.fail();
		}
		
		return resultflag;
	}
	//Retrieve search result item and parameters displayed on each of them
	public List<String> getParamFromAllPages (Integer noOfPages, String param) {
		Integer tryCount=0;
		//Initializing list of search result items parameters: 
		List<String> resultItemParam = new ArrayList<String>(); 
		try {
		if (noOfPages > 0) {
			// Retrieve parameters of search result items from each of the search result page
			// and make it in to an array.
			
			for (int n = 1; n <= noOfPages; n++) {
				if (n != sr.getCurrentPageNo()) {
					// select next page.
					if (sr.selectPageNo(n)) {
						log.info("Successfully found and clicked the page no:" + n);
					}
					waitForPageLoaded();
				}
				
				//Verify if search result page is displayed
				while(tryCount<2) {
					try {
						tryCount++;
						wait.until(ExpectedConditions.visibilityOf(sr.resultList()));
						log.info("Search result page displayed successfully.");
					} catch(TimeoutException e) {
						if(tryCount==2) {
							log.error("TimeOut while loading Search result page. May not be able to continue.");
							log.catching(e);
						}
					}
				}
				log.info("Retrieve items displayed in the search result page: " + n);
				// Part1 get the list of items displayed in the search result page.
				List<WebElement> resultItems = sr.resultItems();
				
				switch(param) {
					case "Name": // Part2 Retrieve names of items displayed in the search result page.
						for (int i = 1; i < resultItems.size(); i++) {
							String itemName = sr.resultItemName(i);
							if(itemName!="INVALID") {
								resultItemParam.add(itemName);
							} else {
								log.info("Name of search item index "+i+" could not be retrieved. Moving to next item.");
							}
							
						}
						break;
					case "Price"://Retrieve price of items displayed in the search result page.
						for (int i = 1; i < resultItems.size(); i++) {
							String price = sr.resultItemPrice(i);
							if ( price != "INVALID") {
								resultItemParam.add(price.substring(1));
							} else {
								log.info("Price for search item " + sr.resultItemName(i) + " is not displayed.");
							}
					
						
						}
						break;
					case "CustRev"://Retrieve customer review of items displayed in the search result page.
						for (int i = 1; i < resultItems.size(); i++) {
							String rating = sr.getAvgCustReview(i);
							if ( rating != "INVALID") {
								resultItemParam.add(rating);
							} else {
								log.info("Customer review for search item " + sr.resultItemName(i) + " is not displayed.");
							}
						}
						break;
				}
			}
		} else {
			log.error("Unable to find no. of search result pages. Cannot continue. Moving to next testcase.");
			Assert.fail();
		}
		} catch (Exception e) {
			log.debug("Exception while executing method getParamFromAllPages. Movin to next testcase.");
			log.catching(e);
		}
		//log.info("Array size:"+resultItemParam.size());
		return resultItemParam;
	}
	
	//Method to get Date Details. It increments today's date with no.of days sent as parameter.
	//It also initializes Objects of GetDateDetails class and get details of today,tomorrow and twodaysmore 
	public void getDateDetails() {
		// get dates incremented values
		LocalDate today = incrementDate(0);
		LocalDate tomorrow = incrementDate(1);
		LocalDate twoDaysMore = incrementDate(2);
		
		todayDet = new GetDateDetails(today);
		tomorrowDet = new GetDateDetails(tomorrow);
		twoDaysMoreDet = new GetDateDetails(twoDaysMore);
		return;
		
	}
	//Compare input HashMap with date details with local date details 
	public Boolean compareDelDate(HashMap<String,String> hmDate, String deliveryDay) {
		Integer dayMatch=0,monthMatch=0,dateMatch=0;
		switch (deliveryDay) {
			case "today":
				if(hmDate.get("day").equalsIgnoreCase(todayDet.day) || hmDate.get("day").equalsIgnoreCase(todayDet.day.substring(0, 3)) || hmDate.get("day").equalsIgnoreCase("Today") ) {
					dayMatch++;
				} 
				if(hmDate.get("date").equalsIgnoreCase(todayDet.date )) {
					dateMatch++;
				}
				if(hmDate.get("month").equalsIgnoreCase(todayDet.month) || hmDate.get("month").equalsIgnoreCase(todayDet.month.substring(0, 3))) {
					monthMatch++;
				}
				break;
			case "twoDaysMore":
				if(hmDate.get("day").equalsIgnoreCase(twoDaysMoreDet.day) || hmDate.get("day").equalsIgnoreCase(twoDaysMoreDet.day.substring(0, 3)) ) {
					dayMatch++;
				} 
				if(hmDate.get("date").equalsIgnoreCase(twoDaysMoreDet.date )) {
					dateMatch++;
				}
				if(hmDate.get("month").equalsIgnoreCase(twoDaysMoreDet.month) || hmDate.get("month").equalsIgnoreCase(twoDaysMoreDet.month.substring(0, 3))) {
					monthMatch++;
				}
				
			case "tomorrow":
				if(hmDate.get("day").equalsIgnoreCase(tomorrowDet.day) || hmDate.get("day").equalsIgnoreCase(tomorrowDet.day.substring(0, 3)) || hmDate.get("day").equalsIgnoreCase("Tomorrow") || hmDate.get("day").equalsIgnoreCase("Tom") ) {
					dayMatch++;
				} 
				if(hmDate.get("date").equalsIgnoreCase(tomorrowDet.date )) {
					dateMatch++;
				}
				if(hmDate.get("month").equalsIgnoreCase(tomorrowDet.month) || hmDate.get("month").equalsIgnoreCase(tomorrowDet.month.substring(0, 3))) {
					monthMatch++;
				}
				break;
		}
		if(dayMatch>0 && dateMatch>0 && monthMatch>0) {
			// log.info("Day,Month and Date values match correctly with the delivery date selected");
			return true;
		} else {
			//log.error("Delivery date values does not match with selected option.");
			return false;
		}
	}
	//Search a product in the website and check if result page is displayed
	public Boolean searchProductMethod() {
		Integer tryCount=0;
		try {
			
			log.info("Entering product name in the search bar");
			
			//Enter product in the search bar and search
			hp.searchBar().sendKeys(inputValues.get("Product"));
			hp.searchButton().click();
			
			Thread.sleep(3000);
			while(tryCount<2) {
				waitForPageLoaded();
				tryCount++;
				//Verify if search result page is displayed
				if(sr.resultListCheckDisplay() > 0) {
					log.info("Search result page displayed successfully.");
					return true;
				} else {
					log.error("Search result page is NOT displayed. May not be able to continue.");
				}
			}
		} catch (Exception e) {
			log.debug("Exception thrown while searching a product in the website.");
			log.catching(e);
		}
		return false;
	}
	//Increment today's date by no. of days by using LocalDate
	public LocalDate incrementDate (Integer i) {
		LocalDate newDate = LocalDate.now().plusDays(i);
		return newDate;
	}

}
