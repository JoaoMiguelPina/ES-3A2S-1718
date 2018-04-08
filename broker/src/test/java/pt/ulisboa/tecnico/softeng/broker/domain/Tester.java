package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

@RunWith(JMockit.class)
public class Tester {
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
	private static final String INVOICE_CANCELLATION = "InvoiceCancellation";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	
//	prices
	private static final double PRICE_ACTIVITY = 20.0;
	private static final double PRICE_HOTEL = 20.0;
	private static final double PRICE_RENT = 20.0;	
	
	@Test
	public void unsuccessSequenceFourBankExceptionCancelled(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface,
			@Mocked final CarInterface carInterface, @Mocked final TaxInterface taxInterface,
			@Mocked final ActivityReservationData activityReservationData, @Mocked final BankOperationData bankOperationData,
			@Mocked final RentingData rentingData, @Mocked final RoomBookingData roomBookingData,
			@Mocked final InvoiceData invoiceData) {
		broker = new Broker(BROKER_CODE, BROKER_NAME, BROKER_NIF_SELLER, BROKER_NIF_BUYER, IBAN_BROKER);
		client = new Client(broker, IBAN_CLIENT, NIF_CLIENT, DRIVING_LICENSE, AGE);
		
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE, BROKER_NIF_BUYER, IBAN_BROKER);
				this.result = ACTIVITY_CONFIRMATION;
			}
			
		};
		
		
		Adventure adventure = new Adventure(broker, begin, end, client, MARGIN_OF_PROFIT, true);

		assertEquals(State.RESERVE_ACTIVITY, adventure.getState());
		adventure.process();
	}
	

}
