package com.shopLister.shoplister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import database.DatabaseControl;

public class SimpleListActivity extends AppCompatActivity implements View.OnClickListener{
    Button saveList;
    Button back;
    EditText listContent;
    DatabaseControl db;
    Intent secondIntent;
    String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseControl(this);
        setContentView(R.layout.activity_simple_list);
        secondIntent = getIntent();
        saveList = findViewById(R.id.savebutton);
        saveList.setOnClickListener(this);
        back = findViewById(R.id.backbutton);
        back.setOnClickListener(this);
        listContent = findViewById(R.id.listContent);
        if (secondIntent.hasExtra("listContent")) {
            listContent.setText(secondIntent.getStringExtra("listContent"));
            listName = secondIntent.getStringExtra("listName");
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savebutton:
                if (TextUtils.isEmpty(listContent.getText())) {
                    Toast toast = Toast.makeText(SimpleListActivity.this, "Need to include at least one item", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
            }
                final EditText listToName = new EditText(this);
                listToName.setText(listName);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("What shall we name this list?");
                builder.setView(listToName);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(listToName.getText())) {
                            Toast toast = Toast.makeText(SimpleListActivity.this, "Need to include a list name", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        boolean alreadyThere = db.checkSimpleName(listToName.getText().toString());
                        if (alreadyThere) {
                         AlertDialog.Builder saveOver = new AlertDialog.Builder(SimpleListActivity.this);
                         saveOver.setMessage("Save over " + listToName.getText().toString() + "?");
                         saveOver.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 db.updateSimpleList(listToName.getText().toString(), listContent.getText().toString());
                                 Toast toast = Toast.makeText(SimpleListActivity.this, listToName.getText().toString() + " Simple List Updated!", Toast.LENGTH_SHORT);
                                 toast.show();
                                 MainActivity.controller.endActivity(SimpleListActivity.this);
                             }
                         });
                         saveOver.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                         });
                         saveOver.show();
                        } else {
                            db.insertSimpleList(listToName.getText().toString(), listContent.getText().toString());
                            Toast toast = Toast.makeText(SimpleListActivity.this, listToName.getText().toString() + " Simple List Saved!", Toast.LENGTH_SHORT);
                            toast.show();
                            MainActivity.controller.endActivity(SimpleListActivity.this);
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;

            case R.id.backbutton:
                MainActivity.controller.endActivity(this);
                break;
        }
    }
}
