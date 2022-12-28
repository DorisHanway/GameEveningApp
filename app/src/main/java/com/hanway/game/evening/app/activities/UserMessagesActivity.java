package com.hanway.game.evening.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanway.game.evening.app.GameEveningApplication;
import com.hanway.game.evening.app.R;
import com.hanway.game.evening.app.adapters.UserMessageAdapter;
import com.hanway.game.evening.app.models.User;
import com.hanway.game.evening.app.models.UserMessage;
import com.hanway.game.evening.app.services.DataProviderResultReceiver;
import com.hanway.game.evening.app.services.DataProviderService;

import java.util.ArrayList;
import java.util.Date;

public class UserMessagesActivity extends AppCompatActivity {

    private User currentUser;
    private DataProviderResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameEveningApplication context = (GameEveningApplication) getApplicationContext();
        currentUser = context.getCurrentUser();
        setContentView(R.layout.user_message_overview);

        setupServiceReceiver();
        getUserMessages();
    }

    public void onClickHome(View button) {
        Intent changeIntent = new Intent(this, MainActivity.class);
        startActivity(changeIntent);
    }

    public void onSendMessage(View button) {
        Intent dataProviderIntent = new Intent(this, DataProviderService.class);
        TextView textView = findViewById(R.id.enter_message_text);
        String message = String.valueOf(textView.getText());
        if(message.equals("")){
            // Don't send an empty message
            return;
        }
        UserMessage userMessage = new UserMessage(0L, currentUser.firstName, message, new Date());
        dataProviderIntent.setAction(DataProviderService.CREATE_USER_MESSAGE);
        dataProviderIntent.putExtra(DataProviderService.USER_MESSAGE, userMessage);
        dataProviderIntent.putExtra(DataProviderService.GENERAL_RECEIVER, resultReceiver);
        startService(dataProviderIntent);
        // update the view
        getUserMessages();
        textView.setText("");
    }

    private void getUserMessages() {
        Intent dataProviderIntent = new Intent(this, DataProviderService.class);
        dataProviderIntent.setAction(DataProviderService.GET_USER_MESSAGES);
        dataProviderIntent.putExtra(DataProviderService.GENERAL_RECEIVER, resultReceiver);
        startService(dataProviderIntent);
    }

    private void setupServiceReceiver() {
        resultReceiver = new DataProviderResultReceiver(new Handler());
        resultReceiver.setReceiver(new DataProviderResultReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    ArrayList<UserMessage> userMessages = (ArrayList<UserMessage>) resultData.getSerializable(DataProviderService.GET_USER_MESSAGES_RESULT);
                    UserMessageAdapter adapter = new UserMessageAdapter(UserMessagesActivity.this, userMessages);
                    ListView listView = findViewById(R.id.user_message_list);
                    listView.setAdapter(adapter);
                    // Scroll to the bottom of the list
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(adapter.getCount() - 1);
                        }
                    });
                } else {
                    Toast.makeText(UserMessagesActivity.this, "Error loading data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
