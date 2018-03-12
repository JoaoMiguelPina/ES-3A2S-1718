package pt.ulisboa.tecnico.softeng.car.domain;

import pt.ulisboa.tecnico.softeng.bank.domain.Car;
import pt.ulisboa.tecnico.softeng.bank.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.RentACar;

public class Car{
	private String plate;
	private int kilometers;
	private RentACar rentACar;
	
	public Bank(String plate, int , RentACar rentACar) {
		checkArguments(plate, kilometers, rentACar);

		this.plate = plate;
		this.kilometers = kilometers;
		this.rentACar = rentACar;

	}
	
	private void checkArguments(String plate, int kilometers, RentACar rentACar) {
		if(kilometers < 0 || plate == 0 || plate == "" || rentACar.getName() == null || rentACar.getName() == "" || rentACar == null) {
			//as especificações do rentACar também podem ser feitas quando criamos o rentACar uma vez que é uma classe
			throw new CarException();
		}
		// fazer ainda a especificação da matricula na classe car
		
	}
	
	public String getPlate() {
		return this.plate;
	}
	public Integer getKilometers() {
		return this.kilometers;
	}
	
	public RentACar getRentACar() {
		return this.rentACar;
	}
	
}
