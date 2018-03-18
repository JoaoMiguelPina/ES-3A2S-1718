package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class VehicleConflictMethodTest {

	private final String drivingLicence = "IMTT000";
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Car car;
	private Renting renting;
	

	@Before
	public void setUp() {
		RentACar rentACar = new RentACar("New rent");
		this.car = new Car("14-14-GG", 0, rentACar);
		this.renting = this.car.rent(this.drivingLicence, this.arrival, this.departure);
	}

	@Test
	public void argumentsAreConsistent() {
		Assert.assertFalse(this.car.conflict(new LocalDate(2016, 12, 9), new LocalDate(2016, 12, 15)));
	}

	@Test
	public void noConflictBecauseItIsCancelled() {
		this.renting.cancel();
		Assert.assertFalse(this.car.conflict(this.arrival, this.departure));
	}

	@Test(expected = CarException.class)
	public void argumentsAreInconsistent() {
		this.car.conflict(new LocalDate(2016, 12, 15), new LocalDate(2016, 12, 9));
	}

	@Test
	public void argumentsSameDay() {
		Assert.assertTrue(this.car.conflict(new LocalDate(2016, 12, 9), new LocalDate(2016, 12, 9)));
	}

	@Test
	public void arrivalAndDepartureAreBeforeBooked() {
		Assert.assertFalse(this.car.conflict(this.arrival.minusDays(10), this.arrival.minusDays(4)));
	}

	@Test
	public void arrivalAndDepartureAreBeforeBookedButDepartureIsEqualToBookedArrival() {
		Assert.assertFalse(this.car.conflict(this.arrival.minusDays(10), this.arrival));
	}

	@Test
	public void arrivalAndDepartureAreAfterBooked() {
		Assert.assertFalse(this.car.conflict(this.departure.plusDays(4), this.departure.plusDays(10)));
	}

	@Test
	public void arrivalAndDepartureAreAfterBookedButArrivalIsEqualToBookedDeparture() {
		Assert.assertFalse(this.car.conflict(this.departure, this.departure.plusDays(10)));
	}

	@Test
	public void arrivalIsBeforeBookedArrivalAndDepartureIsAfterBookedDeparture() {
		Assert.assertTrue(this.car.conflict(this.arrival.minusDays(4), this.departure.plusDays(4)));
	}

	@Test
	public void arrivalIsEqualBookedArrivalAndDepartureIsAfterBookedDeparture() {
		Assert.assertTrue(this.car.conflict(this.arrival, this.departure.plusDays(4)));
	}

	@Test
	public void arrivalIsBeforeBookedArrivalAndDepartureIsEqualBookedDeparture() {
		Assert.assertTrue(this.car.conflict(this.arrival.minusDays(4), this.departure));
	}

	@Test
	public void arrivalIsBeforeBookedArrivalAndDepartureIsBetweenBooked() {
		Assert.assertTrue(this.car.conflict(this.arrival.minusDays(4), this.departure.minusDays(3)));
	}

	@Test
	public void arrivalIsBetweenBookedAndDepartureIsAfterBookedDeparture() {
		Assert.assertTrue(this.car.conflict(this.arrival.plusDays(3), this.departure.plusDays(6)));
	}

	@After
	public void tearDown() {
		Vehicle.vehicles.clear();
		RentACar.rentACars.clear();
	}

}
