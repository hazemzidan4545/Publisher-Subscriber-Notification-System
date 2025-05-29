package ServerSide;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BUEServer extends UnicastRemoteObject implements ServiceInterface {

    private List<PublisherInterface> publishers;
    private List<SubscriberInterface> subscribers;
    private Map<String, List<SubscriberInterface>> topicSubscribers;
    private List<String> availableTopics;
    private Map<String, List<String>> topicMessages; // New field to store messages per topic


    protected BUEServer() throws RemoteException {
        super();
        publishers = new ArrayList<>();
        subscribers = new ArrayList<>();
        topicSubscribers = new HashMap<>();

        availableTopics = Arrays.asList("New Courses", "Student Activities", "Emergency Alerts", "Campus News");

        topicMessages = new HashMap<>();
        for (String topic : availableTopics) {
            topicSubscribers.put(topic, new ArrayList<>());
            topicMessages.put(topic, new ArrayList<>()); // Initialize message lists
        }
    }

    @Override
    public synchronized void registerPublisher(PublisherInterface publisher) throws RemoteException {
        publishers.add(publisher);
        System.out.println("Publisher registered: " + publisher.getName());
    }

    @Override
    public synchronized void deregisterPublisher(PublisherInterface publisher) throws RemoteException {
        publishers.remove(publisher);
        System.out.println("Publisher deregistered: " + publisher.getName());
    }

    @Override
    public synchronized void registerSubscriber(SubscriberInterface subscriber) throws RemoteException {
        subscribers.add(subscriber);
        System.out.println("Subscriber registered: " + subscriber.getName());
    }

    @Override
    public synchronized void deregisterSubscriber(SubscriberInterface subscriber) throws RemoteException {
        subscribers.remove(subscriber);
        for (List<SubscriberInterface> topicSubs : topicSubscribers.values()) {
            topicSubs.remove(subscriber);
        }
        System.out.println("Subscriber deregistered: " + subscriber.getName());
    }

    @Override
    public List<String> getAvailableTopics() throws RemoteException {
        return new ArrayList<>(availableTopics);
    }

    @Override
    public synchronized void subscribe(String topic, SubscriberInterface subscriber) throws RemoteException {
        if (availableTopics.contains(topic)) {
            List<SubscriberInterface> subs = topicSubscribers.get(topic);
            if (!subs.contains(subscriber)) {
                subs.add(subscriber);
                System.out.println(subscriber.getName() + " subscribed to " + topic);
            }
        }
    }

    @Override
    public synchronized void unsubscribe(String topic, SubscriberInterface subscriber) throws RemoteException {
        if (topicSubscribers.containsKey(topic)) {
            List<SubscriberInterface> subs = topicSubscribers.get(topic);
            subs.remove(subscriber);
            System.out.println(subscriber.getName() + " unsubscribed from " + topic);
        }
    }

    @Override
    public synchronized void publishEvent(String topic, String message) throws RemoteException {
        System.out.println("Publication event: " + topic + " - " + message);

        if (topicMessages.containsKey(topic)) {
            topicMessages.get(topic).add(message); // Store message under topic
        }

        if (topicSubscribers.containsKey(topic)) {
            List<SubscriberInterface> subscribers = topicSubscribers.get(topic);
            for (SubscriberInterface subscriber : subscribers) {
                try {
                    subscriber.notify(topic, message);
                } catch (RemoteException e) {
                    System.out.println("Failed to notify subscriber: " + subscriber.getName());
                }
            }
        }
    }

    @Override
    public List<String> getMessagesForTopic(String topic) throws RemoteException {
        return topicMessages.containsKey(topic) ? new ArrayList<>(topicMessages.get(topic)) : new ArrayList<>();
    }

    public static void main(String[] args) {
        try {
            BUEServer service = new BUEServer();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("BUEService", service);
            System.out.println("BUE Service started...");
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
