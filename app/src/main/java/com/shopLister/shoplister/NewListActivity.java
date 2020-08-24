package com.shopLister.shoplister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Iterator;

import database.DatabaseControl;
import models.ListItem;

public class NewListActivity extends AppCompatActivity implements View.OnClickListener {
    Button saveList;
    Button back;
    Button newItem;
    Button deleteItems;
    Button addRecipe;
    DatabaseControl db;
    String listName;
    ArrayList<ListItem> listContent;
    ArrayList<Integer> listContentIds = new ArrayList<>();
    ArrayList<Integer> itemsAlready = new ArrayList<>();
    LinearLayout itemview;
    ArrayList<CheckBox> allboxes = new ArrayList<>();
    Double totalPrice;
    TextView totalPriceShow;
    Boolean saved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        Intent intent = getIntent();
        listContent = intent.getParcelableArrayListExtra("listContent");
        listName = intent.getStringExtra("listName");

        saveList = findViewById(R.id.savebutton);
        back = findViewById(R.id.backbutton);
        newItem = findViewById(R.id.newitem);
        deleteItems = findViewById(R.id.deleteButton);
        addRecipe = findViewById(R.id.addWithRecipe);
        totalPriceShow = findViewById(R.id.priceDisplay);

        itemview = findViewById(R.id.itemselection);


        saveList.setOnClickListener(this);
        back.setOnClickListener(this);
        newItem.setOnClickListener(this);
        deleteItems.setOnClickListener(this);
        addRecipe.setOnClickListener(this);

        db = new DatabaseControl(this);


            setList();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                listContent = data.getParcelableArrayListExtra("result");
                setList();

            }
            if (resultCode == RESULT_CANCELED) {
                Toast toast = Toast.makeText(this, "Canceled New Items", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                ArrayList<ListItem> theData = data.getParcelableArrayListExtra("result");
                if (listContent == null) {
                    listContent = new ArrayList<>();
                } else {
                    for (ListItem item : listContent) {
                        itemsAlready.add(item.getId());
                    }
                }
                for (ListItem item : theData) {
                    if (!(itemsAlready.contains(item.getId()))) {
                        listContent.add(item);
                        } else {

                        for (ListItem list : listContent) {
                            if (list.getId() == item.getId()) {
                                list.setQuantity(list.getQuantity() + item.getQuantity());
                            }
                        }

                        }
                    }

                setList();

            }
            if (resultCode == RESULT_CANCELED) {
                Toast toast = Toast.makeText(this, "Canceled New Items", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savebutton:
                final EditText listToName = new EditText(this);
                listToName.setText(listName);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("What shall we name this list?");
                builder.setView(listToName);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(listToName.getText())) {
                            Toast toast = Toast.makeText(NewListActivity.this, "Need to include a list name", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        boolean alreadyThere = db.checkListName(listToName.getText().toString());


                        if (alreadyThere) {
                            AlertDialog.Builder saveOver = new AlertDialog.Builder(NewListActivity.this);
                            saveOver.setMessage("Save over " + listToName.getText().toString() + "?");
                            saveOver.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (listContent == null) {
                                        Toast toast = Toast.makeText(NewListActivity.this, "You must include at least one item.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        return;
                                    }
                                    int listId = db.getListId(listToName.getText().toString());
                                    db.dropListValues(listId);
                                    for (ListItem item : listContent) {
                                        db.insertListValues(listId, item.getId(), item.getQuantity());
                                    }
                                    Toast toast = Toast.makeText(NewListActivity.this, listToName.getText().toString() + " Simple List Updated!", Toast.LENGTH_SHORT);
                                    toast.show();
                                    MainActivity.controller.endActivity(NewListActivity.this);
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
                            if (listContent == null) {
                                Toast toast = Toast.makeText(NewListActivity.this, "You must include at least one item.", Toast.LENGTH_SHORT);
                                toast.show();
                                return;
                            }
                            db.insertLists(listToName.getText().toString());
                            int listId = db.getListId(listToName.getText().toString());
                            for (ListItem item : listContent) {
                                db.insertListValues(listId, item.getId(), item.getQuantity());
                            }
                            Toast toast = Toast.makeText(NewListActivity.this, listToName.getText().toString() + " List Saved!", Toast.LENGTH_SHORT);
                            toast.show();
                            MainActivity.controller.endActivity(NewListActivity.this);
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
                if(!saved) {
                    AlertDialog.Builder unsaved = new AlertDialog.Builder(NewListActivity.this);
                    unsaved.setMessage("Changes are not saved. Leave page?");
                    unsaved.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.controller.endActivity(NewListActivity.this);
                        }
                    });
                    unsaved.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    unsaved.show();
                } else {
                    MainActivity.controller.endActivity(this);
                }
                break;

            case R.id.newitem:
                MainActivity.controller.newDisplayWithResult(this, ItemSelectorActivity.class, listContent);
                break;

            case R.id.deleteButton:

                    for (CheckBox box: allboxes) {

                        if (box.isChecked()) {

                            Iterator<ListItem> iter = listContent.iterator();

                            while (iter.hasNext()) {
                                ListItem item = iter.next();

                                if (item.getId() == box.getId()) {
                                    iter.remove();

                                }
                            }
                        }
                    }
                    setList();


                break;

            case R.id.addWithRecipe:

                MainActivity.controller.addByRecipe(NewListActivity.this, AddRecipeActivity.class);

                break;

        }
    }

    private void setList() {

        itemview.removeAllViews();

        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param1.width = getResources().getDimensionPixelSize(R.dimen.table1width);

        LinearLayout.LayoutParams param23 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param23.width = getResources().getDimensionPixelSize(R.dimen.table23width);

        LinearLayout.LayoutParams paramd = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramd.width = getResources().getDimensionPixelSize(R.dimen.tabledwidth);
        if (listContent != null) {
            for (ListItem item : listContent) {
                LinearLayout onerow = new LinearLayout(this);
                CheckBox check = new CheckBox(this);
                check.setText(item.getGrocery_name());
                check.setId(item.getId());
                check.setLayoutParams(param1);
                onerow.addView(check);
                allboxes.add(check);

                TextView dollar = new TextView(this);
                dollar.setText("$");
                dollar.setLayoutParams(paramd);
                onerow.addView(dollar);

                final EditText price = new EditText(this);
                price.setHint(Float.toString((float) item.getGrocery_price() / 100));
                price.setId(item.getId());
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
                quantity.setHint(Integer.toString(item.getQuantity()));
                quantity.setId(item.getId());
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

                itemview.addView(onerow);

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
                if (TextUtils.isEmpty(editPrice.getText())) {
                    Toast toast = Toast.makeText(NewListActivity.this, "Need to include a new price", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    int newPrice = Math.round(Float.parseFloat(editPrice.getText().toString()) * 100);
                    price.setText(editPrice.getText());
                    db.updateGroceryPrice(price.getId(), newPrice);
                    for (ListItem item : listContent) {
                        if (item.getId() == price.getId()) {
                            item.setGrocery_price(newPrice);
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
        builder.setMessage("Enter a new quanity");
        builder.setView(editQuantity);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(editQuantity.getText())) {
                    Toast toast = Toast.makeText(NewListActivity.this, "Need to include a quantity", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    quantity.setText(editQuantity.getText());
                    for (ListItem item : listContent) {
                        if (item.getId() == quantity.getId()) {
                            item.setQuantity(Integer.parseInt(editQuantity.getText().toString()));
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
