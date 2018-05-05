package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;

public class VehicleData {
	private String rentACarCode;
	private String rentACarName;
	private List<RentingData> rentings;

	public VehicleData() {
	}

	public VehicleData(Vehicle vehicle) {
		this.rentACarCode = vehicle.getRentACar().getCode();
		this.rentACarName = vehicle.getRentACar().getName();

		this.rentings = vehicle.getRentingSet().stream().sorted((r1, r2) -> r1.getBegin().compareTo(r2.getBegin()))
				.map(r -> new RentingData(r)).collect(Collectors.toList());
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

}