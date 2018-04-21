package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

public class TaxPersistenceTest {
	private static final String ITEM_NAME = "SPORT";
	private static final int ITEM_TAX = 10;
	
	private static final String SELLER_NIF = "500100200";
	private static final String SELLER_NAME = "Continente";
	private static final String SELLER_ADDRESS = "Colombo";
	
	private static final String BUYER_NIF = "225100200";
	private static final String BUYER_NAME = "António";
	private static final String BUYER_ADDRESS = "Rua Nova";
	
	private static final LocalDate DATE =  new LocalDate(2018, 04, 21);
	private static final double VALUE = 100;
	private static final double IVA = VALUE * ITEM_TAX / 100;
	
	private static String invoiceReference;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		IRS irs = IRS.getIRS();
		
		new ItemType(irs, ITEM_NAME, ITEM_TAX);			
		new Seller(irs, SELLER_NIF, SELLER_NAME, SELLER_ADDRESS);		
		new Buyer(irs, BUYER_NIF, BUYER_NAME, BUYER_ADDRESS);
		
		InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, ITEM_NAME, VALUE, DATE);		
		invoiceReference = IRS.submitInvoice(invoiceData);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		//check TaxPayers ------------------------------------------------------------------------
		//check seller
		Seller seller = (Seller) IRS.getIRS().getTaxPayerByNIF(SELLER_NIF);
		assertEquals(SELLER_NIF, seller.getNIF());
		assertEquals(SELLER_NAME, seller.getName());
		assertEquals(SELLER_ADDRESS, seller.getAddress());
		
		//check buyer
		Buyer buyer = (Buyer) IRS.getIRS().getTaxPayerByNIF(BUYER_NIF);
		assertEquals(BUYER_NIF, buyer.getNIF());
		assertEquals(BUYER_NAME, buyer.getName());
		assertEquals(BUYER_ADDRESS, buyer.getAddress());
		
		//check ItemType  ------------------------------------------------------------------------
		ItemType itemType = IRS.getIRS().getItemTypeByName(ITEM_NAME);
		assertEquals(ITEM_NAME, itemType.getName());
		assertEquals(ITEM_TAX, itemType.getTax());
		
		//check Invoice  ------------------------------------------------------------------------
		Invoice invoiceBuyer = buyer.getInvoiceByReference(invoiceReference);
		Invoice invoiceSeller = seller.getInvoiceByReference(invoiceReference);
		
		assertEquals(invoiceBuyer, invoiceSeller);
		Invoice invoice = invoiceBuyer;
		
		assertEquals(SELLER_NIF, invoice.getSeller().getNIF());
		assertEquals(BUYER_NIF, invoice.getBuyer().getNIF());
		assertEquals(ITEM_NAME, invoice.getItemType().getName());
		assertEquals(VALUE, invoice.getValue(), 0);
		assertEquals(IVA, invoice.getIva(), 0);
		assertEquals(DATE, invoice.getDate());
		assertEquals(false, invoice.isCancelled());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		IRS.getIRS().delete();
	}
}
