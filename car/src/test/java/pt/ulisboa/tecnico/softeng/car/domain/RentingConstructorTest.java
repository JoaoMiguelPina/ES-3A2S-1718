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
	
	@Before
	public void setUp() {
		this.renting = new Renting("AAA111", LocalDate.now(), LocalDate.now());
	}
	
	@Test
	public void success() {
		
		//Renting constructor Test
		Assert.assertEquals("1234", renting.getReference());
		Assert.assertEquals("AAA111", renting.getDrivingLicense());
		Assert.assertEquals(LocalDate.now(), renting.getBegin());
		Assert.assertEquals(LocalDate.now(), renting.getEnd());
		Assert.assertEquals(33, renting.getKilometers());
		
		//I assumed that checkout(kms) means that the kms turn into the value that the function receives
		renting.checkout(20);
		Assert.assertEquals(20, this.renting.getKilometers());
	}
	
	@After
	public void tearDown() {
		//renting.destroyRenting();
	}
}