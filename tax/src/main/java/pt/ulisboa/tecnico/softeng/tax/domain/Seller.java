package pt.ulisboa.tecnico.softeng.tax.domain;


public class Seller extends TaxPayer {
	public Seller(String nif, String name, String address) {
		super(nif, name, address);
	}
	
	public float toPay(int year) {
		return this.getIvaValueByYear(year);
	}
}
