package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class IRSgetInvoiceDataMethodTest {
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "987654321";
	private static final String FOOD = "FOOD";
	private static final int VALUE = 16;
	private final LocalDate date = new LocalDate(2018, 02, 13);

	private IRS irs;

	@Before
	public void setUp() {
		this.irs = IRS.getIRS();
		new Seller(this.irs, SELLER_NIF, "José Vendido", "Somewhere");
		new Buyer(this.irs, BUYER_NIF, "Manuel Comprado", "Anywhere");
		new ItemType(this.irs, FOOD, VALUE);
	}

	@Test
	public void success() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date);
		String invoiceReference = this.irs.submitInvoice(invoiceData);

		InvoiceData data = this.irs.getInvoiceData(invoiceReference);
		
		
		assertEquals(invoiceReference, data.getReference());
		assertEquals(SELLER_NIF, data.getSellerNIF());
		assertEquals(BUYER_NIF, data.getBuyerNIF());
		assertEquals(FOOD, data.getItemType());
		assertEquals(VALUE, data.getValue(), 0.0000);
		assertEquals(this.date, data.getDate());
		assertEquals(null, data.getCancel());
		assertEquals(null, data.getCancellationDate());
	}

	@Test
	public void successCancelled() {
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date);
		String invoiceReference = this.irs.submitInvoice(invoiceData);
		
		this.irs.getInvoiceByReference(invoiceReference).cancel();

		InvoiceData data = this.irs.getInvoiceData(invoiceReference);
		
		
		assertEquals(invoiceReference, data.getReference());
		assertEquals(SELLER_NIF, data.getSellerNIF());
		assertEquals(BUYER_NIF, data.getBuyerNIF());
		assertEquals(FOOD, data.getItemType());
		assertEquals(VALUE, data.getValue(), 0.0000);
		assertEquals(this.date, data.getDate());
		assertEquals("CANCEL" + invoiceReference, data.getCancel());
		assertEquals(LocalDate.now(), data.getCancellationDate());
	}

	@Test(expected = TaxException.class)
	public void nullReference() {
		this.irs.getInvoiceData(null);
	}

	@Test(expected = TaxException.class)
	public void emptyReference() {
		this.irs.getInvoiceData("");
	}

	@Test(expected = TaxException.class)
	public void notExistsReference() {
		this.irs.getInvoiceData("XPTO");
	}

	@After
	public void tearDown() {
		this.irs.clearAll();
	}

}