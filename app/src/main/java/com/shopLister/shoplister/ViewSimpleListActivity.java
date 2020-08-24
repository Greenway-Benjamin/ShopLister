package com.shopLister.shoplister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import static com.shopLister.shoplister.MainActivity.controller;

public class ViewSimpleListActivity extends AppCompatActivity implements View.OnClickListener{

    Button back;
    Intent secondIntent;
    TextView listContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_simple_list);
        secondIntent = getIntent();
        back = findViewById(R.id.backbutton);
        back.setOnClickListener(this);
        listContent = findViewById(R.id.listContent);
        if (secondIntent.hasExtra("listContent")) {
            listContent.setText(secondIntent.getStringExtra("listContent"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backbutton:
                controller.endActivity(this);
                break;
        }
    }
}
