package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class CarExceptionTest {

	@Test(expected = CarException.class)
	public void testCarException() {
	    throw new CarException("Erro");
	}

}