package pt.ulisboa.tecnico.softeng.tax.domain;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IRSSubmitInvoiceMethodTest {/*
	IRS irs = IRS.getInstance();
	private InvoiceData invoice;
	
	@Before
	public void setUp() {
		Seller seller = new Seller("888888888", "Maria", "Rua da Estrada");
		Buyer buyer = new Buyer("111111111", "Manel", "Rua da Espera");
		ItemType iType = new ItemType("Mercearia", 2);
		this.invoice = new InvoiceData(seller.getNif(), buyer.getNif(), "iType", (float) 14.2, new LocalDate(2018,10,10));
		
		
	}
	
	@Test
	public void success() {
		this.irs.submitInvoice(this.invoice);
		/*Assert.assertEquals(this.invoice, this.irs.getInvoiceByReference(??));
	}
	
	@Test (expected = TaxException.class)
	public void sellerNotInIRS() {
		String sellerNIF = "88888880";
		Buyer buyer = new Buyer("111111110", "Manel", "Rua da Espera");
		ItemType iType = new ItemType(2);
		this.invoice = new InvoiceData(sellerNIF, buyer.getNif(), "iType", (float) 14.2, new LocalDate(2018,10,10));
		
		this.irs.submitInvoice(this.invoice);
	}
	
	@Test (expected = TaxException.class)
	public void buyerNotInIRS() {
		Seller seller = new Seller("88888881", "Maria", "Rua da Estrada");
		String buyerNIF = "11111112";
		ItemType iType = new ItemType(2);
		this.invoice = new InvoiceData(seller.getNif(), buyerNIF, "iType", (float) 14.2, new LocalDate(2018,10,10));
	
		this.irs.submitInvoice(this.invoice);
	}
	
	/*
	@Test (expected = TaxException.class)
	public void itemTypeNotInIRS() {
		Seller seller = new Seller("88888888", "Maria", "Rua da Estrada");
		Buyer buyer = new Buyer("111111111", "Manel", "Rua da Espera");
		ItemType iType = new ItemType(2);
		this.invoice = new InvoiceData(seller.getNif(), buyer.getNif(), "iType", (float) 14.2, new LocalDate(2018,10,10));
		this.irs.addItemType(iType);
		this.irs.addTaxPayer(seller);
		this.irs.addTaxPayer(buyer);
		this.irs.submitInvoice(this.invoice);
	}
	
	@After
	public void tearDown() {
		IRS.clear();
	}
	*/
}
