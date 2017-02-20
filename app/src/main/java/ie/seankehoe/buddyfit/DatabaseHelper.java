package ie.seankehoe.buddyfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by Sean Kehoe on 22/01/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "buddyfit.db";
    public static final String TABLE_NAME = "user_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "username";
    public static final String COL_3 = "level";
    public static final String COL_4 = "age";
    public static final String COL_5 = "total_steps";
    public static final String COL_6 = "gender";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, level INTEGER DEFAULT 1, age INTEGER, total_steps INTEGER, gender TEXT DEFAULT 'Female')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String username, int age, String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,username);
        contentValues.put(COL_4,age);
        contentValues.put(COL_6,gender);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;

        }
    }
    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[]{id});
    }

    public Cursor getTableData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_NAME,null);
        return result;
    }
    public Cursor getGender(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select gender from "+TABLE_NAME,null);
        return result;
    }
    public Cursor getLevel(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select level from "+TABLE_NAME,null);
        return result;
    }
}
