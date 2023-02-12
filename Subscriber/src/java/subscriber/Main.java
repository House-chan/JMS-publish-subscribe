/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package subscriber;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
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
        String destType = null;
        Connection connection = null;
        Session session = null;
        Destination dest = null;
        MessageConsumer consumer = null;
        TextMessage message = null;

        
        try {
            dest = (Destination) topic;
        
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }

        
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            consumer = session.createConsumer(dest);
            connection.start();
            //int num = 0; /* use in the case to show session recover */
            while (true) {
                Message m = consumer.receive();

                if (m != null) {
                    if (m instanceof TextMessage) {
                        message = (TextMessage) m;
                        if(message.getText().equals("") || message.getText().equals("q") || message.getText().equals("Q")){
                            break;
                        }
                        System.out.println(
                                "Reading message: " + message.getText());
//                        message.acknowledge();
                        /* use in the case to show session recover */
                        //num++;
                        /*if (num == 10) {
                            System.out.println("Stop connection");
                            connection.stop();
                            System.out.println("Recover session");
                            connection.start();
                            session.recover();
                            
                        }*/
                    } 
//                    else {
//                        break;
//                    }
                }
                
                else {
                    break;
                }
            }
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
