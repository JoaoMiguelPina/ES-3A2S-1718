package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.domain.RentingData;

public class VehicleConstructorTest {
	private RentingData rd;
	
	@Before
	public void setUp() {
		
		//Local Data is seen as year-month-day
		rd = new RentingData("1234", "12-13-GF", "123457EFR", "9090", LocalDate.now(), LocalDate.now());
	}

	@Test
	public void success() {
		//Reference must be unique
		for(String iterator : rd._references) {
			Assert.assertNotSame(iterator, rd.getReference());
		}
		
	}
	
	@After
	public void tearDown() {
		rd.destroyRentingData();
	}

}
