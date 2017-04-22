package ie.seankehoe.buddyfit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sean Kehoe on 20/02/2017.
 */

public class StatisticsActivity extends AppCompatActivity{
    DatabaseHelper myDb;
    Button delete_btn;
    Button return_btn;
    ImageView hair;
    ImageView head;
    ImageView body;
    int profileCount = 1;
    ArrayList<Integer> hairList = new ArrayList<>();
    ArrayList<Integer> bodyList = new ArrayList<>();
    ArrayList<Integer> headList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        myDb = new DatabaseHelper(this);
        CharacterStyles style = new CharacterStyles();
        hairList = style.populateHair();
        bodyList = style.populateBody();
        headList = style.populateHead();
        setLook();
        delete_btn = (Button)findViewById(R.id.delete_btn);
        return_btn = (Button)findViewById(R.id.return_btn);

    }

    public void deleteProfile(View view){

        myDb.deleteData();
        Toast.makeText(StatisticsActivity.this, "Profile Removed and Reset", Toast.LENGTH_LONG).show();

    }
    public void returnToMain(View view){
        Intent startNewActivity = new Intent(this, MainActivity.class);
        startActivity(startNewActivity);
    }

    public void setLook(){
        hair = (ImageView) findViewById(R.id.buddyhair);
        body = (ImageView) findViewById(R.id.buddybody);
        head = (ImageView) findViewById(R.id.buddyhead);
        String hairvalue = getHair();
        String bodyvalue = getBody();
        String headvalue = getHead();
        int hairvar = Integer.parseInt(hairvalue);
        int bodyvar = Integer.parseInt(bodyvalue);
        int headvar = Integer.parseInt(headvalue);
        hair.setImageResource(hairList.get(hairvar));
        body.setImageResource(bodyList.get(bodyvar));
        head.setImageResource(headList.get(headvar));
    }
    public String getHair(){
        String hair = "10";
        Cursor bodycursor = myDb.getBody();
        if(bodycursor.moveToFirst()){
            do{
                hair = bodycursor.getString(bodycursor.getColumnIndex("idHead"));
            }while (bodycursor.moveToNext());
        }
        bodycursor.close();
        return hair;
    }
    public String getHead(){
        String head = "10";
        Cursor bodycursor = myDb.getBody();
        if(bodycursor.moveToFirst()){
            do{
                head = bodycursor.getString(bodycursor.getColumnIndex("idBody"));
            }while (bodycursor.moveToNext());
        }
        bodycursor.close();
        return head;
    }
    public String getBody(){
        String body = "10";
        Cursor bodycursor = myDb.getBody();
        if(bodycursor.moveToFirst()){
            do{
                body = bodycursor.getString(bodycursor.getColumnIndex("idLegs"));
            }while (bodycursor.moveToNext());
        }
        bodycursor.close();
        return body;
    }

    public void goToEdit(View view) {
        Intent startNewActivity = new Intent(this, EditBuddy.class);
        startActivity(startNewActivity);
    }

}
