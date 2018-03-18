package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingConstructorTest {
	private final String NAME = "João Siva";
	Renting renting;
	Renting renting2;
	
	LocalDate begin = new LocalDate(2016, 12, 19);
	LocalDate end = new LocalDate(2016, 12, 31);
	
	RentACar RAC = new RentACar("tuxedo cars");
	Car car = new Car("12-12-CJ", 34, RAC);
	
	@Before
	public void setUp() {
		this.renting = new Renting("AAA111", begin, end, car);
	}
	
	@Test
	public void success() {
		//Renting constructor Test
		Assert.assertNotNull(this.renting.getReference());
		Assert.assertEquals("AAA111", renting.getDrivingLicense());
		Assert.assertTrue(begin.equals(renting.getBegin()));
		Assert.assertTrue(end.equals(renting.getEnd()));
		Assert.assertEquals(0, renting.getKilometers());
		
		//I assumed that checkout(kms) means that the kms turn into the value that the function receives
		this.renting.checkout(20);
		Assert.assertEquals(20, this.renting.getKilometers());
	}
	
	@Test (expected = CarException.class)
	public void checkoutTestFail() {
		this.renting.checkout(-1000);
		this.renting.checkout(-1);
	}
	
	@Test
	public void checkoutSuccess() {
		this.renting.checkout(0);
		this.renting.checkout(10000);
	}
	
	
	@After
	public void tearDown() {
		Vehicle.vehicles.clear();
	}
}