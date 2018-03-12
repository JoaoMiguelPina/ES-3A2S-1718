package pt.ulisboa.tecnico.softeng.tax.domain;

public class ItemType {
	private int tax;
	private IRS irs;
	
	public ItemType(int new_tax) {
		if (new_tax < 0) {
			throw new TaxException(); 
		}
		this.tax = new_tax;
		this.irs = IRS.getInstance();
	}

	public int getTax() {
		return this.tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}
	
}
