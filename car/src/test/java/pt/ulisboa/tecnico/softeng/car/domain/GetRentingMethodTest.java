package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class GetRentingMethodTest {
	private final String NAME = "João Siva";
	private final String REFERENCE = "123456789";
	private final String DRIVING_LICENSE = "10-10-ZI";
	private final LocalDate BEGIN = LocalDate.now();
	private final LocalDate END = LocalDate.now();
	
	@Test
	public void success() {
		RentACar rentacar = new RentACar(NAME);
		Car car = new Car("12-14-CJ", 34, rentacar);
		Renting renting = new Renting(DRIVING_LICENSE, BEGIN, END, car);
		assertNotNull(rentacar.getRenting(REFERENCE));
		assertTrue(rentacar.getRenting(REFERENCE).getDrivingLicense() == renting.getDrivingLicense());
		assertTrue(rentacar.getRenting(REFERENCE).getBegin() == renting.getBegin());
		assertTrue(rentacar.getRenting(REFERENCE).getEnd() == renting.getEnd());
		assertTrue(rentacar.getRenting(REFERENCE).getKilometers() == renting.getKilometers());
	}
}