package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;

public class VehicleData {
	private String rentACarCode;
	private String rentACarName;
	private int kilometers;
	private String plate;
	private double price;
	private Class<?> vehicleClass;
	private List<RentingData> rentings;

	public VehicleData() {
	}

	public VehicleData(Vehicle vehicle) {
		this.rentACarCode = vehicle.getRentACar().getCode();
		this.rentACarName = vehicle.getRentACar().getName();
		this.kilometers = vehicle.getKilometers();
		this.plate = vehicle.getPlate();
		this.price = vehicle.getPrice();
		this.vehicleClass = vehicle.getClass();

		this.rentings = vehicle.getRentingSet().stream().sorted((r1, r2) -> r1.getBegin().compareTo(r2.getBegin()))
				.map(r -> new RentingData(r)).collect(Collectors.toList());
	}

	public int getKilometers() {
		return kilometers;
	}

	public void setKilometers(int kilometers) {
		this.kilometers = kilometers;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setRentACarName(String rentACarName) {
		this.rentACarName = rentACarName;
	}

	public String getRentACarCode() {
		return this.rentACarCode;
	}

	public void setRentACarCode(String rentACarCode) {
		this.rentACarCode = rentACarCode;
	}

	public String getRentACarName() {
		return this.rentACarName;
	}

	public void setHotelName(String rentACarName) {
		this.rentACarName = rentACarName;
	}

	public List<RentingData> getRentings() {
		return this.rentings;
	}

	public void setRentings(List<RentingData> rentings) {
		this.rentings = rentings;
	}
	
	public Class<?> getVehicleClass() {
		return vehicleClass;
	}

	public void setVehicleClass(Class<?> vehicleClass) {
		this.vehicleClass = vehicleClass;
	}

}