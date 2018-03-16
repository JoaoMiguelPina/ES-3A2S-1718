package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class MotorcycleConstructorTest {
	private Motorcycle motor;
	private RentACar rentACar;

	@Before
	public void setUp() {
		this.rentACar = new RentACar("Top Fleet");
		this.motor = new Motorcycle("12-14-CJ", 34, this.rentACar);
	}
	
	@Test
	public void checks(){
		
		//Motorcycle cant have negative kms
		Assert.assertTrue(motor.getKilometers() >= 0);
		
		//RentACar cant have a null nor empty name
		Assert.assertFalse(motor.getRentACar().getName() == "");
		Assert.assertNotNull(motor.getRentACar().getName());
		
		//Plates cant be null and need to have the format: "XX-XX-XX"
		Assert.assertNotNull(motor.getPlate());
		
		Assert.assertNotNull(motor.getPlate().charAt(0));
		Assert.assertNotNull(motor.getPlate().charAt(1));
		Assert.assertNotNull(motor.getPlate().charAt(3));
		Assert.assertNotNull(motor.getPlate().charAt(4));
		Assert.assertNotNull(motor.getPlate().charAt(6));
		Assert.assertNotNull(motor.getPlate().charAt(7));
	
		Assert.assertEquals('-', motor.getPlate().charAt(2));
		Assert.assertEquals('-', motor.getPlate().charAt(5));
		
		
		
		//Plates need to be NUMBERS-NUMBERS-LETTERS
		//checking first NUMBERS
		Assert.assertTrue(motor.getPlate().charAt(0) >= 48 && motor.getPlate().charAt(0) <= 57);
		Assert.assertTrue(motor.getPlate().charAt(1) >= 48 && motor.getPlate().charAt(1) <= 57);
		//checking second NUMBERS
		Assert.assertTrue(motor.getPlate().charAt(3) >= 48 && motor.getPlate().charAt(3) <= 57);
		Assert.assertTrue(motor.getPlate().charAt(4) >= 48 && motor.getPlate().charAt(4) <= 57);
		//checking LETTERS
		Assert.assertTrue(motor.getPlate().charAt(6) >= 65 && motor.getPlate().charAt(6) <= 90);
		Assert.assertTrue(motor.getPlate().charAt(7) >= 65 && motor.getPlate().charAt(7) <= 90);
		
	}
	
	@Test(expected= CarException.class)
	public void uniquePlates() {
		new Motorcycle("12-14-CJ", 34, this.rentACar);
	}
	
	@After
	public void tearDown() {
		//rentACar.destroyRentACar();
		motor.destroyVehicles();
	}

}
