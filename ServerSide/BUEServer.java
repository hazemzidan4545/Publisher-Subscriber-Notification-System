package ServerSide;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BUEServer {
    public static void main(String[] args) {
        try {
            // Create the service implementation
            EventServiceImpl service = new EventServiceImpl();

            // Create and start the RMI registry on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the service to the registry
            registry.bind("BUEEventService", service);

            System.out.println("BUE Event Notification Server started successfully");
        } catch (Exception e) {
            System.err.println("Server Exception: " + e.toString());
            e.printStackTrace();
        }
    }
}