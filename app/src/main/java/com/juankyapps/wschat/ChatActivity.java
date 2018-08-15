package com.juankyapps.wschat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    MessageAdapter adaptador;
    EditText messageInput;
    Button sendBtn;
    ListView lista;
    String ip;
    WebServer webserver;
    WebsocketServer websocketserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // Get username
        SharedPreferences user = getSharedPreferences("user", 0);
        final String username = user.getString("username", "");
        // Initialize variables
        lista = (ListView) findViewById(R.id.messages_view);
        adaptador = new MessageAdapter(new ArrayList<Message>(), getApplicationContext());
        lista.setAdapter(adaptador);
        messageInput = (EditText) findViewById(R.id.msgInput);
        // Add button behavior
        sendBtn = (Button) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = messageInput.getText().toString();
                if (content.isEmpty()) {
                    return;
                }
                Message msg = new Message(username, new Date(), content);
                websocketserver.sendMessage(msg);
            }
        });
        // Main function
        enableChat(false);
        serverMessage("Initializing server...");
        // Init server
        Utils utils = new Utils(getApplicationContext());
        ip = utils.getIP();
        if (ip == null) {
            serverMessage("Error: Please connect to wifi.");
            return;
        }
        webserver = new WebServer(8080, getAssets()); // Change port to 80 if possible
        websocketserver = new WebsocketServer(3000, this);
        try {
            webserver.start();
            websocketserver.start();
            serverMessage("Listening on " + ip + ":8080");
            enableChat(true);
        }catch (IOException e) {
            Toast.makeText(getApplicationContext(), "IOException: " +  e.getMessage(), Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Exception: " +  e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void addMessage(Message msg) {
        final Message message = msg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adaptador.add(message);
                adaptador.notifyDataSetChanged();
                messageInput.setText("");
                lista.setSelection(adaptador.getCount()-1);
            }
        });
    }

    private void serverMessage(String content) {
        addMessage(new Message("Server", new Date(), content));
    }

    private void enableChat(Boolean enable) {
        sendBtn.setEnabled(enable);
        messageInput.setEnabled(enable);
    }

    public MessageAdapter getAdapter() {
        return this.adaptador;
    }

    public ListView getList() {
        return this.lista;
    }

    public void onDestroy() {
        try {
            this.webserver.closeAllConnections();
            this.webserver.stop();
            this.websocketserver.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
