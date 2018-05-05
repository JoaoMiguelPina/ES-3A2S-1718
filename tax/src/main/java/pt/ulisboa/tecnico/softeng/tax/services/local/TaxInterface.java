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
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

public class TaxInterface {
	
	@Atomic(mode = TxMode.WRITE)
	public static void createInvoice(InvoiceData invoice) {
		IRS.getIRSInstance();
		IRS.submitInvoice(invoice);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static List<InvoiceData> getInvoices() {
		Set<InvoiceData> invoices = new HashSet<>();
		for(Invoice invoice : IRS.getIRSInstance().getInvoiceSet()){
			invoices.add(new InvoiceData(invoice));
		}
		return invoices.stream().sorted((p1, p2) -> p1.getBuyerNIF().compareTo(p2.getBuyerNIF())).collect(Collectors.toList());
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
	
}
