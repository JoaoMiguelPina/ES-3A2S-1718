package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureConstructorMethodTest {
	
	private Broker broker;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Client client;
	private double marginOfProfit = 20;
	private boolean needsCar = true;
	
	@Before
	public void setUp() {
		
		this.broker = new Broker("BR01", "eXtremeADVENTURE", "123456789", "987654321", "123");
		this.client = new Client(this.broker, "BK011234567", "225031999", "IMT123", 20);
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);

		
		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(20, adventure.getMarginOfProfit(), 0);
		Assert.assertEquals(true, adventure.needsCar());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
		Assert.assertNull(adventure.getVehicleConfirmation());
		Assert.assertNull(adventure.getInvoiceConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void nullBroker() {
		new Adventure(null, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		new Adventure(this.broker, null, this.end, this.client, this.marginOfProfit, this.needsCar);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		new Adventure(this.broker, this.begin, null, this.client, this.marginOfProfit, this.needsCar);
	}
	
	@Test(expected = BrokerException.class)
	public void nullClient() {
		new Adventure(this.broker, this.begin, this.end, null, this.marginOfProfit, this.needsCar);
	}
	


	@Test
	public void successEqual100() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, 100, this.needsCar);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(100, adventure.getMarginOfProfit(), 0);
		Assert.assertEquals(true, adventure.needsCar());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
		Assert.assertNull(adventure.getVehicleConfirmation());
		Assert.assertNull(adventure.getInvoiceConfirmation());
	}
	
	public void successEqual0() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, 0, this.needsCar);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(0, adventure.getMarginOfProfit(), 0);
		Assert.assertEquals(true, adventure.needsCar());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
		Assert.assertNull(adventure.getVehicleConfirmation());
		Assert.assertNull(adventure.getInvoiceConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void over100() {
		new Adventure(this.broker, this.begin, this.end, this.client, 101, this.needsCar);
	}
	
	@Test(expected = BrokerException.class)
	public void below0() {
		new Adventure(this.broker, this.begin, this.end, this.client, -1, this.needsCar);
	}


	@Test(expected = BrokerException.class)
	public void addNegativeAmount() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);
		adventure.addAmount(-100);
	}
	
	@Test(expected = BrokerException.class)
	public void subtractNegativeAmount() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);
		adventure.subtractAmount(-100);
	}

	@Test
	public void successEqualDates() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(20, adventure.getMarginOfProfit(), 0);
		Assert.assertEquals(true, adventure.needsCar());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
		Assert.assertNull(adventure.getVehicleConfirmation());
		Assert.assertNull(adventure.getInvoiceConfirmation());
	}
	
	@Test
	public void successAmount() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);
		
		adventure.addAmount(50);
		
		Assert.assertEquals(50, adventure.getAmount());
		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(20, adventure.getMarginOfProfit(), 0);
		Assert.assertEquals(true, adventure.needsCar());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
		Assert.assertNull(adventure.getVehicleConfirmation());
		Assert.assertNull(adventure.getInvoiceConfirmation());
	}
	
	@Test
	public void successResetAmount() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);
		
		adventure.addAmount(50);
		adventure.subtractAmount(25);
		adventure.addAmount(50);
		adventure.resetAmount();
		
		Assert.assertEquals(0, adventure.getAmount());
		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(20, adventure.getMarginOfProfit(), 0);
		Assert.assertEquals(true, adventure.needsCar());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
		Assert.assertNull(adventure.getVehicleConfirmation());
		Assert.assertNull(adventure.getInvoiceConfirmation());
	}
	
	@Test
	public void successConfirmation() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);
		
		adventure.setPaymentConfirmation("CONFIRMED");
		adventure.setActivityConfirmation("CONFIRMED");
		adventure.setRoomConfirmation("CONFIRMED");
		adventure.setVehicleConfirmation("CONFIRMED");
		adventure.setInvoiceConfirmation("CONFIRMED");
		
		Assert.assertEquals(0, adventure.getAmount());
		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(20, adventure.getMarginOfProfit(), 0);
		Assert.assertEquals(true, adventure.needsCar());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertEquals("CONFIRMED", adventure.getPaymentConfirmation());
		Assert.assertEquals("CONFIRMED" ,adventure.getActivityConfirmation());
		Assert.assertEquals("CONFIRMED", adventure.getRoomConfirmation());
		Assert.assertEquals("CONFIRMED", adventure.getVehicleConfirmation());
		Assert.assertEquals("CONFIRMED", adventure.getInvoiceConfirmation());
	}
	
	@Test
	public void successCancellation() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);
		
		adventure.setPaymentCancellation("CANCELLED");
		adventure.setActivityCancellation("CANCELLED");
		adventure.setRoomCancellation("CANCELLED");
		adventure.setVehicleCancellation("CANCELLED");
		adventure.setInvoiceCancellation("CANCELLED");
		
		Assert.assertEquals(0, adventure.getAmount());
		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(20, adventure.getMarginOfProfit(), 0);
		Assert.assertEquals(true, adventure.needsCar());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertEquals("CANCELLED", adventure.getPaymentCancellation());
		Assert.assertEquals("CANCELLED" ,adventure.getActivityCancellation());
		Assert.assertEquals("CANCELLED", adventure.getRoomCancellation());
		Assert.assertEquals("CANCELLED", adventure.getVehicleCancellation());
		Assert.assertEquals("CANCELLED", adventure.getInvoiceCancellation());
	}
	
	@Test
	public void successCancel() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.client, this.marginOfProfit, this.needsCar);
		
		adventure.setPaymentConfirmation("CONFIRMED");
		adventure.setActivityConfirmation("CONFIRMED");
		adventure.setRoomConfirmation("CONFIRMED");
		adventure.setVehicleConfirmation("CONFIRMED");
		adventure.setInvoiceConfirmation("CONFIRMED");
		
		Assert.assertEquals(0, adventure.getAmount());
		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(20, adventure.getMarginOfProfit(), 0);
		Assert.assertEquals(true, adventure.needsCar());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertEquals(true, adventure.cancelRoom());
		Assert.assertEquals(true ,adventure.cancelActivity());
		Assert.assertEquals(true, adventure.cancelPayment());
		Assert.assertEquals(true, adventure.cancelVehicle());
		Assert.assertEquals(true, adventure.cancelInvoice());
	}

	@Test(expected = BrokerException.class)
	public void inconsistentDates() {
		new Adventure(this.broker, this.begin, this.begin.minusDays(1), this.client, this.marginOfProfit, this.needsCar);
		
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
