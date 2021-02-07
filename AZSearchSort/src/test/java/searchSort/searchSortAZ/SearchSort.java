package searchSort.searchSortAZ;

import java.time.temporal.ValueRange;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import resources.CommonModules;


public class SearchSort extends CommonModules {
	
	private static Logger log= LogManager.getLogger(SearchSort.class.getName());
	
	@Test(priority=0)
	public void searchProduct() {
		Boolean result = searchProductMethod();
		if(!result) {
			log.info("Search result page is NOT displayed as expected. Unable to continue.Exiting. ");
			Assert.fail();
		}
	}
	
	@Test(priority=1,dependsOnMethods="searchProduct")
	public void searchProductNameCheck() {
		Double percent;
		log.info("TESTCASE: searchProductNameCheck ");
		try {
			
			log.info("Find the total no. of search result pages.");
			Integer noOfPages = sr.findNoOfPages();

			//Retrieve names of items from all search result pages and assign it to a list
			List<String> resultItemName = getParamFromAllPages(noOfPages,"Name");
		
			String prodString = inputValues.get("Product");
			String newString = reverseFirstCharCase(prodString);
			//The search result item name may contain product name starting with upper and lower case. Hence searching the item name for the same(Ex:Book and book)
			List<String> itemNameWithString = resultItemName.stream().filter(s->s.contains(prodString)||s.contains(newString)).collect(Collectors.toList());
			//Part3 verify at least 70% of result items contains the product name in it
			percent = (double) ((itemNameWithString.size()*100)/resultItemName.size());
			if(itemNameWithString.size() > (resultItemName.size()*0.7)) {
				log.info("More than "+percent+"% of the search result items has product name displayed in it.");
			} else {
				log.error("Only less than "+percent+"% of the search result items has product name displayed in it. Please check the search result.");
				Assert.fail();
			}
			
			Assert.assertTrue(true, "Searching product name in the website successfully displayed search results.");
		}catch (Exception e) {
			log.info("Exception while executing TESTCASE: searchProductCheck. Exiting testcase.");
			log.catching(e);
		}
	}
	
	@Test(dependsOnMethods="searchProduct",priority=1)
	public void setPriceRange() {
		log.info("TESTCASE: setPriceRange");
		try {
		
		// Set price range for the product
		log.info("Setting min and max price values for the product.");
		if(!sr.setMinMaxValues(inputValues.get("MinPrice"), inputValues.get("MaxPrice"))) {
			log.debug("TESTCASE: setPriceRange: search result page has not appeared after setting min max price values. ");
			log.debug("Cannot continue. Exiting testcase.");
			Assert.fail();
		}
				
		log.info("Find the total no. of search result pages.");
		Integer noOfPages = sr.findNoOfPages();

		//Initializing list of search result items price: 
		//Retrieve price from all search result items from all pages
		List<String> resItemPriceList = getParamFromAllPages(noOfPages,"Price");
		//Converting Price list from String to Integer 
		List<Integer> resItemPriceListInt = resItemPriceList.stream().map(s->Integer.parseInt(s)).collect(Collectors.toList());
		
		//Checking if the search results are with configured minPrice and maxPrice
		log.info("The min and max values set are: "+inputValues.get("MinPrice")+" and "+inputValues.get("MaxPrice"));
		log.info("Checking if the search results are within configured minPrice and maxPrice.");
		Boolean result = resItemPriceListInt.stream().allMatch(s-> ValueRange.of(Integer.parseInt(inputValues.get("MinPrice")),Integer.parseInt(inputValues.get("MaxPrice"))).isValidIntValue(s) );
		if(result) {
			log.info("The search results are with in set min and max price value range.");
		} else {
			log.error("The search results are not with in set min and max price value range.");
			Assert.fail();
		}
		}catch(Exception e) {
			log.info("Exception while executing TESTCASE: setPriceRange. Exiting testcase.");
			log.catching(e);
		}
	}
	
