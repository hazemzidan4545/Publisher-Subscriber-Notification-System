package ServerSide;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface PublisherInterface extends Remote {
    String getName() throws RemoteException;
    void publishNotification(String topic, String message) throws RemoteException;
}