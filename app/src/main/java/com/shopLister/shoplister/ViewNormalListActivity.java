package com.shopLister.shoplister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.util.ArrayList;

import database.DatabaseControl;
import models.CategoryItem;
import models.ListItem;

import static android.text.TextUtils.isEmpty;
import static com.shopLister.shoplister.MainActivity.controller;

public class ViewNormalListActivity extends AppCompatActivity {

    DatabaseControl db;
    LinearLayout itemview;
    Intent intent;
    ArrayList<ListItem> listContent;
    ArrayList<CategoryItem> categories;
    Button back;
    int thisListId;
    Double totalPrice;
    TextView totalPriceShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_normal_list);

        db = new DatabaseControl(this);
        itemview = findViewById(R.id.listContent);
        intent = getIntent();
        listContent = intent.getParcelableArrayListExtra("listContent");
        thisListId = intent.getIntExtra("listId", 0);
        categories = db.getCategories();
        back = findViewById(R.id.backbutton);

        totalPriceShow = findViewById(R.id.priceDisplay);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.endActivity(ViewNormalListActivity.this);
            }
        });

        createList();
    }

    void createList() {

        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param1.width = getResources().getDimensionPixelSize(R.dimen.table1width);

        LinearLayout.LayoutParams param23 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param23.width = getResources().getDimensionPixelSize(R.dimen.table23width);

        LinearLayout.LayoutParams paramd = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramd.width = getResources().getDimensionPixelSize(R.dimen.tabledwidth);

        for (CategoryItem category : categories) {

            Boolean go = db.isCategoryApplicable(category.getId(), thisListId);
            if (go) {
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

                for (ListItem displayItem : listContent) {

                    if (displayItem.getGrocery_section().equals(category.getCategory())) {

                        LinearLayout onerow = new LinearLayout(this);
                        final CheckBox check = new CheckBox(this);
                        check.setText(displayItem.getGrocery_name());
                        check.setId(displayItem.getId());
                        check.setLayoutParams(param1);
                        onerow.addView(check);

                        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b) {
                                    check.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                } else {
                                    check.setPaintFlags(check.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                                }
                            }
                        });

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
        }

        setPrice();
    }

    private void setPrice() {
        totalPrice = 0.00;
        if (listContent != null) {
            for (ListItem item : listContent) {
                totalPrice += (item.getGrocery_price() * item.getQuantity());
            }
        }
        totalPrice = totalPrice / 100;
        totalPriceShow.setText("Total Price: $" + totalPrice);

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
                    Toast toast = Toast.makeText(ViewNormalListActivity.this, "Need to include a new price", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    int newPrice = Math.round(Float.parseFloat(editPrice.getText().toString()) * 100);
                    price.setText(editPrice.getText());
                    db.updateGroceryPrice(price.getId(), newPrice);
                    for (ListItem updater : listContent) {
                        if (updater.getId() == price.getId()) {
                            updater.setGrocery_price(newPrice);
                            setPrice();
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
                    Toast toast = Toast.makeText(ViewNormalListActivity.this, "Need to include a quantity", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    int newQuantity = Integer.parseInt(editQuantity.getText().toString());
                    quantity.setText(editQuantity.getText());
                    for (ListItem updater : listContent) {
                        if (updater.getId() == quantity.getId()) {
                            updater.setQuantity(newQuantity);
                            setPrice();
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
