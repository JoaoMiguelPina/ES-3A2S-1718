package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;

public class ItemTypeData {

	private String name;
	private Integer tax;

	public ItemTypeData() {	
	}
	
	public ItemTypeData(ItemType itemType) {
		this.name = itemType.getName();
		this.tax = itemType.getTax();
	}
	
	public ItemTypeData(String _name, int _tax) {
		this.setName(_name);
		this.setTax(_tax);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTax() {
		return tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
	}
}
