package ClientSide;

import ServerSide.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class SubscriberApp {
    private String subscriberName;
    private SubscriberImpl subscriber;
    private EventService service;

    public SubscriberApp(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public void start() {
        try {
            // Connect to the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Look up the event service
            service = (EventService) registry.lookup("BUEEventService");

            // Create the subscriber implementation
            subscriber = new SubscriberImpl(subscriberName);

            // Register with the service
            boolean registered = service.registerSubscriber(subscriberName, subscriber);
            if (registered) {
                System.out.println(subscriberName + " successfully registered with the BUE Event Service.");
                runCommandLoop();
            } else {
                System.out.println("Registration failed. Subscriber name might already be in use.");
            }

        } catch (Exception e) {
            System.err.println("Subscriber application exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private void runCommandLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n===== BUE Subscriber Application: " + subscriberName + " =====");
        System.out.println("Available commands:");
        System.out.println("1. Subscribe to category");
        System.out.println("2. Unsubscribe from category");
        System.out.println("3. View available categories");
        System.out.println("4. View my subscriptions");
        System.out.println("5. View received notifications");
        System.out.println("6. Deregister");
        System.out.println("7. Exit");

        while (running) {
            System.out.print("\nEnter command (1-7): ");
            String command = scanner.nextLine();

            try {
                switch (command) {
                    case "1":
                        subscribeToCategory(scanner);
                        break;
                    case "2":
                        unsubscribeFromCategory(scanner);
                        break;
                    case "3":
                        viewAvailableCategories();
                        break;
                    case "4":
                        viewMySubscriptions();
                        break;
                    case "5":
                        viewReceivedNotifications();
                        break;
                    case "6":
                        deregister();
                        running = false;
                        break;
                    case "7":
                        System.out.println("Exiting without deregistering...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid command. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("Error executing command: " + e.getMessage());
            }
        }
    }

    private void subscribeToCategory(Scanner scanner) throws Exception {
        viewAvailableCategories();
        System.out.print("Enter category to subscribe to: ");
        String category = scanner.nextLine();

        boolean subscribed = service.subscribeToCategory(subscriberName, category);
        if (subscribed) {
            System.out.println("Successfully subscribed to: " + category);
        } else {
            System.out.println("Failed to subscribe. Invalid category or already subscribed.");
        }
    }

    private void unsubscribeFromCategory(Scanner scanner) throws Exception {
        viewMySubscriptions();
        System.out.print("Enter category to unsubscribe from: ");
        String category = scanner.nextLine();

        boolean unsubscribed = service.unsubscribeFromCategory(subscriberName, category);
        if (unsubscribed) {
            System.out.println("Successfully unsubscribed from: " + category);
        } else {
            System.out.println("Failed to unsubscribe. Invalid category or not subscribed.");
        }
    }

    private void viewAvailableCategories() throws Exception {
        List<String> categories = service.getAvailableCategories();

        System.out.println("\nAvailable Categories:");
        for (String category : categories) {
            System.out.println("- " + category);
        }
    }

    private void viewMySubscriptions() throws Exception {
        List<String> subscriptions = service.getSubscribedCategories(subscriberName);

        System.out.println("\nMy Subscriptions:");
        if (subscriptions.isEmpty()) {
            System.out.println("You are not subscribed to any categories.");
        } else {
            for (String category : subscriptions) {
                System.out.println("- " + category);
            }
        }
    }

    private void viewReceivedNotifications() {
        List<Event> events = subscriber.getReceivedEvents();

        System.out.println("\nReceived Notifications:");
        if (events.isEmpty()) {
            System.out.println("No notifications received yet.");
        } else {
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                System.out.println("\n--- Notification " + (i + 1) + " ---");
                System.out.println("Event: " + event.getEventName());
                System.out.println("Category: " + event.getCategory());
                System.out.println("Description: " + event.getDescription());
                System.out.println("Time: " + event.getTimestamp());
                System.out.println("Expires: " + event.getExpiryTime());
            }
        }
    }

    private void deregister() throws Exception {
        boolean deregistered = service.deregisterSubscriber(subscriberName);
        if (deregistered) {
            System.out.println(subscriberName + " has been deregistered from the BUE Event Service.");
        } else {
            System.out.println("Failed to deregister.");
        }
    }

    public static void main(String[] args) {
        String subscriberName = "DefaultSubscriber";

        if (args.length > 0) {
            subscriberName = args[0];
        } else {
            // If no arguments provided, prompt for a name
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter subscriber name: ");
            subscriberName = scanner.nextLine();
        }

        SubscriberApp app = new SubscriberApp(subscriberName);
        app.start();
    }
}