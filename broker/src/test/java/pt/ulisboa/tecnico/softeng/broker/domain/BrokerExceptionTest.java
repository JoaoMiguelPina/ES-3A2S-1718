package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import java.util.ArrayList;

public class BrokerExceptionTest {

	@Test(expected = BrokerException.class)
	public void testBrokerException() {
	    throw new BrokerException("Erro");
	    
	}

}