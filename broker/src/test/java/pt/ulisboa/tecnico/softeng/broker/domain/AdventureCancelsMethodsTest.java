package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureCancelsMethodsTest {
	private static final int AGE = 20;
	private static final int AMOUNT = 300;
	private static final String IBAN = "BK011234567";
	private Broker broker;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
	}

	@Test
	public void successCancels() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, AMOUNT);
		
		adventure.setRoomConfirmation("Alberto");
		adventure.setActivityConfirmation("Montanhismo");
		adventure.setPaymentConfirmation("Alberto");
		
		// cancelRoom test success
		Assert.assertTrue(adventure.cancelRoom());
		// cancelActivity test success
		Assert.assertTrue(adventure.cancelActivity());
		// cancelPayment test success
		Assert.assertTrue(adventure.cancelPayment());
		
	}
	@Test
	public void failsCancelsFirst() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, AMOUNT);
		// cancelRoom test failure
		Assert.assertFalse(adventure.cancelRoom());
		// cancelActivity test failure
		Assert.assertFalse(adventure.cancelActivity());
		// cancelPayment test failure
		Assert.assertFalse(adventure.cancelPayment());
	}
	
	@Test
	public void failsCancelsSecond() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, AMOUNT);
		
		adventure.setRoomCancellation("Alberto");
		adventure.setActivityCancellation("Montanhismo");
		adventure.setPaymentCancellation("Alberto");
		adventure.setRoomConfirmation("Alberto");
		adventure.setActivityConfirmation("Montanhismo");
		adventure.setPaymentConfirmation("Alberto");
		
		// cancelRoom test failure
		Assert.assertFalse(adventure.cancelRoom());
		// cancelActivity test failure
		Assert.assertFalse(adventure.cancelActivity());
		// cancelPayment test failure
		Assert.assertFalse(adventure.cancelPayment());
	}
	
	@Test
	public void failsCancelsBoth() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, AMOUNT);
		
		adventure.setRoomCancellation("Alberto");
		adventure.setActivityCancellation("Montanhismo");
		adventure.setPaymentCancellation("Alberto");
		
		// cancelRoom test failure
		Assert.assertFalse(adventure.cancelRoom());
		// cancelActivity test failure
		Assert.assertFalse(adventure.cancelActivity());
		// cancelPayment test failure
		Assert.assertFalse(adventure.cancelPayment());
	}
	
	

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
