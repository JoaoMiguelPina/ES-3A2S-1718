package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.Booking; 
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;

public class ActivityInterfaceActivityReservationDataMethodTest {
	private static final String NAME = "ExtremeAdventure";
	private static final String CODE = "XtremX";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private ActivityProvider provider;
	private ActivityOffer offer;
	private Booking booking;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider(CODE, NAME);
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		this.offer = new ActivityOffer(activity, this.begin, this.end);
		this.booking = new Booking(this.provider, this.offer);
	}

	@Test
	public void success() {
		ActivityReservationData data = ActivityInterface.getActivityReservationData(this.booking.getReference());

		assertEquals(this.booking.getReference(), data.getReference());
		assertNull(data.getCancellation());
		assertEquals(NAME, data.getName());
		assertEquals(CODE, data.getCode());
		assertEquals(this.begin, data.getBegin());
		assertEquals(this.end, data.getEnd());
		assertNull(data.getCancellationDate());
	}

	@Test
	public void successCancelled() {
		this.booking.cancel();
		ActivityReservationData data = ActivityInterface.getActivityReservationData(this.booking.getCancellation());

		assertEquals(this.booking.getReference(), data.getReference());
		assertEquals(this.booking.getCancellation(), data.getCancellation());
		assertEquals(NAME, data.getName());
		assertEquals(CODE, data.getCode());
		assertEquals(this.begin, data.getBegin());
		assertEquals(this.end, data.getEnd());
		assertNotNull(data.getCancellationDate());
	}

	@Test(expected = ActivityException.class)
	public void nullReference() {
		ActivityInterface ai = new ActivityInterface();
		ai.getActivityReservationData(null);
	}

	@Test(expected = ActivityException.class)
	public void emptyReference() {
		ActivityInterface.getActivityReservationData("");
	}

	@Test(expected = ActivityException.class)
	public void notExistsReference() {
		ActivityInterface.getActivityReservationData("XPTO");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
