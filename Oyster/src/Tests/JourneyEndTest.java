package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.JourneyEnd;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JourneyEndTest {


    private UUID cardExampleId = UUID.randomUUID();
    private UUID readerDestinationId = UUID.randomUUID();

    @Test
    public void createNewJourneyEndRightId() throws InterruptedException
    {

        JourneyEnd journeyEnd=new JourneyEnd(cardExampleId,readerDestinationId);
        assertThat(journeyEnd.cardId(), is(cardExampleId));
    }

    @Test
    public void createNewJourneyEndRightStationId() throws InterruptedException
    {
        JourneyEnd journeyEnd=new JourneyEnd(cardExampleId, readerDestinationId);
        assertThat(journeyEnd.readerId(), is(readerDestinationId));
    }


}