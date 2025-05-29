package ServerSide;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SubscriberCallback extends Remote {
    // Callback method for subscribers to receive notifications
    public void receiveNotification(Event event) throws RemoteException;
}