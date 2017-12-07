package Tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.underground.Station;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TotalDaySpentTest {
    private ExternalJar externalJarAdapter = new ExternalJarAdapter();
    private  OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
    private OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);
    private List<Journey> journeyList = new ArrayList<>();


    //adds new journeys - created to eliminate duplicate code
    public void addJourney(String timeStart, String timeEnd)
    {

        JourneyStart journeyStart = new JourneyStart(myCard.id(),bakerStreetReader.id(),timeStart);
        JourneyEnd journeyEnd = new JourneyEnd(myCard.id(),paddingtonReader.id(),timeEnd);
        journeyList.add(new Journey(journeyStart,journeyEnd));

    }

    //checks if a short journey during Non-Peak time is charged correctly
    @Test
    public void shortNonPeakChargeTest() throws Exception {

        addJourney("2017/09/10 13:00:00","2017/09/10 13:20:00");
        assertEquals(new TotalDaySpent().customerTotalCost(journeyList),new BigDecimal(1.60));

    }

    //checks if a short journey during Peak time is charged correctly
    @Test
    public void shortPeakChargeTest() throws Exception{

        addJourney("2017/09/10 08:00:00","2017/09/10 08:20:00");
        assertEquals(new TotalDaySpent().customerTotalCost(journeyList),new BigDecimal(2.90));

    }

    //checks if a long journey during Non-Peak time is charged correctly
    @Test
    public void longNonPeakChargeTest() throws Exception{

        addJourney("2017/09/10 13:00:00","2017/09/10 14:20:00");
        assertEquals(new TotalDaySpent().customerTotalCost(journeyList), new BigDecimal(2.70));

    }

    //checks if a long journey during Peak time is charged correctly
    @Test
    public void longPeakChargeTest() throws Exception
    {
        addJourney("2017/09/10 7:00:00","2017/09/10 8:20:00");
        assertEquals(new TotalDaySpent().customerTotalCost(journeyList), new BigDecimal(3.80));

    }


    //checks if the non-peak daily cap is applied
    @Test
    public void nonPeakDailyCapChargeTest() throws Exception{

        addJourney("2017/09/10 13:00:00","2017/09/10 14:20:00");
        addJourney("2017/09/10 14:30:00","2017/09/10 15:30:00");
        addJourney("2017/09/10 21:00:00","2017/09/10 22:20:00");

        TotalDaySpent totalDaySpent= new TotalDaySpent();
        assertEquals(new BigDecimal(7.00),totalDaySpent.customerTotalCost(journeyList));

    }

    //checks if the peak daily cap is applied
    @Test
    public void PeakDailyCapChargeTest() throws Exception{

        addJourney("2017/09/10 08:00:00","2017/09/10 09:20:00");
        addJourney("2017/09/10 11:30:00","2017/09/10 12:30:00");
        addJourney("2017/09/10 21:00:00","2017/09/10 22:20:00");

        TotalDaySpent totalDaySpent= new TotalDaySpent();
        assertEquals(new BigDecimal(9.00),totalDaySpent.customerTotalCost(journeyList));

    }
}
