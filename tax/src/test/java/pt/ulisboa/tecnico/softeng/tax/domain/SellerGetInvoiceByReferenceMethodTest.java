package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class SellerGetInvoiceByReferenceMethodTest {
	private final String NIF = "885506900";
	private final String NAME = "Maria Luisa";
	private final String ADDRESS = "Rua Morais nº3";
	private Seller seller;
	private Invoice invoice;
	
	@Before
	public void setUp() {
		this.seller = new Seller(NIF, NAME, ADDRESS);
		ItemType itemType = new ItemType(2);
		LocalDate date = new LocalDate(2018, 3, 10); 
		Buyer buyer = new Buyer("123456789","Maria","Rua do Carmo"); 
		this.invoice = new Invoice((float)8.4, date, itemType, this.seller, buyer);
		
		this.seller.addInvoice(this.invoice);
		
	}

	@Test
	public void success() {
		Assert.assertEquals(this.invoice, this.seller.getInvoiceByReference(this.invoice.getReference()));
	
	}
	
	@Test
	public void nullInvoice() {
		 this.seller.getInvoiceByReference("uma:referencia:invalida");
	}

	@After
	public void tearDown() {
		IRS.clear();
	}
}
