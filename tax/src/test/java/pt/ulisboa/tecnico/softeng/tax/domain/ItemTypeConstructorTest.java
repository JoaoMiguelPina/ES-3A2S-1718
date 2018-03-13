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
		assertEquals(1, IRS.getNumberItemType());
	}
	
	@Test(expected = TaxException.class)
	public void negativeTax() {
		new ItemType(-14);
	}
	
	@Test(expected = TaxException.class)
	public void overTax() {
		new ItemType(120);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}

}