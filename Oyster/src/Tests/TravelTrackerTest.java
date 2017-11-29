package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.CardInteraction;
import com.tfl.billing.ExternalJar;
import com.tfl.billing.ExternalJarAdapter;
import com.tfl.billing.TravelTracker;
import com.tfl.external.Customer;
import com.tfl.underground.Station;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TravelTrackerTest {


    ExternalJar externalJarAdapter = new ExternalJarAdapter();

    OysterCardReader paddingtonReader = externalJarAdapter.getCardReader(Station.PADDINGTON);
    OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);
    CardInteraction cardInteraction = new CardInteraction();
    TravelTracker travelTracker = new TravelTracker(cardInteraction);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

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


    @Test
    public void chargeAccounts() throws Exception{


        externalJarAdapter.getCustomers().clear();
        OysterCard oysterCard = new OysterCard();
        externalJarAdapter.getCustomers().add(new Customer("John Smith", oysterCard));

        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 08:00:00");
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 08:20:00");
        travelTracker.chargeAccounts();
        setUpStreams();
        assertTrue(outContent.toString().contains("2.90"));
        cleanUpStreams();


    }
    @Test
    public void chargeAccountforNoPeak() throws Exception{

        externalJarAdapter.getCustomers().clear();
        OysterCard oysterCard = new OysterCard();
        externalJarAdapter.getCustomers().add(new Customer("John Smith", oysterCard));
        //assertEquals(externalJarAdapter.getCustomers().size(),1);
        cardInteraction.connect(paddingtonReader,bakerStreetReader);
        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 13:00:00");
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 14:20:00");
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 14:30:00");
        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 15:30:00");
        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 21:00:00");
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 22:20:00");
        travelTracker.chargeAccounts();

        setUpStreams();
        assertTrue(outContent.toString().contains("7.00"));
        //cleanUpStreams();

    }

    @Test
    public void chargeAccountsForPeak() throws Exception
    {
        externalJarAdapter.getCustomers().clear();
        OysterCard oysterCard = new OysterCard();
        externalJarAdapter.getCustomers().add(new Customer("John Smith", oysterCard));
        cardInteraction.connect(paddingtonReader,bakerStreetReader);
        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 8:00:00");
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 9:20:00");
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 11:30:00");
        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 12:30:00");
        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 21:00:00");
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 22:20:00");
        travelTracker.chargeAccounts();

        setUpStreams();
        assertTrue(outContent.toString().contains("9.00"));
        cleanUpStreams();
    }

    @Test
    public void assertConnectMethod() throws Exception
    {   int cardScanned_iteration = 0;
        externalJarAdapter.getCustomers().clear();
        OysterCard oysterCard = new OysterCard();
        externalJarAdapter.getCustomers().add(new Customer("John Smith", oysterCard));
        cardInteraction.connect(paddingtonReader,bakerStreetReader);
        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 8:00:00");
        cardScanned_iteration++;
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 9:20:00");
        cardScanned_iteration++;
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 11:30:00");
        cardScanned_iteration++;
        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 12:30:00");
        cardScanned_iteration++;
        cardInteraction.cardScanned(oysterCard.id(), paddingtonReader.id(), "2017/09/10 21:00:00");
        cardScanned_iteration++;
        cardInteraction.cardScanned(oysterCard.id(), bakerStreetReader.id(), "2017/09/10 22:20:00");
        cardScanned_iteration++;
        assertEquals(cardInteraction.getEventLog().size(),cardScanned_iteration);
    }

}
