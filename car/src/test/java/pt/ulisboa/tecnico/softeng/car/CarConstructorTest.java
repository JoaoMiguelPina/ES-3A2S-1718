package pt.ulisboa.tecnico.softeng.car;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CarConstructorTest {

	@Before
	public void setUp() {
		Car car = new Car("12-14-CJ", 34, "Car Deluxe");
	}

	@Test
	public void success() {

		Assert.assertEquals("12-14-CJ", car.getPlate());
		Assert.assertEquals(34, car.getKm());
		Assert.assertEquals("Car Deluxe", car.getKilometers());
		Assert.assertTrue(car.getKilometers() >= 0);
		
	}
	
	@After
	public void tearDown() {
		car.destroyCar();
	}

}
