package ServerSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BUEServerGUI extends JFrame {
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public BUEServerGUI() {
        setTitle("BUE Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
       
        
       
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        
     
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
       
        setVisible(true);
        
        
    }
    

    public void updateTextArea(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = dateFormat.format(new Date());
            textArea.append(message + " is connected\n");
            
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
    
  
    public void logEvent(String event) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = dateFormat.format(new Date());
            textArea.append("[" + timestamp + "] " + event + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BUEServerGUI gui = new BUEServerGUI();
        });
    }
}