	@Test(dependsOnMethods="searchProduct",priority=2)
	public void sortPriceLowToHigh() {
		log.info("TESTCASE: sortPriceLowToHigh");
		
		try {
			
			log.info("Sorting search result based on Price low to high values.");
			sr.sortPriceLowToHigh();
			waitForPageLoaded();
			
			log.info("Find the total no. of search result pages.");
			Integer noOfPages = sr.findNoOfPages();

			//Initializing list of search result items price: 
			//Retrieve price from all search result items from all pages
			List<String> resItemPriceList = getParamFromAllPages(noOfPages,"Price");
			//Converting Price list from String to Integer 
			List<Integer> resItemPriceListInt = resItemPriceList.stream().map(s->Integer.parseInt(s)).collect(Collectors.toList());
	
			//Sort the price list and save in to another list
			List<Integer> sortedItemPrice = resItemPriceListInt.stream().sorted().collect(Collectors.toList());
			log.info("Verifying if items are sorted according to price low to high.");
			if(resItemPriceListInt.equals(sortedItemPrice) ) {
				log.info("The search results are sorted correctly according to price low to high.");
			} else {
				log.error("The search results are NOT sorted correctly according to price low to high");
				Assert.fail();
			}
		
		} catch (Exception e) {
			log.info("Exception occured in TESTCASE: sortPriceLowToHigh. Exiting testcase.");
			log.catching(e);
		}
	}
	
	@Test(dependsOnMethods="searchProduct",priority=2)
	public void sortPriceHighToLow() {
		log.info("TESTCASE: sortPriceHighToLow");
	
		try {
			
			log.info("Sorting search result based on Price high to low values.");
			sr.sortPriceHighToLow();
			waitForPageLoaded();
			
			log.info("Find the total no. of search result pages.");
			Integer noOfPages = sr.findNoOfPages();

			//Initializing list of search result items price: 
			//Retrieve price from all search result items from all pages
			List<String> resItemPriceList = getParamFromAllPages(noOfPages,"Price");
			//Converting Price list from String to Integer 
			List<Integer> resItemPriceListInt = resItemPriceList.stream().map(s->Integer.parseInt(s)).collect(Collectors.toList());
	
			//Sort the price list and save in to another list
			List<Integer> sortedItemPrice = resItemPriceListInt.stream().sorted().sorted().collect(Collectors.toList());
			Collections.reverse(sortedItemPrice);//Reverse the order in the list to check price from high to low
			log.info("Verifying if items are sorted according to price high to low.");
			if(resItemPriceListInt.equals(sortedItemPrice) ) {
				log.info("The search results are sorted correctly according to price high to low.");
			} else {
				log.error("The search results are NOT sorted correctly according to price high to low");
				Assert.fail();
			}
		
		} catch (Exception e) {
			log.info("Exception occured in TESTCASE: sortPriceHighToLow. Exiting testcase.");
			log.catching(e);
		}
	}
	
	@Test(dependsOnMethods="searchProduct",priority=2)
	public void sortByAvgCustRev() {
		log.info("TESTCASE: SortByAvgCustomerReview");
	
		try {
			
			log.info("Sorting search result based on Avg Customer review.");
			sr.sortAvgCustReview();
			waitForPageLoaded();
			
			log.info("Find the total no. of search result pages.");
			Integer noOfPages = sr.findNoOfPages();

			//Initializing list of search result items CustReview: 
			//Retrieve Cust review from all search result items from all pages
			List<String> resItemCustRev = getParamFromAllPages(noOfPages,"CustRev");
			//Converting Customer review rating from String to Float 
			List<Float> resItemCustRevFloat = resItemCustRev.stream().map(s->Float.parseFloat(s)).collect(Collectors.toList());
	
			//Sort the customer rating list and save in to another list
			List<Float> sortedRating = resItemCustRevFloat.stream().sorted().sorted().collect(Collectors.toList());
			Collections.reverse(sortedRating); //Reverse the customer rating list so we get high ratings first in the list
			log.info("Verifying if items are sorted according to customer rating high to low.");
			//Comparing displayed order of customer rating with sorted rating values and check if they are equal
			if(resItemCustRevFloat.equals(sortedRating) ) {
				log.info("The search results are sorted correctly according to Avg.Customer review.");
			} else {
				log.error("The search results are NOT sorted correctly according to Avg.Customer review");
				Assert.fail();
			}
		
		} catch (Exception e) {
			log.info("Exception occured in TESTCASE: SortByAvgCustomerReview. Exiting testcase.");
			log.catching(e);
		}
	}
	
	
}
