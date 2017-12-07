package com.tfl.billing;

import com.oyster.OysterCardReader;
import com.oyster.ScanListener;
import java.util.*;

public class CardReaderInteraction implements ScanListener {

    Set<UUID> currentlyTravelling;
    List<JourneyEvent> eventLog;


    public CardReaderInteraction() {
        this.currentlyTravelling = new HashSet<>();
        eventLog = new ArrayList<>();
    }

    // connects the OysterCard Readers
    public void connect(OysterCardReader...oysterCardReaders) {
        for (OysterCardReader cardReader : oysterCardReaders) {
            cardReader.register(this);
        }
    }

    // updates EventLog and currentlyTravelling lists
    @Override
    public void cardScanned(UUID cardId, UUID readerId) {
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId));
            currentlyTravelling.remove(cardId);
        } else {
            if (ExternalJarAdapter.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }

    //created for testing purposes - test an event at a given time
    public void cardScanned(UUID cardId, UUID readerId, String time) {
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId, time));
            currentlyTravelling.remove(cardId);
        } else {
            if (ExternalJarAdapter.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId, time));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }

    //getter method
    public List<JourneyEvent> getEventLog() {return eventLog;}

    //getter method
    public Set<UUID> getCurrentlyTravelling() {return currentlyTravelling;}
}