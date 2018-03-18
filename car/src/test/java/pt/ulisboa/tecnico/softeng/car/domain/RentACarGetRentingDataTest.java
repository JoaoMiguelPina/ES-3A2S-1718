package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarGetRentingDataTest {
	private final String drivingLicence = "IMTT000";
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Car car;
	private Renting renting;
	private RentACar rentACar;
	
	@Before
	public void setUp() {
		this.rentACar = new RentACar("New rent");
		this.car = new Car("14-14-GG", 0, rentACar);
		this.renting = this.car.rent(this.drivingLicence, this.arrival, this.departure);
	}
	
	@Test
	public void success() {
		String ref = this.renting.getReference();
		assertNotNull(this.rentACar.getRentingData(ref));
		
	}
	
	@Test (expected = CarException.class)
	public void noReference() {
		assertNull(this.rentACar.getRentingData(""));
	}
	
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.vehicles.clear();
	}
}