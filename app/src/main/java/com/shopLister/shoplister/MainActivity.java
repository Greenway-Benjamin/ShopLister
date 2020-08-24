package com.shopLister.shoplister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




public class MainActivity extends AppCompatActivity {
    public static final ApplicationController controller = new ApplicationController();
    Button newListButton;
    Button savedListButton;
    Button simpleListButton;
    Button recipeButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        newListButton = findViewById(R.id.newlistbutton);
        newListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                controller.newDisplay(MainActivity.this, NewListActivity.class);
            }
        });

        savedListButton = findViewById(R.id.savedlistbutton);
        savedListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                controller.newDisplay(MainActivity.this, SavedListActivity.class);
            }
        });

        simpleListButton = findViewById(R.id.simplelistbutton);
        simpleListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                controller.newDisplay(MainActivity.this, SimpleListActivity.class);
            }
        });

        recipeButton = findViewById(R.id.recipes);
        recipeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                controller.newDisplay(MainActivity.this, RecipeBuilderActivity.class);
            }
        });



    }

}
