# Event Notification System for The British University in Egypt (BUE)

A distributed Publisher-Subscriber messaging system built with Java RMI (Remote Method Invocation) for the British University in Egypt (BUE). This system allows publishers to send notifications on various topics while subscribers can receive real-time updates on topics they're interested in.

## System Architecture

The system consists of three main components:

### Server Side
- **BUE Server**: Central message broker that manages publishers, subscribers, and message routing
- **Server GUI**: Real-time monitoring interface showing system events and connected clients

### Client Side
- **Publisher Application**: Allows users to publish notifications to specific topics
- **Subscriber Application**: Enables users to subscribe to topics and receive notifications
- **Notification Center**: GUI window displaying real-time notifications for subscribers

## Features

- **Real-time Messaging**: Instant notification delivery to subscribed clients
- **Topic Management**: Pre-defined topics including:
  - New Courses
  - Student Activities
  - Emergency Alerts
  - Campus News
- **Message History**: View past messages for subscribed topics
- **GUI Interfaces**: User-friendly graphical interfaces for both server monitoring and subscriber notifications
- **Dynamic Registration**: Publishers and subscribers can join/leave the system at runtime

## Prerequisites

- Java JDK 8 or higher
- Java RMI registry

## Project Structure

```
├── ServerSide/
│   ├── BUEServer.java              # Main server implementation
│   ├── BUEServerGUI.java           # Server monitoring GUI
│   ├── ServiceInterface.java        # Server remote interface
│   ├── PublisherInterface.java      # Publisher remote interface
│   └── SubscriberInterface.java     # Subscriber remote interface
└── ClientSide/
    ├── PublisherApp.java           # Publisher client application
    ├── SubscriberApp.java          # Subscriber client application
    └── SubscriberNotificationCenter.java  # Subscriber GUI notifications
```

## Setup and Installation

### 1. Compile the Code
```bash
javac -d . ServerSide/*.java ClientSide/*.java
```

### 2. Start RMI Registry
```bash
rmiregistry 1099
```

### 3. Run the Server
```bash
java ServerSide.BUEServer
```

### 4. Run Publisher Client
```bash
java ClientSide.PublisherApp
```

### 5. Run Subscriber Client
```bash
java ClientSide.SubscriberApp
```

## Usage Guide

### Server Operation
1. Launch the server first - it will display a GUI showing system events
2. The server automatically creates an RMI registry on port 1099
3. Monitor connected publishers and subscribers through the server GUI

### Publisher Usage
1. Run the publisher application
2. Enter a unique publisher name when prompted
3. Choose from available menu options:
   - **Publish notification**: Select a topic and enter your message
   - **View all messages**: See all published messages by topic
   - **Deregister and exit**: Safely disconnect from the system

### Subscriber Usage
1. Run the subscriber application
2. Enter a unique subscriber name when prompted
3. A notification center window will open automatically
4. Use the console menu to:
   - View available topics
   - Subscribe to topics of interest
   - Unsubscribe from topics
   - View your current subscriptions
   - Review past messages for subscribed topics
   - Deregister and exit

### Notification Center
- Displays real-time notifications with timestamps
- Shows system messages (subscription changes, connection status)
- Flashes when new notifications arrive while window is not focused
- Auto-scrolls to show latest messages

## Available Topics

The system comes with four pre-configured topics:
1. **New Courses** - Academic course announcements
2. **Student Activities** - Campus events and activities
3. **Emergency Alerts** - Important safety notifications
4. **Campus News** - General university news and updates

## Technical Details

- **Communication Protocol**: Java RMI
- **Server Port**: 1099 (configurable)
- **Architecture**: Centralized broker pattern
- **GUI Framework**: Java Swing
- **Threading**: Multi-threaded for concurrent client handling

## Troubleshooting

### Error Messages
- **"Registry not found"**: Start the RMI registry before running applications
- **"Service not found"**: Ensure the server is running and registered properly
- **"Remote connection failed"**: Check network connectivity and firewall settings

## System Requirements

- **Memory**: Minimum 256MB RAM
- **Network**: Local network access for RMI communication
- **Display**: GUI requires graphics environment for subscriber notifications

---

**Note**: This system is designed for educational purposes and not actually used by the BUE.
