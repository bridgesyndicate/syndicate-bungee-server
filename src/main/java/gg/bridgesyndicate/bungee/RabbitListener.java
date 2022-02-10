package gg.bridgesyndicate.bungee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class RabbitListener implements Runnable {
    private final PluginMain pluginMain;
    private static final String EXCHANGE_NAME = "default";

    public RabbitListener(PluginMain pluginMain) {
        this.pluginMain = pluginMain;
    }

    @Override
    public void run() {
        Channel channel = null;
        String queueName = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            String rabbitUri = System.getenv("RABBIT_URI");
            if (rabbitUri != null) {
                factory.setUri(rabbitUri);
                factory.setUsername("AmazonMqUsername");
                factory.setPassword("AmazonMqPassword");
                factory.useSslProtocol();
            } else {
                System.out.println("No RABBIT_URI, using 'rabbit'");
                factory.setHost("rabbit");
            }
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");
        } catch (IOException | TimeoutException |
                URISyntaxException | NoSuchAlgorithmException |
                KeyManagementException exception) {
            exception.printStackTrace();
            System.out.println("This is a fatal ERROR.");
        }
        System.out.println("Waiting for warp messages.");
        ObjectMapper objectMapper = new ObjectMapper();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("RabbitMQ: " + message);
            try {
                WarpList warpList = objectMapper.readValue(message, WarpList.class);
                WarpUsers warpUsers = new WarpUsers(pluginMain, warpList);
                warpUsers.warp();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("could not deserialize warp message or warp user.");
            }
        };
        try {
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
