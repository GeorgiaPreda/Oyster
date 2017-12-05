package com.tfl.billing;

import com.oyster.OysterCardReader;
import com.oyster.ScanListener;
import com.tfl.external.CustomerDatabase;

import java.util.*;

public class CardInteraction implements ScanListener {

    Set<UUID> currentlyTravelling;
    List<JourneyEvent> eventLog;
    ExternalJarAdapter externalJarAdapter = new ExternalJarAdapter();

    public CardInteraction() {
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
            if (externalJarAdapter.isRegisteredId(cardId)) {
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
            if (CustomerDatabase.getInstance().isRegisteredId(cardId)) {
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