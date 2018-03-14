package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

import java.util.HashSet;
import java.util.Set;


public class GetAllAvailableMotorcyclesTest{
	private final String NAME = "João Siva";
	Set<Motorcycle> motorcycles = new HashSet<>();
	LocalDate BEGIN = LocalDate.now();
	LocalDate END = LocalDate.now();
	
	
	@Test
	public void success() {
		RentACar rentacar = new RentACar(NAME);
		Set<Motorcycle> motorcycleSet = new HashSet<>();
		motorcycleSet.add(new Motorcycle("12-14-CJ", 200000, rentacar));
		motorcycleSet.add(new Motorcycle("13-14-CJ", 100000, rentacar));
		motorcycleSet.add(new Motorcycle("14-14-CJ", 50000, rentacar));
		motorcycleSet.add(new Motorcycle("15-14-CJ", 150000, rentacar));
		
		this.motorcycles = rentacar.getAllAvailableMotorcycles(BEGIN, END);
		
		assertTrue(this.motorcycles.containsAll(motorcycleSet));
		
	}
}