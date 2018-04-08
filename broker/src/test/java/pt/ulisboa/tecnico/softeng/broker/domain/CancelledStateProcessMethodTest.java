package pt.ulisboa.tecnico.softeng.broker.domain;

import java.rmi.RemoteException;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
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
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class CancelledStateProcessMethodTest {
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private static final String RENT_CONFIRMATION = "RentConfirmation";
	private static final String RENT_CANCELLATION = "RentCancellation";
	private static final String INVOICE_CONFIRMATION = "InvoiceConfirmation";
	private static final String INVOICE_CANCELLATION = "InvoiceCancellation";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;
	private Client client;
	
	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, this.client, 0.2, true);
		this.adventure.setState(State.CANCELLED);
	}

	@Test
	public void allAlreadyCancelled(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface) {

		setCancel(true, true, true, true, true);
		
		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());

		new Verifications() {
			{
				ActivityInterface.getActivityReservationData(this.anyString);
				this.times = 0;

				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
				
				CarInterface.getRentingData(this.anyString);
				this.times = 0;
				
				BankInterface.getOperationData(this.anyString);
				this.times = 0;
				
				TaxInterface.cancelInvoice(this.anyString);
				this.times = 0;
			}
		};
	}
	
	@Test
	public void nothingCancelled(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface) {

		setCancel(true, true, true, true, true);
		
		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());

		new Verifications() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CANCELLATION);

				HotelInterface.getRoomBookingData(ROOM_CANCELLATION);
				
				CarInterface.getRentingData(RENT_CANCELLATION);
				
				BankInterface.getOperationData(PAYMENT_CANCELLATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				
				TaxInterface.cancelInvoice(INVOICE_CANCELLATION);
			}
		};
	}
	
	@Test
	public void notCancelledActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		setCancel(false, true, true, true, true);
		

		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CANCELLATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	
	@Test
	public void cancellActivityActivityException(@Mocked final ActivityInterface activityInterface) {
		setCancel(false, true, true, true, true);
		

		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CANCELLATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void cancellActivityRemoteException(@Mocked final ActivityInterface activityInterface) {
		setCancel(false, true, true, true, true);
		

		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CANCELLATION);
				this.result = new RemoteException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	
	@Test
	public void notCancelledRoom(@Mocked final HotelInterface hotelInterface) {
		setCancel(true, false, true, true, true);
		

		new Expectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CANCELLATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	
	@Test
	public void cancellRoomHotelException(@Mocked final HotelInterface hotelInterface) {
		setCancel(true, false, true, true, true);
		

		new Expectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CANCELLATION);
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void cancellRoomRemoteException(@Mocked final HotelInterface hotelInterface) {
		setCancel(true, false, true, true, true);
		

		new Expectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CANCELLATION);
				this.result = new RemoteException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void notCancelledRent(@Mocked final CarInterface carInterface) {
		setCancel(true, true, false, true, true);
		

		new Expectations() {
			{
				CarInterface.getRentingData(RENT_CANCELLATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	
	@Test
	public void cancellRentCarException(@Mocked final CarInterface carInterface) {
		setCancel(true, true, false, true, true);
		

		new Expectations() {
			{
				CarInterface.getRentingData(RENT_CANCELLATION);
				this.result = new CarException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void cancellRentRemoteException(@Mocked final CarInterface carInterface) {
		setCancel(true, true, false, true, true);
		

		new Expectations() {
			{
				CarInterface.getRentingData(RENT_CANCELLATION);
				this.result = new RemoteException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledPaymentFirstBankException(@Mocked final BankInterface bankInterface) {
		setConfirm(false, false, false, true, false);
		setCancel(false, false, false, true, false);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledPaymentFirstRemoteAccessException(@Mocked final BankInterface bankInterface) {
		setConfirm(false, false, false, true, false);
		setCancel(false, false, false, true, false);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledPaymentSecondBankException(@Mocked final BankInterface bankInterface) {
		setConfirm(false, false, false, true, false);
		setCancel(false, false, false, true, false);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledPaymentSecondRemoteAccessException(@Mocked final BankInterface bankInterface) {
		setConfirm(false, false, false, true, false);
		setCancel(false, false, false, true, false);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void cancelledPayment(@Mocked final BankInterface bankInterface) {
		setConfirm(false, false, false, true, false);
		setCancel(false, false, false, true, false);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				BankInterface.getOperationData(PAYMENT_CANCELLATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void notCancelledInvoice(@Mocked final TaxInterface taxInterface) {
		setCancel(true, true, true, true, false);
		

		new Expectations() {
			{
				TaxInterface.getInvoiceData(INVOICE_CANCELLATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	
	@Test
	public void cancellInvoiceTaxException(@Mocked final TaxInterface taxInterface) {
		setCancel(true, true, true, true, false);
		

		new Expectations() {
			{
				TaxInterface.getInvoiceData(INVOICE_CANCELLATION);
				this.result = new TaxException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void cancellInvoiceRemoteException(@Mocked final TaxInterface taxInterface) {
		setCancel(true, true, true, true, false);
		

		new Expectations() {
			{
				TaxInterface.getInvoiceData(INVOICE_CANCELLATION);
				this.result = new RemoteException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	private void setCancel(boolean activity, boolean hotel, boolean vehicle, boolean bank, boolean tax){
		if(activity)
			this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		if(hotel)
			this.adventure.setRoomCancellation(ROOM_CANCELLATION);
		if(vehicle)
			this.adventure.setVehicleCancellation(RENT_CANCELLATION);
		if(bank)
			this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		if(tax)
			this.adventure.setInvoiceCancellation(INVOICE_CANCELLATION);;
	}
	
	private void setConfirm(boolean activity, boolean hotel, boolean vehicle, boolean bank, boolean tax){
		if(activity)
			this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		if(hotel)
			this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		if(vehicle)
			this.adventure.setVehicleConfirmation(RENT_CONFIRMATION);
		if(bank)
			this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		if(tax)
			this.adventure.setInvoiceConfirmation(INVOICE_CONFIRMATION);
	}
}