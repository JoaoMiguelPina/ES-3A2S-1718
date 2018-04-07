package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class UndoStateProcessMethodTest {
	
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private static final String INVOICE_CONFIRMATION = "InvoiceConfirmation";
	private static final String INVOICE_CANCELLATION = "InvoiceCancellation";
	private static final String VEHICLE_CONFIRMATION = "VehicleConfirmation";
	private static final String VEHICLE_CANCELLATION = "VehicleCancellation";

//	broker
	private static final String BROKER_CODE = "BR02";
	private static final String BROKER_NAME = "eXtremeADVENTURE";
	private static final String BROKER_NIF_SELLER = "123456789";
	private static final String BROKER_NIF_BUYER = "987654321";
	private static final String IBAN_BROKER = "BK01987654321";
	private static Broker broker;
	
//	client	
	private static final String IBAN_CLIENT = "CLNT01987654321";
	private static final String NIF_CLIENT = "225031999";
	private static final String DRIVING_LICENSE = "IMT123";
	private static final int AGE = 20;
	private static Client client;
	
//	activity
	private static final LocalDate begin = new LocalDate(2016, 12, 19);
	private static final LocalDate end = new LocalDate(2016, 12, 21);
	private static final double MARGIN_OF_PROFIT = 0.5;
	private static final boolean needsCar = true;
	private static final double AMOUNT = 300;
	private Adventure adventure;
	
	@Before
	public void setUp(){
		broker = new Broker(BROKER_CODE, BROKER_NAME, BROKER_NIF_SELLER, BROKER_NIF_BUYER, IBAN_BROKER);
		client = new Client(broker, IBAN_CLIENT, NIF_CLIENT, DRIVING_LICENSE, AGE);
		
		this.adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, needsCar);
		this.adventure.setState(State.UNDO);
		this.adventure.addAmount(AMOUNT);
	}

//	Payment
	@Test
	public void successRevertPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentButBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentButRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

// 	Payment and Activity
	@Test
	public void successRevertPaymentAndActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivityButActivityException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivityButRemoteAccessException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

//	Payment, Activity and Room
	@Test
	public void successRevertPaymentAndActivityAndRoom(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertActivityAndRoom(@Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndRoom(@Mocked final BankInterface bankInterface,
			@Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivityAndRoomButHotelException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void successRevertPaymentAndActivityAndRoomButRemoteAccessException(
			@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
//	TaxPayment, Payment, Activity and Room
	@Test
	public void successTaxPayment(@Mocked final TaxInterface taxInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;
				
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void successTaxPaymentButTaxException(@Mocked final TaxInterface taxInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = new TaxException();
				
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void successTaxReturnRevertPayment(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void successTaxReturnRevertPaymentButTaxException(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = new TaxException();

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void successTaxReturnRevertPaymentButBankException(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new BankException();

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void successTaxReturnRevertPaymentAndActivity(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void successTaxReturnRevertPaymentAndActivityButActivityException(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void successTaxReturnRevertPaymentAndActivityAndRoom(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface,
			@Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface hotelInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void successTaxReturnRevertPaymentAndActivityAndRoomButHotelException(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface,
			@Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface hotelInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = new HotelException();

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void successTaxReturnRevertPaymentAndActivity(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface activityInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}
	
//	Car, Activity, Tax and Payment
	@Test
	public void successTaxReturnRevertPaymentAndCar(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setVehicleConfirmation(VEHICLE_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
				CarInterface.cancelReservation(VEHICLE_CONFIRMATION);
				this.result = VEHICLE_CANCELLATION;

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void successTaxReturnRevertPaymentAndCarButCarException(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setVehicleConfirmation(VEHICLE_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
				CarInterface.cancelReservation(VEHICLE_CONFIRMATION);
				this.result = new CarException();

			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void successTaxReturnRevertPaymentAndCarAndActivity(@Mocked final BankInterface bankInterface,
			@Mocked final TaxInterface taxInterface, @Mocked final CarInterface carInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setVehicleConfirmation(VEHICLE_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				this.result = INVOICE_CANCELLATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
				CarInterface.cancelReservation(VEHICLE_CONFIRMATION);
				this.result = VEHICLE_CANCELLATION;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}
	
	@After
	public void tearDown(){
		Broker.brokers.clear();
	}
}