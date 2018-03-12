package pt.ulisboa.tecnico.softeng.car;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.Car;
import pt.ulisboa.tecnico.softeng.car.RentACar;

public class CarChecksMethodTest {
	private Car car;
	private RentACar rentACar;

	@Before
	public void setUp() {
		this.rentACar = new RentACar();
		this.car = new Car("12-14-CJ", 34, this.rentAcar);
	}
	
	@Test
	public void checks(){
		
		//Car cant have negative kms
		Assert.assertTrue(car.getKilometers() >= 0);
		
		//RentACar cant have a null nor empty name
		Assert.assertFalse(car.getRentACar().getName() == "");
		Assert.assertNotNull(car.getRentACar().getName());
		
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

}
