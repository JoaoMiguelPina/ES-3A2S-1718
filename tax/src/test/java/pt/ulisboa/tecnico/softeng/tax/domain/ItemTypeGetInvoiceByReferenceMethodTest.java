package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class ItemTypeGetInvoiceByReferenceMethodTest {
	
	private ItemType iType;
	private Invoice invoice;
	private IRS irs = IRS.getInstance();
	
	@Before
	public void setUp() {
		this.iType = new ItemType("Mercearia", 8);
		
		ItemType itemType = new ItemType("Cereais", 2);
		
		LocalDate date = new LocalDate(2018, 3, 10); 
		Seller seller = new Seller("111222333","Henrique","Rua da Conceicao");
		Buyer buyer = new Buyer("123456789","Maria","Rua do Carmo"); 
		this.invoice = new Invoice((float)8.4, date, itemType, seller, buyer);
		
	}

	@Test
	public void success() {
		String ref = this.invoice.getReference();
		Assert.assertEquals(this.invoice, this.iType.getInvoiceByReference(ref));
	
	}
	
	@Test(expected = TaxException.class)
	public void nullInvoice() {
		this.iType.getInvoiceByReference("lol");
	}

	@After
	public void tearDown() {
		IRS.clear();
	}
}
