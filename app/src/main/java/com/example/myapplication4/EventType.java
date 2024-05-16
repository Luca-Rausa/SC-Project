package com.example.myapplication4;

public enum EventType {
    MEETING("Meeting"),
    DEBATE("Debate"),
    CONFERENCE("Conference"),
    PRESENTATION("Presentation"),
    WORKSHOP("Workshop");

    private final String stringValue;

    EventType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static EventType fromString(String text) {
        for(EventType eventType: EventType.values())
            if(eventType.stringValue.equalsIgnoreCase(text))
                return eventType;
        return null;
    }
}