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
    public static final String STAGE_COL_3 = "currentenemy";
    //--------------------------------------------------
    public static final String BUDDY_COL_1 = "ID";
    public static final String BUDDY_COL_2 = "idHead";
    public static final String BUDDY_COL_3 = "idBody";
    public static final String BUDDY_COL_4 = "idLegs";
    public static final String BUDDY_COL_5 = "level";
    public static final String BUDDY_COL_6 = "stepStr";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, 12);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, age INTEGER, total_steps INTEGER, total_calories DOUBLE, gender TEXT DEFAULT 'Female')");
        db.execSQL("create table " + ENEMY_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, baseHp INTEGER, currentMax INTEGER, currentHp INTEGER, weakness TEXT)");
        db.execSQL("create table " + STAGE_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, currentstage INTEGER DEFAULT 1, currentenemy INTEGER DEFAULT 1)");
        db.execSQL("create table " + BUDDY_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, idHead INTEGER DEFAULT 1, idBody INTEGER DEFAULT 1, idLegs INTEGER DEFAULT 1, level INTEGER DEFAULT 1, stepSTR INTEGER DEFAULT 1)");

        db.execSQL("insert into " + ENEMY_TABLE + " values(1,'fatcatpink',310,310,310,'rain')");
        db.execSQL("insert into " + ENEMY_TABLE + " values(2,'fatbat',315,315,315,'sun')");
        db.execSQL("insert into " + ENEMY_TABLE + " values(3,'fatlizardred',320,320,320,'cloudy')");
        db.execSQL("insert into " + ENEMY_TABLE + " values(4,'fatcatgreen',320,320,320,'rain')");
        db.execSQL("insert into " + ENEMY_TABLE + " values(5,'fatcatblue',330,330,330,'rain')");
        db.execSQL("insert into " + ENEMY_TABLE + " values(6,'fatlizardblue',340,340,340,'rain')");
        db.execSQL("insert into " + ENEMY_TABLE + " values(7,'fatlizardgreen',345,345,345,'sun')");

        db.execSQL("insert into " + STAGE_TABLE + " values(1,1,1)");
        db.execSQL("insert into " + BUDDY_TABLE + " values(1,1,1,1,1,1)");
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

    //Reseting all modified values
    public void deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ USER_TABLE);
        ContentValues contentValuesStage = new ContentValues();
        contentValuesStage.put(STAGE_COL_2,1);
        db.update(STAGE_TABLE,contentValuesStage, "ID =1", null);

        ContentValues contentValuesBuddy = new ContentValues();
        contentValuesBuddy.put(BUDDY_COL_2,1);
        contentValuesBuddy.put(BUDDY_COL_3,1);
        contentValuesBuddy.put(BUDDY_COL_4,1);
        contentValuesBuddy.put(BUDDY_COL_5,1);
        contentValuesBuddy.put(BUDDY_COL_6,1);

        db.update(BUDDY_TABLE,contentValuesBuddy, "ID =1", null);

        ContentValues baseHp1 = new ContentValues();
        ContentValues baseHp2 = new ContentValues();
        ContentValues baseHp3 = new ContentValues();
        ContentValues baseHp4 = new ContentValues();
        ContentValues baseHp5 = new ContentValues();
        ContentValues baseHp6 = new ContentValues();
        ContentValues baseHp7 = new ContentValues();
        baseHp1.put(ENEMY_COL_3,310);
        db.update(ENEMY_TABLE,baseHp1,"ID =1",null);

        baseHp2.put(ENEMY_COL_3,315);
        db.update(ENEMY_TABLE,baseHp2,"ID =2",null);
        baseHp3.put(ENEMY_COL_3,320);
        db.update(ENEMY_TABLE,baseHp3,"ID =3",null);
        baseHp4.put(ENEMY_COL_3,320);
        db.update(ENEMY_TABLE,baseHp4,"ID =4",null);
        baseHp5.put(ENEMY_COL_3,330);
        db.update(ENEMY_TABLE,baseHp5,"ID =5",null);
        baseHp6.put(ENEMY_COL_3,340);
        db.update(ENEMY_TABLE,baseHp6,"ID =6",null);
        baseHp7.put(ENEMY_COL_3,345);
        db.update(ENEMY_TABLE,baseHp7,"ID =7",null);

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
    public Cursor getStepSTR(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select stepSTR from "+BUDDY_TABLE,null);
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
    public Cursor getBody(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+BUDDY_TABLE,null);
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
    public boolean setCurrentEnemy(int current){
        String newcurrent = Integer.toString(current);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STAGE_COL_3,newcurrent);
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
    public boolean setNewLook(int hair, int body, int head){
        String newHair = Integer.toString(hair);
        String newBody = Integer.toString(body);
        String newHead = Integer.toString(head);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUDDY_COL_2,newHair);
        contentValues.put(BUDDY_COL_3,newHead);
        contentValues.put(BUDDY_COL_4,newBody);
        long result = db.update(BUDDY_TABLE,contentValues,"ID=1", null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }




}
