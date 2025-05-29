package ClientSide;

import ServerSide.ServiceInterface;
import ServerSide.SubscriberInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class SubscriberApp extends UnicastRemoteObject implements SubscriberInterface {

    private String name;
    private ServiceInterface service;
    private List<String> subscribedTopics;
    private SubscriberNotificationCenter notificationCenter;

    public SubscriberApp(String name, ServiceInterface service) throws RemoteException {
        this.name = name;
        this.service = service;
        this.subscribedTopics = new ArrayList<>();

        SwingUtilities.invokeLater(() -> {
            try {
                notificationCenter = new SubscriberNotificationCenter(name);
            } catch (Exception e) {
                System.err.println("Error creating notification center: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.registerSubscriber(this);
        System.out.println("Subscriber [" + name + "] registered with BUE Service");
        
        if (notificationCenter != null) {
            notificationCenter.addNotification("System", "Registered with BUE Service successfully");
        }
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void notify(String topic, String message) throws RemoteException {
        System.out.println("\n[NOTIFICATION] Topic: " + topic + " - Message: " + message);
        
        if (notificationCenter != null) {
            notificationCenter.displayNotification(topic, message);
        }
    }

    public void subscribe(String topic) throws RemoteException {
        service.subscribe(topic, this);
        if (!subscribedTopics.contains(topic)) {
            subscribedTopics.add(topic);
            
            if (notificationCenter != null) {
                notificationCenter.addNotification("System", "Subscribed to topic: " + topic);
                notificationCenter.updateStatus("Active with " + subscribedTopics.size() + " subscriptions");
            }
        }
    }

    public void unsubscribe(String topic) throws RemoteException {
        service.unsubscribe(topic, this);
        subscribedTopics.remove(topic);
        
        if (notificationCenter != null) {
            notificationCenter.addNotification("System", "Unsubscribed from topic: " + topic);
            notificationCenter.updateStatus("Active with " + subscribedTopics.size() + " subscriptions");
        }
    }

    public List<String> getSubscribedTopics() {
        return new ArrayList<>(subscribedTopics);
    }

    public void deregister() throws RemoteException {
        service.deregisterSubscriber(this);
        
        if (notificationCenter != null) {
            notificationCenter.addNotification("System", "Deregistered from BUE Service");
            notificationCenter.updateStatus("Disconnected");
        }
    }

    public void startConsole() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        List<String> availableTopics = service.getAvailableTopics();

        while (true) {
            System.out.println("\n=== Subscriber Menu ===");
            System.out.println("1. View available topics");
            System.out.println("2. Subscribe to a topic");
            System.out.println("3. Unsubscribe from a topic");
            System.out.println("4. View my subscriptions");
            System.out.println("5. View past messages for a subscribed topic");
            System.out.println("6. Deregister and exit");
            System.out.print("Choose an option: ");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (option == 1) {
                System.out.println("\nAvailable topics:");
                for (int i = 0; i < availableTopics.size(); i++) {
                    System.out.println((i+1) + ". " + availableTopics.get(i));
                }

            } else if (option == 2) {
                System.out.println("\nAvailable topics:");
                for (int i = 0; i < availableTopics.size(); i++) {
                    System.out.println((i+1) + ". " + availableTopics.get(i));
                }

                System.out.print("Select a topic to subscribe (1-" + availableTopics.size() + "): ");
                int topicIndex;
                try {
                    topicIndex = Integer.parseInt(scanner.nextLine()) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                if (topicIndex >= 0 && topicIndex < availableTopics.size()) {
                    String topic = availableTopics.get(topicIndex);
                    subscribe(topic);
                    System.out.println("Subscribed to topic: " + topic);
                } else {
                    System.out.println("Invalid topic selection.");
                }

            } else if (option == 3) {
                List<String> subscribedTopics = getSubscribedTopics();

                if (subscribedTopics.isEmpty()) {
                    System.out.println("You are not subscribed to any topics.");
                } else {
                    System.out.println("\nYour subscriptions:");
                    for (int i = 0; i < subscribedTopics.size(); i++) {
                        System.out.println((i+1) + ". " + subscribedTopics.get(i));
                    }

                    System.out.print("Select a topic to unsubscribe (1-" + subscribedTopics.size() + "): ");
                    int topicIndex;
                    try {
                        topicIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        continue;
                    }

                    if (topicIndex >= 0 && topicIndex < subscribedTopics.size()) {
                        String topic = subscribedTopics.get(topicIndex);
                        unsubscribe(topic);
                        System.out.println("Unsubscribed from topic: " + topic);
                    } else {
                        System.out.println("Invalid topic selection.");
                    }
                }

            } else if (option == 4) {
                List<String> subscribedTopics = getSubscribedTopics();

                if (subscribedTopics.isEmpty()) {
                    System.out.println("You are not subscribed to any topics.");
                } else {
                    System.out.println("\nYour subscriptions:");
                    for (int i = 0; i < subscribedTopics.size(); i++) {
                        System.out.println((i+1) + ". " + subscribedTopics.get(i));
                    }
                }

            } else if (option == 5) {
                List<String> subscribedTopics = getSubscribedTopics();

                if (subscribedTopics.isEmpty()) {
                    System.out.println("You are not subscribed to any topics.");
                } else {
                    System.out.println("\nYour subscribed topics:");
                    for (int i = 0; i < subscribedTopics.size(); i++) {
                        System.out.println((i + 1) + ". " + subscribedTopics.get(i));
                    }

                    System.out.print("Select a topic to view past messages (1-" + subscribedTopics.size() + "): ");
                    int topicIndex;
                    try {
                        topicIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        continue;
                    }

                    if (topicIndex >= 0 && topicIndex < subscribedTopics.size()) {
                        String topic = subscribedTopics.get(topicIndex);
                        List<String> messages = service.getMessagesForTopic(topic);

                        if (messages.isEmpty()) {
                            System.out.println("No past messages for topic: " + topic);
                        } else {
                            System.out.println("\n--- Past Messages for [" + topic + "] ---");
                            for (String msg : messages) {
                                System.out.println("- " + msg);
                            }
                        }
                    } else {
                        System.out.println("Invalid topic selection.");
                    }
                }

            } else if (option == 6) {
                deregister();
                System.out.println("Subscriber deregistered. Exiting...");
                break;

            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServiceInterface service = (ServiceInterface) registry.lookup("BUEService");

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter subscriber name: ");
            String subscriberName = scanner.nextLine();
            SubscriberApp subscriber = new SubscriberApp(subscriberName, service);
            subscriber.startConsole();

        } catch (Exception e) {
            System.err.println("Subscriber error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}