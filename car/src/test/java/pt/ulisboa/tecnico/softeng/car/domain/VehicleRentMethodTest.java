package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class VehicleRentMethodTest {

	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private final String drivingLicence = "IMTT000";
	private final String plate = "89-86-KM";
	private final int kilometers = 20;
	private Car car;
	private RentACar rentACar;
	

	@Before
	public void setUp() {
		this.rentACar = new RentACar("Car Deluxe");
		this.car = new Car(this.plate, this.kilometers, this.rentACar);
	}

	@Test
	public void success() {
		assertNotNull(car.rent(drivingLicence, arrival, departure));
	}

	@Test(expected = CarException.class)
	public void noDouble() {
		this.car.rent(this.drivingLicence, this.arrival, this.departure);
	}

	@Test(expected = CarException.class)
	public void nullType() {
		this.car.rent(null, this.arrival, this.departure);
	}

	@Test(expected = CarException.class)
	public void nullArrival() {
		this.car.rent(this.drivingLicence, null, this.departure);
	}

	@Test(expected = CarException.class)
	public void nullDeparture() {
		this.car.rent(this.drivingLicence, this.arrival, null);
	}

	@Test
	public void allConflict() {
		this.car.rent(this.drivingLicence, this.arrival, this.departure);

		try {
			this.car.rent(this.drivingLicence, this.arrival, this.departure);
			fail();
		} catch (CarException Ce) {
			Assert.assertEquals(1, this.car.getNumberOfRentings());
		}
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.vehicles.clear();
		
	}

}
