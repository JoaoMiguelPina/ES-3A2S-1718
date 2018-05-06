package pt.ulisboa.tecnico.softeng.broker.services.local;

import java.util.ArrayList;
import java.util.List;


import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;
import pt.ulisboa.tecnico.softeng.broker.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.AdventureData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData.CopyDepth;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BulkData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.ClientData;

public class BrokerInterface {

	@Atomic(mode = TxMode.READ)
	public static List<BrokerData> getBrokers() {
		List<BrokerData> brokers = new ArrayList<>();
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			brokers.add(new BrokerData(broker, CopyDepth.SHALLOW));
		}
		return brokers;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBroker(BrokerData brokerData) {
		new Broker(brokerData.getCode(), brokerData.getName(), brokerData.getNifAsSeller(), brokerData.getNifAsBuyer(),
				brokerData.getIban());
	}

	@Atomic(mode = TxMode.READ)
	public static BrokerData getBrokerDataByCode(String brokerCode, CopyDepth depth) {
		Broker broker = getBrokerByCode(brokerCode);

		if (broker != null) {
			return new BrokerData(broker, depth);
		} else {
			return null;
		}
	}

	@Atomic(mode = TxMode.READ)
	public static ClientData getClientDataByCode(String brokerCode, String nif) {
		BrokerData broker = BrokerInterface.getBrokerDataByCode(brokerCode, CopyDepth.CLIENTS);
		
		for(ClientData client : broker.getClients()){
			if(client.getNif().equals(nif)){
				return client;
			}
		}
		return null;
	}
	
	@Atomic(mode = TxMode.READ)
	public static Client getClientByCode(String brokerCode, String nif) {
		Broker broker = getBrokerByCode(brokerCode);
		
		return broker.getClientByNIF(nif);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createAdventure(String brokerCode, AdventureData adventureData) {
		if(adventureData.getMargin() == null || adventureData.getAge() == null){
			throw new BrokerException();
		}
		new Adventure(getBrokerByCode(brokerCode), adventureData.getBegin(), adventureData.getEnd(), adventureData.getClient(), adventureData.getMargin());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBulkRoomBooking(String brokerCode, BulkData bulkData) {
		new BulkRoomBooking(getBrokerByCode(brokerCode), bulkData.getNumber() != null ? bulkData.getNumber() : 0,
				bulkData.getArrival(), bulkData.getDeparture(), bulkData.getClientNIF(), bulkData.getBrokerIBAN());

	}

	@Atomic(mode = TxMode.WRITE)
	public static void createClient(String brokerCode, ClientData clientData) {
		if(clientData.getAge() == null){
			throw new BrokerException();
		}
		new Client(getBrokerByCode(brokerCode), clientData.getIban(), clientData.getNif(), clientData.getDrivingLicense(), clientData.getAge());

	}
	
	private static Broker getBrokerByCode(String code) {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			if (broker.getCode().equals(code)) {
				return broker;
			}
		}
		return null;
	}

}
