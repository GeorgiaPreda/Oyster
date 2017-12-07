package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.underground.Station;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JourneyTest {

    ExternalJar externalJarAdapter = new ExternalJarAdapter();
    OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
    OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);

    JourneyStart journeyStart = new JourneyStart(myCard.id(),paddingtonReader.id());
    JourneyEnd journeyEnd = new JourneyEnd(myCard.id(),bakerStreetReader.id());
    Journey journey = new Journey(journeyStart,journeyEnd);


    //checks the card Id
    @Test
    public void assertReturnOfCardID() throws InterruptedException
    {
        assertThat(journeyStart.cardId(), is(myCard.id()));
        assertThat(journeyEnd.cardId(), is(myCard.id()));
    }


    //checks the reader Id
    @Test
    public void assertReturnOfReaderID() throws InterruptedException
    {
        assertThat(journeyStart.readerId(), is(paddingtonReader.id()));
        assertThat(journeyEnd.readerId(), is(bakerStreetReader.id()));
    }


    //checks the start time
    @Test
    public void checkFormattedStartTimeTest() throws InterruptedException
    {

        assertThat(journey.formattedStartTime(), is(SimpleDateFormat.getInstance().format(new Date(journeyStart.time()))));
    }


    //checks the end time
    @Test
    public void checkFormattedEndTimeTest() throws InterruptedException
    {

        assertThat(journey.formattedEndTime(), is(SimpleDateFormat.getInstance().format(new Date(journeyEnd.time()))));
    }


    //checks the origin Id
    @Test
    public void assertJourneyOriginID() throws InterruptedException
    {
        assertEquals(journey.originId(),(journeyStart.readerId()));
    }


    //checks the destination Id
    @Test
    public void assertJourneyDestinationID() throws InterruptedException
    {
        assertEquals(journey.destinationId(),journeyEnd.readerId());
    }

    //checks the start time
    @Test
    public void assertStartTime() throws InterruptedException
    {

        assertThat(journey.startTime(), is(new Date(journeyStart.time())));
    }

    //checks the end time
    @Test
    public void assertEndTimeTest() throws InterruptedException
    {

        assertThat(journey.endTime(), is(new Date(journeyEnd.time())));
    }


    //checks the duration in seconds
    @Test
    public void assertDurationSeconds() throws InterruptedException {
        assertEquals(journey.durationSeconds(), (int) ((journeyEnd.time() - journeyStart.time()) / 1000));
    }


    //checks the duration in minutes
    @Test
    public void assertDurationMinutes() throws InterruptedException  {
        assertEquals(journey.durationMinutes(), journey.durationSeconds() / 60 );
    }

}