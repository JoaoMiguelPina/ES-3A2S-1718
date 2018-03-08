package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashMap;
import java.util.Map;

public class IRS {
	private static IRS instance;
	private static Map<String, TaxPayer> taxPayers;
	
	private IRS(){
		taxPayers = new HashMap<>();
	}
	
	public static IRS getInstance(){
		if(instance == null){
			instance = new IRS();
		}
		return instance;
	}
}
