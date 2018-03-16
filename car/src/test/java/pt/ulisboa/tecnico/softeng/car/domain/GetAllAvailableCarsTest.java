package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

import java.util.HashSet;
import java.util.Set;


public class GetAllAvailableCarsTest{
	private final String NAME = "João Siva";
	//Set<Car> cars = new HashSet<>();
	LocalDate begin = new LocalDate(2018,4,1);
	LocalDate end = new LocalDate(2019,4,1);
	RentACar rac;
	Car c1;
	@Before
	public void setUp() {
		rac = new RentACar("deluxe");
		c1 = new Car("66-66-AS", 20, rac);
	}
	
	@Test
	public void success() {
		/*RentACar rentacar = new RentACar(NAME);
		Set<Car> carSet = new HashSet<>();
		carSet.add(new Car("12-14-CJ", 200000, rentacar));
		carSet.add(new Car("13-14-CJ", 100000, rentacar));
		carSet.add(new Car("14-14-CJ", 50000, rentacar));
		carSet.add(new Car("15-14-CJ", 150000, rentacar));
		
		this.cars = rentacar.getAllAvailableCars(BEGIN, END);
		
		assertTrue(this.cars.containsAll(carSet));*/	
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