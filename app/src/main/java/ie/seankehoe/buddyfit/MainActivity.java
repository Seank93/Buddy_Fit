package ie.seankehoe.buddyfit;
//Sean Kehoe 20/04/2017
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Random;

import static android.R.attr.level;

public class MainActivity extends AppCompatActivity {

    //Global Variables
    DatabaseHelper myDb;
    private TextView totalSteps;
    private TextView totalFloors;
    private TextView totalCalories, playerLevel, currentStageText,weathertext;
    private Button deal1, deal10, deal50;
    private DailyStats mCurrentStats;
    private WeatherStats wCurrentStats;
    public ImageView hair;
    public ImageView body;
    public ImageView head;
    public int unlocks =1;
    public ImageView enemyImage,bgImage;
    protected int delay = 300;
    public String globalWeather = "Sunny";
    public static final String TAG = MainActivity.class.getSimpleName();

    //enemy variables
    TextView hpNumber;
    TextView hpMax;

    //Stage Variable
    public int currentStage = 1;
    public String currentStageStr;

    //damage Variables
    int totalDamage =0;

    //ARRAYLISTS TO HOLD THE CURRENTSET OF VANITY ITEMS
    ArrayList<Integer> hairList = new ArrayList<>();
    ArrayList<Integer> bodyList = new ArrayList<>();
    ArrayList<Integer> headList = new ArrayList<>();
    ArrayList<Integer> enemyList = new ArrayList<>();
    ArrayList<Integer> environList = new ArrayList<>();

    @Override
    //onCreate is ran on opening the activity. Sets up the screen
    //and allows functions to be carried out correctly

