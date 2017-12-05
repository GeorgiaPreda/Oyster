package com.tfl.billing;

import java.util.UUID;

public class JourneyStart extends JourneyEvent {
    public JourneyStart(UUID cardId, UUID readerId) {
        super(cardId, readerId);
    }

    //testing purpose
    public JourneyStart(UUID cardId, UUID readerId, String time) {      //defined to be able to use specific time for testing
        super(cardId, readerId, time);
    }
}
