package pt.ulisboa.tecnico.softeng.car;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class VehicleConstructorTest {

	@Before
	public void setUp() {
		
		//Local Data is seen as year-month-day
		RentingData = new rentingdata("1234", "12-13-GF", "123457EFR", "9090", 10-12-12, 14-2-12);
	}

	@Test
	public void success() {

		
		//Reference must be unique
		for(RentingData rd : _references) {
			Assert.assertNotSame(rentingdata.getReference(), rd.getReference());
		}
		
	}
	
	@After
	public void tearDown() {
		rd.destroyRentingData();
	}

}
