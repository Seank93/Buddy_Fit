package ie.seankehoe.buddyfit;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StageClear extends AppCompatActivity {

    DatabaseHelper myDb;
    public String currentStageStr;
    TextView stage_text;
    Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDb = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_clear);

        continue_btn = (Button) findViewById(R.id.btn_continue);
        stage_text = (TextView) findViewById(R.id.stage_text);
        String currentstage = getStage();
        int stage = Integer.parseInt(currentstage);
        stage_text.setText(currentstage);
        //String currentStage = getStage();
        //stage_text.setText(currentStage);
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
