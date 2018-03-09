package pt.ulisboa.tecnico.softeng.car;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RentingDataConstructorTest {

	@Before
	public void setUp() {
		Car car = new Car("12-14-CJ", 34, "Car Deluxe");
		Motorcycle motor = new Motorcycle("66-65-OC", 80, "Top Fleet");
	}

	@Test
	public void success() {

		
		//Vehicle Rent-A-Car must be != NULL
		Assert.assertNotNull(car.getRentACar());
		Assert.assertNotNull(motor.getRentACar());
		
	}
	
	@After
	public void tearDown() {
		car.destroyCar();
	}

}
