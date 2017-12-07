package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.underground.Station;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class CardReaderInteractionTest {


    private ExternalJar externalJarAdapter = new ExternalJarAdapter();
    private Mockery context = new Mockery(){{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private CardReaderInteraction cardReaderInteraction = new CardReaderInteraction();
    private ExternalJarAdapter mockExternalJarAdapter = context.mock(ExternalJarAdapter.class);




    // checks that if a card is Scanned the the EventLog size gets bigger, while the currentlyTraveling size gets bigger or shrinks
    @Test
    public void cardScannedTest() throws Exception {

        OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);

        context.checking(new Expectations(){{
            oneOf(mockExternalJarAdapter).isRegisteredId(myCard.id()); will(returnValue(true));
        }});

        cardReaderInteraction.cardScanned(myCard.id(),paddingtonReader.id());
        assertEquals(cardReaderInteraction.getEventLog().size(),1);
        assertEquals(cardReaderInteraction.getCurrentlyTravelling().size(),1);
        assertEquals(cardReaderInteraction.getCurrentlyTravelling().contains(myCard.id()),true);
        assertEquals(cardReaderInteraction.getEventLog().get(0).cardId(),myCard.id());
        cardReaderInteraction.cardScanned(myCard.id(),bakerStreetReader.id());
        assertEquals(cardReaderInteraction.getEventLog().size(),2);
        assertEquals(cardReaderInteraction.getCurrentlyTravelling().size(),0);
        assertEquals(cardReaderInteraction.getEventLog().get(1).cardId(),myCard.id());

    }



    //checks that the connect method calls the register method
    @Test
    public void connectTest() throws Exception{
        OysterCardReader mockOysterCardReader1 = context.mock(OysterCardReader.class,"PADDINGTON");
        OysterCardReader mockOysterCardReader2 = context.mock(OysterCardReader.class,"BAKER_STREET");
        OysterCardReader mockOysterCardReader3 = context.mock(OysterCardReader.class,"VICTORIA");

        context.checking(new Expectations(){{
            exactly(1).of(mockOysterCardReader1).register(cardReaderInteraction);
            exactly(1).of(mockOysterCardReader2).register(cardReaderInteraction);
            exactly(1).of(mockOysterCardReader3).register(cardReaderInteraction);
        }});

        cardReaderInteraction.connect(mockOysterCardReader1,mockOysterCardReader2,mockOysterCardReader3);
    }



}
