package resources;

import java.time.LocalDate;

public class GetDateDetails {
//Class to instantiate objects with date details(day,date,month,year)
//-when an object is initialized with a date as a parameter 
	String day, month, date, year;
	
	public GetDateDetails(LocalDate expDate) {
		 day = expDate.getDayOfWeek().toString();
		 date = String.valueOf(expDate.getDayOfMonth());
		 month = expDate.getMonth().toString();
		 year = String.valueOf(expDate.getYear());
		 
	}
	
}
