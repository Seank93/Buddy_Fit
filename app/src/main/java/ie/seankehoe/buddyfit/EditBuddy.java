package ie.seankehoe.buddyfit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class EditBuddy extends AppCompatActivity {

    DatabaseHelper myDb;
    ArrayList<Integer> hairList = new ArrayList<>();
    ArrayList<Integer> bodyList = new ArrayList<>();
    ArrayList<Integer> headList = new ArrayList<>();
    ImageView hair;
    ImageView head;
    ImageView body;
    ImageView hairleft;
    ImageView hairright;
    ImageView headleft;
    ImageView headRight;
    ImageView bodyLeft;
    ImageView bodyRight;

    int hairvar;
    int bodyvar;
    int headvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buddy);
        myDb = new DatabaseHelper(this);
        CharacterStyles style = new CharacterStyles();
        int unlocks = Integer.parseInt(getStage());
        hairList = style.populateHair(unlocks);
        bodyList = style.populateBody();
        headList = style.populateHead();
        setLook();

    }
    public void goToProfile(View view) {
        Intent startNewActivity = new Intent(this, StatisticsActivity.class);
        startActivity(startNewActivity);
    }
    public void saveToProfile(View view) {
        myDb.setNewLook(hairvar,bodyvar,headvar);
        Intent startNewActivity = new Intent(this, StatisticsActivity.class);
        startActivity(startNewActivity);
    }

    public void setLook(){
        hair = (ImageView) findViewById(R.id.hair);
        body = (ImageView) findViewById(R.id.body);
        head = (ImageView) findViewById(R.id.head);
        String hairvalue = getHair();
        String bodyvalue = getBody();
        String headvalue = getHead();
        hairvar = Integer.parseInt(hairvalue);
        bodyvar = Integer.parseInt(bodyvalue);
        headvar = Integer.parseInt(headvalue);
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

    public void hairleft(View view){
        hairvar = hairvar - 1;
        if(hairvar == -1){
            hairvar = hairList.size()-1;
            hair.setImageResource(hairList.get(hairvar));
        }
        else {
            hair.setImageResource(hairList.get(hairvar));
        }

    }
    public void hairright(View view){
        hairvar = hairvar + 1;
        if(hairvar >= hairList.size()){
            hairvar = 0;
            hair.setImageResource(hairList.get(hairvar));
        }
        else {
            hair.setImageResource(hairList.get(hairvar));
        }
    }
    public void bodyleft(View view){
        bodyvar = bodyvar - 1;
        if(bodyvar == -1){
            bodyvar = bodyList.size()-1;
            body.setImageResource(bodyList.get(bodyvar));
        }
        else {
            body.setImageResource(bodyList.get(bodyvar));
        }
    }
    public void bodyright(View view){
        bodyvar = bodyvar + 1;
        if(bodyvar >= bodyList.size()){
            bodyvar = 0;
            body.setImageResource(bodyList.get(bodyvar));
        }
        else {
            body.setImageResource(bodyList.get(bodyvar));
        }
    }
    public void headleft(View view){
        headvar = headvar - 1;
        if(headvar == -1){
            headvar = headList.size()-1;
            head.setImageResource(headList.get(headvar));
        }
        else {
           head.setImageResource(headList.get(headvar));
        }
    }
    public void headright(View view){
        headvar = headvar + 1;
        if(headvar >= headList.size()){
            headvar = 0;
            head.setImageResource(headList.get(headvar));
        }
        else {
            head.setImageResource(headList.get(headvar));
        }
    }
    public String getStage() {
        String StageString = "100";
        Cursor stagecursor = myDb.getStage();
        if (stagecursor.moveToFirst()) {
            do {
                StageString = stagecursor.getString(stagecursor.getColumnIndex("currentstage"));
            } while (stagecursor.moveToNext());
        }
        stagecursor.close();
        return StageString;
    }
}
