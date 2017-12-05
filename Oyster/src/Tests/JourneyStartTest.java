package Tests;


import com.tfl.billing.JourneyStart;
import org.junit.Test;
import java.util.UUID;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JourneyStartTest {

    private UUID cardExampleId = UUID.randomUUID();
    private UUID readerOriginId = UUID.randomUUID();

    @Test
    public void createNewJourneyStartRightIdTest() throws InterruptedException
    {

        JourneyStart journeyStart=new JourneyStart(cardExampleId,readerOriginId);
        assertThat(journeyStart.cardId(), is(cardExampleId));
    }

    @Test
    public void createNewJourneyStartRightStationIdTest() throws InterruptedException
    {
        JourneyStart journeyStart=new JourneyStart(cardExampleId, readerOriginId);
        assertThat(journeyStart.readerId(), is(readerOriginId));
    }

}