package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Renting extends Renting_Base{
	private static String drivingLicenseFormat = "^[a-zA-Z]+\\d+$";
	private static final String type = "RENTAL";

	public Renting(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle, String buyerNIF,
			String buyerIBAN) {
		checkArguments(drivingLicense, begin, end, vehicle);
		setReference(Integer.toString(vehicle.getRentACar().getNextCounter()));
		setDrivingLicense(drivingLicense);
		setBegin(begin);
		setEnd(end);
		setVehicle(vehicle);
		setClientNIF(buyerNIF);
		setClientIBAN(buyerIBAN);
		setPrice(vehicle.getPrice() * (end.getDayOfYear() - begin.getDayOfYear()));
		
		setCancelledInvoice(false);
	}

	private void checkArguments(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle) {
		if (drivingLicense == null || !drivingLicense.matches(drivingLicenseFormat) || begin == null || end == null
				|| vehicle == null || end.isBefore(begin)) {
			throw new CarException();
		}
	}

	public boolean isCancelled() {
		return getCancellationReference != null && getCancellationDate != null;
	}
	
	public String getType() {
		return this.type;
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
		} else if ((getBegin.equals(this.getBegin()) || getBegin().isAfter(this.getBegin()))
				&& (getBegin.isBefore(this.getEnd()) || getBegin().equals(this.getEnd()))) {
			return true;
		} else if ((getEnd.equals(this.getEnd()) || getEnd().isBefore(this.getEnd()))
				&& (getEnd.isAfter(this.getBegin()) || getEnd.isEqual(this.getBegin()))) {
			return true;
		} else if (getBegin.isBefore(this.getBegin()) && getEnd.isAfter(this.getEnd())) {
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
		setKilometers(kilometers);
		getVehicle.addKilometers(getKilometers);
	}

	public String cancel() {
		setCancellationReference(getReference() + "CANCEL");
		setCancellationDate(LocalDate.now());

		this.getVehicle().getRentACar().getProcessor().submitRenting(this);

		return getCancellationReference;
	}

}
