import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Messages;
import models.TextContent;
import tech.gusavila92.websocketclient.WebSocketClient;

import javax.json.JsonObject;

public class SocketClient{
    private WebSocketClient webSocketClient;

    private void createWebSocketClient(int id) {
        URI uri;
        try {
            uri = new URI("ws://localhost:8888/conversation/"+ id);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                System.out.println("onOpen");
            }

            @Override
            public void onTextReceived(String message) {
                System.out.println("onTextReceived");
            }

            @Override
            public void onBinaryReceived(byte[] data) {
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                System.out.println("Exception");
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                System.out.println("onCloseReceived");
            }
        };


        webSocketClient.setConnectTimeout(10000);
        //webSocketClient.addHeader("Origin", "ws://developer.example.com/conversation/1");
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void sendMessage(Messages m){
        ObjectMapper mapper = new ObjectMapper();
        String json = "error";
        try {
            json = mapper.writeValueAsString(m);
            System.out.println("ResultingJSONstring = " + json);
        } catch (IOException e) {
            System.out.println("Error sending playEndOfStream message " + e);
        }
        //webSocketClient.send(json);


    }

    public static void main(String[] args) {
        SocketClient s = new SocketClient();
        s.createWebSocketClient(1);
        s.sendMessage(new Messages(1, 1, 1, 1
                , new TextContent(1, "Jojo Joestar"), false
                , false, Calendar.getInstance(), 2));
    }
}