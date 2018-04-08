package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRS {
	private final static Set<TaxPayer> taxPayers = new HashSet<>();
	private final Set<ItemType> itemTypes = new HashSet<>(); 


	private static IRS instance;

	public static IRS getIRS() {
		if (instance == null) {
			instance = new IRS();
		}
		return instance;
	}

	private IRS() {
	}

	void addTaxPayer(TaxPayer taxPayer) {
		this.taxPayers.add(taxPayer);
	}

	public TaxPayer getTaxPayerByNIF(String NIF) {
		for (TaxPayer taxPayer : this.taxPayers) {
			if (taxPayer.getNIF().equals(NIF)) {
				return taxPayer;
			}
		}
		return null;
	}

	void addItemType(ItemType itemType) {
		this.itemTypes.add(itemType);
	}

	public ItemType getItemTypeByName(String name) {
		for (ItemType itemType : this.itemTypes) {
			if (itemType.getName().equals(name)) {
				return itemType;
			}
		}
		return null;
	}

	public static String submitInvoice(InvoiceData invoiceData) {
		IRS irs = IRS.getIRS();
		Seller seller = (Seller) irs.getTaxPayerByNIF(invoiceData.getSellerNIF());
		Buyer buyer = (Buyer) irs.getTaxPayerByNIF(invoiceData.getBuyerNIF());
		ItemType itemType = irs.getItemTypeByName(invoiceData.getItemType());
		Invoice invoice = new Invoice(invoiceData.getValue(), invoiceData.getDate(), itemType, seller, buyer);

		return invoice.getReference();
	}
	
	public static String cancelInvoice(String invoiceReference) {	
		Invoice invoice = IRS.getInvoiceByReference(invoiceReference);
		return invoice.cancel();		
	}

	public void removeItemTypes() {
		this.itemTypes.clear();
	}

	public void removeTaxPayers() {
		this.taxPayers.clear();
	}

	public void clearAll() {
		removeItemTypes();
		removeTaxPayers();
	}
	
	public static Invoice getInvoiceByReference(String invoiceReference) {
		Invoice invoice = null;
		for(TaxPayer taxpayer : IRS.taxPayers) {
			invoice = taxpayer.getInvoiceByReference(invoiceReference);
		}
		return invoice;
	}
	
	public static InvoiceData getInvoiceData(String invoiceReference) {
		
		Invoice invoice = IRS.getInvoiceByReference(invoiceReference);
		
		if(invoice == null) {
			throw new TaxException();
		}
				
		String sellerNif = invoice.getSeller().getNIF();
		String buyerNif = invoice.getBuyer().getNIF();
		String itemType = invoice.getItemType().getName();
		double value = invoice.getValue();
		LocalDate date = invoice.getDate();
		
		
		InvoiceData invoicedata = new InvoiceData(sellerNif, buyerNif, itemType, value, date);
		
		invoicedata.setCancel(null);
		invoicedata.setCancellationDate(null);
		invoicedata.setReference(invoiceReference);
		
		return invoicedata;
	}

}
