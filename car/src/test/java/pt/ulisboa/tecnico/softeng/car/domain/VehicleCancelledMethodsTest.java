package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class VehicleCancelledMethodsTest {

	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private final String drivingLicence = "IMTT000";
	private final String plate = "89-86-KM";
	private final int kilometers = 20;
	private Car car;
	private RentACar rentACar;
	private Renting renting;
	

	@Before
	public void setUp() {
		this.rentACar = new RentACar("Car Deluxe");
		this.car = new Car(this.plate, this.kilometers, this.rentACar);
		this.renting = this.car.rent(this.drivingLicence, this.arrival, this.departure); 
	}

	@Test
	public void singleRenting() {
		assertEquals(1, this.car.getNumberOfRentings());
	}
	
	@Test
	public void alternateTest() {
		this.renting.cancel();
		assertEquals(0, this.car.getNumberOfRentings());
	}
	
	@Test
	public void rentingCancelled() {
		this.car.rent(this.drivingLicence, this.arrival.minusDays(10), this.arrival.minusDays(4));
		this.renting.cancel();
		assertEquals(1, this.car.getNumberOfRentings());
	}

	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.vehicles.clear();
	}

}
