package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;

public class TaxPayerData {
	private String name;
	private String nif;
	private String address;
	private String type;
	private String id;
	private List<TaxValueData> taxValue = new ArrayList<>();

	public TaxPayerData() {
	}
	
	public TaxPayerData(TaxPayer taxPayer){
		this.name = taxPayer.getName();
		this.nif = taxPayer.getNif();
		this.address = taxPayer.getAddress();
		this.id = taxPayer.getExternalId();
		
		if(taxPayer instanceof Buyer){
			this.type = "Buyer";
			
			for(int year : taxPayer.getYearsTaxation()){
				taxValue.add(new TaxValueData((Buyer) taxPayer, year));
			}
		}
		else if(taxPayer instanceof Seller){
			this.type = "Seller";
			
			for(int year : taxPayer.getYearsTaxation()){
				taxValue.add(new TaxValueData((Seller) taxPayer, year));
			}
		}
		
		taxValue.sort((p1, p2) -> p1.getYear().compareTo(p2.getYear()));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TaxValueData> getTaxValue() {
		return taxValue;
	}

	public String getId() {
		return id;
	}
	
}
