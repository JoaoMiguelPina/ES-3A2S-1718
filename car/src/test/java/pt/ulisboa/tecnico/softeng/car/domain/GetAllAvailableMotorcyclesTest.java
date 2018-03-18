package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

import java.util.HashSet;
import java.util.Set;


public class GetAllAvailableMotorcyclesTest{
	private LocalDate begin = new LocalDate(2018,4,1);
	private LocalDate end = new LocalDate(2019,4,1);
	private final String drivingLicence = "IMTT000";
	private RentACar rentACar;
	private Motorcycle moto1;
	private Motorcycle moto2;

	@Before
	public void setUp() {
		this.rentACar = new RentACar("deluxe");
		this.moto1 = new Motorcycle("66-66-AS", 20, this.rentACar);
		this.moto2 = new Motorcycle("99-66-AS", 20, this.rentACar);
	}
	
	@Test
	public void success() {
		assertEquals(2, RentACar.getAllAvailableMotorcycles(this.begin, this.end).size());	
	}
	
	@Test (expected = CarException.class)
	public void nullDateEnd(){
		RentACar.getAllAvailableMotorcycles(this.begin, null);
	}
	
	@Test (expected = CarException.class)
	public void nullDateBegin() {
		RentACar.getAllAvailableMotorcycles(null, this.end);
	}
	
	@Test (expected = CarException.class)
	public void switchedDate() {
		RentACar.getAllAvailableMotorcycles(this.end, this.begin);
	}
	
	@Test 
	public void sameDate() {
		assertEquals(2, RentACar.getAllAvailableMotorcycles(this.begin, this.begin).size());
	}
	
	@Test 
	public void oneCarAvailable() {
		this.moto1.rent(this.drivingLicence, this.begin, this.end);
		Set<Motorcycle> teste = RentACar.getAllAvailableMotorcycles(this.begin, this.end);
		assertEquals(1, teste.size());
	}
	
	@Test 
	public void noCarsAvailable() {
		this.moto1.rent(this.drivingLicence, this.begin, this.end);
		this.moto2.rent(this.drivingLicence, this.begin, this.end);
		Set<Motorcycle> teste = RentACar.getAllAvailableMotorcycles(this.begin, this.end);
		assertEquals(0, teste.size());
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.vehicles.clear();
	}
}