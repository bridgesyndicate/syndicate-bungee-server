package gg.bridgesyndicate.bungee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitListener implements Runnable {
    private final PluginMain pluginMain;
    private static final String EXCHANGE_NAME = "default";

    public RabbitListener(PluginMain pluginMain) {
        this.pluginMain = pluginMain;
    }

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Channel channel = null;
        String queueName = null;
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");
        } catch (IOException | TimeoutException ioException) {
            ioException.printStackTrace();
        }
        System.out.println("Waiting for warp messages.");
        ObjectMapper objectMapper = new ObjectMapper();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("RabbitMQ: " + message);
            WarpMessage warpMessage = objectMapper.readValue(message, WarpMessage.class);
            WarpUser warpUser = new WarpUser(pluginMain, warpMessage);
            warpUser.warp();
        };
        try {
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class WarpMessage {
        private String player;
        private String host;

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        private String hostname;
        private int port;


        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }


        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        public WarpMessage() {
        }
    }

    public static void main(String[] args) throws IOException {
        String json = "{\"player\":\"fff47ae2-dec5-4de8-9172-1ab9216b30e0\",\"host\":\"0.0.0.0\",\"port\":25565}";
        WarpMessage warpMessage = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            warpMessage = objectMapper.readValue(json, WarpMessage.class);
        } catch (JsonProcessingException e) {
            System.err.println("Cannot parse json");
            e.printStackTrace();
        }
    }

}
