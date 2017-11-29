package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.CardInteraction;
import com.tfl.billing.ExternalJar;
import com.tfl.billing.ExternalJarAdapter;
import com.tfl.external.Customer;
import com.tfl.underground.Station;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CardInteractionTest {
    ExternalJar externalJarAdapter = new ExternalJarAdapter();


    @Test
    public void assertCardScanned() throws Exception {

        OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);

        CardInteraction myCardInteraction = new CardInteraction();

        myCardInteraction.cardScanned(myCard.id(),paddingtonReader.id());
        assertThat(myCardInteraction.getEventLog().size(),is(1));
        assertThat(myCardInteraction.getCurrentlyTravelling().size(),is(1));
        myCardInteraction.cardScanned(myCard.id(),bakerStreetReader.id());
        assertThat(myCardInteraction.getEventLog().size(),is(2));
        assertThat(myCardInteraction.getCurrentlyTravelling().size(),is(0));

    }


    @Test
    public void assertConnectMethod() throws Exception
    {   OysterCardReader paddingtonReader = externalJarAdapter.getCardReader(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);
        CardInteraction cardInteraction = new CardInteraction();
        int cardScanned_iteration = 0;
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
