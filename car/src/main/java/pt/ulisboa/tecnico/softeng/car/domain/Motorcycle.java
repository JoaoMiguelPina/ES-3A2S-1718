package pt.ulisboa.tecnico.softeng.car.domain;



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
	
	public void destroyMotorcycle() {
		
	}
	
}
