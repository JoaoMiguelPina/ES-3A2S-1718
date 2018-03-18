package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class CarConstructorTest {
	private Car car;
	private RentACar rentACar;

	@Before
	public void setUp() {
		this.rentACar = new RentACar("Top Fleet");
	}
	
	@Test
	public void success(){
		this.car = new Car("19-14-CJ", 34, this.rentACar);		
		Assert.assertTrue(car.getPlate().matches("[0-9][0-9]-[0-9][0-9]-[A-Z][A-Z]"));
		Assert.assertTrue(car.getPlate() == "19-14-CJ");
		Assert.assertTrue(car.getRentACar().getName() == "Top Fleet");
		Assert.assertTrue(car.getKilometers() == 34);
	}
	
	@Test
	public void displayPlate() {
		//Plates need to be NUMBERS-NUMBERS-LETTERS
		this.car = new Car("12-14-CJ", 34, this.rentACar);
		Assert.assertTrue(car.getPlate().matches("[0-9][0-9]-[0-9][0-9]-[A-Z][A-Z]"));
	}
	
	@Test(expected = CarException.class)
	public void nullPlate() {
		new Car(null, 32, this.rentACar);
		
	}
	
	@Test(expected= CarException.class)
	public void uniquePlates() {
		new Car("12-14-CJ", 34, this.rentACar);
		new Car("12-14-CJ", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void withoutSpace() {
		new Car("1214-CJ", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void twoLetters() {
		new Car("AF-14-CJ", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void emptyPlate() {
		new Car("", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void justNumbers() {
		new Car("11-11-11", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void blankPlate() {
		new Car("  -  -  ", 34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void negativeKms() {
		new Car("12-14-CJ", -34, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void minusOne() {
		new Car("12-14-CJ", -1, this.rentACar);
	}
	
	@Test
	public void zero() {
		new Car("12-14-CJ", 0, this.rentACar);
	}
	
	@Test
	public void One() {
		new Car("12-14-CJ", 1, this.rentACar);
	}
	
	@Test(expected= CarException.class)
	public void rentNull() {
		new Car("12-14-CJ", 10, null);
	}
	
	@Test
	public void rentACarTest() {
		Car c = new Car("12-14-CJ", 10, this.rentACar);
		c.getRentACar();
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.vehicles.clear();
	}

}
