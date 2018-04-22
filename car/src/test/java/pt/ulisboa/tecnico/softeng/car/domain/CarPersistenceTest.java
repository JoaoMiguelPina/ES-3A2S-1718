package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class CarPersistenceTest {
	
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String PLATE_MOTO = "22-44-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate BEGIN = LocalDate.parse("2018-01-06");
	private static final LocalDate END = LocalDate.parse("2018-01-09");
	private static final LocalDate BEGIN2 = LocalDate.parse("2019-01-01");
	private static final LocalDate END2 = LocalDate.parse("2019-01-04");
	private static final String NIF = "NIF";
	private static final String NIF_BUYER = "NIF2";
	private static final String IBAN = "IBAN";
	private static final String IBAN_BUYER = "IBAN2";

	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, NIF, IBAN);
		Car car = new Car(PLATE_CAR, 10, 10, rentACar);
		new Motorcycle(PLATE_MOTO, 20, 20, rentACar);
		
		RentACar.rent(Car.class, DRIVING_LICENSE, NIF_BUYER, IBAN_BUYER, BEGIN, END);
		
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		//check RentACar  ------------------------------------------------------------------------
		Set<RentACar> rentACars = FenixFramework.getDomainRoot().getRentACarsSet();
		assertEquals(1, rentACars.size());
		
		RentACar rentACar = (new ArrayList<RentACar>(rentACars)).get(0);
		assertEquals(RENT_A_CAR_NAME, rentACar.getName());
		assertEquals(NIF, rentACar.getNIF());
		assertEquals(IBAN, rentACar.getIban());
				
		//check Vehicles ------------------------------------------------------------------------
		//check motorcycle
		Set<Vehicle> motos = RentACar.getAllAvailableMotorcycles(BEGIN2, END2);
		assertEquals(1, motos.size());
		Motorcycle moto = (Motorcycle) (new ArrayList<Vehicle>(motos)).get(0);
		assertEquals(PLATE_MOTO, moto.getPlate());
		assertEquals(20, moto.getKilometers());
		assertEquals(20, moto.getPrice(), 0);
		assertEquals(rentACar, moto.getRentACar());
		
		//check car
		Set<Vehicle> cars = RentACar.getAllAvailableCars(BEGIN2, END2);
		assertEquals(1, cars.size());
		Car car = (Car) (new ArrayList<Vehicle>(cars)).get(0);
		assertEquals(PLATE_CAR, car.getPlate());
		assertEquals(10, car.getKilometers());
		assertEquals(10, car.getPrice(), 0);
		assertEquals(rentACar, car.getRentACar());
				
		//check Renting  ------------------------------------------------------------------------
		Set<Renting> rentings = car.getRentingsSet();
		assertEquals(1, rentings.size());
		
		Renting renting = (new ArrayList<Renting>(rentings)).get(0);
		assertEquals(DRIVING_LICENSE, renting.getDrivingLicense());
		assertEquals(BEGIN, renting.getBegin());
		assertEquals(END, renting.getEnd());
		assertEquals(car, renting.getVehicle());
		assertEquals(NIF_BUYER, renting.getClientNIF());
		assertEquals(IBAN_BUYER, renting.getClientIBAN());
		assertEquals(false, renting.getCancelledInvoice());
		
		//check processor persistence failed list
		List<Renting> rentingsToProcess = new ArrayList<>(rentACar.getProcessor().getRentingToProcessSet());
		assertEquals(1, rentingsToProcess.size());
		assertEquals(renting, rentingsToProcess.get(0));
		
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		RentACar.clear();
	}
	
}
