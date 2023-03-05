package com.google.firebase.udacity.skincancerdetectorhackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button buttonSkinCancer;
    private Button buttonInformation;
    private final String INFORMATION_URL = "https://www.skincancer.org/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSkinCancer = (Button) findViewById(R.id.button_skin_cancer);
        buttonInformation = (Button) findViewById(R.id.button_information);

        buttonSkinCancer.setOnClickListener(v -> {
            Intent startSkinCancerIntent = new Intent(MainActivity.this, SkinCancer.class);
            startActivity(startSkinCancerIntent);
        });

        buttonInformation.setOnClickListener(v -> {
            Intent startInformationSearchIntent = new Intent(Intent.ACTION_VIEW);
            startInformationSearchIntent.setData(Uri.parse(INFORMATION_URL));
            startActivity(startInformationSearchIntent);
        });
    }
}