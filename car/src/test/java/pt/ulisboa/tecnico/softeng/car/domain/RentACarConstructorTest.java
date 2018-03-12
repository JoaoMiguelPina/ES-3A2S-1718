package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarConstructorTest {
	private final String NAME = "João Siva";
	
	@Test
	public void success() {
		RentACar rentacar = new RentACar(NAME);
		assertEquals(NAME, rentacar.getName());
		assertEquals(1, RentACar.rentacars.size());
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
}