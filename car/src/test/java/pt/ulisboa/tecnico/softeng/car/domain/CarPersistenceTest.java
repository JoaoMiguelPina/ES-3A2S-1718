package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ist.fenixframework.FenixFramework;

public class CarPersistenceTest extends RollbackTestAbstractClass{
	
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String PLATE_MOTO = "22-44-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate BEGIN = LocalDate.parse("2018-01-06");
	private static final LocalDate END = LocalDate.parse("2018-01-09");
	private static final String NIF = "NIF";
	private static final String IBAN = "IBAN";
	private static final String IBAN_BUYER = "IBAN";

	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, NIF, IBAN);
		Car car = new Car(PLATE_CAR, 10, 10, rentACar);
		new Renting(DRIVING_LICENSE, BEGIN, END, car, NIF, IBAN_BUYER);
		
		
		RentACar.rent(Car.class, DRIVING_LICENSE, NIF, IBAN_BUYER, BEGIN, END);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		//check RentACar  ------------------------------------------------------------------------
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, NIF, IBAN);
		assertEquals(RENT_A_CAR_NAME, rentACar.getName());
		assertEquals(NIF, rentACar.getNif());
		assertEquals(IBAN, rentACar.getIban());
				
		//check Vehicles ------------------------------------------------------------------------
		//check car
		Car car = new Car(PLATE_CAR, 10, 10, rentACar);
		assertEquals(PLATE_CAR, car.getPlate());
		assertEquals(10, car.getKilometers());
		assertEquals(10, car.getPrice());
		assertEquals(rentAcar, car.getRentACar());
		
		//check motorcycle
		Motorcycle moto = new Motorcycle(PLATE_MOTO, 10, 10, rentACar);
		assertEquals(PLATE_MOTO, moto.getPlate());
		assertEquals(10, moto.getKilometers());
		assertEquals(10, moto.getPrice());
		assertEquals(rentAcar, moto.getRentACar());
		
		//check Renting  ------------------------------------------------------------------------
		Renting renting = new Renting(DRIVING_LICENSE, BEGIN, END, car, NIF, IBAN_BUYER);
		assertEquals(DRIVING_LICENSE, renting.getDrivingLicense());
		assertEquals(BEGIN, renting.getBegin())
		assertEquals(END, renting.getEnd());
		assertEquals(car, renting.getVehicle());
		assertEquals(NIF, renting.getNif());
		assertEquals(IBAN_BUYER, renting.getIbanBuyer());
		assertFalse(renting.getCancelledInvoice());
		
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
	
}
