package pt.ulisboa.tecnico.softeng.car.domain;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Car extends Vehicle{
	private RentACar rentACar;
	
	public Car(String plate, int kilometers, RentACar rentACar) {
		super(plate, kilometers);
		if(rentACar == null) 
			throw new CarException();
		this.rentACar = rentACar;
		rentACar.addCar(this);

	}
	
	public RentACar getRentACar() {
		return this.rentACar;
	}
	
}
