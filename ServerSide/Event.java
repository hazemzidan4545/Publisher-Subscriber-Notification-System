package ServerSide;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
    private String eventName;
    private String description;
    private String category;
    private Date timestamp;
    private Date expiryTime;

    public Event(String eventName, String description, String category) {
        this.eventName = eventName;
        this.description = description;
        this.category = category;
        this.timestamp = new Date();
        // Default expiry time is 24 hours from creation
        this.expiryTime = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
    }

    public Event(String eventName, String description, String category, long expiryTimeInMillis) {
        this.eventName = eventName;
        this.description = description;
        this.category = category;
        this.timestamp = new Date();
        this.expiryTime = new Date(System.currentTimeMillis() + expiryTimeInMillis);
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public boolean isExpired() {
        return new Date().after(expiryTime);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", timestamp=" + timestamp +
                ", expiryTime=" + expiryTime +
                '}';
    }
}