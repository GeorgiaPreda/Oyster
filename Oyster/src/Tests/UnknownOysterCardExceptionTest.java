package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.underground.Station;
import org.junit.Test;

public class UnknownOysterCardExceptionTest {
    private ExternalJar externalJarAdapter = new ExternalJarAdapter();

    //assert if the exception is thrown
    @Test(expected = UnknownOysterCardException.class)
    public void unknownCardExceptionTest() throws Exception
    {

        OysterCard fakeCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef000");
        OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
        CardReaderInteraction myCardReaderInteraction = new CardReaderInteraction();
        myCardReaderInteraction.cardScanned(fakeCard.id(),paddingtonReader.id());
    }

}