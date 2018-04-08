package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking {
	private static int counter = 0;
	
	private static final String type = "HOUSING";

	private final String reference;
	private String paymentReference;
	private String invoiceReference;
	private String cancellation;
	private LocalDate cancellationDate;
	private final LocalDate arrival;
	private final LocalDate departure;
	private final String hotelNif;
	private final String nif;
	private final String iban;
	private final double amount;
	private String cancel;
	private boolean cancelledInvoice = false;
	private String cancelledPaymentReference = null;

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure, String nif, String iban, Type type) {
		checkArguments(hotel, arrival, departure, nif, iban, type);

		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		
		int numberOfDays = departure.getDayOfYear() - arrival.getDayOfYear();
		
		if (type == Type.SINGLE) {
			this.amount = hotel.getPriceSingle() * numberOfDays;
		}
		else {
			this.amount = hotel.getPriceDouble() * numberOfDays;
		}
		this.arrival = arrival;
		this.departure = departure;
		this.hotelNif = hotel.getNIF();
		this.nif = nif;
		this.iban = iban;
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure, String nif, String iban, Type type) {
		if (hotel == null || arrival == null || departure == null || nif == null || nif.trim().length() == 0 || iban == null
				|| iban.trim().length() == 0 || type == null) {
			throw new HotelException();
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}

	public String getReference() {
		return this.reference;
	}
	
	public String getNif() {
		return this.nif;
	}
	
	public String getHotelNif() {
		return this.hotelNif;
	}
	
	public String getType() {
		return Booking.type;
	}
	
	public String getCancel() {
		return this.cancel;
	}
	
	public double getAmount() {
		return this.amount;
	}

	public String getIban() {
		return this.iban;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public LocalDate getArrival() {
		return this.arrival;
	}

	public LocalDate getDeparture() {
		return this.departure;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public String getPaymentReference() {
		return this.paymentReference;
	}
	
	public boolean getCancelledInvoice() {
		return this.cancelledInvoice;
	}
	
	public String getCancelledPaymentReference() {
		return this.cancelledPaymentReference;
	}

	public void setCancelledInvoice(Boolean cancelledInvoice) {
		this.cancelledInvoice = cancelledInvoice;
	}
	
	public void setCancelledPaymentReference(String cancelledPaymentReference) {
		this.cancelledPaymentReference = cancelledPaymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}
	
	public String getInvoiceReference() {
		return this.invoiceReference;
	}

	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}
	
	boolean conflict(LocalDate arrival, LocalDate departure) {
		if (isCancelled()) {
			return false;
		}

		if (arrival.equals(departure)) {
			return true;
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}

		if ((arrival.equals(this.arrival) || arrival.isAfter(this.arrival)) && arrival.isBefore(this.departure)) {
			return true;
		}

		if ((departure.equals(this.departure) || departure.isBefore(this.departure))
				&& departure.isAfter(this.arrival)) {
			return true;
		}

		if ((arrival.isBefore(this.arrival) && departure.isAfter(this.departure))) {
			return true;
		}

		return false;
	}

	public String cancel() {
		this.cancellation = this.reference + "CANCEL";
		this.cancellationDate = new LocalDate();
		return this.cancellation;
	}

	public boolean isCancelled() {
		return this.cancellation != null;
	}

}
