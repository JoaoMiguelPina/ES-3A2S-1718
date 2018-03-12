package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;
import pt.ulisboa.tecnico.softeng.tax.exception.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BuyerConstructorTest {
	private final String NIF = "225506740";
	private final String NAME = "João Silva";
	private final String ADDRESS = "Rua Agusta nº3";
	
	@Test
	public void sucess() {
		Buyer buyer = new Buyer(NIF, NAME, ADDRESS);
		
		assertEquals(NIF, buyer.getNif());		
		assertEquals(NAME, buyer.getName());
		assertEquals(ADDRESS, buyer.getAddress());
		assertEquals(1, IRS.size());
		assertEquals(0, buyer.getNumberOfInvoices());
	}
	
	/**BLANK AND NULL NAMES**/
	@Test(expected = TaxException.class)
	public void nullName() {
		new Buyer(NIF, null, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyNameBlank() {
		new Buyer(NIF, "    ", ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void emptyName() {
		new Buyer(NIF, "", ADDRESS);
	}
	
	/**BLANK AND NULL ADDRESSES**/
	@Test(expected = TaxException.class)
	public void nullAddress() {
		new Buyer(NIF, NAME, null);
	}

	@Test(expected = TaxException.class)
	public void emptyAddressBlank() {
		new Buyer(NIF, NAME, "    ");
	}
	
	@Test(expected = TaxException.class)
	public void emptyAddress() {
		new Buyer(NIF, NAME, "");
	}
	
	/**BLANK, NULL AND WRONG NIF's**/
	@Test(expected = TaxException.class)
	public void nullNIF() {
		new Buyer(null, NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyNIFBlank() {
		new Buyer("    ", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void emptyNIF() {
		new Buyer("", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void eightDigitsNIF() {
		new Buyer("12345678", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void tenDigitsNIF() {
		new Buyer("9876543210", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void noDigitsNIF() {
		new Buyer("TESTESTES", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void uniqueNIF() {
		new Buyer(NIF, NAME, ADDRESS);
		new Buyer(NIF, NAME, ADDRESS);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}

}
