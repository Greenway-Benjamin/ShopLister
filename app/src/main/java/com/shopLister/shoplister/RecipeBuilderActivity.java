package com.shopLister.shoplister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import database.DatabaseControl;
import models.Recipe;

public class RecipeBuilderActivity extends AppCompatActivity implements View.OnClickListener {

    Button back;
    Button newRecipe;
    DatabaseControl db;
    ArrayList<Recipe> recipes;
    LinearLayout recipeList;
    Boolean refresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_builder);

        back = findViewById(R.id.backbutton);
        back.setOnClickListener(this);
        newRecipe = findViewById(R.id.newRecipe);
        newRecipe.setOnClickListener(this);

        recipeList = findViewById(R.id.recipeList);

        db = new DatabaseControl(this);


        setList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.refresh) {
            setList();
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

            case R.id.newRecipe:
                MainActivity.controller.newDisplay(this, NewRecipeActivity.class);
                break;

            default:
                if (v instanceof TextView) {
                    if (((TextView) v).getText().toString() == "Edit") {
                        MainActivity.controller.editRecipe(this, NewRecipeActivity.class, v.getId(), db.getRecipeName(v.getId()));
                    }
                    else if (((TextView) v).getText().toString() == "Delete") {
                        deleteConfirm(v.getId());
                    }
                    else {
                        Toast toast = Toast.makeText(this, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }


        }
    }

    private void deleteConfirm(final int recipe_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String recipeName = db.getRecipeName(recipe_id);
        builder.setMessage("Are you sure you want to delete " + recipeName);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteRecipe(recipe_id);
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

    public void setList() {

        recipeList.removeAllViews();
        recipes = db.getRecipes();

        for (Recipe recipe : recipes) {

            LinearLayout oneLine = new LinearLayout(this);
            oneLine.setOrientation(LinearLayout.HORIZONTAL);

            //Create three textviews
            TextView textly = new TextView(this);
            TextView editlist = new TextView(this);
            TextView deletelist = new TextView(this);

            textly.setText(recipe.getRecipe_name());

            //Set information for the edit
            editlist.setId(recipe.getRecipe_id());
            SpannableString edit = new SpannableString("Edit");
            edit.setSpan(new UnderlineSpan(), 0, 4, 0);
            editlist.setText(edit);

            //Set onclicklistener
            editlist.setOnClickListener(this);

            //Set up delete
            deletelist.setId(recipe.getRecipe_id());
            SpannableString delete = new SpannableString("Delete");
            delete.setSpan(new UnderlineSpan(), 0, 4, 0);
            deletelist.setText(delete);

            deletelist.setOnClickListener(this);

            //Set text sizes
            textly.setTextSize(18);
            textly.setPadding(20, 20, 20, 20);
            editlist.setTextSize(18);
            editlist.setPadding(20, 20, 20, 20);
            deletelist.setTextSize(18);
            deletelist.setPadding(20, 20, 20, 20);

            //Add views to column
            oneLine.addView(textly);
            oneLine.addView(editlist);
            oneLine.addView(deletelist);

            //Add columns to row
            recipeList.addView(oneLine);

        }

    }

}
