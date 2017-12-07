package Tests;


import com.tfl.billing.JourneyEnd;
import org.junit.Test;
import java.util.UUID;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JourneyEndTest {


    private UUID cardExampleId = UUID.randomUUID();
    private UUID readerDestinationId = UUID.randomUUID();


    //checks the journeyEnd card id
    @Test
    public void createNewJourneyEndRightIdTest() throws InterruptedException
    {

        JourneyEnd journeyEnd=new JourneyEnd(cardExampleId,readerDestinationId);
        assertThat(journeyEnd.cardId(), is(cardExampleId));
    }

    //checks the journeyEnd reader id
    @Test
    public void createNewJourneyEndRightStationIdTest() throws InterruptedException
    {
        JourneyEnd journeyEnd=new JourneyEnd(cardExampleId, readerDestinationId);
        assertThat(journeyEnd.readerId(), is(readerDestinationId));
    }


}