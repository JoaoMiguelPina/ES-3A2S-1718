package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RentingDataConstructorTest {
	
	private Car car;
	private Motorcycle motor;
	private RentingData rd;

	@Before
	public void setUp() {
		RentACar rentacar = new RentACar("Car Deluxe");
		RentACar rentacar2 = new RentACar("Top Fleet");
		car = new Car("12-14-CJ", 34, rentacar);
		motor = new Motorcycle("66-65-OC", 80, rentacar2);
		 rd = new RentingData("1234", "66-67-TC", "AAA111", "Top Fleet", LocalDate.now(), LocalDate.now());
	
	}

	@Test
	public void success() {

		
		//Vehicle Rent-A-Car must be != NULL
		Assert.assertNotNull(car.getRentACar());
		Assert.assertNotNull(motor.getRentACar());
		
		//RentingData Constructor test
		Assert.assertEquals("1234", rd.getReference());
		Assert.assertArrayEquals("66-67-TC", rd.getPlate());
		Assert.assertArrayEquals("AAA111", rd.getLicense());
		Assert.assertArrayEquals("Top Fleet", rd.getRentACar());
		Assert.assertArrayEquals(java.time.LocalDate.now(), rd.getBegin());
		Assert.assertArrayEquals(java.time.LocalDate.now(), rd.getEnd());
		
	}
	
	@After
	public void tearDown() {
		car.destroyCar();
		motor.destroyMotorcycle();
		rd.destroyRentingData();
	}

}
