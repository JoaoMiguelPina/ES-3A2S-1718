package pt.ulisboa.tecnico.softeng.car.domain;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Car{
	private String plate;
	private int kilometers;
	private RentACar rentACar;
	
	public Car(String plate, int kilometers, RentACar rentACar) {
		checkArguments(plate, kilometers, rentACar);

		this.plate = plate;
		this.kilometers = kilometers;
		this.rentACar = rentACar;
		rentACar.addCar(this);

	}
	
	private void checkArguments(String plate, int kilometers, RentACar rentACar) {
		if(kilometers < 0 || plate == null || plate == "" || rentACar.getName() == null || rentACar.getName() == "" || rentACar == null) {
			//as especificações do rentACar também podem ser feitas quando criamos o rentACar uma vez que é uma classe
			throw new CarException();
		}
		//Aplicar restricao da matricula da mota
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
	
	public String getPlate() {
		return this.plate;
	}
	public int getKilometers() {
		return this.kilometers;
	}
	
	public RentACar getRentACar() {
		return this.rentACar;
	}
	
}
