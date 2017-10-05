package com.prostage.dental_manage.base;

import android.os.Handler;
import android.util.Log;

import java.util.AbstractQueue;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventDistributor extends Observable {
    private static final String TAG = "EventDistributor";

    public static final int UPDATE_NEW_RESERVATION = 1;
    public static final int UPDATE_NEW_CHAT = 2;

    private Handler handler;
    private AbstractQueue<Integer> events;


    private static EventDistributor instance;

    private EventDistributor() {
        this.handler = new Handler();
        events = new ConcurrentLinkedQueue<>();
    }

    public static synchronized EventDistributor getInstance() {
        if (instance == null) {
            instance = new EventDistributor();
        }
        return instance;
    }

    public void register(EventListener el) {
        addObserver(el);
    }

    public void unregister(EventListener el) {
        deleteObserver(el);
    }

    private void addEvent(Integer i) {
        events.offer(i);
        handler.post(EventDistributor.this::processEventQueue);
    }

    private void processEventQueue() {
        Integer result = 0;
        Log.d(TAG, "Processing event queue. Number of events: " + events.size());
        for (Integer current = events.poll(); current != null; current = events
                .poll()) {
            result |= current;
        }
        if (result != 0) {
            Log.d(TAG, "Notifying observers. Data: " + result);
            setChanged();
            notifyObservers(result);
        } else {
            Log.d(TAG, "Event queue didn't contain any new events. Observers will not be notified.");
        }
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    public void sendUpdateReservation() {
        addEvent(UPDATE_NEW_RESERVATION);
    }

    public void sendUpdateNewChat() {
        addEvent(UPDATE_NEW_CHAT);
    }

    public static abstract class EventListener implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if (observable instanceof EventDistributor
                    && data instanceof Integer) {
                update((EventDistributor) observable, (Integer) data);
            }
        }

        public abstract void update(EventDistributor eventDistributor,
                                    Integer arg);
    }
}
