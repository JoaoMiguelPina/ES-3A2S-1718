package pt.ulisboa.tecnico.softeng.tax.services.local;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

public class TaxInterface {
	
	@Atomic(mode = TxMode.WRITE)
	public static void createInvoice(InvoiceData invoice) {
		if(invoice.getValue() == null){
			throw new TaxException();
		}
		IRS.getIRSInstance();
		IRS.submitInvoice(invoice);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createItemType(ItemTypeData itd) {
		if(itd.getTax() == null){
			throw new TaxException();
		}
		new ItemType(IRS.getIRSInstance(), itd.getName(), itd.getTax());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static List<InvoiceData> getInvoices(String nif) {
		Set<Invoice> invoices= getTaxPayerByNif(nif).getAllInvoices();
		Set<InvoiceData> invoicesData = new HashSet<>();
		
		for(Invoice invoice : invoices){
			invoicesData.add(new InvoiceData(invoice));
		}
		
		return invoicesData.stream().sorted((p1, p2) -> p1.getDate().compareTo(p2.getDate())).collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createTaxPayer(TaxPayerData taxPayer) {
		if(taxPayer.getType().equals("Buyer")){
			new Buyer(IRS.getIRSInstance(), taxPayer.getNif(), taxPayer.getName(), taxPayer.getAddress());
		}
		else if(taxPayer.getType().equals("Seller")){
			new Seller(IRS.getIRSInstance(), taxPayer.getNif(), taxPayer.getName(), taxPayer.getAddress());
		}
	}
	
	
	@Atomic(mode = TxMode.WRITE)
	public static List<TaxPayerData> getBuyers() {
		Set<TaxPayerData> buyers = new HashSet<>();
		for(TaxPayer taxPayer : IRS.getIRSInstance().getTaxPayerSet()){
			if(taxPayer instanceof Buyer){
				buyers.add(new TaxPayerData(taxPayer));
			}
		}
		return buyers.stream().sorted((p1, p2) -> p1.getNif().compareTo(p2.getNif())).collect(Collectors.toList());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static List<TaxPayerData> getSellers() {
		Set<TaxPayerData> sellers = new HashSet<>();
		for(TaxPayer taxPayer : IRS.getIRSInstance().getTaxPayerSet()){
			if(taxPayer instanceof Seller){
				sellers.add(new TaxPayerData(taxPayer));
			}
		}
		return sellers.stream().sorted((p1, p2) -> p1.getNif().compareTo(p2.getNif())).collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.READ)
	public static Double TaxPayerToPay(Seller seller, int year) {
		return seller.toPay(year);
	}

	@Atomic(mode = TxMode.READ)
	public static Double TaxPayerTaxReturn(Buyer buyer, int year) {
		return buyer.taxReturn(year);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static TaxPayerData getTaxPayerDataByNif(String nif) {
		TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNIF(nif);
		if(taxPayer == null){
			throw new TaxException();
		}
		return new TaxPayerData(taxPayer);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static TaxPayer getTaxPayerByNif(String nif) {
		return IRS.getIRSInstance().getTaxPayerByNIF(nif);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static List<ItemTypeData> getItemTypes() {
		return IRS.getIRSInstance().getItemTypeSet().stream().map(t -> new ItemTypeData(t))
				.sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList());
	}
}
