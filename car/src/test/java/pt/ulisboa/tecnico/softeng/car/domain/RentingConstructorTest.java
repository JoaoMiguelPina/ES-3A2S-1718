package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingConstructorTest {
	private final String NAME = "João Siva";
	
	@Test
	public void success() {
		Renting r = new Renting("1234", "AAA111", java.time.LocalDate.now(), java.time.LocalDate.now(), 33);
	}
	
	@Test
	public void success {
		
		//Renting constructor Test
		Assert.assertEquals("1234", r.getReference());
		Assert.assertArrayEquals("AAA111", r.getLicense());
		Assert.assertArrayEquals(java.time.LocalDate.now(), rd.getBegin());
		Assert.assertArrayEquals(java.time.LocalDate.now(), rd.getEnd());
		Assert.assertEquals(33, r.getKm());
	}
	
	@After
	public void tearDown() {
		r.destroyRenting();
	}
}