package com.shopLister.shoplister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.util.ArrayList;

import database.DatabaseControl;
import models.ListItem;
import models.ListView;
import models.SimpleList;

public class SavedListActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseControl db;
    ArrayList<ListView> lists;
    Button back;
    Boolean refresh;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseControl(this);
        setContentView(R.layout.activity_saved_list);
        refresh = false;
        back = findViewById(R.id.backbutton);
        back.setOnClickListener(this);

//Create layout to list items row by row
        linearLayout = findViewById(R.id.linearlayout);
        setList();
    }

    private void setList() {
        linearLayout.removeAllViews();
        lists = db.getAllListnames();
        String t1 = "Simple Lists";
        String t2 = "Normal Lists";

        ToggleButton toggle = new ToggleButton(this);
        toggle.setChecked(true);
        toggle.setText(t1);
        toggle.setTextOn(t1);
        toggle.setTextOff(t1);
        toggle.setTextSize(20);


        ToggleButton toggle2 = new ToggleButton(this);
        toggle2.setChecked(true);
        toggle2.setText(t2);
        toggle2.setTextOn(t2);
        toggle2.setTextOff(t2);
        toggle2.setTextSize(20);


        linearLayout.addView(toggle);
        final LinearLayout simple = new LinearLayout(this);
        final LinearLayout normal = new LinearLayout(this);
        simple.setOrientation(LinearLayout.VERTICAL);
        normal.setOrientation(LinearLayout.VERTICAL);


        for(ListView list : lists){
            if (list.getSimple()) {
                //Create layout to list items in a column
                LinearLayout oneLine = new LinearLayout(this);
                oneLine.setOrientation(LinearLayout.HORIZONTAL);

                //Create three textviews
                TextView textly = new TextView(this);
                TextView editlist = new TextView(this);
                TextView viewlist = new TextView(this);
                TextView deletelist = new TextView(this);

                //Set information for the edit
                editlist.setId(list.getId());
                SpannableString edit = new SpannableString("Edit");
                edit.setSpan(new UnderlineSpan(), 0, 4, 0);
                editlist.setText(edit);
                editlist.setHint("simple");

                //Set information for the view
                viewlist.setId(list.getId());
                SpannableString view = new SpannableString("View");
                view.setSpan(new UnderlineSpan(), 0, 4, 0);
                viewlist.setText(view);
                textly.setText(list.getName());
                viewlist.setHint("simple");

                //Set delete info
                deletelist.setId(list.getId());
                SpannableString delete = new SpannableString("Delete");
                delete.setSpan(new UnderlineSpan(), 0, 4, 0);
                deletelist.setText(delete);
                deletelist.setHint("simple");


                //Set onclicklistener
                editlist.setOnClickListener(this);
                viewlist.setOnClickListener(this);
                deletelist.setOnClickListener(this);

                //Set text sizes
                textly.setTextSize(18);
                textly.setPadding(20, 20, 20, 20);
                editlist.setTextSize(18);
                editlist.setPadding(20, 20, 20, 20);
                viewlist.setTextSize(18);
                viewlist.setPadding(20, 20, 20, 20);
                deletelist.setTextSize(18);
                deletelist.setPadding(20, 20, 20, 20);

                //Add views to column
                oneLine.addView(textly);
                oneLine.addView(viewlist);
                oneLine.addView(editlist);
                oneLine.addView(deletelist);

                //Add columns to row
                simple.addView(oneLine);
            }
        }
        linearLayout.addView(simple);
        linearLayout.addView(toggle2);
        for(ListView list : lists){
            if (!(list.getSimple())) {
                //Create layout to list items in a column
                LinearLayout oneLine = new LinearLayout(this);
                oneLine.setOrientation(LinearLayout.HORIZONTAL);

                //Create three textviews
                TextView textly = new TextView(this);
                TextView editlist = new TextView(this);
                TextView viewlist = new TextView(this);
                TextView deletelist = new TextView(this);

                //Set information for the edit
                editlist.setId(list.getId());
                SpannableString edit = new SpannableString("Edit");
                edit.setSpan(new UnderlineSpan(), 0, 4, 0);
                editlist.setText(edit);
                editlist.setHint("normal");

                //Set information for the view
                viewlist.setId(list.getId());
                SpannableString view = new SpannableString("View");
                view.setSpan(new UnderlineSpan(), 0, 4, 0);
                viewlist.setText(view);
                textly.setText(list.getName());
                viewlist.setHint("normal");

                //Set delete info
                deletelist.setId(list.getId());
                SpannableString delete = new SpannableString("Delete");
                delete.setSpan(new UnderlineSpan(), 0, 4, 0);
                deletelist.setText(delete);
                deletelist.setHint("normal");

                //Set onclicklistener
                editlist.setOnClickListener(this);
                viewlist.setOnClickListener(this);
                deletelist.setOnClickListener(this);

                //Set text sizes
                textly.setTextSize(25);
                textly.setPadding(20, 20, 20, 20);
                editlist.setTextSize(22);
                editlist.setPadding(20, 20, 20, 20);
                viewlist.setTextSize(22);
                viewlist.setPadding(20, 20, 20, 20);
                deletelist.setTextSize(18);
                deletelist.setPadding(20, 20, 20, 20);

                //Add views to column
                oneLine.addView(textly);
                oneLine.addView(viewlist);
                oneLine.addView(editlist);
                oneLine.addView(deletelist);

                //Add columns to row
                normal.addView(oneLine);
            }
        }
        linearLayout.addView(normal);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    simple.setVisibility(View.VISIBLE);
                } else {
                    simple.setVisibility(View.GONE);
                }
            }
        });

        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    normal.setVisibility(View.VISIBLE);
                } else {
                    normal.setVisibility(View.GONE);
                }
            }
        });



    }

    private void deleteSimpleConfirm(final int list_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String listName = db.getSimpleListName(list_id);
        builder.setMessage("Are you sure you want to delete " + listName);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteSimpleList(list_id);
                setList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void deleteNormalConfirm(final int list_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String listName = db.getNormalListName(list_id);
        builder.setMessage("Are you sure you want to delete " + listName);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteNormalList(list_id);
                setList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.refresh) {
            recreate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.refresh = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backbutton:
                MainActivity.controller.endActivity(this);
                break;
            default:
                TextView item = (TextView)v;
                switch(item.getText().toString()) {

                    case "Edit":
                        if (item.getHint().toString().equals("simple")) {
                            SimpleList thisList = db.getSimpleListContent(v.getId());
                            MainActivity.controller.savedSimpleList(this, thisList.getContent(), thisList.getName(), thisList.getId());
                        }
                        if (item.getHint().toString().equals("normal")) {
                            ArrayList<ListItem> thisList = db.getNormalListContent(v.getId());
                            MainActivity.controller.savedNormalList(this, thisList, v.getId(), db.getNormalListName(v.getId()));
                        }

                        break;

                    case "View":
                        if (item.getHint().toString().equals("simple")) {
                            SimpleList thisListt = db.getSimpleListContent(v.getId());
                            MainActivity.controller.viewSimpleList(this, thisListt.getContent());
                        }
                        if (item.getHint().toString().equals("normal")) {
                            ArrayList<ListItem> thisList = db.getNormalListContent(v.getId());
                            MainActivity.controller.viewNormalList(this, thisList, v.getId());
                        }

                        break;

                    case "Delete":
                        if (item.getHint().toString().equals("simple")) {
                            deleteSimpleConfirm(v.getId());
                        }
                        if (item.getHint().toString().equals("normal")) {
                            deleteNormalConfirm(v.getId());
                        }
                        break;

                    default:
                    Toast toasty = Toast.makeText(this, "Could not find item", Toast.LENGTH_SHORT);
                    toasty.show();
                }
        }
    }
}
