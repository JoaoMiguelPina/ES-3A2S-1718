package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.*;

public abstract class Vehicle {
	private String plate;
	private int kilometers;
	
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
}