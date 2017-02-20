package ie.seankehoe.buddyfit;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    DatabaseHelper myDb;
    private GoogleApiClient client;
    private TextView totalSteps;
    private TextView totalFloors;
    private TextView totalCalories,playerLevel;
    private DailyStats mCurrentStats;
    ImageView imageViewAlly;
    public static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDb = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks for user acount on Startup
        Cursor result = myDb.getTableData();
        if(result.getCount() ==0) {
            showMessage("No Profile Found", "Lets Set you up with one!");
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }

        Button btnStats = (Button) findViewById(R.id.btnStats);
        totalSteps = (TextView)findViewById(R.id.JsonItem);
        totalFloors = (TextView) findViewById(R.id.floorsNumber);
        totalCalories = (TextView) findViewById(R.id.caloriesNumber);
        imageViewAlly = (ImageView) findViewById(R.id.imageViewAlly);
        imageViewAlly.setImageResource(R.drawable.char_1);
        playerLevel = (TextView) findViewById(R.id.playerLevelText);
        genderCheck();
        levelCheck();

        //Music
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.eightbitadventure);

        Button play = (Button) findViewById(R.id.buttonsoundon);
        Button stop = (Button) findViewById(R.id.buttonsoundoff);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });
        //music




        //requests
        String fitUrl = "https://api.fitbit.com/1/user/-/activities/date/today.json";
        String fitUrlHeart = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json";

        /**final Request requestStats = new Request.Builder()
                .url(fitUrl)
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0REY0Tk0iLCJhdWQiOiIyMjdXTVkiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJzZXQgcmFjdCBybG9jIHJ3ZWkgcmhyIHJudXQgcnBybyByc2xlIiwiZXhwIjoxNDg1NTIyNTk2LCJpYXQiOjE0ODQ5MTc3OTZ9.7_j3Ao7hk1NsGI-1cpC0xG-kt4CyvT_4Nff_ZfaMicg")
                .build();

        final Request requestHeart = new Request.Builder()
                .url(fitUrlHeart)
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0REY0Tk0iLCJhdWQiOiIyMjdXTVkiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJzZXQgcmFjdCBybG9jIHJ3ZWkgcmhyIHJudXQgcnBybyByc2xlIiwiZXhwIjoxNDg1NTIyNTk2LCJpYXQiOjE0ODQ5MTc3OTZ9.7_j3Ao7hk1NsGI-1cpC0xG-kt4CyvT_4Nff_ZfaMicg")
                .build();

        getStats(requestStats);
        getStats(requestHeart);
        btnStats.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getStats(requestStats);
                getStats(requestHeart);
            }
        }); **/
        //End of Requests
    }



    private void getStats(Request request) {

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    if(response.isSuccessful()){
                        mCurrentStats = getCurrentStats(jsonData);
                        Log.v(TAG,response.body().string());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateStats();
                            }
                        });

                    }
                } catch (IOException e) {
                   Log.e(TAG, "Exception", e);
                }
                catch (JSONException e){
                    Log.e(TAG, "Exception",e);
                }
            }

        });
    }

    private void updateStats() {
        totalSteps.setText(mCurrentStats.getSteps()+"");
        totalFloors.setText(mCurrentStats.getFloors()+"");
        totalCalories.setText(mCurrentStats.getCalories()+"");

    }

    private DailyStats getCurrentStats(String jsonData) throws JSONException {
        JSONObject summarystats = new JSONObject(jsonData);
        JSONObject summary = summarystats.getJSONObject("summary");
        DailyStats dailyStats = new DailyStats();
        dailyStats.setSteps(summary.getInt("steps"));
        dailyStats.setCalories(summary.getInt("caloriesOut"));
        dailyStats.setFloors(summary.getInt("floors"));
        return dailyStats;
    }

    public void fitAuth(View view) {
        String url = "https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=227WMY&redirect_uri=http%3A%2F%2Fwww.seankehoe.ie%2Fbfit&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void goToProfile(View view){
        Intent startNewActivity = new Intent(this, ProfileActivity.class);
        startActivity(startNewActivity);
    }
    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    public void genderCheck(){
        String gender = "Female";
        Cursor result = myDb.getGender();
        imageViewAlly = (ImageView) findViewById(R.id.imageViewAlly);

        if(result.moveToFirst()){
            do{
                gender = result.getString(result.getColumnIndex("gender"));
            }while (result.moveToNext());
        }
        result.close();
        if( gender == "Female") {
            imageViewAlly.setImageResource(R.drawable.char_1);
        }
        else
        {
            imageViewAlly.setImageResource(R.drawable.char_3);
        }
    }
    private void levelCheck(){
        String level = "10";
        Cursor result = myDb.getLevel();
        imageViewAlly = (ImageView) findViewById(R.id.imageViewAlly);

        if(result.moveToFirst()){
            do{
                level = result.getString(result.getColumnIndex("level"));
            }while (result.moveToNext());
        }
        result.close();
        playerLevel = (TextView)findViewById(R.id.playerLevelText);
        playerLevel.setText(level);
     }

    protected void onResume(){
        super.onResume();
        Cursor result = myDb.getTableData();
        if(result.getCount() ==0) {
            showMessage("No Profile Found", "Lets Set you up with one!");
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        genderCheck();
    }
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "On Stop .....");
    }

};




