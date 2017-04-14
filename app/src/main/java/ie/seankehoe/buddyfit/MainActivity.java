package ie.seankehoe.buddyfit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.ArrayList;

import static android.R.attr.level;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    DatabaseHelper myDb;
    private GoogleApiClient client;

    private TextView totalSteps;
    private TextView totalFloors;
    private TextView totalCalories, playerLevel, currentStageText;
    private Button deal1, deal10, deal50;
    private DailyStats mCurrentStats;
    private ProgressBar progressHP;
    public ImageView hair;
    public ImageView body;
    public ImageView head;
    protected int delay = 300;

    ImageView imageViewAlly;
    ImageView background;
    public static final String TAG = MainActivity.class.getSimpleName();

    //enemy variables
    public int currentEnemy = 3;

    TextView hpNumber;
    TextView hpMax;

    //Stage Variable
    public int currentStage = 1;
    public String currentStageStr;

    //damage Variables
    public int damageTotal = 0;

    //looks variables
    ArrayList<Integer> hairList = new ArrayList<>();
    ArrayList<Integer> bodyList = new ArrayList<>();
    ArrayList<Integer> headList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDb = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks for user acount on Startup
        Cursor result = myDb.getTableData();
        if (result.getCount() == 0) {
            showMessage("No Profile Found", "Lets Set you up with one!");
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        CharacterStyles style = new CharacterStyles();
        hairList = style.populateHair();
        bodyList = style.populateBody();
        headList = style.populateHead();

        setLook();


        Button btnStats = (Button) findViewById(R.id.btnStats);
        Button deal1 = (Button) findViewById(R.id.deal1);
        Button deal10 = (Button) findViewById(R.id.deal10);
        Button deal50 = (Button) findViewById(R.id.deal50);

        hpNumber = (TextView) findViewById(R.id.hpNumber);
        hpNumber.setText(getEnemyCurrentHp() + " / ");

        hpMax = (TextView) findViewById(R.id.hpMax);
        hpMax.setText(getEnemyCurrentMax());

        //imageViewAlly.setImageResource(R.drawable.char_1);
        currentStageText = (TextView) findViewById(R.id.current_stage);

        currentStageStr = getStage();
        currentStageText.setText(currentStageStr);
        //genderCheck();


        //Music
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.eightbitadventure);
    }

    //music

    /**
     * Button play = (Button) findViewById(R.id.buttonsoundon);
     * Button stop = (Button) findViewById(R.id.buttonsoundoff);
     * <p>
     * play.setOnClickListener(new View.OnClickListener() {
     *
     * @Override public void onClick(View v) {
     * mediaPlayer.start();
     * }
     * });
     * stop.setOnClickListener(new View.OnClickListener() {
     * @Override public void onClick(View v) {
     * mediaPlayer.pause();
     * }
     * });
     **/

    public static void unleashPower() {
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

        @Override public void onClick(View v) {
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
                    if (response.isSuccessful()) {
                        mCurrentStats = getCurrentStats(jsonData);
                        Log.v(TAG, response.body().string());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateStats();
                            }
                        });

                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception", e);
                } catch (JSONException e) {
                    Log.e(TAG, "Exception", e);
                }
            }

        });
    }

    private void updateStats() {
        totalSteps.setText(mCurrentStats.getSteps() + "");
        totalFloors.setText(mCurrentStats.getFloors() + "");
        totalCalories.setText(mCurrentStats.getCalories() + "");

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

    public void goToProfile(View view) {
        Intent startNewActivity = new Intent(this, StatisticsActivity.class);
        startActivity(startNewActivity);
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    protected void onResume() {
        super.onResume();
        Cursor result = myDb.getTableData();
        if (result.getCount() == 0) {
            showMessage("No Profile Found", "Lets Set you up with one!");
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        //genderCheck();
    }

    protected void onStop() {
        super.onStop();
        Log.i(TAG, "On Stop .....");
    }


    //Methods Involving the Updating and retrieval of the current stage
    public void updateStage() {
        int currentstage = 1234;
        String stageStr = getStage();

        currentstage = Integer.parseInt(stageStr);
        currentstage++;
        currentStageText.setText(String.valueOf(currentstage));
        myDb.setStage(currentstage);
        updateCurrentMax();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
                Intent i3 = new Intent(MainActivity.this, StageClear.class);
                startActivity(i3);
            }
        }, delay);

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

    // ***************************************************************************************

    //Methods Involving the Updating and retrieval of enemy hp and details


    public String getEnemyHpBase() {
        String hpString = "50";
        Cursor hpcursor = myDb.getEnemy(currentEnemy);
        if (hpcursor.moveToFirst()) {
            do {
                hpString = hpcursor.getString(hpcursor.getColumnIndex("baseHp"));
            } while (hpcursor.moveToNext());
        }
        hpcursor.close();
        return hpString;
    }

    public String getEnemyCurrentMax() {
        String hpString = "50";
        Cursor hpcursor = myDb.getEnemy(currentEnemy);
        if (hpcursor.moveToFirst()) {
            do {
                hpString = hpcursor.getString(hpcursor.getColumnIndex("currentMax"));
            } while (hpcursor.moveToNext());
        }
        hpcursor.close();
        return hpString;
    }

    public String getEnemyCurrentHp() {
        String hpString = "50";
        Cursor hpcursor = myDb.getEnemy(currentEnemy);
        if (hpcursor.moveToFirst()) {
            do {
                hpString = hpcursor.getString(hpcursor.getColumnIndex("currentHp"));
            } while (hpcursor.moveToNext());
        }
        hpcursor.close();
        return hpString;
    }

    public void updateCurrentMax() {
        String base = getEnemyHpBase();
        int newmax = Integer.parseInt(base);

        newmax = Integer.parseInt(getStage()) * newmax;
        hpMax.setText(String.valueOf(newmax));
        hpNumber.setText(String.valueOf(newmax));
        myDb.setNewMax(newmax, currentEnemy);
        myDb.setCurrentHp(newmax, currentEnemy);

    }

    public void updateEnemyHp(int calcdamage) {
        String enemyhp = getEnemyCurrentHp();
        int currentEnemyHp = Integer.parseInt(enemyhp);
        int currentEnemyMax = Integer.parseInt(getEnemyCurrentMax());

        currentEnemyHp = currentEnemyHp - calcdamage;

        if ((currentEnemyHp == currentEnemyMax) || (currentEnemyHp <= 0)) {

            hpNumber.setText(String.valueOf(currentEnemyHp + " / "));
            myDb.setCurrentHp(currentEnemyHp, currentEnemy);
            updateStage();
        } else {
            hpNumber.setText(String.valueOf(currentEnemyHp + " / "));
            myDb.setCurrentHp(currentEnemyHp, currentEnemy);
        }
    }

    // ****************************************************************************************
    //Methods retrieving the look of your character

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
    public String getHair() {
        String hair = "10";
        Cursor bodycursor = myDb.getBody();
        if (bodycursor.moveToFirst()) {
            do {
                hair = bodycursor.getString(bodycursor.getColumnIndex("idHead"));
            } while (bodycursor.moveToNext());
        }
        bodycursor.close();
        return hair;
    }

    public String getHead() {
        String head = "10";
        Cursor bodycursor = myDb.getBody();
        if (bodycursor.moveToFirst()) {
            do {
                head = bodycursor.getString(bodycursor.getColumnIndex("idBody"));
            } while (bodycursor.moveToNext());
        }
        bodycursor.close();
        return head;
    }

    public String getBody() {
        String body = "10";
        Cursor bodycursor = myDb.getBody();
        if (bodycursor.moveToFirst()) {
            do {
                body = bodycursor.getString(bodycursor.getColumnIndex("idLegs"));
            } while (bodycursor.moveToNext());
        }
        bodycursor.close();
        return body;
    }
    //*****************************************************************************************
    //Damage dealing test buttons
    public void deal1(View view){
        int deal1 = 1;
        updateEnemyHp(deal1);
    }
    public void deal10(View view){
        int deal10 = 10;
        updateEnemyHp(deal10);
    }
    public void deal50(View view){
        int deal50 = 50;
        updateEnemyHp(deal50);
    }

    public void continueScreen(View view){
        Intent startNewActivity = new Intent(this,StageClear.class);
        startActivity(startNewActivity);
    }


};




