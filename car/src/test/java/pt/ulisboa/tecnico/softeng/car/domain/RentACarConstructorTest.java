package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarConstructorTest {
	private static final String NAME = "eartz";
	private static final String NIF = "224194217";
	private static final String IBAN = "1234567890";

	@Test
	public void success() {
		RentACar rentACar = new RentACar(NAME, NIF, IBAN);
		assertEquals(NAME, rentACar.getName());
	}

	@Test(expected = CarException.class)
	public void nullEverything() {
		new RentACar(null, null, null);
	}

	@Test(expected = CarException.class)
	public void emptyEverything() {
		new RentACar("", "", "");
	}
	
	@Test(expected = CarException.class)
	public void emptyName() {
		new RentACar("", NIF, IBAN);
	}
	
	@Test(expected = CarException.class)
	public void nullName() {
		new RentACar(null, NIF, IBAN);
	}
	
	@Test(expected = CarException.class)
	public void emptyNif() {
		new RentACar(NAME, "", IBAN);
	}
	
	@Test(expected = CarException.class)
	public void nullNif() {
		new RentACar(NAME, null, IBAN);
	}
	

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
	}
}
