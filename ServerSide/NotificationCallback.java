package ServerSide;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationCallback extends Remote {
    // Callback method for notifiers to receive confirmation
    public void notificationPublished(Event event, boolean success) throws RemoteException;
}