package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACar{
	public static Set<RentACar> rentACars = new HashSet<>();
	public Set<Car> cars = new HashSet<>();
	public Set<Motorcycle> motorcycles = new HashSet<>();
	private final String name;
	private static int counter = 0;
	private final String code;

	public RentACar(String name) {
		this.code = Integer.toString(++RentACar.counter);
		checkName(name);
		this.name = name;
		RentACar.rentACars.add(this);
	}

	public String getName() {
		return this.name;
	}
	
	private void checkName(String name) {
		if(name == null || name.equals("") || (name.trim()).equals("")){
			throw new CarException();
		}		
	}
	
	void addCar(Car car) {
		this.cars.add(car);
	}
	
}
