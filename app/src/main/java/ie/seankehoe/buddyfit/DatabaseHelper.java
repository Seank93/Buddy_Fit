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


//DATABASE TABLE CREATION CODE - TABLE FIRST FOLLOWED BY INDIVIDUAL TABLES COLUMNS
//--------------------------------------------------
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "buddyfit.db";
    public static final String USER_TABLE = "user_table";
    public static final String ENEMY_TABLE = "enemy_table";
    public static final String STAGE_TABLE = "stage_table";
    public static final String BUDDY_TABLE = "buddy_table";
    //--------------------------------------------------
    public static final String COL_1 = "ID";
    public static final String COL_2 = "username";
    public static final String COL_4 = "age";
    public static final String COL_5 = "total_steps";
    public static final String COL_6 = "total_calories";
    public static final String COL_7 = "gender";
    //--------------------------------------------------
    public static final String ENEMY_COL_1 = "ID";
    public static final String ENEMY_COL_2 = "name";
    public static final String ENEMY_COL_3 = "baseHp";
    public static final String ENEMY_COL_4 = "currentMax";
    public static final String ENEMY_COL_5 = "currentHp";
    public static final String ENEMY_COL_6 = "weakness";
    //--------------------------------------------------
    public static final String STAGE_COL_1 = "ID";
    public static final String STAGE_COL_2 = "currentstage";
    //--------------------------------------------------
    public static final String BUDDY_COL_1 = "ID";
    public static final String BUDDY_COL_2 = "idHead";
    public static final String BUDDY_COL_3 = "idBody";
    public static final String BUDDY_COL_4 = "idLegs";
    public static final String BUDDY_COL_5 = "level";
    public static final String BUDDY_COL_6 = "stepStr";








    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, age INTEGER, total_steps INTEGER, total_calories DOUBLE, gender TEXT DEFAULT 'Female')");
        db.execSQL("create table " + ENEMY_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, baseHp INTEGER, currentMax INTEGER, currentHp INTEGER, weakness TEXT)");
        db.execSQL("create table " + STAGE_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, currentstage INTEGER DEFAULT 1)");
        db.execSQL("create table " + BUDDY_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, idhead INTEGER DEFAULT 1, idBody INTEGER DEFAULT 1, idLegs INTEGER DEFAULT 1, level INTEGER DEFAULT 1, stepSTR INTEGER DEFAULT 1)");

        db.execSQL("insert into " + ENEMY_TABLE + " values(1,'fatcat',100,100,100,'rain')");
        db.execSQL("insert into " + ENEMY_TABLE + " values(2,'fatlizard',140,140,140,'sun')");
        db.execSQL("insert into " + ENEMY_TABLE + " values(3,'fatdog',160,160,160,'cloudy')");
        db.execSQL("insert into " + ENEMY_TABLE + " values(4,'fatbat',180,180,180,'sun')");

        db.execSQL("insert into " + STAGE_TABLE + " values(1, 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ ENEMY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ STAGE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ BUDDY_TABLE);
        onCreate(db);
    }

    public boolean insertData(String username, int age, String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,username);
        contentValues.put(COL_4,age);
        contentValues.put(COL_6,gender);
        long result = db.insert(USER_TABLE,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_TABLE,"ID = ?",new String[]{id});
    }

    public Cursor getTableData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+USER_TABLE,null);
        return result;
    }
    public Cursor getGender(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select gender from "+USER_TABLE,null);
        return result;
    }
    public Cursor getLevel(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select level from "+USER_TABLE,null);
        return result;
    }

    public int getCurrentEnemyHp(int currentId){
        SQLiteDatabase db = this.getWritableDatabase();
        int number=0;
        Cursor result = db.rawQuery("select maxhp from "+ENEMY_TABLE+" where ID = " + currentId,null);
        while (result.moveToNext()) {
            number = result.getInt(0);
        }
        return number;
    }
    public int getCurrentEnemyDamage(int currentId){
        SQLiteDatabase db = this.getWritableDatabase();
        int number=0;
        Cursor result = db.rawQuery("select currenthp from "+ENEMY_TABLE+" where ID = " + currentId,null);
        while (result.moveToNext()) {
            number = result.getInt(0);
        }
        return number;
    }


    public Cursor getStage(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+STAGE_TABLE,null);
        return result;
    }

    public Cursor getEnemy(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+ENEMY_TABLE + " where id = "+id,null);
        return result;
    }
    public Cursor getEnemyHp(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select currentHp from "+ENEMY_TABLE + " where id = "+id,null);
        return result;
    }

    public boolean setStage(int current){
        String newcurrent = Integer.toString(current);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STAGE_COL_2,newcurrent);
        long result = db.update(STAGE_TABLE,contentValues, "ID =1", null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean setNewMax(int current,int id){
        String newmax = Integer.toString(current);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENEMY_COL_4,newmax);
        long result = db.update(ENEMY_TABLE,contentValues, "ID = "+id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean setCurrentHp(int current,int id){
        String newcurrent = Integer.toString(current);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENEMY_COL_5,newcurrent);
        long result = db.update(ENEMY_TABLE,contentValues, "ID = "+id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }




}
