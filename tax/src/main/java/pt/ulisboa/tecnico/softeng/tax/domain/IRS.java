package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRS {
	private static IRS instance = getInstance();
	private static Map<String, TaxPayer> taxPayers;
	private static Set<InvoiceData> invoices;
	private static Set<ItemType> itemTypes;
	
	private IRS(){
		taxPayers = new HashMap<>();
		invoices = new HashSet<>();
		itemTypes = new HashSet<>();
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
	
	public static int getNumberTaxPayers(){
		return taxPayers.size();
	}
	
	public static int getNumberInvoices(){
		return invoices.size();
	}
	
	public static int getNumberItemType(){
		return itemTypes.size();
	}
	
	public static void clear(){
		taxPayers.clear();
		invoices.clear();
		itemTypes.clear();
	}
}
