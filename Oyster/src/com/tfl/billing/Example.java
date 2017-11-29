package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.Station;

public class Example {
    public static void main(String[] args) throws Exception {


        ExternalJarAdapter externalJarAdapter = new ExternalJarAdapter();
        OysterCard myCard = externalJarAdapter.getOysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        OysterCardReader paddingtonReader  = externalJarAdapter.getCardReader(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = externalJarAdapter.getCardReader(Station.BAKER_STREET);
        OysterCardReader kingsCrossReader = externalJarAdapter.getCardReader(Station.KINGS_CROSS);



        CardInteraction cardInteraction = new CardInteraction();
        //cardReadersData.connect(paddingtonReader,bakerStreetReader,kingsCrossReader);
        cardInteraction.connect(paddingtonReader,bakerStreetReader,kingsCrossReader,externalJarAdapter.getCardReader(Station.VICTORIA_STATION),externalJarAdapter.getCardReader(Station.ALDGATE),externalJarAdapter.getCardReader(Station.BANK));
        TravelTracker travelTracker = new TravelTracker(cardInteraction);
        paddingtonReader.touch(myCard);
        //minutesPass(5);
        bakerStreetReader.touch(myCard);
        //minutesPass(15);
        bakerStreetReader.touch(myCard);
        //minutesPass(10);
        kingsCrossReader.touch(myCard);



        kingsCrossReader.touch(myCard);
        externalJarAdapter.getCardReader(Station.VICTORIA_STATION).touch(myCard);
        externalJarAdapter.getCardReader(Station.VICTORIA_STATION).touch(myCard);
        externalJarAdapter.getCardReader(Station.ALDGATE).touch(myCard);
        externalJarAdapter.getCardReader(Station.ALDGATE).touch(myCard);
        externalJarAdapter.getCardReader(Station.BAKER_STREET).touch(myCard);
        externalJarAdapter.getCardReader(Station.BAKER_STREET).touch(myCard);
        externalJarAdapter.getCardReader(Station.BANK).touch(myCard);
        travelTracker.chargeAccounts();
    }
    private static void minutesPass(int n) throws InterruptedException {
        Thread.sleep(n * 60 * 1000);
    }
}
