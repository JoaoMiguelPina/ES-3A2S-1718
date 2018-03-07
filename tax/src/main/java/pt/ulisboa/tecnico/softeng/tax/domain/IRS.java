package pt.ulisboa.tecnico.softeng.tax.domain;

public class IRS {
	private static IRS instance;
	private static Set<TaxPayer> taxPayers;
	
	private IRS(){
		taxPayers = new HashSet<>();
	}
	
	public static IRS getInstance(){
		if(instance == null){
			instance = new IRS();
		}
		return instance;
	}
}
