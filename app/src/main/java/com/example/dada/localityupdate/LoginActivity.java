package com.example.dada.localityupdate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/* Created by Madhujit
 */

/* This is the Login Activity which deals with login of the app. It has two input fields i.e unsername and password where
   user will enter their username and password.
  */

public class LoginActivity extends AppCompatActivity {


    protected EditText userNAMELogin;
    protected EditText passWORDLogin;
    protected Button buttonLogin;
    protected Button buttonCreateAccLogin;
    protected ProgressDialog loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);



        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null))
        {
            Intent mainActivity = new Intent(this, MainActivity.class);
            finish();
            startActivity(mainActivity);

        }
else {
            //intialise


            userNAMELogin = (EditText) findViewById(R.id.usernameLoginActivity);
            passWORDLogin = (EditText) findViewById(R.id.passwordLoginActivity);
            buttonLogin = (Button) findViewById(R.id.loginButton);
            buttonCreateAccLogin = (Button) findViewById(R.id.createAccButton);


            //Listen when mLogin is clicked!!!
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //get the user inputs and convert them to a string

                    String username = userNAMELogin.getText().toString().trim();
                    String password = passWORDLogin.getText().toString().trim();

                    loadingScreen= ProgressDialog.show(LoginActivity.this, "Loading", "Please Wait", true);

                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (e == null)
                            {
                                // Hooray! The user is logged in.
                                Toast.makeText(LoginActivity.this, "Success Welcome!", Toast.LENGTH_SHORT).show();

                                //take user to home
                                Intent takeUserToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                finish();
                                startActivity(takeUserToMainActivity);
                                loadingScreen.dismiss();
                            }
                            else
                            {
                                // Signup failed. Look at the ParseException to see what happened.

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(e.getMessage());
                                builder.setTitle("Sorry!!!");
                                builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //close the dialog
                                        dialog.dismiss();

                                        userNAMELogin.setText("");
                                        passWORDLogin.setText("");
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                                loadingScreen.dismiss();


                            }
                        }
                    });

                }
            });


            //Listen when mCreateAccount is clicked!!!
            buttonCreateAccLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Take User to Register Activity
                    Intent userToRegisterActivity = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(userToRegisterActivity);
                }
            });
        }

    }

   @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }


}

