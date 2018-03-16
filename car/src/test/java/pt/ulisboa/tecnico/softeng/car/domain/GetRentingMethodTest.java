package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class GetRentingMethodTest {
	private final String NAME = "João Siva";
	private final String referenceFail = "123456789";
	private final String referenceSuccess = "1";
	private final String DRIVING_LICENSE = "AAA111";
	private final LocalDate BEGIN = LocalDate.now();
	private final LocalDate END = LocalDate.now();
	
	@Test
	public void success() {
		RentACar rentacar = new RentACar(NAME);
		Car car = new Car("12-14-CJ", 34, rentacar);
		Renting renting = new Renting(DRIVING_LICENSE, BEGIN, END, car);
		assertNull(rentacar.getRenting(referenceFail));
		assertNotNull(rentacar.getRenting(referenceSuccess));
		assertTrue(rentacar.getRenting(referenceSuccess).getDrivingLicense() == renting.getDrivingLicense());
		assertTrue(rentacar.getRenting(referenceSuccess).getBegin() == renting.getBegin());
		assertTrue(rentacar.getRenting(referenceSuccess).getEnd() == renting.getEnd());
		assertTrue(rentacar.getRenting(referenceSuccess).getKilometers() == renting.getKilometers());
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.vehicles.clear();
	}
}