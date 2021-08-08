package com.example.fortest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fortest.drawing.Themes;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private Button bHud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
    }

    private void init(){

        View tools = findViewById(R.id.tools_btn);
        tools.setOnClickListener(v -> finish());

        SwitchCompat sAutoHud = findViewById(R.id.autoHudSwitch);
        sAutoHud.setChecked(DisplayParameters.autoHud);
        sAutoHud.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                DisplayParameters.autoHud = true;
                bHud.setVisibility(View.INVISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("HUD режим")
                        .setMessage("положите устройство под лобовое стекло автомобиля и режим HUD включится автоматически." +
                                "\nТакже, HUD режим можно включать вручную")
                        .setCancelable(true)
                        .setNegativeButton("Ок",
                                (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                DisplayParameters.autoHud = false;
                bHud.setVisibility(View.VISIBLE);
            }
        });

        SwitchCompat sType = findViewById(R.id.analogSwitch);
        sType.setChecked(DisplayParameters.displayAnalog);
        sType.setOnCheckedChangeListener((buttonView, isChecked) -> DisplayParameters.displayAnalog = isChecked);

        SwitchCompat sDistChange = findViewById(R.id.mlSwitch);
        sDistChange.setChecked(DisplayParameters.displayMiles);
        sDistChange.setOnCheckedChangeListener((buttonView, isChecked) -> DisplayParameters.displayMiles = isChecked);

        Button orangeButton = findViewById(R.id.orangeTheme);
        orangeButton.setOnClickListener(v -> Themes.mainThemeColor = Themes.elementsColorOrange);

        Button whiteButton = findViewById(R.id.whiteTheme);
        whiteButton.setOnClickListener(v -> Themes.mainThemeColor = Themes.elementsColorWhite);

        Button redButton = findViewById(R.id.redTheme);
        redButton.setOnClickListener(v -> Themes.mainThemeColor = Themes.elementsColorRed);

        bHud = findViewById(R.id.hud);
        if (DisplayParameters.autoHud)
            bHud.setVisibility(View.INVISIBLE);
        bHud.setOnClickListener(v -> {
            DisplayParameters.displayHud = !DisplayParameters.displayHud;
            finish();
        });
    }
}

