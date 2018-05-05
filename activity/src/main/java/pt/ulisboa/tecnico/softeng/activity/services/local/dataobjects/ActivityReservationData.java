package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityReservationData {
	private String reference;
	private String cancellation;
	private String name;
	private String code;
	private String nifBuyer;
	private String ibanBuyer;
	private LocalDate begin;
	private LocalDate end;
	private LocalDate cancellationDate;
	private Double price;
	private String paymentReference;
	private String invoiceReference;

	public ActivityReservationData(){
	}
	
	public ActivityReservationData(ActivityProvider provider, ActivityOffer offer, Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancel();
		this.name = provider.getName();
		this.code = provider.getCode();
		this.begin = offer.getBegin();
		this.end = offer.getEnd();
		this.cancellationDate = booking.getCancellationDate();
		this.price = offer.getPrice();
		this.nifBuyer = booking.getBuyerNif();
		this.ibanBuyer = booking.getIban();
		this.paymentReference = booking.getPaymentReference();
		this.invoiceReference = booking.getInvoiceReference();
	}

	public ActivityReservationData(Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancel();
		this.name = booking.getActivityOffer().getActivity().getActivityProvider().getName();
		this.code = booking.getActivityOffer().getActivity().getActivityProvider().getCode();
		this.begin = booking.getActivityOffer().getBegin();
		this.end = booking.getActivityOffer().getEnd();
		this.cancellationDate = booking.getCancellationDate();
		this.price = booking.getAmount();
		this.nifBuyer = booking.getBuyerNif();
		this.ibanBuyer = booking.getIban();
		this.paymentReference = booking.getPaymentReference();
		this.invoiceReference = booking.getInvoiceReference();
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.code;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

	public Double getPrice() {
		return this.price;
	}

	public String getPaymentReference() {
		return this.paymentReference;
	}

	public String getInvoiceReference() {
		return this.invoiceReference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}

	public String getNifBuyer() {
		return nifBuyer;
	}

	public void setNifBuyer(String nifBuyer) {
		this.nifBuyer = nifBuyer;
	}

	public String getIbanBuyer() {
		return ibanBuyer;
	}

	public void setIbanBuyer(String ibanBuyer) {
		this.ibanBuyer = ibanBuyer;
	}

}
