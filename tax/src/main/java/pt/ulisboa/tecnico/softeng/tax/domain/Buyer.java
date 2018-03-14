package pt.ulisboa.tecnico.softeng.tax.domain;

public class Buyer extends TaxPayer {

	public Buyer(String nif, String name, String address) {
		super(nif, name, address);
	}
	
	public float taxReturn (int year) {
		return (float) 0;
	}

}
