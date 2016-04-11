package com.example.dada.localityupdate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/* Created by Madhujit
 */
/* This Update Activity is used to update the status by an user.
*/

public class UpdateActivity extends AppCompatActivity {


    protected EditText updateStatus;
    protected Button updateButton;
    protected String newStatus;
    private Dialog loadingScreens;
    private Spinner mSpinUpdate;
    private String areaNames;

    private String cityNames;

    private ArrayAdapter<String> bangaloreSpin;
    private ArrayAdapter<String> kolkataSpin;
    private ArrayAdapter<String> mumbaiSpin;
    private ArrayAdapter<String> chennaiSpin;
    private ArrayAdapter<String> delhiSpin;


    private String[] mBLR = {"Banshankari", "BTM Layout","Jayanagar","JP Nagar","Majestic","Kumaraswamy Layout","MG Road"};

    private String[] mMUM = {"Colaba", "Churchgate", "Andheri", "Bandra", "Dadar" };
    private String[] mCHN = {"Marina Beach","Ashok Nagar","Madipakkam","Egmore","Kottur"};

    private String[] mKOL = {"Godiya Hut", "Howrah", "Alipore", "Kalighat"};
    private String[] mDLH = {"Dwarka","Chittaranjan Park", "Karol Bagh", "Connaught Place"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        ParsePush.subscribeInBackground("Status");

        savedInstanceState = this.getIntent().getExtras();

        //Receiving the intent data from UpdateActivity

        final String cityName = savedInstanceState.getString("City");
        final String area = savedInstanceState.getString("Area");
        final String manyCityNames = savedInstanceState.getString("CityNames");
        final String manyAreaNames = savedInstanceState.getString("AreaNames");

        if(cityName != null)
               cityNames = cityName;
        else
               cityNames = manyCityNames;



        final MainActivity mn = new MainActivity();

        //Initialising the array adapter objects

        bangaloreSpin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mBLR);
        kolkataSpin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mKOL);
        mumbaiSpin =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mMUM);
        chennaiSpin =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mCHN);
        delhiSpin =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mDLH);


        //Determining the current user of the app

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {

        mSpinUpdate = (Spinner)findViewById(R.id.spinner_UpdateActivity);
        updateStatus = (EditText) findViewById(R.id.update_activity_StatusTextBox);
        updateButton = (Button) findViewById(R.id.update_activity_Button);

        final String currentUserName = currentUser.getUsername();



        //This switch case is used to display the area names of the selected city
            switch(cityNames)
            {
                case "Bangalore":
                    mSpinUpdate.setAdapter(bangaloreSpin);
                    break;
                case "Kolkata":
                    mSpinUpdate.setAdapter(kolkataSpin);
                    break;
                case "Mumbai":
                    mSpinUpdate.setAdapter(mumbaiSpin);
                    break;
                case "Chennai":
                    mSpinUpdate.setAdapter(chennaiSpin);
                    break;
                case "Delhi":
                    mSpinUpdate.setAdapter(delhiSpin);
                    break;

            }



     //Below button will update the status of user

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                newStatus = updateStatus.getText().toString();
                areaNames = mSpinUpdate.getSelectedItem().toString();
                if (newStatus.isEmpty()) {
                    //status can't be left empty
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                    builder.setMessage("Status Shouldn't be empty!!!");
                    builder.setTitle("OOPS!!!");
                    builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close the dialog
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                else
                {
                    loadingScreens= ProgressDialog.show(UpdateActivity.this, "Loading", "Please Wait...", false, true);
                    //save the status in parse
                    ParseObject statusOjbect =  ParseObject.create("Status");
                    statusOjbect.put("newStatus", newStatus);
                    statusOjbect.put("userName", currentUserName);
                    statusOjbect.put("cityName", cityNames);
                    statusOjbect.put("area", areaNames);

                    statusOjbect.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                //successfully new status in parse
                                Toast.makeText(UpdateActivity.this, "Success!!!", Toast.LENGTH_LONG).show();

                                //take user to homepage
                                Intent takeUserToMainPage = new Intent(UpdateActivity.this, Main2Activity.class);
                                takeUserToMainPage.putExtra("CityName", cityNames);//Throwing the intent data to Main2Activity
                                takeUserToMainPage.putExtra("AreaName", areaNames);//Throwing the intent data to Main2Activity
                                startActivity(takeUserToMainPage);

                                finish();


                            } else {
                                //there was a problem in storing
                                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                                builder.setMessage(e.getMessage());
                                builder.setTitle("Sorry!!!");
                                builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //close the dialog
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        }
                    });
                }


            }
        });



    }
        else
        {
            // show the signup or login screen

            Intent toLogin = new Intent(this,LoginActivity.class);
            startActivity(toLogin);
        }
    }

}
