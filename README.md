# Event Notification System for The British University in Egypt (BUE)

A distributed event notification system built with Java RMI that allows notifiers to publish events and subscribers to receive notifications based on their category subscriptions.

## Overview

The BUE Event Notification System is a publish-subscribe messaging system that enables:
- **Notifiers** to publish events with categories and expiry times
- **Subscribers** to subscribe to specific event categories and receive real-time notifications
- **Centralized management** of events, categories, and subscriptions through an RMI server

## Architecture

The system follows a client-server architecture using Java RMI:

### Server Side
- **BUEServer**: Main server application that starts the RMI registry
- **EventServiceImpl**: Core service implementation handling all business logic
- **Event**: Data model for events with name, description, category, and expiry time
- **EventService**: Remote interface defining available operations
- **NotificationCallback & SubscriberCallback**: Callback interfaces for remote notifications

### Client Side
- **NotificationApp**: Client application for event publishers
- **SubscriberApp**: Client application for event subscribers
- **NotifierImpl & SubscriberImpl**: Callback implementations for receiving server responses

## Features

### For Notifiers
- Register with unique notifier names
- Publish events with custom categories
- Set event expiry times (default: 24 hours)
- Receive publication confirmation callbacks
- View available event categories

### For Subscribers
- Register with unique subscriber names
- Subscribe to multiple event categories
- Receive real-time notifications for subscribed categories
- View subscription history and received notifications
- Manage subscriptions (subscribe/unsubscribe)

### System Features
- Automatic cleanup of expired events
- Concurrent handling of multiple clients
- Default event categories: Academic, Sports, Cultural, Administrative
- Dynamic category creation when publishing new events

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Basic understanding of Java RMI concepts

## Installation & Setup

1. **Compile the project**:
   ```bash
   javac -d . ServerSide/*.java ClientSide/*.java
   ```

2. **Start the RMI Registry** (if not using LocateRegistry.createRegistry):
   ```bash
   rmiregistry 1099
   ```

3. **Run the server**:
   ```bash
   java ServerSide.BUEServer
   ```

## Usage

### Starting a Notifier Client

```bash
java ClientSide.NotificationApp [notifier_name]
```

If no name is provided, you'll be prompted to enter one.

**Available Commands:**
1. **Publish Event** - Create and publish a new event
2. **View Available Categories** - See all event categories
3. **Deregister** - Remove notifier from the system
4. **Exit** - Close application without deregistering

### Starting a Subscriber Client

```bash
java ClientSide.SubscriberApp [subscriber_name]
```

If no name is provided, you'll be prompted to enter one.

**Available Commands:**
1. **Subscribe to category** - Add a category subscription
2. **Unsubscribe from category** - Remove a category subscription
3. **View available categories** - See all event categories
4. **View my subscriptions** - List your current subscriptions
5. **View received notifications** - See all received events
6. **Deregister** - Remove subscriber from the system
7. **Exit** - Close application without deregistering

## Example Usage

### Publishing an Event
1. Start the server: `java ServerSide.BUEServer`
2. Start a notifier: `java ClientSide.NotificationApp Publisher1`
3. Choose option 1 (Publish Event)
4. Enter event details:
   - Name: "Java Workshop"
   - Description: "Introduction to Java RMI programming"
   - Category: "Academic"
   - Expiry: 48 (hours)

### Receiving Notifications
1. Start a subscriber: `java ClientSide.SubscriberApp Student1`
2. Choose option 1 (Subscribe to category)
3. Select "Academic" category
4. The subscriber will automatically receive notifications for any "Academic" events

## Configuration

- **RMI Registry Port**: 1099 (default)
- **Server Host**: localhost (can be modified in client code)
- **Service Name**: "BUEEventService"
- **Default Event Expiry**: 24 hours
- **Cleanup Interval**: 1 hour

## Error Handling

The system includes robust error handling for:
- Duplicate registration attempts
- Invalid category subscriptions
- Network connectivity issues
- Remote method invocation failures
- Expired event cleanup

## Project Structure

```
├── ServerSide/
│   ├── BUEServer.java           # Main server application
│   ├── EventServiceImpl.java   # Core service implementation
│   ├── EventService.java       # Remote service interface
│   ├── Event.java              # Event data model
│   ├── NotificationCallback.java # Notifier callback interface
│   └── SubscriberCallback.java  # Subscriber callback interface
└── ClientSide/
    ├── NotificationApp.java     # Notifier client application
    ├── NotifierImpl.java        # Notifier callback implementation
    ├── SubscriberApp.java       # Subscriber client application
    └── SubscriberImpl.java      # Subscriber callback implementation
```

## Technical Details

- **Communication Protocol**: Java RMI
- **Threading**: Concurrent HashMap for thread-safe operations
- **Serialization**: Events implement Serializable for RMI transmission
- **Callback Pattern**: Bidirectional communication between server and clients
- **Timer-based Cleanup**: Automatic removal of expired events
