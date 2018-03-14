package pt.ulisboa.tecnico.softeng.car.domain;

import pt.ulisboa.tecnico.softeng.car.exception.MotorcycleException;

public class Motorcycle extends Vehicle{
	private RentACar rentACar;
	
	public Motorcycle(String plate, int kilometers, RentACar rentACar) {
		super(plate, kilometers);
		
		this.rentACar = rentACar;
		
		rentACar.addMotorcycle(this);

	}
	
	public RentACar getRentACar() {
		return this.rentACar;
	}
	
}
