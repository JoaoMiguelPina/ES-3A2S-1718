package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.Assert;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;


public class ActivityProviderReserveActivityMethodTest {
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 25;
	private static final int AGE = 40;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);

		this.offer = new ActivityOffer(this.activity, this.begin, this.end);
	}
	

	@Test
	public void success() {
		String referenceBooking = ActivityProvider.reserveActivity(begin, end, AGE);
		
		Assert.assertNotNull(offer.getBooking(referenceBooking));
	}
	
	@Test(expected = ActivityException.class)
	public void noProviders() {
		ActivityProvider.providers.clear();
		ActivityProvider.reserveActivity(begin, end, AGE);
	}
	
	/** BOUNDARY TESTS **/
	@Test(expected = ActivityException.class)
	public void agePlusOneThanMaximum() {
		ActivityProvider.reserveActivity(begin, end, MAX_AGE + 1);
	}
	
	@Test(expected = ActivityException.class)
	public void ageMinusOneThanMinimal() {
		ActivityProvider.reserveActivity(begin, end, MIN_AGE - 1);
	}
	
	@Test
	public void successAgeEqualMax() {	
		String referenceBooking = ActivityProvider.reserveActivity(begin, end, MAX_AGE);
		
		Assert.assertNotNull(offer.getBooking(referenceBooking));
	}
	
	@Test
	public void successAgeEqualMin() {	
		String referenceBooking = ActivityProvider.reserveActivity(begin, end, MIN_AGE);
		
		Assert.assertNotNull(offer.getBooking(referenceBooking));
	}

	@Test
	public void sucessAgeMinusOneThanMaximum() {	
		String referenceBooking = ActivityProvider.reserveActivity(begin, end, MAX_AGE - 1);
		
		Assert.assertNotNull(offer.getBooking(referenceBooking));
	}
	
	@Test
	public void sucessAgePlusOneThanMinimal() {	
		String referenceBooking = ActivityProvider.reserveActivity(begin, end, MIN_AGE + 1);
		
		Assert.assertNotNull(offer.getBooking(referenceBooking));
	}
	
	/** DATE TESTS **/
	@Test(expected = ActivityException.class)
	public void nullBeginDate() {
		ActivityProvider.reserveActivity(null, end, AGE);

	}

	@Test(expected = ActivityException.class)
	public void nullEndDate() {
		ActivityProvider.reserveActivity(begin, null, AGE);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}
		
}
