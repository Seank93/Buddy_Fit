package ie.seankehoe.buddyfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Sean Kehoe on 20/02/2017.
 */

public class StatisticsActivity extends AppCompatActivity{
    DatabaseHelper myDb;
    Button delete_btn;
    Button return_btn;
    int profileCount = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        myDb = new DatabaseHelper(this);

        delete_btn = (Button)findViewById(R.id.delete_btn);
        return_btn = (Button)findViewById(R.id.return_btn);

    }

    public void deleteProfile(View view){
        Integer deletedRows = myDb.deleteData(String.valueOf(profileCount));
        if(deletedRows > 0){
            Toast.makeText(StatisticsActivity.this, "Profile Removed", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(StatisticsActivity.this, "Error, Not Removed", Toast.LENGTH_LONG).show();
        }
        profileCount++;
    }
    public void returnToMain(View view){
        Intent startNewActivity = new Intent(this, MainActivity.class);
        startActivity(startNewActivity);
    }
}
