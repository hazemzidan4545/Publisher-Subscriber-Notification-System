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
import javax.swing.SwingUtilities;

public class BUEServer extends UnicastRemoteObject implements ServiceInterface {

    private List<PublisherInterface> publishers;
    private List<SubscriberInterface> subscribers;
    private Map<String, List<SubscriberInterface>> topicSubscribers;
    private List<String> availableTopics;
    private Map<String, List<String>> topicMessages;
    static BUEServerGUI server_form;
    
    protected BUEServer() throws RemoteException {
        super();
        publishers = new ArrayList<>();
        subscribers = new ArrayList<>();
        topicSubscribers = new HashMap<>();

        availableTopics = Arrays.asList("New Courses", "Student Activities", "Emergency Alerts", "Campus News");

        topicMessages = new HashMap<>();
        for (String topic : availableTopics) {
            topicSubscribers.put(topic, new ArrayList<>());
            topicMessages.put(topic, new ArrayList<>());
        }
    }

    @Override
    public synchronized void registerPublisher(PublisherInterface publisher) throws RemoteException {
        publishers.add(publisher);
        String message = "Publisher registered: " + publisher.getName();
        System.out.println(message);
        server_form.logEvent(message);
    }

    @Override
    public synchronized void deregisterPublisher(PublisherInterface publisher) throws RemoteException {
        publishers.remove(publisher);
        String message = "Publisher deregistered: " + publisher.getName();
        System.out.println(message);
        server_form.logEvent(message);
    }

    @Override
    public synchronized void registerSubscriber(SubscriberInterface subscriber) throws RemoteException {
        subscribers.add(subscriber);
        String message = "Subscriber registered: " + subscriber.getName();
        System.out.println(message);
        server_form.logEvent(message);
        server_form.updateTextArea(subscriber.getName());
    }

    @Override
    public synchronized void deregisterSubscriber(SubscriberInterface subscriber) throws RemoteException {
        subscribers.remove(subscriber);
        for (List<SubscriberInterface> topicSubs : topicSubscribers.values()) {
            topicSubs.remove(subscriber);
        }
        String message = "Subscriber deregistered: " + subscriber.getName();
        System.out.println(message);
        server_form.logEvent(message);
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
                String message = subscriber.getName() + " subscribed to " + topic;
                System.out.println(message);
                server_form.logEvent(message);
            }
        }
    }

    @Override
    public synchronized void unsubscribe(String topic, SubscriberInterface subscriber) throws RemoteException {
        if (topicSubscribers.containsKey(topic)) {
            List<SubscriberInterface> subs = topicSubscribers.get(topic);
            subs.remove(subscriber);
            String message = subscriber.getName() + " unsubscribed from " + topic;
            System.out.println(message);
            server_form.logEvent(message);
        }
    }

    @Override
    public synchronized void publishEvent(String topic, String message) throws RemoteException {
        String logMessage = "Publication event: " + topic + " - " + message;
        System.out.println(logMessage);
        server_form.logEvent(logMessage);

        if (topicMessages.containsKey(topic)) {
            topicMessages.get(topic).add(message);
        }

        if (topicSubscribers.containsKey(topic)) {
            List<SubscriberInterface> subscribers = topicSubscribers.get(topic);
            for (SubscriberInterface subscriber : subscribers) {
                try {
                    subscriber.notify(topic, message);
                } catch (RemoteException e) {
                    System.out.println("Failed to notify subscriber: " + subscriber.getName());
                    server_form.logEvent("Failed to notify subscriber: " + subscriber.getName());
                }
            }
        }
    }

    @Override
    public List<String> getMessagesForTopic(String topic) throws RemoteException {
        return topicMessages.containsKey(topic) ? new ArrayList<>(topicMessages.get(topic)) : new ArrayList<>();
    }
    
    @Override
    public void updateGUI(String name) throws RemoteException {      
        server_form.updateTextArea(name);
    }
    
    public static void main(String[] args) {
        try {

            SwingUtilities.invokeLater(() -> {
                server_form = new BUEServerGUI();
            });
            Thread.sleep(500);
            BUEServer service = new BUEServer();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("BUEService", service);
            
            String message = "BUE Service started successfully...";
            System.out.println(message);
            server_form.logEvent(message);
            
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}