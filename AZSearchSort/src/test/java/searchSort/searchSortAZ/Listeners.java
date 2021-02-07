package searchSort.searchSortAZ;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import resources.CommonModules;

public class Listeners extends CommonModules implements ITestListener {

	private static Logger log = LogManager.getLogger((Listeners.class.getName()));
	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestStart(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestSuccess(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		//ITestListener.super.onTestFailure(result);
		String methodName = result.getMethod().getMethodName();
		try {
			Object currClass = result.getInstance();
			WebDriver driver = ((CommonModules)currClass).getDriver();
			
			String FilePath = getScreenShot(methodName, driver);
			log.info("Screenshot taken for the method name: "+methodName+" and is available in path: "+FilePath);
			//System.out.println(FilePath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.debug("IOException while taking screenshot of failed testcase.");
			log.catching(e);
		} catch (Exception e) {
			log.debug("Exception while taking screenshot of failed testcase.");
			log.catching(e);
		}
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		//ITestListener.super.onTestSkipped(result);
		String methodName = result.getMethod().getMethodName();
		try {
			Object currClass = result.getInstance();
			WebDriver driver = ((CommonModules)currClass).getDriver();
			
			String FilePath = getScreenShot(methodName, driver);
			log.info("Screenshot taken for the method name: "+methodName+" and is available in path: "+FilePath);
			//System.out.println(FilePath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.debug("IOException while taking screenshot of failed testcase.");
			log.catching(e);
		} catch (Exception e) {
			log.debug("Exception while taking screenshot of failed testcase.");
			log.catching(e);
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedWithTimeout(result);
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		ITestListener.super.onStart(context);
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		ITestListener.super.onFinish(context);
	}
	
	
}
