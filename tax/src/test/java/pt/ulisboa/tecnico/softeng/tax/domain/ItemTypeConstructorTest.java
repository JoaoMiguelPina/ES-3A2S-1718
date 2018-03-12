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
		
		assertEquals(23, iType.getTax());
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
	public void floatTax() {
		new ItemType(23,2);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}

}