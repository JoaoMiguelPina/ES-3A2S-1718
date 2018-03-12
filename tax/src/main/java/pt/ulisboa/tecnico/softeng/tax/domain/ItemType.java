package pt.ulisboa.tecnico.softeng.tax.domain;

public class ItemType {
	private static int tax;
	
	public ItemType(int new_tax) {
		setTax(new_tax);
	}

	public static int getTax() {
		return tax;
	}

	public static void setTax(int tax) {
		ItemType.tax = tax;
	}
	
}
