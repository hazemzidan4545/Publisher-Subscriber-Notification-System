package ServerSide;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface EventService extends Remote {
    // Registration methods
    public boolean registerNotifier(String notifierName, NotificationCallback callback) throws RemoteException;
    public boolean registerSubscriber(String subscriberName, SubscriberCallback callback) throws RemoteException;

    // Deregistration methods
    public boolean deregisterNotifier(String notifierName) throws RemoteException;
    public boolean deregisterSubscriber(String subscriberName) throws RemoteException;

    // Event management methods
    public boolean publishEvent(String notifierName, Event event) throws RemoteException;
    public List<String> getAvailableCategories() throws RemoteException;

    // Subscription management methods
    public boolean subscribeToCategory(String subscriberName, String category) throws RemoteException;
    public boolean unsubscribeFromCategory(String subscriberName, String category) throws RemoteException;
    public List<String> getSubscribedCategories(String subscriberName) throws RemoteException;
}