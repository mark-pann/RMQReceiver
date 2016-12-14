
package rmqreceiver;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RMQReceiver {

    public static void main(String[] args) throws TimeoutException, IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();

            
            channel.queueDeclare("MyQeue", false, false, false, null);
            
            byte[] inputbyte;
            String input = "";

            do {
                GetResponse response = channel.basicGet("MyQeue", false);
                if (response != null) {
                    byte[] body = response.getBody();
                    String message = new String(body);
                    System.out.println(message);
                }
                if(System.in.available() > 0) {
                    inputbyte = new byte[4];
                    System.in.read(inputbyte);
                    input = new String(inputbyte);
                }
            } while (!input.equals("stop"));
            channel.close();
            System.out.println("You stopped the program.");
        }
    }
}