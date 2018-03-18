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
		new Car("09-46-DF", 89, rentacar);
		new Motorcycle("11-22-XA", 70, rentacar);
		assertEquals(NAME, rentacar.getName());
		assertEquals(1, RentACar.rentACars.size());
		assertEquals(2, this.rentacar.getVehicles().size());
		
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
		new Car("90-46-DF", 89, rentacar);
		assertTrue(rentacar.getVehicles().size() == 1);
		Vehicle.destroyVehicles();
	}
	
	@Test(expected = CarException.class)
	public void emptyMotorcyclesSet() {
		rentacar.getMotorcycles();
	}
	
	
	@Test
	public void getMotorcyclesTest2() {
		new Motorcycle("05-46-DF", 89, rentacar);
		new Motorcycle("70-46-DF", 90, rentacar);
		assertTrue(rentacar.getMotorcycles().size() == 2);
		Vehicle.destroyVehicles();
	}
	
	@Test(expected = CarException.class)
	public void emptyCarSet() {
		rentacar.getCars();
	}
	
	@Test
	public void getCarsTest2() {
		new Car("95-46-DF", 89, rentacar);
		new Car("60-46-DF", 9, rentacar);
		assertTrue(rentacar.getCars().size() == 2);
	}
	
	@Test
	public void getCodeTest() {
		assertNotNull(rentacar.getCode());
	}
	
	
	@After
	public void tearDown() {
		rentacar.tearDown();
		Vehicle.vehicles.clear();
		RentACar.rentACars.clear();
	}
}