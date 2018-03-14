package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.Car;
import pt.ulisboa.tecnico.softeng.car.RentACar;

public class MotorcycleChecksMethodTest {
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
		
		Assert.assertNotNull(car.getPlate().charAt(0));
		Assert.assertNotNull(car.getPlate().charAt(1));
		Assert.assertNotNull(car.getPlate().charAt(3));
		Assert.assertNotNull(car.getPlate().charAt(4));
		Assert.assertNotNull(car.getPlate().charAt(6));
		Assert.assertNotNull(car.getPlate().charAt(7));
	
		Assert.assertEquals("-", car.getPlate().charAt(2));
		Assert.assertEquals("-", car.getPlate().charAt(5));
		
		//Plates cant be duplicate
		for(Car c : _vehicles) {
			Assert.assertNotSame(car.getPlate(), c.getPlate());
		}
		
		//Plates need to be NUMBERS-NUMBERS-LETTERS
		//checking first NUMBERS
		Assert.assertTrue(car.getPlate().charAt(0) >= 48 && car.getPlate().charAt(0) <= 57);
		Assert.assertTrue(car.getPlate().charAt(1) >= 48 && car.getPlate().charAt(1) <= 57);
		//checking second NUMBERS
		Assert.assertTrue(car.getPlate().charAt(3) >= 48 && car.getPlate().charAt(3) <= 57);
		Assert.assertTrue(car.getPlate().charAt(4) >= 48 && car.getPlate().charAt(4) <= 57);
		//checking LETTERS
		Assert.assertTrue(car.getPlate().charAt(6) >= 65 && car.getPlate().charAt(6) <= 90);
		Assert.assertTrue(car.getPlate().charAt(7) >= 65 && car.getPlate().charAt(7) <= 90);
		
	}

}
