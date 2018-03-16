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
	}
	
	@Test
	public void checks(){
		
		/*//Motorcycle cant have negative kms
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
		Assert.assertEquals('-', motor.getPlate().charAt(5));*/	
		
	}
	
	@Test
	public void displayPlate() {
		//Plates need to be NUMBERS-NUMBERS-LETTERS
		this.motor = new Motorcycle("12-14-CJ", 34, this.rentACar);
		Assert.assertTrue(motor.getPlate().matches("[0-9][0-9]-[0-9][0-9]-[A-Z][A-Z]"));
	}
	
	@Test(expected = CarException.class)
	public void nullPlate() {
		new Motorcycle(null, 32, this.rentACar);
		
	}
	
	@Test(expected= CarException.class)
	public void uniquePlates() {
		new Motorcycle("12-14-CJ", 34, this.rentACar);
		new Motorcycle("12-14-CJ", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void withoutSpace() {
		new Motorcycle("1214-CJ", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void twoLetters() {
		new Motorcycle("AF-14-CJ", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void emptyPlate() {
		new Motorcycle("", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void justNumbers() {
		new Motorcycle("11-11-11", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void blankPlate() {
		new Motorcycle("  -  -  ", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void negativeKms() {
		new Motorcycle("12-14-CJ", -34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void minusOne() {
		new Motorcycle("12-14-CJ", -1, this.rentACar);
	}
	
	@Test
	public void zero() {
		new Motorcycle("12-14-CJ", 0, this.rentACar);
	}
	
	@Test
	public void One() {
		new Motorcycle("12-14-CJ", 1, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void rentNull() {
		new Motorcycle("12-14-CJ", 10, null);
	}
	
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.destroyVehicles();
	}

}
