package ie.seankehoe.buddyfit;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
DatabaseHelper myDb;
    EditText editUsername, editAge, editTextId;
    RadioGroup editGender;
    RadioButton genderButton;
    Button btnAddProfile;
    Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myDb = new DatabaseHelper(this);

        editUsername = (EditText)findViewById(R.id.editText_username);
        editAge = (EditText)findViewById(R.id.editText_age);
        editGender = (RadioGroup)findViewById(R.id.editGender);


        getProfile();
    }
   /** public void deleteProfile(View view){
        Integer deletedRows = myDb.deleteData(String.valueOf(1));
        if(deletedRows > 0){
            Toast.makeText(ProfileActivity.this, "Profile Removed", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(ProfileActivity.this, "Error, Not Removed", Toast.LENGTH_LONG).show();
        }
    }
**/
    public void returnToMain(View view){
        Intent startNewActivity = new Intent(this, MainActivity.class);
        startActivity(startNewActivity);
    }
    public void getProfile(){
        Cursor result = myDb.getTableData();
        if(result.getCount() ==0)
        {
            showMessage("Error","No Profile Found");
            return;
        }
        else{
           StringBuffer buffer = new StringBuffer();
            while(result.moveToNext()){
                buffer.append("Id:" + result.getString(0)+"\n");
                buffer.append("Username:" + result.getString(1)+"\n");
                buffer.append("Age:" + result.getString(3)+"\n");
                buffer.append("Gender:" + result.getString(5)+"\n");

            }
            showMessage("Profile", buffer.toString());
        }

    }
    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    public void addProfile(View view){
        int selectedId = editGender.getCheckedRadioButtonId();
        genderButton = (RadioButton)findViewById(selectedId);

        boolean isInserted =  myDb.insertData(editUsername.getText().toString(), Integer.parseInt(editAge.getText().toString()), genderButton.getText().toString());
        if(isInserted == true){
            Toast.makeText(ProfileActivity.this, "Profile Created", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this, "It failed", Toast.LENGTH_SHORT).show();
                        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}
