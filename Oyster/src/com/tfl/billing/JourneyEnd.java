package com.tfl.billing;

import java.util.UUID;

public class JourneyEnd extends JourneyEvent {

    public JourneyEnd(UUID cardId, UUID readerId) {
        super(cardId, readerId);
    }

    //testing purpose
    public JourneyEnd(UUID cardId, UUID readerId, String time) {            //defined to be able to use specific time for testing
        super(cardId, readerId, time);
    }
}
