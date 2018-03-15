package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.*;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public abstract class Vehicle {
	private String plate;
	private int kilometers;
	public Set<Renting> rentings = new HashSet<>();
	
	public Vehicle(String plate, int kilometers){
		checkArguments(plate, kilometers);
		
		this.plate = plate;
		this.kilometers = kilometers;
		
	}
	
	private void checkArguments(String plate, int kilometers){
		checkPlate(plate);
		checkKilometers(kilometers);
	}
	
	private void checkPlate(String plate) {
		if(plate == null || plate == "") {
			throw new CarException();
		}
		//Aplicar restricao da matricula da mota e do carro
		//Assumindo que a matricula tem o formato NUMEROS-NUMEROS-LETRAS
		//Assumindo que a matricula tem letras em CAPS
		if(
			plate.charAt(0) < 48 || plate.charAt(0) > 57 || 
			plate.charAt(1) < 48 || plate.charAt(1) > 57 || 
			plate.charAt(3) < 48 || plate.charAt(3) > 57 || 
			plate.charAt(4) < 48 || plate.charAt(4) > 57 ||
			plate.charAt(6) < 65 || plate.charAt(6) > 90 ||
			plate.charAt(7) < 65 || plate.charAt(7) > 90) {
		throw new CarException();
		}
	}

	private void checkKilometers(int kilometers) {
		if(kilometers < 0) {
			throw new CarException();
		}		
	}
	
	public String getPlate() {
		return this.plate;
	}
	
	public int getKilometers() {
		return this.kilometers;
	}
	
	public Renting getRenting(String reference) {
		for (Renting renting : this.rentings) {
			if (renting.getReference().equals(reference)
					|| (renting.isCancelled() && renting.getCancellation().equals(reference))) {
				return renting;
			}
		}
		return null;
	}

	boolean isFree(LocalDate arrival, LocalDate departure) {
		for (Renting renting : this.rentings) {
			if (renting.conflict(arrival, departure)) {
				return false;
			}
		}

		return true;
	}
	
	public Renting rent(String drivingLicence, LocalDate arrival, LocalDate departure) {
		if (!isFree(arrival, departure)) {
			throw new CarException();
		}

		Renting renting = new Renting(drivingLicence, arrival, departure);
		this.rentings.add(renting);

		return renting;
	}
}