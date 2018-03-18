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
	private LocalDate begin = new LocalDate(2018,4,1);
	private LocalDate end = new LocalDate(2019,4,1);
	private final String drivingLicence = "IMTT000";
	private RentACar rentACar;
	private Car car1;
	private Car car2;

	@Before
	public void setUp() {
		this.rentACar = new RentACar("deluxe");
		this.car1 = new Car("66-66-AS", 20, this.rentACar);
		this.car2 = new Car("99-66-AS", 20, this.rentACar);
	}
	
	@Test
	public void success() {
		assertEquals(2, RentACar.getAllAvailableCars(this.begin, this.end).size());	
	}
	
	@Test (expected = CarException.class)
	public void nullDateEnd(){
		RentACar.getAllAvailableCars(this.begin, null);
	}
	
	@Test (expected = CarException.class)
	public void nullDateBegin() {
		RentACar.getAllAvailableCars(null, this.end);
	}
	
	@Test (expected = CarException.class)
	public void switchedDate() {
		RentACar.getAllAvailableCars(this.end, this.begin);
	}
	
	@Test 
	public void sameDate() {
		assertEquals(2, RentACar.getAllAvailableCars(this.begin, this.begin).size());
	}
	
	@Test 
	public void oneCarAvailable() {
		this.car1.rent(this.drivingLicence, this.begin, this.end);
		Set<Car> teste = RentACar.getAllAvailableCars(this.begin, this.end);
		assertEquals(1, teste.size());
	}
	
	@Test 
	public void noCarsAvailable() {
		this.car1.rent(this.drivingLicence, this.begin, this.end);
		this.car2.rent(this.drivingLicence, this.begin, this.end);
		Set<Car> teste = RentACar.getAllAvailableCars(this.begin, this.end);
		assertEquals(0, teste.size());
	}
	
	@After
	public void tearDown() {
		rentACar.tearDown();
		RentACar.rentACars.clear();
		Vehicle.destroyVehicles();
	}
}