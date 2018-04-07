package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
//	broker
	private static final String BROKER_CODE = "BR01";
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
	
//	confirmations and cancellations	
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private static final String RENT_CONFIRMATION = "RentConfirmation";
	private static final String RENT_CANCELLATION = "RentCancellation";
	private static final String INVOICE_CONFIRMATION = "InvoiceConfirmation";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	
//	prices
	private static final double PRICE_ACTIVITY = 20.0;
	private static final double PRICE_HOTEL = 20.0;
	private static final double PRICE_RENT = 20.0;	
	
	@Before
	public void setUp(){
		broker = new Broker(BROKER_CODE, BROKER_NAME, BROKER_NIF_SELLER, BROKER_NIF_BUYER, IBAN_BROKER);
		client = new Client(broker, IBAN_CLIENT, NIF_CLIENT, DRIVING_LICENSE, AGE);
	}

	@Test
	public void successSequenceOneAllModules(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_HOTEL + PRICE_RENT) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = activityReservationData;
				
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = roomBookingData;
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Rent Vehicle
				CarInterface.reserveVehicle(begin, end, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = rentingData;
				
				rentingData.getAmmount();
				this.result = PRICE_RENT;
				
//				(4) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(5) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = INVOICE_CONFIRMATION;
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.CONFIRMED, adventure.getState());
		
		assertEquals(ACTIVITY_CONFIRMATION, adventure.getActivityConfirmation());
		assertEquals(ROOM_CONFIRMATION, adventure.getRoomConfirmation());
		assertEquals(RENT_CONFIRMATION, adventure.getVehicleConfirmation());
		assertEquals(PAYMENT_CONFIRMATION, adventure.getPaymentConfirmation());
		assertEquals(INVOICE_CONFIRMATION, adventure.getInvoiceConfirmation());
	}

	@Test
	public void successSequenceTwoAllModulesExceptHotel(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_RENT) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, begin, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = activityReservationData;
				
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Rent Vehicle
				CarInterface.reserveVehicle(begin, begin, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = rentingData;
				
				rentingData.getAmmount();
				this.result = PRICE_RENT;
				
//				(3) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(4) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = INVOICE_CONFIRMATION;
			}
		};

		Adventure adventure = new Adventure(broker, begin, begin, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.CONFIRMED, adventure.getState());

		assertEquals(ACTIVITY_CONFIRMATION, adventure.getActivityConfirmation());
		assertEquals(RENT_CONFIRMATION, adventure.getVehicleConfirmation());
		assertEquals(PAYMENT_CONFIRMATION, adventure.getPaymentConfirmation());
		assertEquals(INVOICE_CONFIRMATION, adventure.getInvoiceConfirmation());
		assertNull(adventure.getRoomConfirmation());
	}
	
	@Test
	public void successSequenceThreeAllModulesExceptCar(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_HOTEL) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = activityReservationData;
				
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = roomBookingData;
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(4) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = INVOICE_CONFIRMATION;
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, false);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.CONFIRMED, adventure.getState());

		assertEquals(ACTIVITY_CONFIRMATION, adventure.getActivityConfirmation());
		assertEquals(ROOM_CONFIRMATION, adventure.getRoomConfirmation());
		assertEquals(PAYMENT_CONFIRMATION, adventure.getPaymentConfirmation());
		assertEquals(INVOICE_CONFIRMATION, adventure.getInvoiceConfirmation());
		
		assertNull(adventure.getVehicleConfirmation());
	}
	
	@Test
	public void successSequenceFourAllModulesExceptHotelAndCar(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, begin, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = activityReservationData;
				
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(3) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = INVOICE_CONFIRMATION;
			}
		};

		Adventure adventure = new Adventure(broker, begin, begin, client, MARGIN_OF_PROFIT, false);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.CONFIRMED, adventure.getState());

		assertEquals(ACTIVITY_CONFIRMATION, adventure.getActivityConfirmation());
		assertEquals(PAYMENT_CONFIRMATION, adventure.getPaymentConfirmation());
		assertEquals(INVOICE_CONFIRMATION, adventure.getInvoiceConfirmation());
		assertNull(adventure.getRoomConfirmation());
		assertNull(adventure.getVehicleConfirmation());
	}
	
	@Test
	public void unsuccessSequenceOneActivityExceptionCancelled(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
				
		new Expectations() {
			{
//				(1) Activity Reservation (ActivityException)
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
				
//				(2) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
	}
	
	@Test
	public void unsuccessSequenceTwoHotelExceptionCancelled(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
				
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = activityReservationData;
				
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new HotelException();
				
//				(3) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
//				(3) Booking UNDO
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
		assertEquals(ROOM_CANCELLATION, adventure.getRoomCancellation());
		assertNull(adventure.getRoomConfirmation());
	}
	
	@Test
	public void unsuccessSequenceThreeCarExceptionCancelled(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
				
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = activityReservationData;
				
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = roomBookingData;
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Rent Vehicle
				CarInterface.reserveVehicle(begin, end, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = new CarException();
				
//				(4) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
//				(5) Booking UNDO
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
//				(6) Rent UNDO
				CarInterface.cancelReservation(RENT_CONFIRMATION);
				this.result = RENT_CANCELLATION;
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
		assertEquals(ROOM_CANCELLATION, adventure.getRoomCancellation());
		assertNull(adventure.getRoomConfirmation());
		assertEquals(RENT_CANCELLATION, adventure.getVehicleCancellation());
		assertNull(adventure.getVehicleConfirmation());
	}
	
	@Test
	public void unsuccessSequenceFourBankExceptionCancelled(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_HOTEL + PRICE_RENT) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = activityReservationData;
				
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = roomBookingData;
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Rent Vehicle
				CarInterface.reserveVehicle(begin, end, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = rentingData;
				
				rentingData.getAmmount();
				this.result = PRICE_RENT;
				
//				(4) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = new BankException();
				
//				(5) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
//				(6) Booking UNDO
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
//				(7) Rent UNDO
				CarInterface.cancelReservation(RENT_CONFIRMATION);
				this.result = RENT_CANCELLATION;
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
		assertEquals(ROOM_CANCELLATION, adventure.getRoomCancellation());
		assertNull(adventure.getRoomConfirmation());
		assertEquals(RENT_CANCELLATION, adventure.getVehicleCancellation());
		assertNull(adventure.getVehicleConfirmation());
		assertNull(adventure.getPaymentConfirmation());
	}
	
	@Test
	public void unsuccessSequenceFiveTaxExceptionCancelled(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_HOTEL + PRICE_RENT) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = activityReservationData;
				
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = roomBookingData;
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Rent Vehicle
				CarInterface.reserveVehicle(begin, end, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = rentingData;
				
				rentingData.getAmmount();
				this.result = PRICE_RENT;
				
//				(4) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(5) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = new TaxException();
				
//				(6) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
//				(7) Booking UNDO
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
//				(8) Rent UNDO
				CarInterface.cancelReservation(RENT_CONFIRMATION);
				this.result = RENT_CANCELLATION;
				
//				(9) Payment UNDO
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
		assertEquals(ROOM_CANCELLATION, adventure.getRoomCancellation());
		assertNull(adventure.getRoomConfirmation());
		assertEquals(RENT_CANCELLATION, adventure.getVehicleCancellation());
		assertNull(adventure.getVehicleConfirmation());
		assertNull(adventure.getPaymentConfirmation());
		assertNull(adventure.getInvoiceConfirmation());
	}

	@Test
	public void unsuccessSequenceSixActivityExceptionConfirmed(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_HOTEL + PRICE_RENT) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION); 
				this.result = new Delegate() {
					int i = 0;

					public ActivityReservationData delegate() {
						if (this.i == 0) {
							this.i++;
							return activityReservationData;
						} else {
							throw new ActivityException();
						}
					}
				};
				this.times = 2;
				
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = roomBookingData;
				this.times = 2;
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Rent Vehicle
				CarInterface.reserveVehicle(begin, end, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = rentingData;
				this.times = 2;
				
				rentingData.getAmmount();
				this.result = PRICE_RENT;
				
//				(4) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(5) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = INVOICE_CONFIRMATION;
								
//				(6) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
//				(7) Booking UNDO
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
//				(8) Rent UNDO
				CarInterface.cancelReservation(RENT_CONFIRMATION);
				this.result = RENT_CANCELLATION;
				
//				(9) Payment UNDO
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
//				(10) Invoice UNDO
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.CONFIRMED, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
		assertEquals(ROOM_CANCELLATION, adventure.getRoomCancellation());
		assertNull(adventure.getRoomConfirmation());
		assertEquals(RENT_CANCELLATION, adventure.getVehicleCancellation());
		assertNull(adventure.getVehicleConfirmation());
		assertEquals(PAYMENT_CANCELLATION, adventure.getPaymentCancellation());
		assertNull(adventure.getPaymentConfirmation());
		assertEquals(true, adventure.isInvoiceCancelled());
		assertNull(adventure.getInvoiceConfirmation());
	}
	
	@Test
	public void unsuccessSequenceSevenHotelExceptionConfirmed(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_HOTEL + PRICE_RENT) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION); 
				this.result = activityReservationData;
				this.times = 2;
						
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					public RoomBookingData delegate() {
						if (this.i == 0) {
							this.i++;
							return roomBookingData;
						} else {
							throw new HotelException();
						}
					}
				};
				this.times = 2;
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Rent Vehicle
				CarInterface.reserveVehicle(begin, end, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = rentingData;
				this.times = 2;
				
				rentingData.getAmmount();
				this.result = PRICE_RENT;
				
//				(4) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(5) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = INVOICE_CONFIRMATION;
								
//				(6) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
//				(7) Booking UNDO
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
//				(8) Rent UNDO
				CarInterface.cancelReservation(RENT_CONFIRMATION);
				this.result = RENT_CANCELLATION;
				
//				(9) Payment UNDO
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
//				(10) Invoice UNDO
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.CONFIRMED, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
		assertEquals(ROOM_CANCELLATION, adventure.getRoomCancellation());
		assertNull(adventure.getRoomConfirmation());
		assertEquals(RENT_CANCELLATION, adventure.getVehicleCancellation());
		assertNull(adventure.getVehicleConfirmation());
		assertEquals(PAYMENT_CANCELLATION, adventure.getPaymentCancellation());
		assertNull(adventure.getPaymentConfirmation());
		assertEquals(true, adventure.isInvoiceCancelled());
		assertNull(adventure.getInvoiceConfirmation());
	}
	
	@Test
	public void unsuccessSequenceEightCarExceptionConfirmed(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_HOTEL + PRICE_RENT) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION); 
				this.result = activityReservationData;
				this.times = 2;
						
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = roomBookingData;
				this.times = 2; 
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Rent Vehicle
				CarInterface.reserveVehicle(begin, end, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = new Delegate() {
					int i = 0;

					public RentingData delegate() {
						if (this.i == 0) {
							this.i++;
							return rentingData;
						} else {
							throw new CarException();
						}
					}
				};
				this.times = 2;
				
				rentingData.getAmmount();
				this.result = PRICE_RENT;
				
//				(4) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(5) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = INVOICE_CONFIRMATION;
								
//				(6) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
//				(7) Booking UNDO
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
//				(8) Rent UNDO
				CarInterface.cancelReservation(RENT_CONFIRMATION);
				this.result = RENT_CANCELLATION;
				
//				(9) Payment UNDO
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
//				(10) Invoice UNDO
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.CONFIRMED, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
		assertEquals(ROOM_CANCELLATION, adventure.getRoomCancellation());
		assertNull(adventure.getRoomConfirmation());
		assertEquals(RENT_CANCELLATION, adventure.getVehicleCancellation());
		assertNull(adventure.getVehicleConfirmation());
		assertEquals(PAYMENT_CANCELLATION, adventure.getPaymentCancellation());
		assertNull(adventure.getPaymentConfirmation());
		assertEquals(true, adventure.isInvoiceCancelled());
		assertNull(adventure.getInvoiceConfirmation());
	}
	
	@Test
	public void unsuccessSequenceNineBankExceptionConfirmed(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_HOTEL + PRICE_RENT) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION); 
				this.result = activityReservationData;
				this.times = 2;
						
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = roomBookingData;
				this.times = 2; 
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Rent Vehicle
				CarInterface.reserveVehicle(begin, end, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = rentingData;
				this.times = 2;
				
				rentingData.getAmmount();
				this.result = PRICE_RENT;
				
//				(4) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(5) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = INVOICE_CONFIRMATION;
								
//				(6) [CONFIRMED] getOperationData
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				
//				(7) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
//				(8) Booking UNDO
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
//				(9) Rent UNDO
				CarInterface.cancelReservation(RENT_CONFIRMATION);
				this.result = RENT_CANCELLATION;
				
//				(10) Payment UNDO
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
//				(11) Invoice UNDO
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.CONFIRMED, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
		assertEquals(ROOM_CANCELLATION, adventure.getRoomCancellation());
		assertNull(adventure.getRoomConfirmation());
		assertEquals(RENT_CANCELLATION, adventure.getVehicleCancellation());
		assertNull(adventure.getVehicleConfirmation());
		assertEquals(PAYMENT_CANCELLATION, adventure.getPaymentCancellation());
		assertNull(adventure.getPaymentConfirmation());
		assertEquals(true, adventure.isInvoiceCancelled());
		assertNull(adventure.getInvoiceConfirmation());
	}
	
	@Test
	public void unsuccessSequenceTenTaxExceptionConfirmed(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		
		double ammountToCharge = (PRICE_ACTIVITY + PRICE_HOTEL + PRICE_RENT) * (1 + MARGIN_OF_PROFIT);
		
		new Expectations() {
			{
//				(1) Activity Reservation
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION); 
				this.result = activityReservationData;
				this.times = 2;
						
				activityReservationData.getAmount();
				this.result = PRICE_ACTIVITY;
				
//				(2) Hotel Room Reservation
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ROOM_CONFIRMATION;
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = roomBookingData;
				this.times = 2; 
				
				roomBookingData.getAmmount();
				this.result = PRICE_HOTEL;
				
//				(3) Rent Vehicle
				CarInterface.reserveVehicle(begin, end, DRIVING_LICENSE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = RENT_CONFIRMATION;
				
				CarInterface.getRentingData(RENT_CONFIRMATION);
				this.result = rentingData;
				this.times = 2;
				
				rentingData.getAmmount();
				this.result = PRICE_RENT;
				
//				(4) Process Payment
				BankInterface.processPayment(IBAN_CLIENT, ammountToCharge);
				this.result = PAYMENT_CONFIRMATION;			

//				(5) Submit Invoice
				new InvoiceData(BROKER_NIF_SELLER, NIF_CLIENT, "ADVENTURE", ammountToCharge, begin);
				TaxInterface.submitInvoice((InvoiceData) withNotNull());
				this.result = INVOICE_CONFIRMATION;
								
//				(6) [CONFIRMED] getOperationData
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = bankOperationData;
					
//				(7) Activity UNDO
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
//				(8) Booking UNDO
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
//				(9) Rent UNDO
				CarInterface.cancelReservation(RENT_CONFIRMATION);
				this.result = RENT_CANCELLATION;
				
//				(10) Payment UNDO
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
//				(11) Invoice UNDO
				TaxInterface.cancelInvoice(INVOICE_CONFIRMATION);
				
			}
		};

		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
		assertEquals(State.BOOK_ROOM, adventure.getState());
		adventure.process();
		assertEquals(State.RENT_VEHICLE, adventure.getState());
		adventure.process();
		assertEquals(State.PROCESS_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.TAX_PAYMENT, adventure.getState());
		adventure.process();
		assertEquals(State.CONFIRMED, adventure.getState());
		adventure.process();
		assertEquals(State.UNDO, adventure.getState());
		adventure.process();
		assertEquals(State.CANCELLED, adventure.getState());
		
		assertEquals(ACTIVITY_CANCELLATION, adventure.getActivityCancellation());
		assertNull(adventure.getActivityConfirmation());
		assertEquals(ROOM_CANCELLATION, adventure.getRoomCancellation());
		assertNull(adventure.getRoomConfirmation());
		assertEquals(RENT_CANCELLATION, adventure.getVehicleCancellation());
		assertNull(adventure.getVehicleConfirmation());
		assertEquals(PAYMENT_CANCELLATION, adventure.getPaymentCancellation());
		assertNull(adventure.getPaymentConfirmation());
		assertEquals(true, adventure.isInvoiceCancelled());
		assertNull(adventure.getInvoiceConfirmation());
	}
	
	@After
	public void tearDown(){
		Broker.brokers.clear();
	}

}