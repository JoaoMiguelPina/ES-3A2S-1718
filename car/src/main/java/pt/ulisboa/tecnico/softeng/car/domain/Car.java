package pt.ulisboa.tecnico.softeng.car.domain;



public class Car extends Vehicle{
	private RentACar rentACar;
	
	public Car(String plate, int kilometers, RentACar rentACar) {
		super(plate, kilometers);
		
		this.rentACar = rentACar;
		rentACar.addCar(this);

	}
	
	public RentACar getRentACar() {
		return this.rentACar;
	}
	
	public void destroyCar() {
		
	}
	
}
