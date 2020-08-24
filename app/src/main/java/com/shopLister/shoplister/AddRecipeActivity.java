package com.shopLister.shoplister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;


import java.util.ArrayList;

import database.DatabaseControl;
import models.ListItem;
import models.Recipe;

import static com.shopLister.shoplister.MainActivity.controller;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    Button back;
    Button addRecipes;
    LinearLayout recipeContent;
    DatabaseControl db;
    ArrayList<Recipe> recipeList;
    ArrayList<ListItem> listContent = new ArrayList<>();
    ArrayList<CheckBox> allboxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        back = findViewById(R.id.backbutton);
        back.setOnClickListener(this);
        addRecipes = findViewById(R.id.addRecipes);
        addRecipes.setOnClickListener(this);
        recipeContent = findViewById(R.id.recipeContent);

        db = new DatabaseControl(this);

        recipeList = db.getRecipes();

        setList();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backbutton:

                controller.endActivity(this);

                break;

            case R.id.addRecipes:

                for (CheckBox box : allboxes) {
                    if (box.isChecked()) {
                        ArrayList<ListItem> temporary = db.getRecipeValues(box.getId());

                        for (ListItem item : temporary) {
                            listContent.add(item);
                        }
                    }
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", listContent);
                setResult(RESULT_OK, resultIntent);
                finish();

                break;
        }
    }

    public void setList() {

        recipeContent.removeAllViews();

        for (Recipe recipe : recipeList) {

            CheckBox check = new CheckBox(this);
            check.setText(recipe.getRecipe_name());
            check.setId(recipe.getRecipe_id());
            recipeContent.addView(check);
            allboxes.add(check);
        }

    }
}
