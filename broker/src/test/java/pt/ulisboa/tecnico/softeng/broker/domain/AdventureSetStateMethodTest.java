package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureSetStateMethodTest {
	
	public static enum State {
        PROCESS_PAYMENT, RESERVE_ACTIVITY, BOOK_ROOM, UNDO, CONFIRMED, CANCELLED
    }
	
	private static final int AGE = 20;
	private static final int AMOUNT = 300;
	private static final String IBAN = "BK011234567";
	private Broker broker;
	private Adventure adventure;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	
	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
		this.adventure = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, AMOUNT);
	}

	@Test
	public void undoState() {
		this.adventure.setState(Adventure.State.UNDO);
	}
	
	@Test
	public void cancelledState() {
		this.adventure.setState(Adventure.State.CANCELLED);
	}
	
	@Test
	public void defaultState() {
		this.adventure.setState(Adventure.State.DEFAULT);
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
