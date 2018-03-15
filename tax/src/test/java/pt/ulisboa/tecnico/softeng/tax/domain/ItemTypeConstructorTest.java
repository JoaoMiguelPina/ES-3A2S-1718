package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemTypeConstructorTest {
	private final int TAX = 23;
	private final String TYPE_NAME = "Mercearia";
	
	@Test
	public void sucess() {
		ItemType iType = new ItemType(TAX, TYPE_NAME);
		
		assertEquals(TAX, iType.getTax());
		assertEquals(TYPE_NAME, iType.getName());
		assertEquals(1, IRS.getNumberItemType());
		assertEquals(0, iType.getNumberOfInvoices());
	}
	
	@Test(expected = TaxException.class)
	public void negativeTax() {
		new ItemType(-14, TYPE_NAME);
	}
	
	@Test(expected = TaxException.class)
	public void overTax() {
		new ItemType(120, TYPE_NAME);
	}
	
	@Test(expected = TaxException.class)
	public void nullName(){
		new ItemType(TAX, null);
	}
	
	@Test(expected = TaxException.class)
	public void emptyNameBlank(){
		new ItemType(TAX, "    ");
	}
	
	@Test(expected = TaxException.class)
	public void emptyName(){
		new ItemType(TAX, "");
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}

}