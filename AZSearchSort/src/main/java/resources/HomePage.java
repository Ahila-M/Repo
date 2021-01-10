package resources;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class HomePage extends CommonModules {

	public HomePage(WebDriver driver) {
		this.driver = driver;
	}
	
		private By logo = By.xpath("//a[@aria-label='Amazon' and @id='nav-logo-sprites']");
		private By searchBar = By.id("twotabsearchtextbox");
		private By searchButton = By.xpath("//input[@id='nav-search-submit-button' and @type='submit']"); 
		
		
	public Integer logo() {
		//wait.until(ExpectedConditions.visibilityOfElementLocated(logo));
		return driver.findElements(logo).size();
	}
	
	public WebElement searchBar() {
		return driver.findElement(searchBar);
	}
	
	public WebElement searchButton() {
		return driver.findElement(searchButton);
	}
	
	
}
