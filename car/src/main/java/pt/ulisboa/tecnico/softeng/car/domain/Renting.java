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


	Renting(String drivingLicense, LocalDate begin, LocalDate end) {
		checkArguments(drivingLicense, begin, end);

		this.drivingLicense = drivingLicense;
		this.reference = Integer.toString(++Renting.counter);
		this.begin = begin;
		this.end = end;
		this.kilometers = 0;
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
}
