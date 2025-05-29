package ServerSide;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface SubscriberInterface extends Remote {
    String getName() throws RemoteException;
    void notify(String topic, String message) throws RemoteException;
}