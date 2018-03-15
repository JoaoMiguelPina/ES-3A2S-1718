package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class ItemTypeGetInvoiceByReferenceMethodTest {
	private ItemType itemType;
	private LocalDate date;
	private Seller seller;
	private Buyer buyer;
	private Invoice invoice;

	
	@Before
	public void setUp() {
		this.itemType = new ItemType("Mercearia", 8);
				
		this.date = new LocalDate(2018, 3, 10); 
		this.seller = new Seller("111222333","Henrique","Rua da Conceicao");
		this.buyer = new Buyer("123456789","Maria","Rua do Carmo"); 
		this.invoice = new Invoice((float)8.4, this.date, this.itemType, this.seller, this.buyer);
	}

	@Test
	public void nullInvoice() {
		Assert.assertNull(this.itemType.getInvoiceByReference(null));
	}
	
	@Test
	public void noInvoice() {
		String ref = this.invoice.getReference();
		this.tearDown();
		Assert.assertNull(this.itemType.getInvoiceByReference(ref));
	}
	
	@Test
	public void oneInvoiceSuccess() {
		String ref = this.invoice.getReference();
		
		Assert.assertEquals(this.invoice, this.itemType.getInvoiceByReference(ref));
	}
	
	@Test
	public void oneInvoiceFail() {
		Assert.assertNull(this.itemType.getInvoiceByReference("WrongReference"));
	}
	
	@Test
	public void twoInvoiceSuccess() {
		String ref = this.invoice.getReference();
		new Invoice((float)8.4, this.date, this.itemType, this.seller, this.buyer);
		
		Assert.assertEquals(this.invoice, this.itemType.getInvoiceByReference(ref));
	}

	@Test
	public void twoInvoiceFail() {
		new Invoice((float)8.4, this.date, this.itemType, this.seller, this.buyer);
		
		Assert.assertNull(this.itemType.getInvoiceByReference("WrongReference"));
	}
	
	@After
	public void tearDown() {
		IRS.clear();
		this.itemType.clear();
	}
}
