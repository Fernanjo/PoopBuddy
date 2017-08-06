package com.fernanjo.poopbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Anthony on 04/06/2017.
 * This activity is the first screen loaded and serves as the navigation point for all other screens
 */

//TODO Set up repository on GIT for version control


public class HomeActivity extends AppCompatActivity {
    Button logPage;
    Button recordPage;
    Button dataPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        logPage = (Button)findViewById(R.id.home_logBtn);
        recordPage = (Button)findViewById(R.id.home_recordBtn);
        dataPage = (Button)findViewById(R.id.home_dataBtn);

        logButton();
        recordButton();
        dataButton();
    }

    private void dataButton() {
        dataPage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, DataActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    private void recordButton() {
        recordPage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, RecordsActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void logButton() {
        logPage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, LogActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
