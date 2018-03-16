package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;

public class IRSGetTaxPayerByNIFMethodTest {
	
	private static final String NIF1 = "111111111";
	private static final String Name1 = "Manuela";
	private static final String Address1 = "Rua da Igreja";
	
	private static final String NIF2 = "222222222";
	private static final String Name2 = "Manel";
	private static final String Address2 = "Rua da Igreja do Monte";
		
	
	@Test
	public void nullParameter() {		
		new Seller(NIF1, Name1, Address1);
		
		assertNull(IRS.getTaxPayerByNIF(null));
	}
	
	@Test
	public void noTaxPayer() {		
		assertNull(IRS.getTaxPayerByNIF(NIF1));
	}
	
	@Test
	public void oneTaxPayerSucess() {
		Seller expected = new Seller(NIF1, Name1, Address1);
		Seller obtained = (Seller) IRS.getTaxPayerByNIF(NIF1);
		
		assertEquals(NIF1, obtained.getNif());
		assertEquals(Name1, obtained.getName());
		assertEquals(Address1, obtained.getAddress());
		assertEquals(expected, obtained);
	}
	
	@Test
	public void oneTaxPayerFail() {
		new Seller(NIF1, Name1, Address1);
		Seller obtained = (Seller) IRS.getTaxPayerByNIF(NIF2);
		
		assertNull(obtained);
	}
	
	@Test
	public void twoTaxPayerSucess() {
		Seller expected = new Seller(NIF1, Name1, Address1);
		new Buyer(NIF2, Name2, Address2);
		
		Seller obtained = (Seller) IRS.getTaxPayerByNIF(NIF1);
		
		assertEquals(NIF1, obtained.getNif());
		assertEquals(Name1, obtained.getName());
		assertEquals(Address1, obtained.getAddress());
		assertEquals(expected, obtained);
	}
	
	@Test
	public void twoTaxPayerFail() {
		new Seller(NIF1, Name1, Address1);
		new Buyer(NIF2, Name2, Address2);
		
		TaxPayer obtained = IRS.getTaxPayerByNIF("Teste");
		
		assertNull(obtained);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}
}
