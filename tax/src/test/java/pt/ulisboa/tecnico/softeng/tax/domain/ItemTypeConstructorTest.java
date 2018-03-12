package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemTypeConstructorTest {
	private final int tax = 23;
	
	@Test
	public void sucess() {
		ItemType iType = new ItemType(tax);
		
		assertEquals(tax, iType.getTax());
		assertEquals(1, IRS.size());
	}
	
	@Test(expected = TaxException.class)
	public void nullTax() {
		new ItemType(null);
	}

	@Test(expected = TaxException.class)
	public void stringTax() {
		new ItemType("string");
	}
	
	@Test(expected = TaxException.class)
	public void doubleTax() {
		new ItemType(23.6);
	}
	
	@Test(expected = TaxException.class)
	public void floatTax() {
		new ItemType(23/100);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}

}