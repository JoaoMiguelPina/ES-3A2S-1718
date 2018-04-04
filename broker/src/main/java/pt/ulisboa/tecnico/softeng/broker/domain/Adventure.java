package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Adventure {
	private static Logger logger = LoggerFactory.getLogger(Adventure.class);

	public static enum State {
		PROCESS_PAYMENT, RESERVE_ACTIVITY, BOOK_ROOM, RENT_VEHICLE, TAX_PAYMENT, UNDO, CONFIRMED, CANCELLED
	}

	private static int counter = 0;

	private final String ID;
	private final Broker broker;
	private Client client;
	private double marginOfProfit;
	private boolean needsCar;
	private final LocalDate begin;
	private final LocalDate end;
	private int amount = 0;
	private String paymentConfirmation;
	private String paymentCancellation;
	private String roomConfirmation;
	private String roomCancellation;
	private String activityConfirmation;
	private String activityCancellation;
	private String vehicleConfirmation;
	private String vehicleCancellation;
	private String invoiceConfirmation;
	private String invoiceCancellation;

	private AdventureState state;

	public Adventure(Broker broker, LocalDate begin, LocalDate end, Client client, double marginOfProfit, boolean needsCar) {
		checkArguments(broker, begin, end, client, marginOfProfit, needsCar);

		this.ID = broker.getCode() + Integer.toString(++counter);
		this.broker = broker;
		this.begin = begin;
		this.end = end;
		this.client = client;
		this.marginOfProfit = marginOfProfit;
		this.needsCar = needsCar;

		broker.addAdventure(this);

		setState(State.RESERVE_ACTIVITY);
	}

	private void checkArguments(Broker broker, LocalDate begin, LocalDate end, Client client, double marginOfProfit, boolean needsCar) {
		if (broker == null || begin == null || end == null || client == null ) {
			throw new BrokerException();
		}

		if (end.isBefore(begin)) {
			throw new BrokerException();
		}

		if (marginOfProfit < 0 || marginOfProfit > 100) {
			throw new BrokerException();
		}
	}

	public String getID() {
		return this.ID;
	}

	public Broker getBroker() {
		return this.broker;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}
	
	public double getMarginOfProfit() {
		return this.marginOfProfit;
	}
	
	public boolean needsCar() {
		return this.needsCar;
	}
	
	public Client getClient() {
		return this.client;
	}

	public int getAmount() {
		return this.amount;
	}
	
	public void addAmount(int amount) {
		checkAmount(amount);
		this.amount += amount;
	}
	
	public void subtractAmount(int amount) {
		checkAmount(amount);
		this.amount -= amount;
	}
	
	public void checkAmount(int amount) {
		if (amount < 0) throw new BrokerException();
	}
	
	public void resetAmount() {
		this.amount = 0;
	}

	public String getPaymentConfirmation() {
		return this.paymentConfirmation;
	}

	public void setPaymentConfirmation(String paymentConfirmation) {
		this.paymentConfirmation = paymentConfirmation;
	}

	public String getPaymentCancellation() {
		return this.paymentCancellation;
	}

	public void setPaymentCancellation(String paymentCancellation) {
		this.paymentCancellation = paymentCancellation;
	}

	public String getActivityConfirmation() {
		return this.activityConfirmation;
	}

	public void setActivityConfirmation(String activityConfirmation) {
		this.activityConfirmation = activityConfirmation;
	}

	public String getActivityCancellation() {
		return this.activityCancellation;
	}

	public void setActivityCancellation(String activityCancellation) {
		this.activityCancellation = activityCancellation;
	}

	public String getRoomConfirmation() {
		return this.roomConfirmation;
	}

	public void setRoomConfirmation(String roomConfirmation) {
		this.roomConfirmation = roomConfirmation;
	}

	public String getRoomCancellation() {
		return this.roomCancellation;
	}

	public void setRoomCancellation(String roomCancellation) {
		this.roomCancellation = roomCancellation;
	}
	
	public String getVehicleConfirmation() {
		return this.vehicleConfirmation;
	}

	public void setVehicleConfirmation(String vehicleConfirmation) {
		this.vehicleConfirmation = vehicleConfirmation;
	}
	
	
	public String getVehicleCancellation() {
		return this.vehicleCancellation;
	}

	public void setVehicleCancellation(String vehicleCancellation) {
		this.vehicleCancellation = vehicleCancellation;
	}
	
	public String getInvoiceConfirmation() {
		return this.invoiceConfirmation;
	}

	public void setInvoiceConfirmation(String invoiceConfirmation) {
		this.invoiceConfirmation = invoiceConfirmation;
	}
	
	public String getInvoiceCancellation() {
		return this.invoiceCancellation;
	}

	public void setInvoiceCancellation(String invoiceCancellation) {
		this.invoiceCancellation = invoiceCancellation;
	}

	public State getState() {
		return this.state.getState();
	}

	public void setState(State state) {
		
		switch (state) {
		case PROCESS_PAYMENT:
			this.state = new ProcessPaymentState();
			break;
		case RESERVE_ACTIVITY:
			this.state = new ReserveActivityState();
			break;
			
		// TODO: Create 	TaxPaymentState() and RentVehicleState() classes (see #226, #221).
		// They are commented here so it is possible to run the tests.	
		/*	
		case TAX_PAYMENT:
			this.state = new TaxPaymentState();
			break;
		case RENT_VEHICLE:
			this.state = new RentVehicleState();
			break;	
		*/
			
		case BOOK_ROOM:
			this.state = new BookRoomState();
			break;
		case UNDO:
			this.state = new UndoState();
			break;
		case CONFIRMED:
			this.state = new ConfirmedState();
			break;
		case CANCELLED:
			this.state = new CancelledState();
			break;
		default:
			new BrokerException();
			break;
		}
	}

	public void process() {
		this.state.process(this);
	}

	public boolean cancelRoom() {
		return getRoomConfirmation() != null && getRoomCancellation() == null;
	}

	public boolean cancelActivity() {
		return getActivityConfirmation() != null && getActivityCancellation() == null;
	}

	public boolean cancelPayment() {
		return getPaymentConfirmation() != null && getPaymentCancellation() == null;
	}
	
	public boolean cancelVehicle() {
		return getVehicleConfirmation() != null && getVehicleCancellation() == null;
	}

	public boolean cancelInvoice() {
		return getInvoiceConfirmation() != null && getInvoiceCancellation() == null;
	}


}
