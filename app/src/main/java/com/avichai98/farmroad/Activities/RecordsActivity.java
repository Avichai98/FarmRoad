package com.avichai98.farmroad.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.avichai98.farmroad.Manager.RecordsFragmentList;
import com.avichai98.farmroad.R;
import com.avichai98.farmroad.Manager.RecordsFragmentMap;
import com.google.android.material.button.MaterialButton;

public class RecordsActivity extends AppCompatActivity {
    private RecordsFragmentMap mapFragment;
    private RecordsFragmentList listFragment;
    private MaterialButton records_BTN_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        findViews();
        listFragment = new RecordsFragmentList();
        mapFragment = new RecordsFragmentMap();
        getSupportFragmentManager().beginTransaction().add(R.id.records_FRAME_list, listFragment)
                                                        .add(R.id.records_FRAME_map, mapFragment).commit();

        listFragment.setCallBack_List(record -> mapFragment.zoomOnLocation(record.getLat(), record.getLon()));

        records_BTN_home.setOnClickListener(v -> backToHomeClicked());
    }

    private void backToHomeClicked() {
        Intent intent = new Intent(RecordsActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void findViews() {
        records_BTN_home = findViewById(R.id.records_BTN_home);
    }
}


