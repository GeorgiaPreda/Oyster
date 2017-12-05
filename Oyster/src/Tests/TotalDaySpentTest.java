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
    ExternalJar externalJarAdapter = new ExternalJarAdapter();
    OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    OysterCardReader paddingtonReader =  externalJarAdapter.getCardReader(Station.PADDINGTON);
    OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);
    List<Journey> journeyList = new ArrayList<>();


    //asserts that a short journey during Non-Peak time is charged correctly
    @Test
    public void shortNonPeakChargeTest() throws Exception {


        JourneyStart short_nonpeak_journeyStart=new JourneyStart(myCard.id(), bakerStreetReader.id(), "2017/09/10 13:00:00");
        JourneyEnd short_nonpeak_journeyEnd=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 13:20:00");

        journeyList.add(new Journey(short_nonpeak_journeyStart,short_nonpeak_journeyEnd));

        assertEquals(new TotalDaySpent().customerTotalCost(journeyList),new BigDecimal(1.60));

    }

    //asserts that a short journey during Peak time is charged correctly
    @Test
    public void shortPeakChargeTest() throws Exception{



        JourneyStart short_peak_journeyStart=new JourneyStart(myCard.id(), paddingtonReader.id(), "2017/09/10 08:00:00");
        JourneyEnd short_peak_journeyEnd=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 08:20:00");


        journeyList.clear();
        journeyList.add(new Journey(short_peak_journeyStart,short_peak_journeyEnd));

        assertEquals(new TotalDaySpent().customerTotalCost(journeyList),new BigDecimal(2.90));

    }

    //asserts that a long journey during Non-Peak time is charged correctly
    @Test
    public void longNonPeakChargeTest() throws Exception{



        JourneyStart long_nonpeak_journeyStart=new JourneyStart(myCard.id(), bakerStreetReader.id(), "2017/09/10 13:00:00");
        JourneyEnd long_nonpeak_journeyEnd=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 14:20:00");


        journeyList.clear();
        journeyList.add(new Journey(long_nonpeak_journeyStart,long_nonpeak_journeyEnd));

        assertEquals(new TotalDaySpent().customerTotalCost(journeyList), new BigDecimal(2.70));

    }

    //asserts that a long journey during Peak time is charged correctly
    @Test
    public void longPeakChargeTest() throws Exception
    {



        JourneyStart long_peak_journeyStart=new JourneyStart(myCard.id(), bakerStreetReader.id(), "2017/09/10 07:00:00");
        JourneyEnd long_peak_journeyEnd=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 08:20:00");


        journeyList.clear();
        journeyList.add(new Journey(long_peak_journeyStart,long_peak_journeyEnd));


        assertEquals(new TotalDaySpent().customerTotalCost(journeyList), new BigDecimal(3.80));;
    }


    //asserts that the non-peak daily cap is applied
    @Test
    public void nonPeakDailyCapChargeTest() throws Exception{



        JourneyStart start1=new JourneyStart(myCard.id(), bakerStreetReader.id(), "2017/09/10 13:00:00");
        JourneyEnd end1=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 14:20:00");

        JourneyStart start2=new JourneyStart(myCard.id(), bakerStreetReader.id(), "2017/09/10 14:30:00");
        JourneyEnd end2=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 15:30:00");

        JourneyStart start3=new JourneyStart(myCard.id(), bakerStreetReader.id(), "2017/09/10 21:00:00");
        JourneyEnd end3=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 22:20:00");

        journeyList.clear();
        journeyList.add(new Journey(start1,end1));
        journeyList.add(new Journey(start2,end2));
        journeyList.add(new Journey(start3,end3));

        TotalDaySpent totalDaySpent= new TotalDaySpent();
        assertEquals(new BigDecimal(7.00),totalDaySpent.customerTotalCost(journeyList));


    }

    //asserts that the peak daily cap is applied
    @Test
    public void PeakDailyCapChargeTest() throws Exception{




        JourneyStart start1=new JourneyStart(myCard.id(), bakerStreetReader.id(), "2017/09/10 08:00:00");
        JourneyEnd end1=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 09:20:00");

        JourneyStart start2=new JourneyStart(myCard.id(), bakerStreetReader.id(), "2017/09/10 11:30:00");
        JourneyEnd end2=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 12:30:00");

        JourneyStart start3=new JourneyStart(myCard.id(), bakerStreetReader.id(), "2017/09/10 21:00:00");
        JourneyEnd end3=new JourneyEnd(myCard.id(), paddingtonReader.id(), "2017/09/10 22:20:00");

        journeyList.clear();
        journeyList.add(new Journey(start1,end1));
        journeyList.add(new Journey(start2,end2));
        journeyList.add(new Journey(start3,end3));

        TotalDaySpent totalDaySpent= new TotalDaySpent();
        assertEquals(new BigDecimal(9.00),totalDaySpent.customerTotalCost(journeyList));


    }
}
