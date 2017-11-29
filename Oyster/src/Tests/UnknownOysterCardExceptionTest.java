package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.CardInteraction;
import com.tfl.billing.TravelTracker;
import com.tfl.billing.UnknownOysterCardException;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.Test;

public class UnknownOysterCardExceptionTest {

    @Test(expected = UnknownOysterCardException.class)
    public void assertExeptionIsThrownForUnknownCustomer()
    {

        OysterCard fakeCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef000");
        OysterCardReader paddingtonReader = OysterReaderLocator.atStation(Station.PADDINGTON);
        CardInteraction cardInteraction =new CardInteraction();
        TravelTracker travelTracker=new TravelTracker(cardInteraction);
        travelTracker.cardScanned(fakeCard.id(),paddingtonReader.id());
    }

}