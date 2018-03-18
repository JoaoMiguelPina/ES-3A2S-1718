package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingCheckArgumentsTest {
	RentACar RAC = new RentACar("tuxedo cars");
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	Car car;
	
	@Before
	public void setUp() {
		this.car = new Car("12-12-CJ", 34, RAC);
	}
	
	@Test
	public void success() {
		Renting renting = new Renting("AAA111", this.begin, this.end, car);
		Assert.assertTrue(renting.getBegin() == begin);
		Assert.assertTrue(renting.getEnd() == end);
		Assert.assertTrue(renting.getDrivingLicense() == "AAA111");
	}
	
	@Test (expected = CarException.class)
	public void nullCar() {
		new Renting("AAA111", this.begin, this.end, null);
	}
	
	@Test (expected = CarException.class)
	public void nullDriving() {
		new Renting(null, this.begin, this.end, car);
	}	
	
	@Test (expected = CarException.class)
	public void stringVazia() {
		new Renting("", this.begin, this.end, car);
	}
	
	@Test (expected = CarException.class)
	public void nullBegin() {
		new Renting("AAA111", null, this.end, car);
	}
	
	@Test (expected = CarException.class)
	public void nullEnd() {
		new Renting("AAA111", this.begin, null, car);
	}
	
	@Test (expected = CarException.class)
	public void endBeforeBegin() {
		new Renting("AAA111", this.end, this.begin, car);
		
	}
	
	@Test 
	public void equalDates() {
		assertNotNull(new Renting("AAA111", this.begin, this.begin, car));
	}
	
	@Test (expected = CarException.class)
	public void invalidOnlyLetters() {
		new Renting("AAAAAA", this.begin, this.end, car);	
	}
	
	@Test (expected = CarException.class)
	public void invalidSingleLetter() {
		new Renting("A", this.begin, this.end, car);	
	}
	
	@Test (expected = CarException.class)
	public void invalidOnlyNumbers() {
		new Renting("111111", this.begin, this.end, car);	
	}
	
	@Test (expected = CarException.class)
	public void invalidSingleNumber() {
		new Renting("1", this.begin, this.end, car);	
	}
	
	@Test (expected = CarException.class)
	public void invalidNumbersBeforeLetters() {
		new Renting("111AAA", this.begin, this.end, car);	
	}
	
	@Test (expected = CarException.class)
	public void invalidSingleNumberBeforeLetter() {
		new Renting("1A", this.begin, this.end, car);	
	}
	
	@Test (expected = CarException.class)
	public void invalidMoreCombinations() {
		new Renting("A1A1", this.begin, this.end, car);	
	}
	
	@After
	public void tearDown() {
		Vehicle.vehicles.clear();
	}
}