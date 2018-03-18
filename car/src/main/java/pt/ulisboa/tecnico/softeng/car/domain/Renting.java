package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Renting{
	private static int counter = 0;

	private final String reference;
	private String drivingLicense;

	private String cancellation;
	private LocalDate cancellationDate;
	private final LocalDate begin;
	private final LocalDate end;
	private int kilometers;
	private Vehicle vehicle;


	Renting(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle) {
		checkArguments(drivingLicense, begin, end, vehicle);

		this.drivingLicense = drivingLicense;
		this.reference = Integer.toString(++Renting.counter);
		this.begin = begin;
		this.end = end;
		this.kilometers = 0;
		this.vehicle = vehicle;
		
		
	}

	private void checkArguments(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle) {
		if (drivingLicense == null || drivingLicense == ""  || begin == null || end == null || vehicle == null) {
			throw new CarException();
		}

		if (end.isBefore(begin)) {
			throw new CarException();
		}
		
		if (invalidDrivingLicence(drivingLicense)) {
			throw new CarException();
		}
	}
	
	public boolean invalidDrivingLicence(String drivingLicence){
		return !(drivingLicence.matches("[a-zA-Z]+[0-9]+"));
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancellation() {
		return this.cancellation;
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
	
	public String getDrivingLicense() {
		return drivingLicense;
	}
	
	public int getKilometers() {
		return kilometers;
	}


	public String cancel() {
		this.cancellation = this.reference + "CANCEL";
		this.cancellationDate = new LocalDate();
		return this.cancellation;
	}

	public boolean isCancelled() {
		return this.cancellation != null;
	}
	

	boolean conflict(LocalDate begin, LocalDate end) {
		if (isCancelled()) {
			return false;
		}

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
	
	public void checkout(int kilometers) {
		if(kilometers >= 0) {
			this.kilometers = kilometers;
			vehicle.setKilometers(vehicle.getKilometers() + kilometers);
		}
		else {
			throw new CarException();
		}
	}
	
}
