package ServerSide;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EventServiceImpl extends UnicastRemoteObject implements EventService {
    // Maps to store notifiers and subscribers
    private final Map<String, NotificationCallback> notifiers;
    private final Map<String, SubscriberCallback> subscribers;

    // Map to store subscriber categories
    private final Map<String, Set<String>> subscriberCategories;

    // Map to store events with their categories
    private final Map<String, List<Event>> categoryEvents;

    // Timer for event cleanup
    private final Timer cleanupTimer;

    public EventServiceImpl() throws RemoteException {
        notifiers = new ConcurrentHashMap<>();
        subscribers = new ConcurrentHashMap<>();
        subscriberCategories = new ConcurrentHashMap<>();
        categoryEvents = new ConcurrentHashMap<>();

        // Initialize with some default categories
        categoryEvents.put("Academic", new ArrayList<>());
        categoryEvents.put("Sports", new ArrayList<>());
        categoryEvents.put("Cultural", new ArrayList<>());
        categoryEvents.put("Administrative", new ArrayList<>());

        // Setup periodic cleanup of expired events
        cleanupTimer = new Timer(true);
        cleanupTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cleanupExpiredEvents();
            }
        }, 0, 60 * 60 * 1000); // Run every hour
    }

    @Override
    public boolean registerNotifier(String notifierName, NotificationCallback callback) throws RemoteException {
        if (notifiers.containsKey(notifierName)) {
            return false; // Already registered
        }
        notifiers.put(notifierName, callback);
        System.out.println("Notifier registered: " + notifierName);
        return true;
    }

    @Override
    public boolean registerSubscriber(String subscriberName, SubscriberCallback callback) throws RemoteException {
        if (subscribers.containsKey(subscriberName)) {
            return false; // Already registered
        }
        subscribers.put(subscriberName, callback);
        subscriberCategories.put(subscriberName, new HashSet<>());
        System.out.println("Subscriber registered: " + subscriberName);
        return true;
    }

    @Override
    public boolean deregisterNotifier(String notifierName) throws RemoteException {
        if (!notifiers.containsKey(notifierName)) {
            return false; // Not registered
        }
        notifiers.remove(notifierName);
        System.out.println("Notifier deregistered: " + notifierName);
        return true;
    }

    @Override
    public boolean deregisterSubscriber(String subscriberName) throws RemoteException {
        if (!subscribers.containsKey(subscriberName)) {
            return false; // Not registered
        }
        subscribers.remove(subscriberName);
        subscriberCategories.remove(subscriberName);
        System.out.println("Subscriber deregistered: " + subscriberName);
        return true;
    }

    @Override
    public boolean publishEvent(String notifierName, Event event) throws RemoteException {
        if (!notifiers.containsKey(notifierName)) {
            return false; // Notifier not registered
        }

        String category = event.getCategory();
        if (!categoryEvents.containsKey(category)) {
            categoryEvents.put(category, new ArrayList<>());
        }

        // Store the event
        categoryEvents.get(category).add(event);

        // Notify all subscribers who are subscribed to this category
        for (Map.Entry<String, Set<String>> entry : subscriberCategories.entrySet()) {
            String subscriberName = entry.getKey();
            Set<String> categories = entry.getValue();

            if (categories.contains(category)) {
                try {
                    subscribers.get(subscriberName).receiveNotification(event);
                    System.out.println("Notification sent to: " + subscriberName);
                } catch (RemoteException e) {
                    System.err.println("Failed to notify subscriber: " + subscriberName);
                    // Handling subscriber failure - could implement retry logic
                }
            }
        }

        // Notify the notifier of successful publication
        notifiers.get(notifierName).notificationPublished(event, true);

        System.out.println("Event published: " + event.getEventName() + " by " + notifierName);
        return true;
    }

    @Override
    public List<String> getAvailableCategories() throws RemoteException {
        return new ArrayList<>(categoryEvents.keySet());
    }

    @Override
    public boolean subscribeToCategory(String subscriberName, String category) throws RemoteException {
        if (!subscribers.containsKey(subscriberName) || !categoryEvents.containsKey(category)) {
            return false; // Invalid subscriber or category
        }

        subscriberCategories.get(subscriberName).add(category);
        System.out.println(subscriberName + " subscribed to: " + category);
        return true;
    }

    @Override
    public boolean unsubscribeFromCategory(String subscriberName, String category) throws RemoteException {
        if (!subscribers.containsKey(subscriberName) || !categoryEvents.containsKey(category)) {
            return false; // Invalid subscriber or category
        }

        subscriberCategories.get(subscriberName).remove(category);
        System.out.println(subscriberName + " unsubscribed from: " + category);
        return true;
    }

    @Override
    public List<String> getSubscribedCategories(String subscriberName) throws RemoteException {
        if (!subscriberCategories.containsKey(subscriberName)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(subscriberCategories.get(subscriberName));
    }

    private void cleanupExpiredEvents() {
        Date now = new Date();
        for (String category : categoryEvents.keySet()) {
            List<Event> events = categoryEvents.get(category);
            List<Event> validEvents = events.stream()
                    .filter(event -> !event.isExpired())
                    .collect(Collectors.toList());

            int removed = events.size() - validEvents.size();
            if (removed > 0) {
                categoryEvents.put(category, validEvents);
                System.out.println("Cleaned up " + removed + " expired events from category: " + category);
            }
        }
    }
}