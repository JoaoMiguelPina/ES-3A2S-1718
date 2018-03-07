package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class BuyerConstructorTest {
	private final Buyer buyer;
	
	private final String NIF = "225506740";
	private final String NAME = "Ronnie O'Sullivan";
	private final String ADDRESS = "Rua Agusta nº3";
	
	@Test
	public void sucess() {
		buyer = new Buyer(NIF, NAME, ADDRESS);
		
		assertEquals(NIF, buyer.getNIF());		
		assertEquals(NAME, buyer.getName());
		assertEquals(ADDRESS, buyer.getAddress());
		assertEquals(1, IRS.taxPayers.size());
		assertEquals(0, buyer.getNumberOfInvoice());
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
	public void emptyNIF() {
		new Buyer(NIF, NAME, "");
	}
	
	@After
	public void tearDown() {
		IRS.taxPayers.clear();
	}

}
