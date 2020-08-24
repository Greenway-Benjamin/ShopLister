package com.shopLister.shoplister;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

import models.ListItem;


public class ApplicationController {


    public void newDisplay(Activity activity, Class className) {
    Intent intent = new Intent(activity, className);
    activity.startActivity(intent);
    }

    public void newDisplayWithResult(Activity activity, Class className, ArrayList<ListItem> listContent) {
        Intent intent = new Intent(activity, className);
        intent.putExtra("listContent", listContent);
        activity.startActivityForResult(intent, 1);

    }

    public void editRecipe(Activity activity, Class className, int recipeId, String recipeName) {
        Intent intent = new Intent(activity, className);
        intent.putExtra("recipeId", recipeId);
        intent.putExtra("recipeName", recipeName);
        activity.startActivity(intent);
    }

    public void addByRecipe(Activity activity, Class className) {
        Intent intent = new Intent(activity, className);
        activity.startActivityForResult(intent, 2);
    }

    public void savedSimpleList(Activity activity, String listContent, String listName, Integer listId) {

        Intent intent = new Intent(activity, SimpleListActivity.class);
        intent.putExtra("listContent", listContent);
        intent.putExtra("listName", listName);
        intent.putExtra("listId", listId);
        activity.startActivity(intent);

    }

    public void savedNormalList(Activity activity, ArrayList<ListItem> listContent, int id, String listName) {
        Intent intent = new Intent(activity, NewListActivity.class);
        intent.putExtra("listContent", listContent);
        intent.putExtra("listId", id);
        intent.putExtra("listName", listName);
        activity.startActivity(intent);
    }

    public void viewSimpleList(Activity activity, String listContent) {
        Intent intent = new Intent(activity, ViewSimpleListActivity.class);
        intent.putExtra("listContent", listContent);
        activity.startActivity(intent);
    }

    public void viewNormalList(Activity activity, ArrayList<ListItem> listContent, int id) {
        Intent intent = new Intent(activity, ViewNormalListActivity.class);
        intent.putExtra("listContent", listContent);
        intent.putExtra("listId", id);
        activity.startActivity(intent);
    }

    public void endActivity(Activity activity) {
        activity.finish();
    }
}
