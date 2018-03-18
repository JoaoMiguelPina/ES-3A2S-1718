package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class SellerConstructorTest {
	private final String NIF = "885506940";
	private final String NAME = "Maria Luisa";
	private final String ADDRESS = "Rua Morais nº3";
	
	@Test
	public void sucess() {
		Seller seller = new Seller(NIF, NAME, ADDRESS);
		
		assertEquals(NIF, seller.getNif());		
		assertEquals(NAME, seller.getName());
		assertEquals(ADDRESS, seller.getAddress());
		assertEquals(1, IRS.getNumberTaxPayers());
		assertEquals(0, seller.getNumberOfInvoices());
	}
	
	/**BLANK AND NULL NAMES**/
	@Test(expected = TaxException.class)
	public void nullName() {
		new Seller(NIF, null, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyNameBlank() {
		new Seller(NIF, "    ", ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void emptyName() {
		new Seller(NIF, "", ADDRESS);
	}
	
	/**BLANK AND NULL ADDRESSES**/
	@Test(expected = TaxException.class)
	public void nullAddress() {
		new Seller(NIF, NAME, null);
	}

	@Test(expected = TaxException.class)
	public void emptyAddressBlank() {
		new Seller(NIF, NAME, "    ");
	}
	
	@Test(expected = TaxException.class)
	public void emptyAddress() {
		new Seller(NIF, NAME, "");
	}
	
	/**BLANK, NULL AND WRONG NIF's**/
	@Test(expected = TaxException.class)
	public void nullNIF() {
		new Seller(null, NAME, ADDRESS);
	}

	@Test(expected = TaxException.class)
	public void emptyNIFBlank() {
		new Seller("    ", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void emptyNIF() {
		new Seller("", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void eightDigitsNIF() {
		new Seller("12345678", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void tenDigitsNIF() {
		new Seller("9876543210", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void noDigitsNIF() {
		new Seller("TESTESTES", NAME, ADDRESS);
	}
	
	@Test(expected = TaxException.class)
	public void uniqueNIF() {
		new Seller(NIF, NAME, ADDRESS);
		new Seller(NIF, NAME, ADDRESS);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}
}