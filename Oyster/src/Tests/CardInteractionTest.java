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


public class CardInteractionTest {


    ExternalJar externalJarAdapter = new ExternalJarAdapter();
    private Mockery context = new Mockery(){{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    CardInteraction cardInteraction = new CardInteraction();
    ExternalJarAdapter mockExternalJarAdapter = context.mock(ExternalJarAdapter.class);




    // assert that if a card is Scanned the the EventLog size gets bigger, while the currentlyTraveling size gets bigger or shrinks
    @Test
    public void cardScannedTest() throws Exception {

        OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);

        context.checking(new Expectations(){{
            oneOf(mockExternalJarAdapter).isRegisteredId(myCard.id()); will(returnValue(true));
        }});

        cardInteraction.cardScanned(myCard.id(),paddingtonReader.id());
        assertEquals(cardInteraction.getEventLog().size(),1);
        assertEquals(cardInteraction.getCurrentlyTravelling().size(),1);
        assertEquals(cardInteraction.getCurrentlyTravelling().contains(myCard.id()),true);
        assertEquals(cardInteraction.getEventLog().get(0).cardId(),myCard.id());
        cardInteraction.cardScanned(myCard.id(),bakerStreetReader.id());
        assertEquals(cardInteraction.getEventLog().size(),2);
        assertEquals(cardInteraction.getCurrentlyTravelling().size(),0);
        assertEquals(cardInteraction.getEventLog().get(1).cardId(),myCard.id());

    }



    //assert that the connect method calls the register method
    @Test
    public void connectTest() throws Exception{
        OysterCardReader mockOysterCardReader1 = context.mock(OysterCardReader.class,"PADDINGTON");
        OysterCardReader mockOysterCardReader2 = context.mock(OysterCardReader.class,"BAKER_STREET");
        OysterCardReader mockOysterCardReader3 = context.mock(OysterCardReader.class,"VICTORIA");

        context.checking(new Expectations(){{
            exactly(1).of(mockOysterCardReader1).register(cardInteraction);
            exactly(1).of(mockOysterCardReader2).register(cardInteraction);
            exactly(1).of(mockOysterCardReader3).register(cardInteraction);
        }});

        cardInteraction.connect(mockOysterCardReader1,mockOysterCardReader2,mockOysterCardReader3);
    }



}
