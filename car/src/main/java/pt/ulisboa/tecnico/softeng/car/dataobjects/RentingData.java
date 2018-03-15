package pt.ulisboa.tecnico.softeng.car.dataobjects;

import org.joda.time.LocalDate;

public class RentingData {
	private String reference;
	private String plate;
	private String drivingLicense;
	private String rentACarCode;
	private LocalDate begin;
	private LocalDate end;

	public RentingData() {
	}

	public RentingData(String reference, String plate, String drivingLicense, String rentACarCode, LocalDate begin, LocalDate end) {
		this.reference = reference;
		this.plate = plate;
		this.drivingLicense = drivingLicense;
		this.rentACarCode = rentACarCode;
		this.begin = begin;
		this.end = end;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public String getRentACarCode() {
		return rentACarCode;
	}

	public void setRentACarCode(String rentACarCode) {
		this.rentACarCode = rentACarCode;
	}

	public LocalDate getBegin() {
		return begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}
	
	

}