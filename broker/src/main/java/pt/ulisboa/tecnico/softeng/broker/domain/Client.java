package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;

public class Client {

	public Client(Broker broker, String IBAN, String NIF, String drivingLicense, int age) {
		checkArguments(broker, IBAN, NIF, drivingLicense, age);
		setIban(IBAN);
		setNif(NIF);
		setDrivingLicense(drivingLicense);
		setAge(age);

		broker.addClient(this);
	}

	private void checkArguments(Broker broker, String IBAN, String NIF, String drivingLicense, int age) {
		if (broker == null || IBAN == null || NIF == null ||
				IBAN.trim().isEmpty() || NIF.trim().isEmpty()) {
			throw new BrokerException();
		}

		if (drivingLicense != null && drivingLicense.trim().isEmpty()) {
			throw new BrokerException();
		}

		if (age < 0) {
			throw new BrokerException();
		}

		if (broker.getClientByNIF(NIF) != null) {
			throw new BrokerException();
		}

		if (broker.drivingLicenseIsRegistered(drivingLicense)) {
			throw new BrokerException();
		}

	}
	
	public void delete() {
		setRoot(null);
		deleteDomainObject();
	}
}
