package database;
import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "shoplisterDB.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
/*
    //Create Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        String sqlCommand = "CREATE TABLE IF NOT EXISTS lists (" +
                "list_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "list_name TEXT)";

        String sqlCommand2 = "CREATE TABLE IF NOT EXISTS listvalues (" +
                "value_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "list_id INTEGER," +
                "grocery_id INTEGER," +
                "FOREIGN KEY(list_id) REFERENCES lists(list_id)," +
                "FOREIGN KEY(grocery_id) REFERENCES groceryitems(grocery_id))";
        
        String sqlCommand3 = "CREATE TABLE IF NOT EXISTS groceryitems (" +
                "grocery_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "grocery_name TEXT," +
                "grocery_price INTEGER)";

        String sqlCommand4 = "CREATE TABLE IF NOT EXISTS simplelists (" +
                "simplelist_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "simplelist_name TEXT," +
                "simplelist_content TEXT)";

        String sqlCommandInsert1 = "INSERT INTO simplelists (simplelist_name, simplelist_content)" +
                "VALUES ('testlist', 'supergreattestlist')";

        db.execSQL(sqlCommand);
        db.execSQL(sqlCommand3);
        db.execSQL(sqlCommand2);
        db.execSQL(sqlCommand4);
        db.execSQL(sqlCommandInsert1);

    }

    //Upgrade Database

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropcommand1 = "DROP TABLE listvalues";
        String dropcommand2 = "DROP TABLE groceryitems";
        String dropcommand3 = "DROP TABLE lists";
        String dropcommand4 = "DROP TABLE simplelists";

        db.execSQL(dropcommand1);
        db.execSQL(dropcommand2);
        db.execSQL(dropcommand3);
        db.execSQL(dropcommand4);

        onCreate(db);
    }
*/
}
