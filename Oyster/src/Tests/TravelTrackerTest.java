package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.external.Customer;
import com.tfl.underground.Station;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TravelTrackerTest {



    ExternalJar externalJarAdapter = new ExternalJarAdapter();
    OysterCardReader paddingtonReader = externalJarAdapter.getCardReader(Station.PADDINGTON);
    OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);
    CardReaderInteraction cardReaderInteraction = new CardReaderInteraction();
    TravelTracker travelTracker = new TravelTracker(cardReaderInteraction);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();


    private Mockery context = new Mockery(){{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    ExternalJarAdapter mockExternalJarAdapter = context.mock(ExternalJarAdapter.class);

    private final TotalDaySpent mockTotalDaySpent = context.mock(TotalDaySpent.class);

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    //asserts that a customer is charged for a simple one journey
    @Test
    public void chargeAccounts() throws Exception{


        externalJarAdapter.getCustomers().clear();
        OysterCard oysterCard = new OysterCard();
        externalJarAdapter.getCustomers().add(new Customer("John Smith", oysterCard));

        cardReaderInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 08:00:00");
        cardReaderInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 08:20:00");
        travelTracker.chargeAccounts();
        setUpStreams();
        assertTrue(outContent.toString().contains("2.90"));
        cleanUpStreams();


    }


    //mock test - not working atm
    @Test
    public void chargeAccountsTest() throws Exception{
        List<Customer> customers = new ArrayList<>();
        List<JourneyEvent> eventLogTest = new ArrayList<>();

        customers.add(new Customer("John Smith", new OysterCard()));
        customers.add(new Customer("Jack Smith", new OysterCard()));


        JourneyStart journeyStartTest = new JourneyStart(customers.get(0).cardId(),paddingtonReader.id(),"2017/09/10 08:00:00");
        JourneyEnd journeyEndTest = new JourneyEnd(customers.get(0).cardId(), bakerStreetReader.id(), "2017/09/10 08:20:00");

        BigDecimal totalCost = new BigDecimal(2.40).setScale(2, BigDecimal.ROUND_HALF_UP);
        eventLogTest.add(journeyStartTest);
        eventLogTest.add(journeyEndTest);

        context.checking(new Expectations(){{
            oneOf(mockExternalJarAdapter).getCustomers(); will(returnValue(customers));
            oneOf(mockTotalDaySpent).customerTotalCost(with(any(List.class))); will(returnValue(totalCost));
            oneOf(mockExternalJarAdapter).charge(with(customers.get(0)),with(any(List.class)),with(totalCost));

        }});

        travelTracker.chargeAccounts();


        context.assertIsSatisfied();
    }






}
