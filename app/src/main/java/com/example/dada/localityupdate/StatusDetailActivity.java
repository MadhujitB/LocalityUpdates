package com.example.dada.localityupdate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;



/* Created by Madhujit
 */
/* This Status Detail Activity is used to edit the status.
*/

public class StatusDetailActivity extends AppCompatActivity {


    String objectID;
    String userNAME;
    protected EditText mStatus;
    protected Button mUpdateStatusButton;
    protected ProgressDialog loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            //intialize
            mStatus = (EditText) findViewById(R.id.objectIdThatStarted);
            mUpdateStatusButton = (Button)findViewById(R.id.update_Button);

            Intent intent = getIntent();
            objectID = intent.getStringExtra("objectId");

            Log.d("samskrut", "Current User:" + currentUser);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Status");
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {

                    if (e == null) {
                        //success
                        String userStatus = parseObject.getString("newStatus");
                        mStatus.setText(userStatus);
                        userNAME = parseObject.getString("userName");


                    } else {
                        //we have an error!!!

                    }


                }
            });

            mUpdateStatusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String updatedStatus = mStatus.getText().toString();
                    //Toast.makeText(getApplicationContext(), updatedStatus, Toast.LENGTH_LONG).show();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Status");

                             // Retrieve the object by id
                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
                        public void done(ParseObject updateStatus, ParseException e) {
                            if (e == null) {
                                // Now let's update it with some new data. In this case, only cheatMode and score
                                // will get sent to the Parse Cloud. playerName hasn't changed.
                                loadingScreen= ProgressDialog.show(StatusDetailActivity.this, "Updating", "Please Wait..", true);
                                updateStatus.put("newStatus", updatedStatus);

                                updateStatus.saveInBackground();


                                Intent toLogin = new Intent(StatusDetailActivity.this,MyComments.class);
                                finish();
                                startActivity(toLogin);

                            }
                        }
                    });
                }
            });

        }
        else
        {
            // show the signup or login screen

            Intent toLogin = new Intent(StatusDetailActivity.this,LoginActivity.class);
            finish();
            startActivity(toLogin);
        }
    }
}