    protected void onCreate(Bundle savedInstanceState) {
        myDb = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks for user acount on Startup if none, create a profile
        Cursor result = myDb.getTableData();
        if (result.getCount() == 0) {
            showMessage("No Profile Found", "Lets Set you up with one!");
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        //Deals with loading correct stage
        currentStageText = (TextView) findViewById(R.id.current_stage);
        weathertext = (TextView) findViewById(R.id.weathertext);

        currentStageStr = getStage();
        int currentstageInt = Integer.parseInt(currentStageStr);
        currentStageText.setText(currentStageStr);


        //Populate character/enemy/background styles
        bgImage = (ImageView) findViewById(R.id.bgImg);
        CharacterStyles style = new CharacterStyles();
        hairList = style.populateHair(currentstageInt);
        bodyList = style.populateBody();
        headList = style.populateHead();
        enemyList = style.populateEnemies();
        environList = style.populateEnvirons();

        //Set Player character/background to look currently determined by database.
        setLook();
        setBackground();
        int curEnemy = setEnemyModel();

        //Test + debug buttons (Not Used in Final Version)
        Button deal1 = (Button) findViewById(R.id.deal1);
        Button deal10 = (Button) findViewById(R.id.deal10);
        Button deal50 = (Button) findViewById(R.id.deal50);

        //Declaring current enemy HP
        hpNumber = (TextView) findViewById(R.id.hpNumber);
        hpNumber.setText(getEnemyCurrentHp(curEnemy) + " /");

        //Declaring current max hp
        hpMax = (TextView) findViewById(R.id.hpMax);
        hpMax.setText(getEnemyCurrentMax(curEnemy));

        //imageViewAlly.setImageResource(R.drawable.char_1);



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

    //How the user progresses in the game.
    public void unleashPower(View view) {
        //requests

        String fitUrlStep = "https://api.fitbit.com/1/user/-/activities/date/today.json";
       // String fitUrlDistance = "https://api.fitbit.com/1/user/-/activities/distance/today/1d.json";
        //String fitUrlFloors = "https://api.fitbit.com/1/user/-/activities/floors/today/1d.json";


        final Request requestStats = new Request.Builder()
         .url(fitUrlStep)
         .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0REY0Tk0iLCJhdWQiOiIyMjdXTVkiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJzZXQgcmFjdCBybG9jIHJ3ZWkgcmhyIHJudXQgcnBybyByc2xlIiwiZXhwIjoxNTI1MjkwNTc0LCJpYXQiOjE0OTM3NTY4MDZ9.CPSnLTwaKluborwFiJf3vRKMi0lOxidIT7S682WQcSA")
         .build();

        getStats(requestStats);

        Toast.makeText(MainActivity.this, "Retrieving Your Stats!", Toast.LENGTH_SHORT).show();
    }

    //Retrieval of stats
    public void getStats(Request request) {

        //Request Weather and Heart Data Before finishing this
        String lat = "52.2462";
        String longi = "7.1402";
        String weatherRequest = "https://api.darksky.net/forecast/e5aff2c280012546662f72c85823b0cd/"+lat+","+longi+"";
        String fitUrlHeart = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json";

        final Request requestWeather = new Request.Builder()
                .url(weatherRequest)
                .build();

       /** final Request requestHeart = new Request.Builder()
                .url(fitUrlHeart)
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0REY0Tk0iLCJhdWQiOiIyMjdXTVkiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJzZXQgcmFjdCBybG9jIHJ3ZWkgcmhyIHJudXQgcnBybyByc2xlIiwiZXhwIjoxNTI1MjkwNTc0LCJpYXQiOjE0OTM3NTY4MDZ9.CPSnLTwaKluborwFiJf3vRKMi0lOxidIT7S682WQcSA")
                .build();
**/
      //  getHeartStats(requestHeart);
        getWeatherStats(requestWeather);

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

    public void getWeatherStats(Request request) {

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
                        wCurrentStats = getWeatherStats(jsonData);
                        Log.v(TAG, response.body().string());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateWeather();
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

    public void getHeartStats(Request request){

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
                        mCurrentStats = getHeartRateStats(jsonData);
                        Log.v(TAG, response.body().string());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateHeart();
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
        int steps = mCurrentStats.getSteps();
        int floors = mCurrentStats.getFloors();
        int distance = mCurrentStats.getDistance();
        int calories = mCurrentStats.getCalories();

        calculateStats(steps,floors,distance,calories);

    }
    private void updateHeart() {
        int resting = mCurrentStats.getHeartRateRest();
        int floors = mCurrentStats.getFloors();
        int distance = mCurrentStats.getDistance();
        int calories = mCurrentStats.getCalories();
        Toast.makeText(MainActivity.this, "Total Rest Mins: "+ resting, Toast.LENGTH_SHORT).show();

    }

    private void updateWeather() {
        String currentWeather = wCurrentStats.getWeather();
        globalWeather = currentWeather;
        weathertext.setText(currentWeather);
    }

    private void calculateStats(int steps,int floors,int distance,int calories){

        //Calculation of damagae.
        // Steps are worth 1:1 multiplied by step strength
        //Stairs are worth 5 damgae points per flight of stairs
        // Calories are worth 10% of what the daily total would be
        // (These are working figures and can change easily based on range of data taken)
        Toast.makeText(MainActivity.this, "GlobalWeather ::"+ globalWeather, Toast.LENGTH_SHORT).show();
        int bonuspercent = 0;
        String StepStrength = getStepSTR();
        int stepStrenghtInt = Integer.parseInt(StepStrength);

        int totalDamageRound1 = steps * stepStrenghtInt;
        int totalDamageRound2 = totalDamageRound1 + (floors*5);
        double totalDamageRound3 = totalDamageRound2 + (calories/10);
        totalDamage = (int) totalDamageRound3;

        if(globalWeather.equalsIgnoreCase("Drizzle"))
        {
            totalDamage = (int) (totalDamage*1.2);
            bonuspercent = 20;
        }
        else if(globalWeather.equalsIgnoreCase("Overcast"))
        {
            totalDamage = (int) (totalDamage*1.4);
            bonuspercent = 40;
        }
        else if(globalWeather.equalsIgnoreCase("Rain"))
        {
            totalDamage = (int) (totalDamage*1.5);
            bonuspercent = 50;
        }
        else if(globalWeather.equalsIgnoreCase("Light Rain"))
        {
            totalDamage = (int) (totalDamage*1.4);
            bonuspercent = 40;
        }
        else if(globalWeather.equalsIgnoreCase("Sun"))
        {
            totalDamage = (int) (totalDamage*1.1);
            bonuspercent = 10;
        }
        else if(globalWeather.equalsIgnoreCase("Cloudy"))
        {
            totalDamage = (int) (totalDamage*1.15);
            bonuspercent = 15;
        }
        else if(globalWeather.equalsIgnoreCase("Partly Cloudy"))
        {
            totalDamage = (int) (totalDamage*1.10);
            bonuspercent = 10;
        }
        else if(globalWeather.equalsIgnoreCase("Mostly Cloudy"))
        {
            totalDamage = (int) (totalDamage*1.2);
            bonuspercent = 20;
        }
        else if(globalWeather.equalsIgnoreCase("Clear"))
        {
            totalDamage = (int) (totalDamage*1.05);
            bonuspercent = 5;
        }

        showOption("Here is the Damage","Steps: "+steps + " (Step Strength: " + stepStrenghtInt + ")" +'\n' + "Calories: " + calories/10 +'\n'+"Flights of Stairs: " + floors*5 +'\n' + "Distance: " +distance + '\n' + "Weather Bonus: " + globalWeather + "(Bonus: " +bonuspercent+"%)"+ '\n' +
        "------------------" + '\n' + "Total Damage: " + totalDamage);


    }

    //only retrieves json relating to steps/distance/floors
    private DailyStats getCurrentStats(String jsonData) throws JSONException {
        JSONObject summarystats = new JSONObject(jsonData);
        JSONObject summary = summarystats.getJSONObject("summary");
        DailyStats dailyStats = new DailyStats();
        dailyStats.setSteps(summary.getInt("steps"));
        dailyStats.setCalories(summary.getInt("caloriesOut"));
        dailyStats.setFloors(summary.getInt("floors"));
        return dailyStats;
    }
    //gets data only relating to heart zones.
    private DailyStats getHeartRateStats(String jsonData) throws JSONException{

        int res1 = 0;
        int res2 = 0;
        int res3 = 0;
        int res4 = 0;
        DailyStats heartStats = new DailyStats();

        JSONObject heartstats = new JSONObject(jsonData);
        JSONArray heartsummary = heartstats.getJSONArray("activities-heart");

        for(int j=0;j<heartsummary.length();j++){
            JSONObject heartData = heartsummary.getJSONObject(j);
            JSONObject heartValues = heartData.getJSONObject("value");
            JSONArray heartareas = heartValues.getJSONArray("heartRateZones");

            for(int i = 0; i < 4; i++) {
                if(i == 0) {
                    res1 = heartareas.getJSONObject(i).getInt("minutes");
                }
                else if(i==1){
                    res2 = heartareas.getJSONObject(i).getInt("minutes");
                }
                else if(i==2){
                    res3 = heartareas.getJSONObject(i).getInt("minutes");
                }
                else if(i==3){
                    res4 = heartareas.getJSONObject(i).getInt("minutes");
                }
            }
        }
        //Sets the heart rates and how long you spent in them
        heartStats.setHeartRateRest(res1);
        heartStats.setHeartRatefatburn(res2);
        heartStats.setHeartRatecardio(res3);
        heartStats.setHeartRatePeak(res4);
        Toast.makeText(MainActivity.this, "Heart Zone 1 "+ res1, Toast.LENGTH_SHORT).show();

        return heartStats;
    }
    private WeatherStats getWeatherStats(String jsonData) throws JSONException {
        JSONObject weatherstats = new JSONObject(jsonData);
        JSONObject currentWeather = weatherstats.getJSONObject("currently");
        WeatherStats weatherstat = new WeatherStats();
        weatherstat.setWeather(currentWeather.getString("summary"));
        return weatherstat;
    }
    //Authorise your fitbit account
    public void fitAuth(View view) {
        String url = "https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=227WMY&redirect_uri=http%3A%2F%2Fwww.seankehoe.ie%2Fbfit&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //Misc Methods - Changing Activities
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

    public void showOption(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message)
                .setPositiveButton("ATTACK!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle Ok
                        updateEnemyHp(totalDamage);
                    }
                })
                .setNegativeButton("Wait! Not Yet!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle Cancel
                    }
                })
                .create();
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
        CharacterStyles stageupdate = new CharacterStyles();
        enemyImage = (ImageView) findViewById(R.id.currentenemy);

        int currentstage = 1234;
        String stageStr = getStage();

        currentstage = Integer.parseInt(stageStr);

        //Randomly loading a new enemy to display on the next stage.
        Random r = new Random();
        int gen = r.nextInt(7 - 1) + 1;
        String currentenemy = getCurrentEnemy();
        int currentEnemyInt = Integer.parseInt(currentenemy);
        currentEnemyInt = gen;
        enemyImage.setImageResource(enemyList.get(gen));
        myDb.setCurrentEnemy(currentEnemyInt);

        //change the stage background
        setBackground();

        currentstage++;
        currentStageText.setText(String.valueOf(currentstage));
        myDb.setStage(currentstage);
        stageupdate.setLocalStage(currentstage);

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

    public String getCurrentEnemy() {
        String id = "4";
        Cursor idcursor = myDb.getStage();
        if (idcursor.moveToFirst()) {
            do {
                id = idcursor.getString(idcursor.getColumnIndex("currentenemy"));
            } while (idcursor.moveToNext());
        }
        idcursor.close();
        return id;
    }

    public String getEnemyHpBase() {
        String currentenemy = getCurrentEnemy();
        int currentEnemyInt = Integer.parseInt(currentenemy);
        String hpString = "10";
        Cursor hpcursor = myDb.getEnemy(currentEnemyInt);
        if (hpcursor.moveToFirst()) {
            do {
                hpString = hpcursor.getString(hpcursor.getColumnIndex("baseHp"));
            } while (hpcursor.moveToNext());
        }
        hpcursor.close();
        return hpString;
    }

    public String getEnemyCurrentMax(int current) {
        String hpString = "50";
        Cursor hpcursor = myDb.getEnemy(current);
        if (hpcursor.moveToFirst()) {
            do {
                hpString = hpcursor.getString(hpcursor.getColumnIndex("currentMax"));
            } while (hpcursor.moveToNext());
        }
        hpcursor.close();
        return hpString;
    }

    public String getEnemyCurrentHp(int current) {

        String hpString = "50";
        Cursor hpcursor = myDb.getEnemy(current);
        if (hpcursor.moveToFirst()) {
            do {
                hpString = hpcursor.getString(hpcursor.getColumnIndex("currentHp"));
            } while (hpcursor.moveToNext());
        }
        hpcursor.close();
        return hpString;
    }

    public void updateCurrentMax() {
        String currentenemy = getCurrentEnemy();
        int currentEnemyInt = Integer.parseInt(currentenemy);
        String base = getEnemyHpBase();

        int newmax = Integer.parseInt(base);

        newmax = Integer.parseInt(getStage()) * newmax;

        hpMax.setText(String.valueOf(newmax));
        hpNumber.setText(String.valueOf(newmax));
        myDb.setNewMax(newmax, currentEnemyInt);
        myDb.setCurrentHp(newmax, currentEnemyInt);

    }

    public void updateEnemyHp(int calcdamage) {
        String currentenemy = getCurrentEnemy();
        int currentEnemyInt = Integer.parseInt(currentenemy);
        String enemyhp = getEnemyCurrentHp(currentEnemyInt);


        int currentEnemyHp = Integer.parseInt(enemyhp);
        int currentEnemyMax = Integer.parseInt(getEnemyCurrentMax(currentEnemyInt));

        currentEnemyHp = currentEnemyHp - calcdamage;

        if ((currentEnemyHp == currentEnemyMax) || (currentEnemyHp <= 0)) {

            hpNumber.setText(String.valueOf(currentEnemyHp + " / "));
            myDb.setCurrentHp(currentEnemyHp, currentEnemyInt);
            updateStage();
        } else {
            hpNumber.setText(String.valueOf(currentEnemyHp + " / "));
            myDb.setCurrentHp(currentEnemyHp, currentEnemyInt);
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
    public void setBackground(){
        String stageCount = getStage();
        int stagecount = Integer.parseInt(stageCount);


        int loadstage = stagecount % 3;

        bgImage.setImageResource(environList.get(loadstage));
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
    public String getStepSTR() {
        String str = "10";
        Cursor strcursor = myDb.getBody();
        if (strcursor.moveToFirst()) {
            do {
                str = strcursor.getString(strcursor.getColumnIndex("stepSTR"));
            } while (strcursor.moveToNext());
        }
        strcursor.close();
        return str;
    }
    public int setEnemyModel(){
        String enemy = getCurrentEnemy();
        int enemyint = Integer.parseInt(enemy);
        enemyImage = (ImageView) findViewById(R.id.currentenemy);
        enemyImage.setImageResource(enemyList.get(enemyint));
        return enemyint;
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
        int deal50 = 150;
        updateEnemyHp(deal50);
    }

    public void continueScreen(View view){
        Intent startNewActivity = new Intent(this,StageClear.class);
        startActivity(startNewActivity);
    }


};




