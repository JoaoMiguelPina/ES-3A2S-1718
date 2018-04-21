package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public abstract class Vehicle extends Vehicle_Base{
	private static Logger logger = LoggerFactory.getLogger(Vehicle.class);

	private static String plateFormat = "..-..-..";

	public void init(String plate, int kilometers, double price, RentACar rentACar) {
		logger.debug("Vehicle plate: {}", plate);
		checkArguments(plate, kilometers, rentACar);

		setKilometers(kilometers);
		setPrice(price);
		setRentACar(rentACar);

		getPlates.add(plate.toUpperCase());
		getRentACars.addVehicle(this);
	}
	

	private void checkArguments(String plate, int kilometers, RentACar rentACar) {
		if (plate == null || !plate.matches(plateFormat) || plates.contains(plate.toUpperCase())) {
			throw new CarException();
		} else if (kilometers < 0) {
			throw new CarException();
		} else if (rentACar == null) {
			throw new CarException();
		}
		
		for( RentACar rac: getRentACars()) {
			if(rac.hasVehicle(plate)) {
				throw new CarException();
			}
		}
	}

	/**
	 * @param kilometers
	 *            the kilometers to set
	 */
	public void addKilometers(int kilometers) {
		if (kilometers < 0) {
			throw new CarException();
		}
		int n = getKilometers += kilometers;
		setKilometers(n);
	}

	public void delete() {
		for(Renting renting: getRentingsSet()) {
			renting.delete();
		}
		setRentACar(rentACar);
		deleteDomainObject();
		
	}
	

	public boolean isFree(LocalDate begin, LocalDate end) {
		if (begin == null || end == null) {
			throw new CarException();
		}
		for (Renting renting : getRentingsSet()) {
			if (renting.conflict(begin, end)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Add a <code>Renting</code> object to the vehicle. Use with caution --- no
	 * validation is being made.
	 *
	 * @param renting
	 */
	private void addRenting(Renting renting) {
		getRentingsSet().add(renting);
	}

	/**
	 * Lookup for a <code>Renting</code> with the given reference.
	 *
	 * @param reference
	 * @return Renting with the given reference
	 */
	public Renting getRenting(String reference) {
		if (reference == null || reference.isEmpty()) {
			throw new CarException();
		}

		for (Renting renting : getRentingsSet()) {
			if (renting.getReference().equals(reference)) {
				return renting;
			}
		}
		return null;
	}

	/**
	 * @param drivingLicense
	 * @param begin
	 * @param end
	 * @return
	 */
	public Renting rent(String drivingLicense, LocalDate begin, LocalDate end, String buyerNIF, String buyerIBAN) {
		if (!isFree(begin, end)) {
			throw new CarException();
		}

		Renting renting = new Renting(drivingLicense, begin, end, this, buyerNIF, buyerIBAN);
		this.addRenting(renting);

        getRentACar().getProcessor().submitRenting(renting);


        return renting;
	}
}
