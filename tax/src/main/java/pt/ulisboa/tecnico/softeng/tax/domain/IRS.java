package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRS {
	private static IRS instance;
	private static Map<String, TaxPayer> taxPayers;
	private static Set<InvoiceData> invoices;
	
	private IRS(){
		taxPayers = new HashMap<>();
		invoices = new HashSet<>();
	}
	
	public static IRS getInstance(){
		if(instance == null){
			instance = new IRS();
		}
		return instance;
	}
	
	public void addTaxPayer(TaxPayer taxPayer){
		if(taxPayers.get(taxPayer.getNif()) != null){
			throw new TaxException();
		}
		taxPayers.put(taxPayer.getNif(), taxPayer);
	}
	
	public static TaxPayer getTaxPayerByNIF(String nif){
		return taxPayers.get(nif);
	}
	
	public static int size(){
		return taxPayers.size();
	}
	
	public static void clear(){
		taxPayers.clear();
	}
}
