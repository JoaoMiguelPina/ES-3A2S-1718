package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CarConstructorTest {
	private Car car;
	private RentACar rentACar;

	@Before
	public void setUp() {
		this.rentACar = new RentACar("Deluxe Car");
		this.car = new Car("12-14-CJ", 34, this.rentACar);
	}

	@Test
	public void success() {
		Assert.assertEquals("12-14-CJ", this.car.getPlate());
		Assert.assertEquals(34, this.car.getKilometers());
		Assert.assertNotNull(this.car.getRentACar());
		
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
	}

}
