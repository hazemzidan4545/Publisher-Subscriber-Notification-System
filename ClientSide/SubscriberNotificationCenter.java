package ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubscriberNotificationCenter  extends JFrame {
    private JTextArea notificationArea;
    private JScrollPane scrollPane;
    private JLabel statusLabel;
    private JPanel statusPanel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private String subscriberName;

    public SubscriberNotificationCenter (String subscriberName) {
        this.subscriberName = subscriberName;
        
        setTitle("Subscriber Notification Center - " + subscriberName);
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        notificationArea.setFont(new Font("Arial", Font.PLAIN, 12));
        
        scrollPane = new JScrollPane(notificationArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Subscriber: " + subscriberName);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        
        setVisible(true);
        
        addNotification("System", "Notification center initialized for " + subscriberName);
    }
    
  
    public void displayNotification(String topic, String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = dateFormat.format(new Date());
            String notificationText = "[" + timestamp + "] " + topic + ": " + message + "\n";
            notificationArea.append(notificationText);
            
         
            notificationArea.setCaretPosition(notificationArea.getDocument().getLength());
           
            if (!this.isFocused()) {
                flashWindow();
            }
        });
    }
    

    public void addNotification(String source, String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = dateFormat.format(new Date());
            String notificationText = "[" + timestamp + "] " + source + " - " + message + "\n";
            notificationArea.append(notificationText);
            notificationArea.setCaretPosition(notificationArea.getDocument().getLength());
        });
    }
    

    public void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("Subscriber: " + subscriberName + " - " + status);
        });
    }
    
 
    private void flashWindow() {
        final int flashCount = 3;
        final int flashDelay = 250;
        
        Thread flashThread = new Thread(() -> {
            try {
                for (int i = 0; i < flashCount; i++) {
                    toFront();
                    Thread.sleep(flashDelay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        flashThread.start();
    }
    
  
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SubscriberNotificationCenter  gui = new SubscriberNotificationCenter ("TestStudent");
        });
    }
}