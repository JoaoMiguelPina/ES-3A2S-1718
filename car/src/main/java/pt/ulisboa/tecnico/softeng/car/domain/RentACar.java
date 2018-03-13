package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACar{
	public static Set<RentACar> rentacars = new HashSet<>();
	//public Set<Vehicle> vehicles = new HashSet<>();
	private final String name;
	private static int counter = 0;
	private final String code;

	public RentACar(String name) {
		this.code = Integer.toString(++RentACar.counter);
		checkName(name);
		this.name = name;
		RentACar.rentacars.add(this);
	}

	public String getName() {
		return this.name;
	}
	
	private void checkName(String name) {
		if(name == null || name.equals("") || (name.trim()).equals("")){
			throw new CarException();
		}		
	}
}
