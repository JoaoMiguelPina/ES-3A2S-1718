package pt.ulisboa.tecnico.softeng.tax.domain;

public class Buyer extends TaxPayer {
	private static int RETURN_TAX = 5; 

	public Buyer(String nif, String name, String address) {
		super(nif, name, address);
	}
	
	public float taxReturn (int year) {
		return this.getIvaValueByYear(year) * RETURN_TAX / 100;
	}

}
