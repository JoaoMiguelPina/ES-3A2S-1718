package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Broker {
	private static Logger logger = LoggerFactory.getLogger(Broker.class);

	public static Set<Broker> brokers = new HashSet<>();

	private final String code;
	private final String name;
	private final String nifAsSeller;
	private final String nifAsBuyer;
	private String iban;
	private final Set<Adventure> adventures = new HashSet<>();
	private final Set<BulkRoomBooking> bulkBookings = new HashSet<>();
	private final Set<Client> clients = new HashSet<>();


	public Broker(String code, String name, String nifAsSeller, String nifAsBuyer, String iban) {
		checkArguments(code, name, nifAsSeller, nifAsBuyer, iban);
		
		this.code = code;
		this.name = name;
		this.nifAsSeller = nifAsSeller;
		this.nifAsBuyer = nifAsBuyer;
		this.iban = iban;
		
		Broker.brokers.add(this);
	}

	private void checkArguments(String code, String name, String nifAsSeller, String nifAsBuyer, String iban) {
		checkCode(code);
		checkName(name);
		checkNif(nifAsSeller);
		checkNif(nifAsBuyer);
		checkIban(iban);
		
		if(nifAsSeller.equals(nifAsBuyer)){
			throw new BrokerException();
		}
	}

	private void checkCode(String code) {
		if (code == null || code.trim().length() == 0) {
			throw new BrokerException();
		}

		for (Broker broker : Broker.brokers) {
			if (broker.getCode().equals(code)) {
				throw new BrokerException();
			}
		}
	}

	private void checkName(String name) {
		if (name == null || name.trim().length() == 0) {
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
		if(getBrokerByNif(nif) != null){
			throw new BrokerException();
		}
	}
	
	private void checkIban(String iban) {
		if(iban == null || iban.equals("") || (iban.trim()).equals("")){
			throw new BrokerException();
		}
	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}
	
	public String getNifAsSeller() {
		return nifAsSeller;
	}

	public String getNifAsBuyer() {
		return nifAsBuyer;
	}

	public String getIban() {
		return iban;
	}

	public void addClient(Client client){
		if(getClientByNif(client.getNif()) != null){
			throw new BrokerException();
		}
		clients.add(client);
	}
	
	public Client getClientByNif(String nif){
		for(Client client: clients){
			if(client.getNif().equals(nif)){
				return client;
			}
		}
		return null;
	}
	
	public Broker getBrokerByNif(String nif){
		for(Broker broker: brokers){
			if(nif.equals(broker.getNifAsSeller()) || nif.equals(broker.getNifAsBuyer())){
				return broker;
			}
		}
		return null;
	}
	
	public int getNumberOfAdventures() {
		return this.adventures.size();
	}

	public void addAdventure(Adventure adventure) {
		this.adventures.add(adventure);
	}

	public boolean hasAdventure(Adventure adventure) {
		return this.adventures.contains(adventure);
	}

	public void bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		BulkRoomBooking bulkBooking = new BulkRoomBooking(number, arrival, departure);
		this.bulkBookings.add(bulkBooking);
		bulkBooking.processBooking();
	}

}
