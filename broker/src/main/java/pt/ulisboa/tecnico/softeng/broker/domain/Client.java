package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client {
	private Broker broker;
	private String iban;
	private String nif;
	private String drivingLicense;
	private int age;
	
	public Client(Broker broker, String iban, String nif, String drivingLicense, int age) {
		checkArguments(broker, iban, nif, drivingLicense, age);
		
		this.broker = broker;
		this.iban = iban;
		this.nif = nif;
		this.drivingLicense = drivingLicense;
		this.age = age;
		
		this.broker.addClient(this);
	}

	private void checkArguments(Broker broker, String iban, String nif, String drivingLicense, int age) {
		checkBroker(broker);
		checkIban(iban);
		checkNif(nif);
		checkDrivingLicense(drivingLicense);
		checkAge(age);
	}

	private void checkBroker(Broker broker) {
		if(broker == null){
			throw new BrokerException();
		}
	}

	private void checkIban(String iban) {
		if(iban == null || iban.equals("") || (iban.trim()).equals("")){
			throw new BrokerException();
		}
	}

	private void checkNif(String nif) {
		if(nif == null || nif.equals("") || (nif.trim()).equals("")){
			throw new BrokerException();
		}
		if(nif.length() != 9){
			throw new BrokerException();
		}
		if(!nif.matches("[0-9]+")){
			throw new BrokerException();
		}
	}

	private void checkDrivingLicense(String drivingLicense) {
		if(drivingLicense == null){
			return;
		}
		if(drivingLicense.equals("") || (drivingLicense.trim()).equals("")){
			throw new BrokerException();
		}
		if(!drivingLicense.matches("[a-zA-Z]+[0-9]+")){
			throw new BrokerException();
		}
	}

	private void checkAge(int age) {
		if(age < 18 || age > 100){
			throw new BrokerException();
		}
		
	}

	public Broker getBroker() {
		return broker;
	}

	public String getIban() {
		return iban;
	}

	public String getNif() {
		return nif;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public int getAge() {
		return age;
	}	
}
