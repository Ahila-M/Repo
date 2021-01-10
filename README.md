############################
###ReadME - AZSearchSort ###
############################
About project:
=============
  This is a Selenium test project in Java to test UI of an e-commerce website.
The project currently tests a famous website UI.
On creating new Page files and changing values in Input data sheet, this project can be used to test any other website.
The project contains:
a) Testclasses under -> src/test/java/searchSort/searchSortAZ:
  1. SearchSort.java -> Search a product in the website and check whether sorting based on price and customer ratings are done correctly.
  2. CheckDeliveryDate.java -> Select delivery date in the website and search product. Check whether products available for delivery on the deliverydate are displayed correctly.
  3. Listeners.java -> This is a TestNG Listener class. It has methods to implement when events like success,failure.. happens to testcases

b) TestResources under -> src/main/java/resources :
  1. DataSheet.xls -> Input data sheet that provides URL of the website to be tested, product to be searched, brand name to be searched.
  2. Driver files for various browsers.
  3. HomePage.java and SearchResultPage.java -> Page files of the website
  4. CommonModules.java -> has all common methods used by the testcases.

c) Log4j2.xml file under -> src/main/resources : This file is configured to save log messages from testcases to logreport files.
d) pom.xml -> File to configure dependencies required for the project.
e) testng.xml -> testng configuration file -> to mention testcases to be executed.
