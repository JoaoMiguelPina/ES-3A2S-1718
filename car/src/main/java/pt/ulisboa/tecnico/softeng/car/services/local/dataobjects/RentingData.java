package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.domain.Renting;

public class RentingData {
	private String reference;
	private String plate;
	private String drivingLicense;
	private String rentACarCode;
	private LocalDate begin;
	private LocalDate end;
	private String paymentReference;
	private String invoiceReference;
	private double price;
	private String nif;
	private String iban;
	
	public RentingData() {
	}

	public RentingData(Renting renting) {
		this.reference = renting.getReference();
		this.plate = renting.getVehicle().getPlate();
		this.drivingLicense = renting.getDrivingLicense();
		this.rentACarCode = renting.getVehicle().getRentACar().getCode();
		this.begin = renting.getBegin();
		this.end = renting.getEnd();
		this.paymentReference = renting.getPaymentReference();
		this.invoiceReference = renting.getInvoiceReference();
		this.price = renting.getPrice();
		this.nif = renting.getClientNif();
		this.iban = renting.getClientIban();
		
	}

	public String getReference() {
		return reference;
	}

	public String getPlate() {
		return plate;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public String getRentACarCode() {
		return rentACarCode;
	}

	public LocalDate getBegin() {
		return begin;
	}

	public LocalDate getEnd() {
		return end;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public String getInvoiceReference() {
		return invoiceReference;
	}

	public double getPrice() {
		return price;
	}

	public String getNif() {
		return nif;
	}

	public String getIban() {
		return iban;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public void setRentACarCode(String rentACarCode) {
		this.rentACarCode = rentACarCode;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

}
