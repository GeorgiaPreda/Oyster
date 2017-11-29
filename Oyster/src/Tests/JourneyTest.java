package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.external.CustomerDatabase;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JourneyTest {

    OysterCard myCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");

    OysterCardReader paddingtonReader = OysterReaderLocator.atStation(Station.PADDINGTON);
    OysterCardReader bakerStreetReader = OysterReaderLocator.atStation(Station.BAKER_STREET);



    JourneyStart journeyStart = new JourneyStart(myCard.id(),paddingtonReader.id());
    JourneyEnd journeyEnd = new JourneyEnd(myCard.id(),bakerStreetReader.id());
    Journey journey = new Journey(journeyStart,journeyEnd);


    @Test
    public void assertReturnOfCardID() throws InterruptedException
    {
        assertThat(journeyStart.cardId(), is(myCard.id()));
        assertThat(journeyEnd.cardId(), is(myCard.id()));
    }

    @Test
    public void assertReturnOfReaderID() throws InterruptedException
    {
        assertThat(journeyStart.readerId(), is(paddingtonReader.id()));
        assertThat(journeyEnd.readerId(), is(bakerStreetReader.id()));
    }

    @Test
    public void checkFormattedStartTimeTest() throws InterruptedException
    {

        assertThat(journey.formattedStartTime(), is(SimpleDateFormat.getInstance().format(new Date(journeyStart.time()))));
    }

    @Test
    public void checkFormattedEndTimeTest() throws InterruptedException
    {

        assertThat(journey.formattedEndTime(), is(SimpleDateFormat.getInstance().format(new Date(journeyEnd.time()))));
    }

    @Test
    public void assertJourneyOriginID() throws InterruptedException
    {
        assertEquals(journey.originId(),(journeyStart.readerId()));
    }

    @Test
    public void assertJourneyDestinationID() throws InterruptedException
    {
        assertEquals(journey.destinationId(),journeyEnd.readerId());
    }

    @Test
    public void assertStartTime() throws InterruptedException
    {

        assertThat(journey.startTime(), is(new Date(journeyStart.time())));
    }

    @Test
    public void assertEndTimeTest() throws InterruptedException
    {

        assertThat(journey.endTime(), is(new Date(journeyEnd.time())));
    }

    @Test
    public void durationSeconds() throws InterruptedException {
        assertEquals(journey.durationSeconds(), (int) ((journeyEnd.time() - journeyStart.time()) / 1000));
    }

    @Test
    public void durationMinutes() throws InterruptedException  {
        assertEquals(journey.durationMinutes(), journey.durationSeconds() / 60 );
    }

}