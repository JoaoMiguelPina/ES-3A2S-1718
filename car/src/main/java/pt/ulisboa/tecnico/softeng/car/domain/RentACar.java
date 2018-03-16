package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACar{
	public static Set<RentACar> rentACars = new HashSet<>();
	private Set<Vehicle> vehicles = new HashSet<>();
	private Set<Car> cars = new HashSet<>();
	private Set<Motorcycle> motorcycles = new HashSet<>();
	private final String name;
	private static int counter = 0;
	private final String code;

	public RentACar(String name) {
		this.code = Integer.toString(++RentACar.counter);
		checkName(name);
		this.name = name;
		RentACar.rentACars.add(this);
	}

	public Set<Vehicle> getVehicles() {
		return this.vehicles;
	}
	
	public Set<Car> getCars() {
		return this.cars;
	}
	
	public Set<Motorcycle> getMotorycles() {
		return this.motorcycles;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCode() {
		return this.code;
	}
	
	private void checkName(String name) {
		if(name == null || name.equals("") || (name.trim()).equals("")){
			throw new CarException();
		}		
	}
	
	void addCar(Car car) {
		this.cars.add(car);
		this.vehicles.add(car);
	}
	
	void addMotorcycle(Motorcycle motorcycle) {
		this.motorcycles.add(motorcycle);
		this.vehicles.add(motorcycle);
	}
	
	Set<Car> getAllAvailableCars(LocalDate begin, LocalDate end) {
		Set<Car> cars = new HashSet<>();
		for (RentACar rentacar : rentACars) {
			for (Car car : rentacar.cars) {
				if (car.isFree(begin, end)) {
					cars.add(car);
				}
			}
		}
		return cars;
	}
	
	
	Set<Motorcycle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		Set<Motorcycle> motorcycles = new HashSet<>();
		for (RentACar rentacar : rentACars) {
			for (Motorcycle motorcycle : rentacar.motorcycles) {
				if (motorcycle.isFree(begin, end)) {
					motorcycles.add(motorcycle);
				}
			}
		}
		return motorcycles;
	}
	
	public Renting getRenting(String reference) {
		for (Vehicle vehicle : this.vehicles) {
			Renting renting = vehicle.getRenting(reference);
			if (renting != null) {
				return renting;
			}
		}
		return null;
	}
	
	public static RentingData getRentingData(String reference) {
		for (RentACar rentACar : RentACar.rentACars) {
			for (Vehicle vehicle : rentACar.getVehicles()) {
				Renting renting = vehicle.getRenting(reference);
				if (renting != null) {
					return new RentingData(reference, vehicle.getPlate(), renting.getDrivingLicense(), rentACar.getCode(), renting.getBegin(), renting.getEnd());
				}
			}
		}
		throw new CarException();
	}
}
