package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.services.local.TaxInterface;

public class TaxValueData {
	private String nif;
	private Integer year;
	private Double value;
	
	public TaxValueData() {
	}

	public TaxValueData(Buyer buyer, int year) {
		this.nif = buyer.getNif();
		this.year = year;
		this.value = TaxInterface.TaxPayerTaxReturn(buyer, year);
	}
	
	public TaxValueData(Seller seller, int year) {
		this.nif = seller.getNif();
		this.year = year;
		this.value = TaxInterface.TaxPayerToPay(seller, year);
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
}
