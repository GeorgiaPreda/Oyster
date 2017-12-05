package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.underground.Station;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TotalDaySpentTest {
    ExternalJar externalJarAdapter = new ExternalJarAdapter();
    OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
    OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);


    //asserts that a short journey during Non-Peak time is charged correctly
    @Test
    public void chargeShortNonPeakAccounts() throws Exception {

        String startNonPeakTimeForShortJourney = "2017/09/10 13:00:00";
        String endNonPeakTimeForShortJourney="2017/09/10 13:20:00";

        JourneyStart short_nonpeak_journeyStart=new JourneyStart(myCard.id(), bakerStreetReader.id(), startNonPeakTimeForShortJourney);
        JourneyEnd short_nonpeak_journeyEnd=new JourneyEnd(myCard.id(), paddingtonReader.id(), endNonPeakTimeForShortJourney);

        Journey short_nonpeak_journey=new Journey(short_nonpeak_journeyStart, short_nonpeak_journeyEnd);

        assertEquals(new TotalDaySpent().costOfOneJourney(short_nonpeak_journey),new BigDecimal(1.60));

    }

    //asserts that a short journey during Peak time is charged correctly
    @Test
    public void chargeShortPeakAccounts() throws Exception{

        String startPeakTimeForShortJourney = "2017/09/10 08:00:00";
        String endPeakTimeForShortJourney="2017/09/10 08:20:00";

        JourneyStart short_peak_journeyStart=new JourneyStart(myCard.id(), paddingtonReader.id(), startPeakTimeForShortJourney);
        JourneyEnd short_peak_journeyEnd=new JourneyEnd(myCard.id(), paddingtonReader.id(), endPeakTimeForShortJourney);

        Journey short_peak_journey=new Journey(short_peak_journeyStart, short_peak_journeyEnd);

        assertEquals(new TotalDaySpent().costOfOneJourney(short_peak_journey),new BigDecimal(2.90));

    }

    //asserts that a long journey during Non-Peak time is charged correctly
    @Test
    public void chargeLongNonPeakAccounts() throws Exception{

        String startNonPeakTimeForLongJourney = "2017/09/10 13:00:00";
        String endNonPeakTimeForLongJourney="2017/09/10 14:20:00";

        JourneyStart long_nonpeak_journeyStart=new JourneyStart(myCard.id(), bakerStreetReader.id(), startNonPeakTimeForLongJourney);
        JourneyEnd long_nonpeak_journeyEnd=new JourneyEnd(myCard.id(), paddingtonReader.id(), endNonPeakTimeForLongJourney);

        Journey long_nonpeak_journey=new Journey(long_nonpeak_journeyStart, long_nonpeak_journeyEnd);

        assertEquals(new TotalDaySpent().costOfOneJourney(long_nonpeak_journey), new BigDecimal(2.70));

    }

    //asserts that a long journey during Peak time is charged correctly
    @Test
    public void chargeLongPeakAccounts() throws Exception
    {

        String startPeakTimeForLongJourney = "2017/09/10 07:00:00";
        String endPeakTimeForLongJourney="2017/09/10 08:20:00";

        JourneyStart long_peak_journeyStart=new JourneyStart(myCard.id(), bakerStreetReader.id(), startPeakTimeForLongJourney);
        JourneyEnd long_peak_journeyEnd=new JourneyEnd(myCard.id(), paddingtonReader.id(), endPeakTimeForLongJourney);

        Journey long_peak_journey=new Journey(long_peak_journeyStart, long_peak_journeyEnd);

        assertEquals(new TotalDaySpent().costOfOneJourney(long_peak_journey), new BigDecimal(3.80));;
    }
}
