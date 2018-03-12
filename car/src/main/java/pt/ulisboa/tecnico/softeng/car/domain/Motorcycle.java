package pt.ulisboa.tecnico.softeng.car.domain;

import pt.ulisboa.tecnico.softeng.car.exception.MotorcycleException;


public class Motorcycle{
	private String plate;
	private int kilometers;
	private RentACar rentACar;
	
	public Motorcycle(String plate, int kilometers, RentACar rentACar) {
		checkArguments(plate, kilometers, rentACar);

		this.plate = plate;
		this.kilometers = kilometers;
		this.rentACar = rentACar;

	}
	
	private void checkArguments(String plate, int kilometers, RentACar rentACar) {
		if(kilometers < 0 || plate == null || plate == "" || rentACar.getName() == null || rentACar.getName() == "" || rentACar == null) {
			//as especificações do rentACar também podem ser feitas quando criamos o rentACar uma vez que é uma classe
			throw new MotorcycleException();
		}
		// fazer ainda a especificação da matricula na classe car
		
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
