package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;

public class RentACar {
	public static final Set<RentACar> rentACars = new HashSet<>();

	private static int counter;

	private final String name;
	private final String code;
	private final Map<String, Vehicle> vehicles = new HashMap<>();
	private final String NIF;
	private final String IBAN;
	private Processor processor = new Processor();

	public RentACar(String name, String nif, String iban) {
		checkArguments(name);
		this.NIF = nif;
		this.IBAN = iban;
		this.name = name;
		this.code = Integer.toString(++RentACar.counter);

		rentACars.add(this);
	}

	private void checkArguments(String name) {
		if (name == null || name.isEmpty()) {
			throw new CarException();
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	void addVehicle(Vehicle vehicle) {
		this.vehicles.put(vehicle.getPlate(), vehicle);
	}

	public boolean hasVehicle(String plate) {
		return vehicles.containsKey(plate);
	}

	public Set<Vehicle> getAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		Set<Vehicle> availableVehicles = new HashSet<>();
		for (Vehicle vehicle : this.vehicles.values()) {
			if (cls == vehicle.getClass() && vehicle.isFree(begin, end)) {
				availableVehicles.add(vehicle);
			}
		}
		return availableVehicles;
	}

	private static Set<Vehicle> getAllAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		Set<Vehicle> vehicles = new HashSet<>();
		for (RentACar rentACar : rentACars) {
			vehicles.addAll(rentACar.getAvailableVehicles(cls, begin, end));
		}
		return vehicles;
	}

	public static Set<Vehicle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		return getAllAvailableVehicles(Motorcycle.class, begin, end);
	}

	public static Set<Vehicle> getAllAvailableCars(LocalDate begin, LocalDate end) {
		return getAllAvailableVehicles(Car.class, begin, end);
	}

	/**
	 * Lookup for a renting using its reference.
	 * 
	 * @param reference
	 * @return the renting with the given reference.
	 */
	public static Renting getRenting(String reference) {
		for (RentACar rentACar : rentACars) {
			for (Vehicle vehicle : rentACar.vehicles.values()) {
				Renting renting = vehicle.getRenting(reference);
				if (renting != null) {
					return renting;
				}
			}
		}
		return null;
	}

	public static RentingData getRentingData(String reference) {
		Renting renting = getRenting(reference);
		if (renting == null) {
			throw new CarException();
		}
		if(BankInterface.getOperationData(reference) == null) {
			throw new CarException();
		}
		if(TaxInterface.getInvoiceData(reference) == null) {
			throw new CarException();
		}
		
		return new RentingData(
			renting.getReference(),
			renting.getVehicle().getPlate(),
			renting.getDrivingLicense(),
			renting.getVehicle().getRentACar().getCode(),
			renting.getBegin(),
			renting.getEnd(), renting.getAmount()
		);
	}

	public String getNIF() {
		return NIF;
	}

	public String getIBAN() {
		return IBAN;
	}
	
	public Processor getProcessor() {
		return this.processor;
	}
	
	public static String cancelReservation(String reference) {
		Renting renting = getRenting(reference);
		if (renting != null) 
			return renting.cancel();
		throw new CarException();
	}	
	
	public static String reserveVehicle(LocalDate begin, LocalDate end, String drivingLicense , String nif, String iban) throws CarException {
		Set<Vehicle> availableSet = getAllAvailableVehicles(Car.class, begin, end);
		if(availableSet.size() == 0)
			throw new CarException();
		Vehicle vehicle = availableSet.iterator().next();
		Renting renting = new Renting(drivingLicense, begin, end, vehicle);
		return renting.getReference();
	}
	
	public Set<RentACar> getRentACars() {
		return RentACar.rentACars;
	}
}
