package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GetRentingMethodTest {
	private final String DRIVING_LICENSE = "AAA111";
	private final LocalDate BEGIN = LocalDate.now();
	private final LocalDate END = LocalDate.now();
	private RentACar rentACar;
	private Car car;
	
	@Before
	public void setUp() {
		this.rentACar = new RentACar("deluxe");
		this.car = new Car("66-66-AS", 20, this.rentACar);
	}
	
	@Test
	public void getRentingTestSucess() {
		Renting renting = car.rent(this.DRIVING_LICENSE, this.BEGIN, this.END); 
		assertNotNull(rentACar.getRenting(renting.getReference()));
	}
	
	@Test
	public void getRentingNoVehiclesTest() {
		RentACar rentacar =  new RentACar("New rents test 1");
		assertNull(rentacar.getRenting(""));
	}
	
	@Test
	public void getRentingNoRentingsTest() {
		RentACar rentacar =  new RentACar("New rents test 2");
		assertNull(rentacar.getRenting(""));
	}
	
	@Test
	public void getRentingDoesNotExistTest() {
		this.car.rent(this.DRIVING_LICENSE, this.BEGIN, this.END); 
		assertNull(this.rentACar.getRenting("9876"));
	}
	
	@Test
	public void getRentingCancelled() {
		RentACar rentacar =  new RentACar("New rents test 3");
		Car car = new Car("67-66-AS", 20, rentacar);
		Renting renting = car.rent(this.DRIVING_LICENSE, this.BEGIN, this.END); 
		renting.cancel();
		assertNotNull(car.getRenting(renting.getReference() + "CANCEL"));		
	}

	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.vehicles.clear();
	}
}