package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

import java.util.HashSet;
import java.util.Set;


public class GetAllAvailableCarsTest{
	private final String NAME = "João Siva";
	Set<Car> cars = new HashSet<>();
	LocalDate BEGIN = LocalDate.now();
	LocalDate END = LocalDate.now();
	
	
	@Test
	public void success() {
		RentACar rentacar = new RentACar(NAME);
		Set<Car> carSet = new HashSet<>();
		carSet.add(new Car("12-14-CJ", 200000, rentacar));
		carSet.add(new Car("13-14-CJ", 100000, rentacar));
		carSet.add(new Car("14-14-CJ", 50000, rentacar));
		carSet.add(new Car("15-14-CJ", 150000, rentacar));
		
		this.cars = rentacar.getAllAvailableCars(BEGIN, END);
		
		assertTrue(this.cars.containsAll(carSet));
		
	}
}