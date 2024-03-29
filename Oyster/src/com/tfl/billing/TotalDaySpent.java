package com.tfl.billing;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TotalDaySpent {
    static final BigDecimal PEAK_LONG_JOURNEY_PRICE = new BigDecimal(3.80);
    static final BigDecimal PEAK_SHORT_JOURNEY_PRICE = new BigDecimal(2.90);
    static final BigDecimal OFF_PEAK_LONG_JOURNEY_PRICE = new BigDecimal(2.70);
    static final BigDecimal OFF_PEAK_SHORT_JOURNEY_PRICE = new BigDecimal(1.60);
    static final int longJourneyDuration = 25;
    static final int morningPeakHourStart = 6;
    static final int morningPeakHourEnd = 10;
    static final int afternoonPeakHourStart = 17;
    static final int afternoonPeakHourEnd = 20;

    boolean peak_found=false;


    public TotalDaySpent() {}

    // does calculation per each journey and checks if the daily cap is reached
    public BigDecimal customerTotalCost(List<Journey> journeys) {
        BigDecimal customerTotal = new BigDecimal(0);
        for (Journey journey : journeys) {

            BigDecimal journeyPrice = this.costOfOneJourney(journey);
            customerTotal = customerTotal.add(journeyPrice);
        }
        customerTotal = testForDailyCap(customerTotal);
        return customerTotal;
    }


    //calculates the cost of one Journey
    private BigDecimal costOfOneJourney(Journey journey) {
        BigDecimal journeyPrice = OFF_PEAK_SHORT_JOURNEY_PRICE;
        boolean short_journey = short_journey(journey);
        if (peakJourney(journey)) {
            if (short_journey == true)
                journeyPrice = PEAK_SHORT_JOURNEY_PRICE;
            else
                journeyPrice = PEAK_LONG_JOURNEY_PRICE;
            peak_found=true;
        } else if (short_journey == false)
            journeyPrice = OFF_PEAK_LONG_JOURNEY_PRICE;
        return journeyPrice;
    }


    //rounds the result
    BigDecimal roundToNearestPenny(BigDecimal poundsAndPence) {
        return poundsAndPence.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    //checks if either the start Time or end Time is done during peak time
    boolean peakJourney(Journey journey) {
        return peak(journey.startTime()) || peak(journey.endTime());
    }


    // gets the hour of a given time and checks if it is rush hour
    boolean peak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return travelDuringPeakHour(hour);
    }


    //checks if a given hour is peak or not
    boolean travelDuringPeakHour(int hour) {
        return (hour >= morningPeakHourStart && hour <= morningPeakHourEnd) || (hour >= afternoonPeakHourStart && hour <= afternoonPeakHourEnd);
    }



    //checks if a hourney is short or long
    boolean short_journey(Journey journey) {
        if (journey.durationMinutes() <= longJourneyDuration)
            return true;
        else
            return false;
    }


    // checks if a peak journey exists
    public boolean found_peak()
    {
        return peak_found;
    }


    //checks if either peak or non peak daily cap must be applied
    private BigDecimal testForDailyCap(BigDecimal customerTotal) {
        BigDecimal dailyCapForPeak=new BigDecimal(9);
        BigDecimal dailyCapForNonPeak=new BigDecimal(7);

        if(this.found_peak()==true) {
            if(customerTotal.compareTo(dailyCapForPeak)>0)
                customerTotal=dailyCapForPeak;}
        else
        if(customerTotal.compareTo(dailyCapForNonPeak)>0)
            customerTotal=dailyCapForNonPeak;

        return customerTotal;
    }
}