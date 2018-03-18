package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarConstructorTest {
	private final String NAME = "João Siva";
	RentACar rentacar = new RentACar(NAME);
	
	@Test
	public void success() {
		
		assertEquals(NAME, rentacar.getName());
		assertEquals(1, RentACar.rentACars.size());
	}
	
	/**BLANK AND NULL NAMES**/
	@Test(expected = CarException.class)
	public void nullName() {
		new RentACar(null);
	}

	@Test(expected = CarException.class)
	public void emptyNameBlank() {
		new RentACar("    ");
	}
	
	@Test(expected = CarException.class)
	public void emptyName() {
		new RentACar("");
	}
	
	@Test(expected = CarException.class)
	public void emptyVehiclesSet() {
		rentacar.getVehicles();
	}
	
	@Test
	public void getVehiclesTest() {
		new Car("45-46-DF", 89, rentacar);
		assertTrue(rentacar.getVehicles().size() == 1);
		Vehicle.destroyVehicles();
		RentACar.cars.clear();
	}
	
	@Test(expected = CarException.class)
	public void emptyMotorcyclesSet() {
		rentacar.getMotorcycles();
	}
	
	
	@Test
	public void getMotorcyclesTest2() {
		new Motorcycle("45-46-DF", 89, rentacar);
		new Motorcycle("70-46-DF", 90, rentacar);
		assertTrue(rentacar.getMotorcycles().size() == 2);
		Vehicle.destroyVehicles();
		RentACar.motorcycles.clear();
	}
	
	@Test(expected = CarException.class)
	public void emptyCarSet() {
		rentacar.getCars();
	}
	
	@Test
	public void getCarsTest() {
		new Car("45-46-DF", 89, rentacar);
		assertTrue(rentacar.getCars().size() == 1);
		RentACar.vehicles.clear();
		RentACar.cars.clear();
	}
	
	@Test
	public void getCarsTest2() {
		new Car("45-46-DF", 89, rentacar);
		new Car("60-46-DF", 9, rentacar);
		assertTrue(rentacar.getCars().size() == 2);
		RentACar.vehicles.clear();
		RentACar.cars.clear();
	}
	
	@Test
	public void getCodeTest() {
		assertNotNull(rentacar.getCode());
	}
	
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		RentACar.vehicles.clear();
	}
}