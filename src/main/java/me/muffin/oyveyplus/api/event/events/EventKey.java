package me.muffin.oyveyplus.api.event.events;

import me.muffin.oyveyplus.api.event.Event;

/**
 * @author fuckyouthinkimboogieman
 */

public class EventKey extends Event {

    private final int key;

    public EventKey(int key) { this.key = key; }

    public int getKey() { return key; }
}
