package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class RentingConflictMethodTest {

	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private final String drivingLicence = "IMTT000";
	private Renting renting;

	@Before
	public void setUp() {

		this.renting = new Renting(this.drivingLicence, this.arrival, this.departure);
	}

	@Test
	public void argumentsAreConsistent() {
		Assert.assertFalse(this.renting.conflict(new LocalDate(2016, 12, 9), new LocalDate(2016, 12, 15)));
	}

	@Test
	public void noConflictBecauseItIsCancelled() {
		this.renting.cancel();
		Assert.assertFalse(this.renting.conflict(this.renting.getArrival(), this.renting.getDeparture()));
	}

	@Test(expected = CarException.class)
	public void argumentsAreInconsistent() {
		this.renting.conflict(new LocalDate(2016, 12, 15), new LocalDate(2016, 12, 9));
	}

	@Test
	public void argumentsSameDay() {
		Assert.assertTrue(this.renting.conflict(new LocalDate(2016, 12, 9), new LocalDate(2016, 12, 9)));
	}

	@Test
	public void arrivalAndDepartureAreBeforeBooked() {
		Assert.assertFalse(this.renting.conflict(this.arrival.minusDays(10), this.arrival.minusDays(4)));
	}

	@Test
	public void arrivalAndDepartureAreBeforeBookedButDepartureIsEqualToBookedArrival() {
		Assert.assertFalse(this.renting.conflict(this.arrival.minusDays(10), this.arrival));
	}

	@Test
	public void arrivalAndDepartureAreAfterBooked() {
		Assert.assertFalse(this.renting.conflict(this.departure.plusDays(4), this.departure.plusDays(10)));
	}

	@Test
	public void arrivalAndDepartureAreAfterBookedButArrivalIsEqualToBookedDeparture() {
		Assert.assertFalse(this.renting.conflict(this.departure, this.departure.plusDays(10)));
	}

	@Test
	public void arrivalIsBeforeBookedArrivalAndDepartureIsAfterBookedDeparture() {
		Assert.assertTrue(this.renting.conflict(this.arrival.minusDays(4), this.departure.plusDays(4)));
	}

	@Test
	public void arrivalIsEqualBookedArrivalAndDepartureIsAfterBookedDeparture() {
		Assert.assertTrue(this.renting.conflict(this.arrival, this.departure.plusDays(4)));
	}

	@Test
	public void arrivalIsBeforeBookedArrivalAndDepartureIsEqualBookedDeparture() {
		Assert.assertTrue(this.renting.conflict(this.arrival.minusDays(4), this.departure));
	}

	@Test
	public void arrivalIsBeforeBookedArrivalAndDepartureIsBetweenBooked() {
		Assert.assertTrue(this.renting.conflict(this.arrival.minusDays(4), this.departure.minusDays(3)));
	}

	@Test
	public void arrivalIsBetweenBookedAndDepartureIsAfterBookedDeparture() {
		Assert.assertTrue(this.renting.conflict(this.arrival.plusDays(3), this.departure.plusDays(6)));
	}

	@After
	public void tearDown() {
		//Vehicle.vehicles.clear();
	}

}
