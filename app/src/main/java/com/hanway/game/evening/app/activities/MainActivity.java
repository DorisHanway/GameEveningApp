package com.hanway.game.evening.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hanway.game.evening.app.GameEveningApplication;
import com.hanway.game.evening.app.R;
import com.hanway.game.evening.app.models.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView name = findViewById(R.id.greeting_text);
        GameEveningApplication application = (GameEveningApplication) getApplicationContext();
        User currentUser = application.getCurrentUser();
        String greetingText = "Willkommen zurück " + currentUser.firstName + "!";
        name.setText(greetingText);
    }

    public void onGameEveningOverviewClicked(View button) {
        Intent changeIntent = new Intent(this, GameEveningOverviewActivity.class);
        startActivity(changeIntent);
    }

    public void onUserMessagesClicked(View button) {
        Intent changeIntent = new Intent(this, UserMessagesActivity.class);
        startActivity(changeIntent);
    }
}