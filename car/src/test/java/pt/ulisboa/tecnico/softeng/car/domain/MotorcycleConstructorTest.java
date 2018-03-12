package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MotorcycleConstructorTest {
	private Motorcycle moto;
	private RentACar rentACar;

	@Before
	public void setUp() {
		this.rentACar = new RentACar("Deluxe Car");
		this.moto = new Motorcycle("12-14-CJ", 34, this.rentACar);
	}

	@Test
	public void success() {
		Assert.assertEquals("12-14-CJ", this.moto.getPlate());
		Assert.assertEquals(34, this.moto.getKilometers());
		Assert.assertNotNull(this.moto.getRentACar());
		
	}
	
	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}

}
