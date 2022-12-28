package com.hanway.game.evening.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hanway.game.evening.app.R;
import com.hanway.game.evening.app.adapters.GameEveningAdapter;
import com.hanway.game.evening.app.models.GameEvening;
import com.hanway.game.evening.app.services.DataProviderResultReceiver;
import com.hanway.game.evening.app.services.DataProviderService;

import java.util.ArrayList;

public class GameEveningOverviewActivity extends AppCompatActivity {

    private DataProviderResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_evening_overview);

        setupServiceReceiver();
        getGameEveningEntries();
    }

    public void onClickHome(View button) {
        Intent changeIntent = new Intent(this, MainActivity.class);
        startActivity(changeIntent);
    }

    public void onCreateNextAppointment(View button) {
        Intent dataProviderIntent = new Intent(this, DataProviderService.class);
        dataProviderIntent.setAction(DataProviderService.GENERATE_NEXT_GAME_EVENING);
        dataProviderIntent.putExtra(DataProviderService.GENERAL_RECEIVER, resultReceiver);
        startService(dataProviderIntent);

        // Update the view
        getGameEveningEntries();
    }

    public void getGameEveningEntries() {
        Intent dataProviderIntent = new Intent(this, DataProviderService.class);
        dataProviderIntent.setAction(DataProviderService.GET_ALL_GAME_EVENING);
        dataProviderIntent.putExtra(DataProviderService.GENERAL_RECEIVER, resultReceiver);
        startService(dataProviderIntent);
    }

    private void setupServiceReceiver() {
        resultReceiver = new DataProviderResultReceiver(new Handler());
        resultReceiver.setReceiver(new DataProviderResultReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    ArrayList<GameEvening> gameEveningList = (ArrayList<GameEvening>) resultData.getSerializable(DataProviderService.GET_ALL_GAME_EVENING_RESULT);
                    GameEveningAdapter adapter = new GameEveningAdapter(GameEveningOverviewActivity.this, gameEveningList);

                    ListView listView = findViewById(R.id.list_overview);
                    // Scroll to the bottom of the list
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(adapter.getCount() - 1);
                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            GameEvening gameEvening = (GameEvening) adapterView.getItemAtPosition(i);
                            Intent intent;
                            if(gameEvening.isUpcoming()){
                                intent = new Intent(GameEveningOverviewActivity.this, UpcomingGameEveningDetailActivity.class);
                            } else {
                                intent = new Intent(GameEveningOverviewActivity.this, PastGameEveningDetailActivity.class);
                            }
                            intent.putExtra("GAME_EVENING", gameEvening);
                            startActivity(intent);
                        }
                    });
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(GameEveningOverviewActivity.this, "Error loading data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}