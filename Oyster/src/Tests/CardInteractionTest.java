package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.CardInteraction;
import com.tfl.billing.ExternalJar;
import com.tfl.billing.ExternalJarAdapter;
import com.tfl.external.Customer;
import com.tfl.underground.Station;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CardInteractionTest {


    ExternalJar externalJarAdapter = new ExternalJarAdapter();
    private Mockery context = new Mockery(){{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    OysterCardReader mockOysterCardReader =  context.mock(OysterCardReader.class);
    CardInteraction cardInteraction = new CardInteraction();



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
    public void assertConnectMethodCallsRegisterMethod() {
        context.checking(new Expectations() {{
            exactly(1).of(mockOysterCardReader).register(cardInteraction);
        }});
        cardInteraction.connect(mockOysterCardReader);
    }



}
