package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Test;


public class GetRentingMethodTest {
	private final String DRIVING_LICENSE = "AAA111";
	private final LocalDate BEGIN = LocalDate.now();
	private final LocalDate END = LocalDate.now();
	
	@Test
	public void getRentingTestSucess() {
		RentACar rentACar =  new RentACar("New rentACar");
		Car car = new Car("12-12-CJ", 34, rentACar);
		Renting renting = car.rent(this.DRIVING_LICENSE, this.BEGIN, this.END); 
		assertNotNull(rentACar.getRenting(renting.getReference()));
	}
	
	@Test
	public void getRentingNoVehiclesTest() {
		RentACar rentACar =  new RentACar("New rents test 1");
		assertNull(rentACar.getRenting(""));
	}
	
	@Test
	public void getRentingNoRentingsTest() {
		RentACar rentACar =  new RentACar("New rents test 2");
		assertNull(rentACar.getRenting(""));
	}
	
	@Test
	public void getRentingDoesNotExistTest() {
		RentACar rentACar =  new RentACar("New rents test 3");
		assertNull(rentACar.getRenting(""));
	}

	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.vehicles.clear();
	}
}