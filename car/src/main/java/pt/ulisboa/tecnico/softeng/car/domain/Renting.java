package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Renting {
	private static String drivingLicenseFormat = "^[a-zA-Z]+\\d+$";
	private static int counter;

	private final String reference;
	private final String drivingLicense;
	private final LocalDate begin;
	private final LocalDate end;
	private int kilometers = -1;
	private final Vehicle vehicle;
	private double amount;
	private String cancellation;
	private LocalDate cancellationDate;
	private String nif;
	private String iban;
	private String paymentReference;
	private static final String Type = "vehicle";
	private String invoiceReference;
	private String cancelledPaymentReference = null;
	private boolean cancelledInvoice = false;

	public Renting(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle) {
		checkArguments(drivingLicense, begin, end, vehicle);
		this.reference = Integer.toString(++Renting.counter);
		this.drivingLicense = drivingLicense;
		this.begin = begin;
		this.end = end;
		this.vehicle = vehicle;
	}

	private void checkArguments(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle) {
		if (drivingLicense == null || !drivingLicense.matches(drivingLicenseFormat) || begin == null || end == null || vehicle == null
				|| end.isBefore(begin)) 
			throw new CarException();
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @return the drivingLicense
	 */
	public String getDrivingLicense() {
		return drivingLicense;
	}

	/**
	 * @return the begin
	 */
	public LocalDate getBegin() {
		return begin;
	}

	/**
	 * @return the end
	 */
	public LocalDate getEnd() {
		return end;
	}

	/**
	 * @return the vehicle
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}

	/**
	 * @param begin
	 * @param end
	 * @return <code>true</code> if this Renting conflicts with the given date
	 *         range.
	 */
	public boolean conflict(LocalDate begin, LocalDate end) {
		if (end.isBefore(begin)) {
			throw new CarException("Error: end date is before begin date.");
		} else if ((begin.equals(this.getBegin()) || begin.isAfter(this.getBegin()))
				&& (begin.isBefore(this.getEnd()) || begin.equals(this.getEnd()))) {
			return true;
		} else if ((end.equals(this.getEnd()) || end.isBefore(this.getEnd()))
				&& (end.isAfter(this.getBegin()) || end.isEqual(this.getBegin()))) {
			return true;
		} else if ((begin.isBefore(this.getBegin()) && end.isAfter(this.getEnd()))) {
			return true;
		}

		return false;
	}

	/**
	 * Settle this renting and update the kilometers in the vehicle.
	 * 
	 * @param kilometers
	 */
	public void checkout(int kilometers) {
		this.kilometers = kilometers;
		this.vehicle.addKilometers(this.kilometers);
	}

	public double getAmount() {
		// TODO Auto-generated method stub
		return this.amount;
	}
	
	public String cancel() {
		this.cancellation = "CANCEL" + this.reference;
		this.setCancellationDate(new LocalDate());
		
		this.vehicle.getRentACar().getProcessor().submitRenting(this);
		
		return this.cancellation;
	}
	
	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
	
	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}
	
	public boolean isCancelled() {
		return this.cancellation != null;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public static String getType() {
		return Type;
	}

	public String getInvoiceReference() {
		return invoiceReference;
	}

	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}

	public String getCancelledPaymentReference() {
		return cancelledPaymentReference;
	}

	public void setCancelledPaymentReference(String cancelledPaymentReference) {
		this.cancelledPaymentReference = cancelledPaymentReference;
	}

	public boolean isCancelledInvoice() {
		return cancelledInvoice;
	}

	public void setCancelledInvoice(boolean cancelledInvoice) {
		this.cancelledInvoice = cancelledInvoice;
	}
	
	public String getCancellation() {
		return cancellation;
	}

}
