package com.juankyapps.wschat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences userPref = getSharedPreferences("user", 0);
        final SharedPreferences.Editor userPrefEditor = userPref.edit();

        Button startBtn = (Button) findViewById(R.id.startBtn);
        final EditText usernameInput = (EditText) findViewById(R.id.usernameInput);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter a username.", Toast.LENGTH_SHORT).show();
                    return;
                }
                userPrefEditor.putString("username", username);
                userPrefEditor.commit();
                Intent myIntent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
