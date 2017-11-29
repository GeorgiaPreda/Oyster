package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.CardInteraction;
import com.tfl.billing.ExternalJar;
import com.tfl.billing.ExternalJarAdapter;
import com.tfl.billing.UnknownOysterCardException;
import com.tfl.underground.Station;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
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

    @Test (expected = UnknownOysterCardException.class)
    public void assertExceptionIsThrownForUnknownCustomer()
    {
        OysterCard fakeCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef000");
        OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
        CardInteraction myCardInteraction = new CardInteraction();
        myCardInteraction.cardScanned(fakeCard.id(),paddingtonReader.id());
    }

}
