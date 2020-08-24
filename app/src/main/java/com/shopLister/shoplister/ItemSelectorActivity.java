package com.shopLister.shoplister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.util.ArrayList;

import database.DatabaseControl;
import models.CategoryItem;
import models.ListItem;

import static android.text.TextUtils.isEmpty;
import static com.shopLister.shoplister.MainActivity.controller;

public class ItemSelectorActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseControl db;
    Button back;
    Button additems;
    Button createNew;
    ArrayList<ListItem> listContent = new ArrayList<>();
    ArrayList<ListItem> displayContent;
    ArrayList<CheckBox> allboxes = new ArrayList<>();
    ArrayList<CategoryItem> categories;
    ArrayList<Integer> checkedBoxes = new ArrayList<>();
    ArrayList<ListItem> checkedList = new ArrayList<>();
    LinearLayout itemview;
    SearchView searchBar;
    Intent fromIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selector);

        fromIntent = getIntent();

            checkedList = fromIntent.getParcelableArrayListExtra("listContent");

        back = findViewById(R.id.backbutton);
        additems = findViewById(R.id.additems);
        additems.setOnClickListener(this);
        db = new DatabaseControl(this);
        itemview = findViewById(R.id.itemselection);
        searchBar = findViewById(R.id.searchbar);

        createNew = new Button(this);
        createNew.setOnClickListener(this);
        createNew.setText("Create New item");
        createNew.setId(0);

        back.setOnClickListener(this);
        additems.setOnClickListener(this);





        createList();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backbutton:
                controller.endActivity(this);
                break;

            case R.id.additems:

                for (CheckBox box : allboxes) {
                    if (box.isChecked()) {
                        for (ListItem updater : displayContent) {
                            if (updater.getId() == box.getId()) {
                                listContent.add(updater);
                            }
                        }
                    }
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", listContent);
                setResult(RESULT_OK, resultIntent);
                finish();

                break;

            //Create New Item
            case 0:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please enter an item name and select a category");
                LinearLayout builderLayout = new LinearLayout(this);
                builderLayout.setOrientation(LinearLayout.HORIZONTAL);
                final EditText itemName = new EditText(this);
                itemName.setHint("Item Name");
                builderLayout.addView(itemName);
                final Spinner categoryList = new Spinner(this);
                builderLayout.addView(categoryList);
                ArrayAdapter<CategoryItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
                categoryList.setAdapter(adapter);
                builder.setView(builderLayout);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        String itemAdd = itemName.getText().toString();
                        CategoryItem categoryAdd = (CategoryItem)categoryList.getSelectedItem();
                        if (isEmpty(itemAdd)) {
                            Toast toast = Toast.makeText(ItemSelectorActivity.this, "Please enter an item name", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            db.insertGroceryItems(itemAdd, categoryAdd.getId());
                            dialog.dismiss();
                            for (CheckBox box : allboxes) {
                                if (box.isChecked()) {
                                    checkedBoxes.add(box.getId());
                                    box.setChecked(false);
                                }
                            }
                            createList();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                builder.show();

                break;

        }
    }

    void createList() {

        itemview.removeAllViews();

        displayContent = db.getGroceries();
        categories = db.getCategories();
        if (checkedList != null) {
            for (ListItem checkedItem : checkedList) {
                for (ListItem displayItem : displayContent) {
                    if (checkedItem.getId() == displayItem.getId()) {
                        displayItem.setChecked(true);
                        displayItem.setQuantity(checkedItem.getQuantity());
                    }
                }
            }
        }

        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param1.width = getResources().getDimensionPixelSize(R.dimen.table1width);

        LinearLayout.LayoutParams param23 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param23.width = getResources().getDimensionPixelSize(R.dimen.table23width);

        LinearLayout.LayoutParams paramd = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramd.width = getResources().getDimensionPixelSize(R.dimen.tabledwidth);

        for (CategoryItem category : categories) {

            ToggleButton toggle = new ToggleButton(this);
            toggle.setText(category.getCategory());
            toggle.setTextOn(category.getCategory());
            toggle.setTextOff(category.getCategory());
            toggle.setId(category.getId());
            toggle.setTextSize(20);
            itemview.addView(toggle);

            final LinearLayout toggleLayout = new LinearLayout(this);

            toggleLayout.setOrientation(LinearLayout.VERTICAL);
            toggleLayout.setVisibility(View.GONE);

            LinearLayout tablehead = new LinearLayout(this);
            tablehead.setOrientation(LinearLayout.HORIZONTAL);
            TextView table1 = new TextView(this);
            table1.setText("Item name");
            table1.setTextSize(15);
            table1.setLayoutParams(param1);
            tablehead.addView(table1);
            TextView table2 = new TextView(this);
            table2.setLayoutParams(param23);
            table2.setText("Price");
            table2.setTextSize(15);
            tablehead.addView(table2);
            TextView table3 = new TextView(this);
            table3.setText("Quantity");
            table3.setTextSize(15);
            table3.setLayoutParams(param23);
            tablehead.addView(table3);

            toggleLayout.addView(tablehead);

            for (ListItem displayItem : displayContent) {

                if (displayItem.getGrocery_section().equals(category.getCategory())) {

                    LinearLayout onerow = new LinearLayout(this);
                    CheckBox check = new CheckBox(this);
                    check.setText(displayItem.getGrocery_name());
                    check.setId(displayItem.getId());
                    check.setLayoutParams(param1);
                    if (checkedBoxes.contains(displayItem.getId()) || displayItem.getChecked()) {
                        check.setChecked(true);
                    }
                    onerow.addView(check);
                    allboxes.add(check);

                    TextView dollar = new TextView(this);
                    dollar.setText("$");
                    dollar.setLayoutParams(paramd);
                    onerow.addView(dollar);

                    final EditText price = new EditText(this);
                    price.setHint(Float.toString((float) displayItem.getGrocery_price() / 100));
                    price.setId(displayItem.getId());
                    price.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    price.setFocusable(false);
                    onerow.addView(price);
                    price.setLayoutParams(param23);
                    price.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editPrice(price);
                        }
                    });

                    final EditText quantity = new EditText(this);
                    quantity.setHint(Integer.toString(displayItem.getQuantity()));
                    quantity.setId(displayItem.getId());
                    onerow.addView(quantity);
                    quantity.setFocusable(false);
                    quantity.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    quantity.setLayoutParams(param23);
                    quantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editQuantity(quantity);
                        }
                    });

                    toggleLayout.addView(onerow);

                }
            }
            itemview.addView(toggleLayout);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        toggleLayout.setVisibility(View.VISIBLE);
                    } else {
                        toggleLayout.setVisibility(View.GONE);
                    }
                }
            });


        }
        createNew.setBackgroundColor(Color.GREEN);
        itemview.addView(createNew);

    }

    private void editPrice(final EditText price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editPrice = new EditText(this);
        editPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setMessage("Enter a new price");
        builder.setView(editPrice);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isEmpty(editPrice.getText())) {
                    Toast toast = Toast.makeText(ItemSelectorActivity.this, "Need to include a new price", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    int newPrice = Math.round(Float.parseFloat(editPrice.getText().toString()) * 100);
                    price.setText(editPrice.getText());
                    db.updateGroceryPrice(price.getId(), newPrice);
                    for (ListItem updater : displayContent) {
                        if (updater.getId() == price.getId()) {
                            updater.setGrocery_price(newPrice);
                            return;
                        }
                    }
                    dialog.dismiss();
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
    }
    private void editQuantity (final EditText quantity){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editQuantity = new EditText(this);
        editQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setMessage("Enter a new price");
        builder.setView(editQuantity);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isEmpty(editQuantity.getText())) {
                    Toast toast = Toast.makeText(ItemSelectorActivity.this, "Need to include a quantity", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    int newQuantity = Integer.parseInt(editQuantity.getText().toString());
                    quantity.setText(editQuantity.getText());
                    for (ListItem updater : displayContent) {
                        if (updater.getId() == quantity.getId()) {
                            updater.setQuantity(newQuantity);
                            return;
                        }
                    }
                    dialog.dismiss();
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

    }

}

