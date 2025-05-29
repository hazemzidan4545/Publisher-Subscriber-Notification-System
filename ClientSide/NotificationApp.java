package ClientSide;

import ServerSide.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class NotificationApp {
    private String notifierName;
    private NotifierImpl notifier;
    private EventService service;

    public NotificationApp(String notifierName) {
        this.notifierName = notifierName;
    }

    public void start() {
        try {
            // Connect to the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Look up the event service
            service = (EventService) registry.lookup("BUEEventService");

            // Create the notifier implementation
            notifier = new NotifierImpl(notifierName);

            // Register with the service
            boolean registered = service.registerNotifier(notifierName, notifier);
            if (registered) {
                System.out.println(notifierName + " successfully registered with the BUE Event Service.");
                runCommandLoop();
            } else {
                System.out.println("Registration failed. Notifier name might already be in use.");
            }

        } catch (Exception e) {
            System.err.println("Notification application exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private void runCommandLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n===== BUE Notification Application: " + notifierName + " =====");
        System.out.println("Available commands:");
        System.out.println("1. Publish Event");
        System.out.println("2. View Available Categories");
        System.out.println("3. Deregister");
        System.out.println("4. Exit");

        while (running) {
            System.out.print("\nEnter command (1-4): ");
            String command = scanner.nextLine();

            try {
                switch (command) {
                    case "1":
                        publishEvent(scanner);
                        break;
                    case "2":
                        viewCategories();
                        break;
                    case "3":
                        deregister();
                        running = false;
                        break;
                    case "4":
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

    private void publishEvent(Scanner scanner) throws Exception {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();

        System.out.print("Enter event description: ");
        String description = scanner.nextLine();

        viewCategories();
        System.out.print("Enter event category: ");
        String category = scanner.nextLine();

        System.out.print("Enter expiry time in hours (default 24): ");
        String expiryInput = scanner.nextLine();
        long expiryTimeInMillis = 24 * 60 * 60 * 1000; // Default 24 hours

        if (!expiryInput.isEmpty()) {
            try {
                expiryTimeInMillis = Integer.parseInt(expiryInput) * 60 * 60 * 1000;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using default expiry time of 24 hours.");
            }
        }

        Event event = new Event(eventName, description, category, expiryTimeInMillis);
        boolean published = service.publishEvent(notifierName, event);

        if (published) {
            System.out.println("Event published successfully!");
        } else {
            System.out.println("Failed to publish event.");
        }
    }

    private void viewCategories() throws Exception {
        System.out.println("\nAvailable Categories:");
        for (String category : service.getAvailableCategories()) {
            System.out.println("- " + category);
        }
    }

    private void deregister() throws Exception {
        boolean deregistered = service.deregisterNotifier(notifierName);
        if (deregistered) {
            System.out.println(notifierName + " has been deregistered from the BUE Event Service.");
        } else {
            System.out.println("Failed to deregister.");
        }
    }

    public static void main(String[] args) {
        String notifierName = "DefaultNotifier";

        if (args.length > 0) {
            notifierName = args[0];
        } else {
            // If no arguments provided, prompt for a name
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter notifier name: ");
            notifierName = scanner.nextLine();
        }

        NotificationApp app = new NotificationApp(notifierName);
        app.start();
    }
}