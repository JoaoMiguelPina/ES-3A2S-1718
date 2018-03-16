package pt.ulisboa.tecnico.softeng.tax.domain;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class IRSSubmitInvoiceMethodTest {
	private static final String BUYER_NIF = "225506740";
	private static final String SELLER_NIF = "885506940";
	private static final float VALUE = (float) 100.0;
	private static final float MIN_VALUE = (float) 0.0;
	
	private Seller seller;
	private Buyer buyer;
	private ItemType itemType;
	private LocalDate date;
	
	
	@Before
	public void setUp() {
		this.buyer = new Buyer(BUYER_NIF, "João Silva", "Rua Agusta nº3");
		this.seller = new Seller(SELLER_NIF, "Jacinto, Lda", "Rua Morais nº3");
		this.itemType = new ItemType("Mercearia", 23);
		date = new LocalDate(2018, 3, 6);
	}
	
	@Test
	public void sucess(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, this.itemType.getName(), VALUE, date));
		assertEquals(1, this.seller.getNumberOfInvoices());
		assertEquals(1, this.buyer.getNumberOfInvoices());
		assertEquals(1, this.itemType.getNumberOfInvoices());
	}
	
	/*BUYER NIF ERRORS*/
	@Test(expected = TaxException.class)
	public void nullBuyerNif(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, null, this.itemType.getName(), VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void blankBuyerNif(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, "    ", this.itemType.getName(), VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void emptyBuyerNif(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, "", this.itemType.getName(), VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void buyerNifEqualsSellerNif(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, SELLER_NIF, this.itemType.getName(), VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void wrongBuyerNif(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, "123456789", this.itemType.getName(), VALUE, date));
	}
	
	/*SELLER NIF ERRORS*/
	@Test(expected = TaxException.class)
	public void nullSellerNif(){
		IRS.submitInvoice(new InvoiceData(null, BUYER_NIF, this.itemType.getName(), VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void blankSellerNif(){
		IRS.submitInvoice(new InvoiceData("    ", BUYER_NIF, this.itemType.getName(), VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void emptySellerNif(){
		IRS.submitInvoice(new InvoiceData("", BUYER_NIF, this.itemType.getName(), VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void sellerNifEqualsBuyerNif(){
		IRS.submitInvoice(new InvoiceData(BUYER_NIF, BUYER_NIF, this.itemType.getName(), VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void wrongSellerNif(){
		IRS.submitInvoice(new InvoiceData("123456789", BUYER_NIF, this.itemType.getName(), VALUE, date));
	}
	
	/*TYPEITEM ERRORS*/
	@Test(expected = TaxException.class)
	public void nullItemTypeName(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, this.itemType.getName(), VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void blankItemTypeName(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, "   ", VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void emptyItemTypeName(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, "", VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void wrongItemTypeName(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, "WrongName", VALUE, date));
	}
	
	/*VALUE TESTS*/
	@Test(expected = TaxException.class)
	public void negativeValue(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, this.itemType.getName(), -VALUE, date));
	}
	
	@Test(expected = TaxException.class)
	public void valueMinusOneThanMinimal(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, this.itemType.getName(), MIN_VALUE - 1, date));
	}
	
	@Test
	public void valueEqualsMinimal(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, this.itemType.getName(), MIN_VALUE, date));
		assertEquals(1, this.seller.getNumberOfInvoices());
		assertEquals(1, this.buyer.getNumberOfInvoices());
		assertEquals(1, this.itemType.getNumberOfInvoices());
	}
	
	@Test
	public void valuePlusOneThanMinimal(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, this.itemType.getName(), MIN_VALUE + 1, date));
		assertEquals(1, this.seller.getNumberOfInvoices());
		assertEquals(1, this.buyer.getNumberOfInvoices());
		assertEquals(1, this.itemType.getNumberOfInvoices());
	}
	
	/*DATE TESTS*/
	@Test(expected = TaxException.class)
	public void yearBefore1970(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, this.itemType.getName(), VALUE, new LocalDate(1969, 12, 31)));
	}
	
	@Test
	public void date1Jan1970(){
		IRS.submitInvoice(new InvoiceData(SELLER_NIF, BUYER_NIF, this.itemType.getName(), VALUE, new LocalDate(1970, 1, 1)));
		assertEquals(1, this.seller.getNumberOfInvoices());
		assertEquals(1, this.buyer.getNumberOfInvoices());
		assertEquals(1, this.itemType.getNumberOfInvoices());
	}
	@After
	public void tearDown(){
		IRS.clear();
	}
	
	
}
