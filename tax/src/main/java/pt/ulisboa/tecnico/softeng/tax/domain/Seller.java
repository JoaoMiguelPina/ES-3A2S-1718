package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Seller extends TaxPayer {

	public Seller(String nif, String name, String address) {
		super(nif, name, address);
	}
	
	public float toPay(int year) {
		// TODO
		if (year < 1970) {
			throw new TaxException();
		}
		return (float) 0;
	}
}
