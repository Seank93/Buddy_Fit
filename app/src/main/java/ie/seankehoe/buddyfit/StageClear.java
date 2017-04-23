package ie.seankehoe.buddyfit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StageClear extends AppCompatActivity {

    DatabaseHelper myDb;
    public String currentStageStr;
    TextView stage_text,unlocktext;
    Button continue_btn;
    ImageView imgunlock;
    ArrayList<Integer> hairList = new ArrayList<>();
    int hair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_clear);

        myDb = new DatabaseHelper(this);
        imgunlock = (ImageView) findViewById(R.id.imgunlock);
        unlocktext = (TextView) findViewById(R.id.unlocktext);
        continue_btn = (Button) findViewById(R.id.btn_continue);
        stage_text = (TextView) findViewById(R.id.stage_text);

        String currentstage = getStage();
        int stage = Integer.parseInt(currentstage);
        CharacterStyles style = new CharacterStyles();
        style.setLocalStage(stage);
        hairList = style.populateHair();

        stage_text.setText(currentstage);



        if(stage == 5){

            imgunlock.setVisibility(View.VISIBLE);
            unlocktext.setVisibility(View.VISIBLE);
            imgunlock.setImageResource(hairList.get(5));
            hair++;

        }
        else if(stage == 10){

            imgunlock.setVisibility(View.VISIBLE);
            unlocktext.setVisibility(View.VISIBLE);
            imgunlock.setImageResource(hairList.get(6));
            hair++;

        }
        else{
            unlocktext.setVisibility(View.INVISIBLE);
            imgunlock.setVisibility(View.INVISIBLE);
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

    public void gameContinue(View view){
        Intent startNewActivity = new Intent(this, MainActivity.class);
        startActivity(startNewActivity);
    }


}
