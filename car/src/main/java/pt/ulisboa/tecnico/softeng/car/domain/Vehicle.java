package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public abstract class Vehicle {
	public static Set<Vehicle> vehicles = new HashSet<>();

	private String plate;
	private int kilometers;
	private Set<Renting> rentings = new HashSet<>();
	
	public Vehicle(String plate, int kilometers){
		checkArguments(plate, kilometers);
		
		this.plate = plate;
		this.kilometers = kilometers;
		
		Vehicle.vehicles.add(this);
		
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
		
		//Aplicar restricao de nao poder ser repetido
		for(Vehicle v : vehicles) {
			if (v.getPlate() == plate)
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
	
	boolean conflict(LocalDate arrival, LocalDate departure) {
		
		for ( Renting renting: rentings) {
			boolean vality = renting.conflict(arrival, departure);
			if (vality) {
				return true;
				}
		}
		return false;
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

		Renting renting = new Renting(drivingLicence, arrival, departure, this);
		this.rentings.add(renting);

		return renting;
	}
	
	public int getNumberOfRentings() {
		int count = 0;
		for (Renting renting : this.rentings) {
			if (!renting.isCancelled()) {
				count++;
			}
		}
		return count;
	}
	
	public void setKilometers(int kilometers) {
		this.kilometers = kilometers;
	}
	
	public static void destroyVehicles() {
		vehicles.clear();
	}
}