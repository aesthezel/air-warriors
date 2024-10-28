package com.quesillostudios.testgamegdx.utils;

import com.quesillostudios.testgamegdx.utils.interfaces.EventListener;

import java.util.ArrayList;
import java.util.List;

public class EventManager
{
    private List<EventListener> listeners = new ArrayList<>();

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (EventListener listener : listeners) {
            listener.onTriggered();
        }
    }
}
