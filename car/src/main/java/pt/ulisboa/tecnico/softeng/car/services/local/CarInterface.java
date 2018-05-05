package pt.ulisboa.tecnico.softeng.car.services.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.Motorcycle;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.Renting;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

public class CarInterface {
	
	@Atomic(mode = TxMode.READ)
	public static List<RentACarData> getRentACars() {
		return FenixFramework.getDomainRoot().getRentACarSet().stream().map(h -> new RentACarData(h))
				.collect(Collectors.toList());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createRentACar(RentACarData rentACarData) {
		new RentACar(rentACarData.getName(), rentACarData.getNif(), rentACarData.getIban());
	}
	
	@Atomic(mode = TxMode.READ)
	public static RentACarData getRentACarDataByCode(String code) {
		RentACar rentACar = getRentACarByCode(code);

		if (rentACar != null) {
			return new RentACarData(rentACar);
		}

		return null;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createVehicle(String rentACarCode, VehicleData vehicleData) {
		if(vehicleData.getVehicleClass() == Car.class ) {
			new Car(vehicleData.getPlate(), vehicleData.getKilometers(), vehicleData.getPrice(), getRentACarByCode(rentACarCode));
		}
		else if(vehicleData.getVehicleClass() == Motorcycle.class) {
			new Motorcycle(vehicleData.getPlate(), vehicleData.getKilometers(), vehicleData.getPrice(), getRentACarByCode(rentACarCode));
		}
		
	}

	/*@Atomic(mode = TxMode.READ)
	public static VehicleData getVehicleDataByPlate(String code, String plate) {
		Vehicle vehicle = getVehicleByPlate(code, plate);
		if (vehicle == null) {
			return null;
		}

		return new VehicleData(vehicle);
	}*/

	/*@Atomic(mode = TxMode.WRITE)
	public static void createRenting(String code, String plate, RentingData renting) {
		Vehicle vehicle = getVehicleByPlate(code, plate);
		if (vehicle == null) {
			throw new CarException();
		}

		new Renting(renting.getDrivingLicense(), renting.getBegin(), renting.getEnd(), vehicle, renting.getNif(), renting.getIban());
	}*/

	@Atomic(mode = TxMode.WRITE)
	public static String rentVehicle(String drivingLicense, Class<?> cls, LocalDate begin, LocalDate end, String nif,
			String iban) {
		for (RentACar rentACar : FenixFramework.getDomainRoot().getRentACarSet()) {
			Set<Vehicle> vehicles = rentACar.getAvailableVehicles(cls, begin, end);
			if (!vehicles.isEmpty()) {
				for(Vehicle vehicle : vehicles) {
					return vehicle.rent(drivingLicense, begin, end, nif, iban).getReference();
				}
			}
		}
		throw new CarException();
	}

	/*@Atomic(mode = TxMode.WRITE)
	public static String cancelRenting(String reference) {
		for (RentACar rentACar : FenixFramework.getDomainRoot().getRentACarSet()) {
			RentingData data = rentACar.getRentingData(reference);
			Renting renting = new Renting(data.getDrivingLicense(), data.getBegin(), data.getEnd(), getVehicleByPlate(rentACar.getCode(), data.getPlate()), data.getNif(), data.getIban());
			if (renting != null) {
				return renting.cancel();
			}
		}
		throw new CarException();
	}*/

	@Atomic(mode = TxMode.READ)
	public static RentingData getRentingData(String reference) {
		for (RentACar rentACar : FenixFramework.getDomainRoot().getRentACarSet()) {
			for (Vehicle vehicle : rentACar.getVehicleSet()) {
				Renting renting = vehicle.getRenting(reference);
				if (renting != null) {
					return new RentingData(renting);
				}
			}
		}
		throw new CarException();
	}
	
	static List<Vehicle> getAvailableRooms(int number, Class<?> cls, LocalDate begin, LocalDate end) {
		List<Vehicle> availableVehicles = new ArrayList<>();
		for (RentACar rentACar : FenixFramework.getDomainRoot().getRentACarSet()) {
			availableVehicles.addAll(rentACar.getAvailableVehicles(cls, begin, end));
			if (availableVehicles.size() >= number) {
				return availableVehicles;
			}
		}
		return availableVehicles;
	}

	private static RentACar getRentACarByCode(String code) {
		return FenixFramework.getDomainRoot().getRentACarSet().stream().filter(h -> h.getCode().equals(code)).findFirst()
				.orElse(null);
	}

	/*private static Vehicle getVehicleByPlate(String code, String plate) {
		RentACar rentACar = getRentACarByCode(code);
		if (rentACar == null) {
			return null;
		}

		Vehicle vehicle = rentACar.getVehicleByPlate(plate);
		if (vehicle == null) {
			return null;
		}
		return vehicle;
	}*/

}
