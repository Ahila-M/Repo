package searchSort.searchSortAZ;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import resources.CommonModules;

public class CheckDeliveryDate extends CommonModules {
	
	private static Logger log= LogManager.getLogger(SearchSort.class.getName());
	
	@Test
	public void setUp() {
		// Search product name in the website
		searchProductMethod();
		//Get today's date and increment it with no. of days to check.
		getDateDetails();
	}
	
	@Test(dependsOnMethods="setUp")
	public void CheckTodayOption() {
		Integer noOfPages;
		try {
			log.info("TESTCASE:CheckTodayDeliveryOption");
			
			if (sr.getItTodayIsPresent() > 0) {
				log.info("'Get It Today' option is available. Going to select the same.");
				sr.selectChkBox("today");

				noOfPages = sr.findNoOfPages();
				log.info("Total no. of search result pages.:"+noOfPages);
				// Retrieve delivery details of items from all search result pages and compare with expected date details
				Boolean result = getCompDelDate(noOfPages, "TodayDelivery");
				//Clear the delivery option
				sr.selectClearDelOption();
				if(!result) {
					Assert.fail();
				}
								
			} else {
				log.debug("'Get it Today' option is NOT available right now. Skipping. Moving to next testcase ");
				throw new SkipException("'Get it Today' option is NOT available right now. Skipping. Moving to next testcase ");
			}
		} catch (Exception e) {
			log.info("Exception while executing TESTCASE:CheckTodayDeliveryOption. Exiting testcase.");
			log.catching(e);
		}

	}
	@Test(dependsOnMethods="setUp")
	public void Check2DaysOption() {
		Integer noOfPages;
		try {
			log.info("TESTCASE:Check2DaysDeliveryOption");
			
			if (sr.getIn2DaysIsPresent() > 0) {
				log.info("Get in 2 days option is available. Going to select the same.");
				sr.selectChkBox("twoDays");
				
				log.info("Find the total no. of search result pages.");
				noOfPages = sr.findNoOfPages();
				// Retrieve delivery details of items from all search result pages and compare with expected date details
				Boolean result = getCompDelDate(noOfPages, "TwoDaysDelivery");
				//Clear the delivery option
				sr.selectClearDelOption();
				if(!result) {
					Assert.fail();
				}
								
			} else {
				log.info("Get in 2 days option is NOT available right now. Skipping. Moving to next testcase ");
				throw new SkipException("'Get in 2 days' option is NOT available right now. Skipping. Moving to next testcase ");
			}
		} catch (Exception e) {
			log.info("Exception while executing TESTCASE:Check2DaysDeliveryOption. Exiting testcase.");
			log.catching(e);
		}

	}
	@Test(dependsOnMethods="setUp")
	public void CheckTomorrowOption() {
		Integer noOfPages;
		try {
			log.info("TESTCASE:CheckTomorrowDeliveryOption");
			
			if (sr.getItTomorrowIsPresent() > 0) {
				log.info("Get by tomorrow option is available. Going to select the same.");
				sr.selectChkBox("tomorrow");
				
				log.info("Find the total no. of search result pages.");
				noOfPages = sr.findNoOfPages();
				// Retrieve delivery details of items from all search result pages and compare with expected date details
				Boolean result = getCompDelDate(noOfPages, "TomorrowDelivery");
				//Clear the delivery option
				sr.selectClearDelOption();
				if(!result) {
					Assert.fail();
				}
								
			} else {
				log.debug("Get it tomorrow option is NOT available right now. Skipping. Moving to next testcase ");
				throw new SkipException("Get it tomorrow option is NOT available. Skipping. Moving to next testcase ");
			}
		} catch (Exception e) {
			log.info("Exception while executing TESTCASE:CheckTomorrowDeliveryOption. Exiting testcase.");
			log.catching(e);
		}

	}
	
}
