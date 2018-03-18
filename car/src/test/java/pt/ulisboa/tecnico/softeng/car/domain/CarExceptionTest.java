package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class CarExceptionTest {

	@Test(expected = CarException.class)
	public void testCarException() {
	    throw new CarException("Erro");
	}

}