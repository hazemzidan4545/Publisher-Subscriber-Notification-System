package ClientSide;

import ServerSide.ServiceInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Demo application to show how to create multiple publishers and subscribers
 * as required by the assignment (at least 3 publishers and 3 subscribers).
 */
public class MultiPublisherSubscriber{

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServiceInterface service = (ServiceInterface) registry.lookup("BUEService");

            System.out.println("===== BUE Event Notification System =====");
            System.out.println();

            // Create multiple publishers
            PublisherApp publisher1 = new PublisherApp("Faculty_Publisher", service);
            PublisherApp publisher2 = new PublisherApp("Student_Union_Publisher", service);
            PublisherApp publisher3 = new PublisherApp("Admin_Publisher", service);

            System.out.println("\nCreated 3 publishers:");
            System.out.println("1. " + publisher1.getName());
            System.out.println("2. " + publisher2.getName());
            System.out.println("3. " + publisher3.getName());

            // Create multiple subscribers
            SubscriberApp subscriber1 = new SubscriberApp("ComputerScience_Student", service);
            SubscriberApp subscriber2 = new SubscriberApp("Engineering_Student", service);
            SubscriberApp subscriber3 = new SubscriberApp("Business_Student", service);

            System.out.println("\nCreated 3 subscribers:");
            System.out.println("1. " + subscriber1.getName());
            System.out.println("2. " + subscriber2.getName());
            System.out.println("3. " + subscriber3.getName());

            // Subscribe subscribers to topics
            System.out.println("\nSubscribing subscribers to topics...");

            // Subscribe subscriber1 to topics
            subscriber1.subscribe("New Courses");
            subscriber1.subscribe("Emergency Alerts");
            System.out.println(subscriber1.getName() + " subscribed to: New Courses, Emergency Alerts");

            // Subscribe subscriber2 to topics
            subscriber2.subscribe("New Courses");
            subscriber2.subscribe("Student Activities");
            System.out.println(subscriber2.getName() + " subscribed to: New Courses, Student Activities");

            // Subscribe subscriber3 to topics
            subscriber3.subscribe("Campus News");
            subscriber3.subscribe("Emergency Alerts");
            System.out.println(subscriber3.getName() + " subscribed to: Campus News, Emergency Alerts");

            // Demo publishing
            System.out.println("\nPublishing notifications...");
            System.out.println("Press Enter to continue with each step...");
            scanner.nextLine();

            // Publisher 1 publishes a notification
            System.out.println("\nPublisher 1 publishing to 'New Courses'...");
            publisher1.publishNotification("New Courses", "New AI course available for registration!");
            System.out.println("Notification sent. Press Enter to continue...");
            scanner.nextLine();

            // Publisher 2 publishes a notification
            System.out.println("\nPublisher 2 publishing to 'Student Activities'...");
            publisher2.publishNotification("Student Activities", "Sports day scheduled for next week.");
            System.out.println("Notification sent. Press Enter to continue...");
            scanner.nextLine();

            // Publisher 3 publishes a notification
            System.out.println("\nPublisher 3 publishing to 'Emergency Alerts'...");
            publisher3.publishNotification("Emergency Alerts", "Fire drill scheduled for tomorrow at 2 PM.");
            System.out.println("Notification sent. Press Enter to continue...");
            scanner.nextLine();

            // Cleanup
            System.out.println("\nCleaning up...");
            publisher1.deregister();
            publisher2.deregister();
            publisher3.deregister();
            subscriber1.deregister();
            subscriber2.deregister();
            subscriber3.deregister();

            System.out.println("Demo completed successfully!");

        } catch (Exception e) {
            System.err.println("Demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}