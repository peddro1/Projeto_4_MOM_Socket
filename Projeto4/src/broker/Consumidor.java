package broker;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javafx.scene.control.TextArea;

public class Consumidor {
	private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private String topicName = "";
	
	private TextArea textArea;
	
	public Consumidor(String topicName, TextArea textArea) throws JMSException{
		this.topicName = topicName;
		this.textArea = textArea;
		this.receiveMessage();
		
	}
	
	public void receiveMessage() throws JMSException {
		System.out.println("passou");
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		ActiveMQConnection connection = (ActiveMQConnection)connectionFactory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
		Destination destination = session.createQueue(this.topicName);
		
		MessageConsumer consumer = session.createConsumer(destination);
		
		
		Message message;
        while ((message = consumer.receive(1000)) != null) { // 1000ms de timeout para esperar mensagem
            if (message instanceof TextMessage) {
                 TextMessage textMessage = (TextMessage) message;
                 this.textArea.appendText( ((TextMessage)message).getText() + "\n");
             } else {
                 System.out.println(message);
             }
        }
        
        consumer.close();
        session.close();
        connection.close();
	}
	
}
