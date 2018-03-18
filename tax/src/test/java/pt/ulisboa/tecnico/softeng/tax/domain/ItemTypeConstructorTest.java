package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemTypeConstructorTest {
	private static final int TAX = 23;
	private static final String TYPE_NAME = "Mercearia";
	private static final int MIN_TAX =  0;
	private static final int MAX_TAX = 100;
	
	@Test
	public void sucess() {
		ItemType iType = new ItemType(TYPE_NAME, TAX);
		
		assertEquals(TYPE_NAME, iType.getName());
		assertEquals(TAX, iType.getTax());		
		assertEquals(1, IRS.getNumberItemType());
		assertEquals(0, iType.getNumberOfInvoices());
	}
	
	/*BOUNDARY LIMIT*/
	@Test(expected = TaxException.class)
	public void negativeTax() {
		new ItemType(TYPE_NAME, -14);
	}
	
	@Test(expected = TaxException.class)
	public void taxMinusOneThanMinimal() {
		new ItemType(TYPE_NAME, MIN_TAX - 1);
	}
	
	@Test()
	public void taxEqualsMinimal() {
		new ItemType(TYPE_NAME, MIN_TAX);
	}
	
	@Test
	public void taxPlusOneThanMinimal() {
		new ItemType(TYPE_NAME, MIN_TAX);
	}
	
	@Test
	public void taxMedian() {
		new ItemType(TYPE_NAME, (MAX_TAX - MIN_TAX)/2);
	}
	
	@Test
	public void taxMinusOneThanMax() {
		new ItemType(TYPE_NAME, MAX_TAX - 1);
	}
	
	@Test()
	public void taxEqualsMax() {
		new ItemType(TYPE_NAME, MAX_TAX);
	}
	
	@Test(expected = TaxException.class)
	public void taxPlusOneThanMax() {
		new ItemType(TYPE_NAME, MAX_TAX + 1);
	}
	
	@Test(expected = TaxException.class)
	public void overTax() {
		new ItemType(TYPE_NAME, 120);
	}
	
	
	/*NULL AND BLANK NAME*/
	@Test(expected = TaxException.class)
	public void nullName(){
		new ItemType(null, TAX);
	}
	
	@Test(expected = TaxException.class)
	public void emptyNameBlank(){
		new ItemType("    ", TAX);
	}
	
	@Test(expected = TaxException.class)
	public void emptyName(){
		new ItemType("", TAX);
	}
	
	@Test(expected = TaxException.class)
	public void uniqueName(){
		new ItemType(TYPE_NAME, TAX);
		new ItemType(TYPE_NAME, TAX);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}
}