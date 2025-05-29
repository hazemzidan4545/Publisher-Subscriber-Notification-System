package ClientSide;

import ServerSide.PublisherInterface;
import ServerSide.ServiceInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class PublisherApp extends UnicastRemoteObject implements PublisherInterface {

    private String name;
    private ServiceInterface service;

    public PublisherApp(String name, ServiceInterface service) throws RemoteException {
        this.name = name;
        this.service = service;
        service.registerPublisher(this);
        System.out.println("Publisher [" + name + "] registered with BUE Service");
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void publishNotification(String topic, String message) throws RemoteException {
        service.publishEvent(topic, message);
    }

    public void deregister() throws RemoteException {
        service.deregisterPublisher(this);
    }

    public void startConsole() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        List<String> topics = service.getAvailableTopics();

        System.out.println("\nAvailable topics:");
        for (int i = 0; i < topics.size(); i++) {
            System.out.println((i+1) + ". " + topics.get(i));
        }

        while (true) {
            System.out.println("\n=== Publisher Menu ===");
            System.out.println("1. Publish notification");
            System.out.println("2. Deregister and exit");
            System.out.print("Choose an option: ");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (option == 1) {
                System.out.println("\nSelect a topic (1-" + topics.size() + "): ");
                int topicIndex;
                try {
                    topicIndex = Integer.parseInt(scanner.nextLine()) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                if (topicIndex >= 0 && topicIndex < topics.size()) {
                    String topic = topics.get(topicIndex);

                    System.out.print("Enter notification message: ");
                    String message = scanner.nextLine();

                    publishNotification(topic, message);
                    System.out.println("Notification published to topic: " + topic);
                } else {
                    System.out.println("Invalid topic selection.");
                }
            } else if (option == 2) {
                deregister();
                System.out.println("Publisher deregistered. Exiting...");
                break;
            }
            else if (option == 3) {
                List<String> availableTopics = service.getAvailableTopics();
                System.out.println("\n--- All Published Messages ---");
                for (String topic : availableTopics) {
                    List<String> messages = service.getMessagesForTopic(topic);
                    System.out.println("\n[" + topic + "]");
                    if (messages.isEmpty()) {
                        System.out.println("  (No messages)");
                    } else {
                        for (String msg : messages) {
                            System.out.println("  - " + msg);
                        }
                    }
                }
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

            System.out.print("Enter publisher name: ");
            String publisherName = scanner.nextLine();

            PublisherApp publisher = new PublisherApp(publisherName, service);
            publisher.startConsole();

        } catch (Exception e) {
            System.err.println("Publisher error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}