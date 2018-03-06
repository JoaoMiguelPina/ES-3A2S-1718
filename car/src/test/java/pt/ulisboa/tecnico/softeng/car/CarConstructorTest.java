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
		Assert.assertEquals("Car Deluxe", car.getRentACar());
		
		//Car cant have negative kms
		Assert.assertTrue(car.getKilometers() >= 0);
		
		//RentACar cant have a null nor empty name
		Assert.assertFalse(car.getRentACar() == "");
		Assert.assertNotNull(car.getRentACar());
		
		//Plates cant be null and need to have the format: "XX-XX-XX"
		Assert.assertNotNull(car.getPlate());
		
		Assert.assertNotNull(car.getPlate()[0]);
		Assert.assertNotNull(car.getPlate()[1]);
		Assert.assertNotNull(car.getPlate()[3]);
		Assert.assertNotNull(car.getPlate()[4]);
		Assert.assertNotNull(car.getPlate()[6]);
		Assert.assertNotNull(car.getPlate()[7]);
	
		Assert.assertEquals("-", car.getPlate()[2]);
		Assert.assertEquals("-", car.getPlate()[5]);
		
		//Plates cant be duplicate
		for(Car c : _vehicles) {
			Assert.assertNotSame(car.getPlate(), c.getPlate());
		}
		
		
	}
	
	@After
	public void tearDown() {
		car.destroyCar();
	}

}
