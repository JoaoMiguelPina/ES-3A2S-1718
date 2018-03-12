package pt.ulisboa.tecnico.softeng.car;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.Car;
import pt.ulisboa.tecnico.softeng.car.RentACar;

public class CarConstructorTest {
	private Car car;
	private RentACar rentACar;

	@Before
	public void setUp() {
		this.car = new Car("12-14-CJ", 34, "Car Deluxe");
		this.rentACar = new RentACar();
	}

	@Test
	public void success() {
		junit.framework.Assert.assertEquals("12-14-CJ", this.car.getPlate());
		junit.framework.Assert.assertEquals(34, this.car.getKilometers());
		junit.framework.Assert.assertNotNull(this.car.getRentACar());
		
	}

}
