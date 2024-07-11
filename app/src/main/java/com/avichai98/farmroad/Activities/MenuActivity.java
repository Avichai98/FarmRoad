package com.avichai98.farmroad.Activities;

import static com.avichai98.farmroad.Manager.GameManager.RECORDSLIST;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.avichai98.farmroad.R;
import com.avichai98.farmroad.Manager.RecordsManager;
import com.avichai98.farmroad.Utilities.MyLocationManager;
import com.avichai98.farmroad.Utilities.MSPV;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MenuActivity extends AppCompatActivity {
    private MaterialButton menu_BTN_start;
    private MaterialButton menu_BTN_records;
    private SwitchCompat menu_SWC_mode;
    private SwitchCompat menu_SWC_speed;
    private MyLocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        locationManager= new MyLocationManager(this);
        findViews();
        getDataFromJson();
        menu_BTN_start.setOnClickListener(View -> startGame());
        menu_BTN_records.setOnClickListener(View -> showRecords());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void showRecords() {
        Intent intent = new Intent(MenuActivity.this, RecordsActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void getDataFromJson() {
        String fromSP = MSPV.getInstance().readString(RECORDSLIST, "");
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();
        RecordsManager recordsListFromJson;
        recordsListFromJson = gson.fromJson(fromSP, RecordsManager.class);
        if (!fromSP.isEmpty()) {
            RecordsManager.getInstance().setRecordsManager(recordsListFromJson);
        }
    }

    private void startGame() {
        boolean isFast, isArrows;
        isFast = !menu_SWC_speed.isChecked();

        isArrows = menu_SWC_mode.isChecked();

        locationManager.updateLocation(this);
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        Bundle bundle = new Bundle();
        //Location coordinates
        bundle.putDouble("latitude", locationManager.getLatitude());
        bundle.putDouble("longitude", locationManager.getLongitude());
        bundle.putBoolean("isFast", isFast);
        bundle.putBoolean("isArrows", isArrows);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void findViews() {
        menu_BTN_start = findViewById(R.id.menu_BTN_start);
        menu_BTN_records = findViewById(R.id.menu_BTN_records);
        menu_SWC_mode = findViewById(R.id.menu_SWC_mode);
        menu_SWC_speed = findViewById(R.id.menu_SWC_speed);
    }
}