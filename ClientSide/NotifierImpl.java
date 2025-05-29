package ClientSide;

import ServerSide.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NotifierImpl extends UnicastRemoteObject implements NotificationCallback {
    private String notifierName;

    public NotifierImpl(String notifierName) throws RemoteException {
        this.notifierName = notifierName;
    }

    @Override
    public void notificationPublished(Event event, boolean success) throws RemoteException {
        if (success) {
            System.out.println("[" + notifierName + "] Event successfully published: " + event.getEventName());
        } else {
            System.out.println("[" + notifierName + "] Failed to publish event: " + event.getEventName());
        }
    }

    public String getNotifierName() {
        return notifierName;
    }
}