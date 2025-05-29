package ServerSide;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface ServiceInterface extends Remote {
    void registerPublisher(PublisherInterface publisher) throws RemoteException;
    void deregisterPublisher(PublisherInterface publisher) throws RemoteException;
    void registerSubscriber(SubscriberInterface subscriber) throws RemoteException;
    void deregisterSubscriber(SubscriberInterface subscriber) throws RemoteException;
    List<String> getAvailableTopics() throws RemoteException;
    void subscribe(String topic, SubscriberInterface subscriber) throws RemoteException;
    void unsubscribe(String topic, SubscriberInterface subscriber) throws RemoteException;
    void publishEvent(String topic, String message) throws RemoteException;
    List<String> getMessagesForTopic(String topic) throws RemoteException;
    public void updateGUI(String name) throws RemoteException;    

}