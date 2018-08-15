package com.juankyapps.wschat;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class WebsocketServer extends WebSocketServer {

    private static int TCP_PORT = 4444;

    private Set<WebSocket> conns;
    private Context context;
    private ChatActivity activity;

    public WebsocketServer(ChatActivity activity) {
        super(new InetSocketAddress(TCP_PORT));
        conns = new HashSet<>();
        this.activity = activity;
    }

    public WebsocketServer(int port, ChatActivity activity) {
        super(new InetSocketAddress(port));
        TCP_PORT = port;
        conns = new HashSet<>();
        this.activity = activity;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from client: " + message);
        Gson gson = new Gson();
        Message msg = gson.fromJson(message, MessageReciver.class).toMessage();
        addMessage(msg);
        // TODO: Do something with the message
        for (WebSocket sock : conns) {
            sock.send(message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //ex.printStackTrace();
        if (conn != null) {
            System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
            conns.remove(conn);
            // do some thing if required
        }
        System.out.println("Websocket error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        // TODO: put some code in this method
    }

    public void sendMessage(Message msg) {
        Gson gson = new Gson();
        String str = gson.toJson(msg.toMessageReciever());
        for (WebSocket sock : conns) {
            sock.send(str);
        }
        addMessage(msg);
    }

    private void addMessage(Message msg) {
        this.activity.addMessage(msg);
    }
}
