package ClientSide;

import ServerSide.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class SubscriberImpl extends UnicastRemoteObject implements SubscriberCallback {
    private String subscriberName;
    private List<Event> receivedEvents;

    public SubscriberImpl(String subscriberName) throws RemoteException {
        this.subscriberName = subscriberName;
        this.receivedEvents = new ArrayList<>();
    }

    @Override
    public void receiveNotification(Event event) throws RemoteException {
        System.out.println("\n[" + subscriberName + "] Received notification:");
        System.out.println("Event: " + event.getEventName());
        System.out.println("Category: " + event.getCategory());
        System.out.println("Description: " + event.getDescription());
        System.out.println("Time: " + event.getTimestamp());
        System.out.println("Expires: " + event.getExpiryTime());

        // Store the event
        receivedEvents.add(event);
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public List<Event> getReceivedEvents() {
        return new ArrayList<>(receivedEvents);
    }
}