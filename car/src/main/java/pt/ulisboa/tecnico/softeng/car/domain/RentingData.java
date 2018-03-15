package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingData{
	
	private static int counter = 0;
	private final String reference;
	private String plate;
	private String drivingLicense;
	private String rentACarCode;
	private final LocalDate begin;
	private final LocalDate end;
	
	public static Set<String> _references = new HashSet<>();

	RentingData(String reference, String plate, String drivingLicense, String rentACarCode, LocalDate begin, LocalDate end) {
		checkArguments(drivingLicense, begin, end);
		
		this.reference = Integer.toString(++RentingData.counter);
		this.plate = plate;
		this.drivingLicense = drivingLicense;
		this.rentACarCode = rentACarCode;
		this.begin = begin;
		this.end = end;
		
		RentingData._references.add(reference);
	}

	private void checkArguments(String drivingLicence, LocalDate begin, LocalDate end) {
		if (drivingLicence == null || drivingLicence == ""  || begin == null || end == null) {
			throw new CarException();
		}

		if (end.isBefore(begin)) {
			throw new CarException();
		}
		
		if (invalidDrivingLicence(this.drivingLicense)) {
			throw new CarException();
		}
	}
	
	public boolean invalidDrivingLicence(String drivingLicence){
		if(drivingLicence == null || drivingLicence == "") {
			return true;
		}
		
		String[] servicoEmissor = drivingLicence.split("\\d+");
		String[] ordinal = drivingLicence.split("[a-zA-Z]+");
		if (servicoEmissor.length != 1 || ordinal.length != 1) {
			return true;
		}
		
		String checkDriving = servicoEmissor[0] + ordinal[0];
		if(checkDriving != drivingLicence) {
			return true;
		}
		
		return false;
	}

	public String getReference() {
		return this.reference;
	}
	
	public String getPlate() {
		return this.plate;
	}
	
	public String getDrivingLicense() {
		return drivingLicense;
	}
	
	public String getRentACarCode() {
		return rentACarCode;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}
	
	
	boolean conflict(LocalDate begin, LocalDate end) {

		if (begin.equals(end)) {
			return true;
		}

		if (end.isBefore(begin)) {
			throw new CarException();
		}

		if ((begin.equals(this.begin) || begin.isAfter(this.begin)) && begin.isBefore(this.end)) {
			return true;
		}

		if ((end.equals(this.end) || end.isBefore(this.end))
				&& end.isAfter(this.begin)) {
			return true;
		}

		if ((begin.isBefore(this.begin) && end.isAfter(this.end))) {
			return true;
		}

		return false;
	}
	
	public void destroyRentingData() {
		
	}
}
