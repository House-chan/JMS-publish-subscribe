/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package footballliveproducer;


import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 *
 * @author sarun
 */
public class Main {
    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final int NUM_MSGS;
        Connection connection = null;
        
        System.out.println("Destination type is topic");

        Destination dest = null;

        //create distination topic
        try {
            dest = (Destination) topic;
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }

        
        try {
            connection = connectionFactory.createConnection();

            Session session = connection.createSession(
                        false,
                        Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(dest);
            TextMessage message = session.createTextMessage();
            Scanner input = new Scanner(System.in);
            System.out.print("Insert Text:");
            String report = "";
            System.out.println("To end program, type Q or q, then <return>");
            while(!(report.equals("q") || report.equals("Q") )){
                report = input.nextLine();
                message.setText(report);
                System.out.println("Sending message: " + message.getText());
                producer.send(message);
            }
                  /*if (i == 2) {
                    producer.send(message, DeliveryMode.NON_PERSISTENT, 4, 5000);
                }
                  else {
                      producer.send(message);
                  }*/
            

            /*
             * Send a non-text control message indicating end of
             * messages.
             */
            producer.send(session.createMessage());
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }

    
    
}
