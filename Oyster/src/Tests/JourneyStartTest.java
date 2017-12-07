package Tests;


import com.tfl.billing.JourneyStart;
import org.junit.Test;
import java.util.UUID;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JourneyStartTest {

    private UUID cardExampleId = UUID.randomUUID();
    private UUID readerOriginId = UUID.randomUUID();


    //checks the journeyStart card id
    @Test
    public void createNewJourneyStartRightIdTest() throws InterruptedException
    {

        JourneyStart journeyStart=new JourneyStart(cardExampleId,readerOriginId);
        assertThat(journeyStart.cardId(), is(cardExampleId));
    }

    //checks the journeyStart reader id
    @Test
    public void createNewJourneyStartRightStationIdTest() throws InterruptedException
    {
        JourneyStart journeyStart=new JourneyStart(cardExampleId, readerOriginId);
        assertThat(journeyStart.readerId(), is(readerOriginId));
    }

}