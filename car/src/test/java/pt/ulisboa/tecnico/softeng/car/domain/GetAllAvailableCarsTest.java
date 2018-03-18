package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


import java.util.Set;


public class GetAllAvailableCarsTest{
	LocalDate begin = new LocalDate(2018,4,1);
	LocalDate end = new LocalDate(2019,4,1);
	RentACar rac;
	Car c1;
	Car c2;
	Motorcycle m1;
	Motorcycle m2;
	@Before
	public void setUp() {
		rac = new RentACar("deluxe");
		c1 = new Car("66-66-AS", 20, rac);
		c2 = new Car("99-66-AS", 20, rac);
		m1 = new Motorcycle("66-34-AS", 20, rac);
		m2 = new Motorcycle("66-12-AS", 20, rac);
	}
	
	@Test
	public void success() {
		Assert.assertTrue(rac.getVehicles().size() == 4);
		Assert.assertTrue(rac.getCars().size() == 2);
		Assert.assertTrue(rac.getMotorcycles().size() == 2);	
	}
	
	@Test (expected = CarException.class)
	public void nullDateEnd() {
		rac.getAllAvailableCars(begin, null);
	}
	
	@Test (expected = CarException.class)
	public void nullDateBegin() {
		rac.getAllAvailableCars(null, end);
	}
	
	@Test (expected = CarException.class)
	public void switchedDate() {
		rac.getAllAvailableCars(end, begin);
	}
	
	@Test 
	public void sameDate() {
		rac.getAllAvailableCars(begin, begin);
	}
	
	@Test 
	public void noCar() {
		this.tearDown();
		Set<Car> teste = rac.getAllAvailableCars(begin, end);
		assertEquals(0, teste.size());
	}
	
	@After
	public void tearDown() {
		rac.tearDown();
		Vehicle.destroyVehicles();
	}
}