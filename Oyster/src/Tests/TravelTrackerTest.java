package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.external.Customer;
import com.tfl.underground.Station;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static org.junit.Assert.assertTrue;

public class TravelTrackerTest {



    private ExternalJar externalJarAdapter = new ExternalJarAdapter();
    private OysterCardReader paddingtonReader = externalJarAdapter.getCardReader(Station.PADDINGTON);
    private OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);
    private CardReaderInteraction cardReaderInteraction = new CardReaderInteraction();
    private TravelTracker travelTracker = new TravelTracker(cardReaderInteraction);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();


    //set up streams
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }


    //clean up streams
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
        externalJarAdapter.addCustomerToDatabase(new Customer("John Smith", oysterCard));

        cardReaderInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 08:00:00");
        cardReaderInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 08:20:00");
        travelTracker.chargeAccounts();
        setUpStreams();
        assertTrue(outContent.toString().contains("2.90"));
        cleanUpStreams();


    }


}
