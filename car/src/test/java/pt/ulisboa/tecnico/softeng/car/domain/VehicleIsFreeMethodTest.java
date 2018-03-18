package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class VehicleIsFreeMethodTest {

	private final String drivingLicence = "IMTT000";
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Car car;
	

	@Before
	public void setUp() {
		RentACar rentACar = new RentACar("New rent");
		this.car = new Car("14-14-GG", 0, rentACar);
		this.car.rent(this.drivingLicence, this.arrival, this.departure);
	}

	@Test
	public void success() {
		Assert.assertTrue(this.car.isFree(this.arrival.minusDays(10), this.arrival.minusDays(4)));
	}
	
	@Test
	public void notFree() {
		Assert.assertFalse(this.car.isFree(this.arrival, this.departure));
	}

	@Test (expected = CarException.class)
	public void invalidDates() {
		Assert.assertTrue(this.car.isFree(this.departure, this.arrival));
	}
	
	@After
	public void tearDown() {
		Vehicle.vehicles.clear();
		RentACar.rentACars.clear();
	}

}
