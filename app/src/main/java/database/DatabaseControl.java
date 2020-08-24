package database;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import models.CategoryItem;
import models.ListItem;
import models.ListView;
import models.Recipe;
import models.SimpleList;

import java.util.ArrayList;

public class DatabaseControl {
    DatabaseHelper dbhelper;
    public DatabaseControl(Context context) {
        dbhelper = new DatabaseHelper(context);
    }
    public void insertLists(String listName) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("list_name", listName);
        db.insert("lists", null, content);
    }
    public int getListId(String list) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "SELECT list_id FROM lists where list_name = '" + list + "'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int returnId = c.getInt(0);
        c.close();
        return returnId;
    }
    public void insertListValues(int list_id, int grocery_id, int quantity) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("list_id", list_id);
        content.put("grocery_id", grocery_id);
        content.put("quantity", quantity);
        db.insert("listvalues", null, content);
    }
    public void dropListValues(int list_id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "DELETE FROM listvalues WHERE list_id = '" + list_id + "'";
        db.execSQL(query);
    }
    public void insertGroceryItems(String grocery_name, int grocery_section) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("grocery_name", grocery_name);
        content.put("grocery_section", grocery_section);
        db.insert("groceryitems", null, content);
    }

    public void updateGroceryPrice(int id, int grocery_price) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues updated = new ContentValues();
        updated.put("grocery_price", grocery_price);
        db.update("groceryitems", updated, "grocery_id = '" + id + "'", null);

    }

    public void insertSimpleList(String list_name, String list_text) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("simplelist_name", list_name);
        content.put("simplelist_content", list_text);
        db.insert("simplelists", null, content);
    }

    public void updateSimpleList(String simplelist_name, String list_text) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues updated = new ContentValues();
        updated.put("simplelist_content", list_text);
        db.update("simplelists", updated, "simplelist_name = '" + simplelist_name + "'", null);

    }
    public void deleteSimpleList(int list_id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "DELETE FROM simplelists WHERE simplelist_id = '" + list_id + "'";
        db.execSQL(query);
    }
    public void deleteNormalList(int list_id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "DELETE FROM lists WHERE list_id = '" + list_id + "'";
        db.execSQL(query);
    }

    public String getSimpleListName(int simplelist_id) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT simplelist_name FROM simplelists WHERE simplelist_id = " + simplelist_id;
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() > 0) {
            c.moveToFirst();
        }
        String result = c.getString(0);
        return result;
    }

    public SimpleList getSimpleListContent(int simplelist_id) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM simplelists WHERE simplelist_id = " + simplelist_id, null);
        if(c.getCount() > 0) {
            c.moveToFirst();
        }
        SimpleList list = new SimpleList(c.getInt(0), c.getString(1), c.getString(2));
        c.close();
        return list;
    }

    public ArrayList<ListItem> getNormalListContent(int list_id) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        ArrayList<ListItem> returnList = new ArrayList<>();
        String query = "SELECT g.grocery_id, lv.quantity, g.grocery_name, gc.category_name, g.grocery_price FROM listvalues lv INNER JOIN groceryitems g ON lv.grocery_id = g.grocery_id INNER JOIN grocerycategories gc ON gc.category_id = g.grocery_section WHERE list_id = '" + list_id + "'";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            ListItem oneItem = new ListItem(c.getInt(0), c.getString(2), c.getString(3), c.getInt(4));
            oneItem.setQuantity(c.getInt(1));
            returnList.add(oneItem);
        }
        c.close();
        return returnList;
    }
    public String getNormalListName(int list_id) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String name = "";
        String query = "SELECT list_name FROM lists WHERE list_id = '" + list_id + "'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        name = c.getString(0);
        return name;
    }

    public ArrayList<ListView> getAllListnames() {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query1 = "SELECT simplelist_id, simplelist_name FROM simplelists";
        String query2 = "SELECT list_id, list_name FROM lists";
        Cursor c = db.rawQuery(query1, null);
        ArrayList<ListView> names = new ArrayList<ListView>();
        while (c.moveToNext()) {
            names.add(new ListView(c.getInt(0), c.getString(1), true));
        }
        c.close();
        Cursor d = db.rawQuery(query2, null);
        while (d.moveToNext()) {
            names.add(new ListView(d.getInt(0), d.getString(1), false));
        }
        d.close();
        return names;
    }
    public boolean checkSimpleName(String name) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String check = "SELECT * FROM simplelists WHERE simplelist_name = '" + name + "'";
        Cursor c = db.rawQuery(check, null);
        int checked = c.getCount();
        c.close();
        return (checked > 0);
    }
    public boolean checkListName(String name) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String check = "SELECT * FROM lists WHERE list_name = '" + name + "'";
        Cursor c = db.rawQuery(check, null);
        int checked = c.getCount();
        c.close();
        return (checked > 0);
    }
    public ArrayList<ListItem> getGroceries() {
        ArrayList<ListItem> returnList = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM groceryitems g INNER JOIN grocerycategories gc ON g.grocery_section = gc.category_id ORDER BY g.grocery_name COLLATE NOCASE ASC",null);
        while (c.moveToNext()) {
            returnList.add(new ListItem(c.getInt(0), c.getString(1), c.getString(5), c.getInt(3)));
        }
        c.close();
        return returnList;
    }

    public Boolean isCategoryApplicable(int category_id, int list_id) {
        Boolean go = false;
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT * FROM lists l " +
                "INNER JOIN listvalues lv ON l.list_id = lv.list_id " +
                "INNER JOIN groceryitems gi ON gi.grocery_id = lv.grocery_id " +
                "INNER JOIN grocerycategories gc ON gi.grocery_section = gc.category_id " +
                "WHERE gc.category_id = '" + category_id + "' " +
                "AND l.list_id = '" + list_id + "'";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() > 0) {
            go = true;
        }

        return go;
    }

    public ArrayList<CategoryItem> getCategories() {
        ArrayList<CategoryItem> returnList = new ArrayList<CategoryItem>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM grocerycategories",null);
        while (c.moveToNext()) {
            returnList.add(new CategoryItem(c.getInt(0), c.getString(1)));
        }
        c.close();
        return returnList;
    }

    public void insertRecipe(String recipe_name) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("recipe_name", recipe_name);
        db.insert("recipe", null, content);
    }

    public void deleteRecipe(int recipe_id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "DELETE FROM recipe WHERE recipe_id = '" + recipe_id + "'";
        db.execSQL(query);
    }

    public void insertRecipeValues(int recipe_id, int grocery_id, int quantity) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("recipe_id", recipe_id);
        content.put("grocery_id", grocery_id);
        content.put("quantity", quantity);
        db.insert("recipevalues", null, content);
    }

    public void dropRecipeValues(int recipe_id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "DELETE FROM recipevalues WHERE recipe_id = '" + recipe_id + "'";
        db.execSQL(query);
    }

    public boolean checkRecipeName(String recipe_name) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT * FROM recipe where recipe_name = '" + recipe_name + "'";
        Cursor c = db.rawQuery(query, null);
        int checked = c.getCount();
        c.close();
        return (checked > 0);
    }

    public int getRecipeId(String recipe) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "SELECT recipe_id FROM recipe where recipe_name = '" + recipe + "'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int returnId = c.getInt(0);
        c.close();
        return returnId;
    }

    public String getRecipeName(int recipe_id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "SELECT recipe_name FROM recipe where recipe_id = '" + recipe_id + "'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String returnId = c.getString(0);
        c.close();
        return returnId;
    }

    public ArrayList<ListItem> getRecipeValues(int recipe_id) {
        ArrayList<ListItem> returnList = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT g.grocery_id, g.grocery_name, gc.category_name, g.grocery_price, rv.quantity FROM recipevalues rv INNER JOIN groceryitems g ON rv.grocery_id = g.grocery_id INNER JOIN grocerycategories gc ON g.grocery_section = gc.category_id WHERE rv.recipe_id = '" + recipe_id + "'";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            ListItem newItem = new ListItem(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
            newItem.setQuantity(c.getInt(4));
            returnList.add(newItem);
        }
        return returnList;
    }

    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> returnList = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT * FROM recipe";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            returnList.add(new Recipe(c.getInt(0), c.getString(1)));
        }
        return returnList;
    }

    //test stuff
    public void selectData(Context context) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from lists", null);
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("List Name: " + res.getString(1) + "\n\n");
        }

        showMessage("Data", buffer.toString(), context);
    }
    public void showMessage(String title,String Message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
