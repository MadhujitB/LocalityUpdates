package com.example.dada.localityupdate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/* Created by Madhujit
 */

/* This Registration Activity, as the name suggests, is for the new users. This activity contains four input fields which
 are username, email, password and confirm password. Once user register himself then from the next time by entering the username
 and password for authentication in order to enter the app
 */



public class RegistrationActivity extends AppCompatActivity {


    protected EditText userNAME;
    protected EditText emaIL;
    protected EditText passWORD;
    protected EditText confirm_PASSWORD;
    protected Button signUP;
    protected Button reSET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //intialize

        userNAME = (EditText) findViewById(R.id.usernameRegistrationActivity);
        emaIL = (EditText) findViewById(R.id.emailRegistrationActivity);
        passWORD = (EditText) findViewById(R.id.passwordRegistrationActivity);
        confirm_PASSWORD = (EditText) findViewById(R.id.confirm_passwordRegistrationActivity);
        signUP = (Button) findViewById(R.id.buttonSignUpRegistrationActivity);
        reSET = (Button) findViewById(R.id.buttonResetRegistrationActivity);


        //listen to register button click
        signUP.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                //get the username, email and password

                String username = userNAME.getText().toString().trim();
                String email = emaIL.getText().toString().trim();
                String password = passWORD.getText().toString().trim();
                String confirm_password = confirm_PASSWORD.getText().toString().trim();

                if (email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                    builder.setMessage("Please Provide Your Email");
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



               else if (password.equals(confirm_password)) {
                    //store user in parse

                    ParseUser user = new ParseUser();
                    user.setUsername(username);

                    user.setPassword(password);
                    user.setEmail(email);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //user signed up successfully
                                Toast.makeText(RegistrationActivity.this, "Success Welcome!", Toast.LENGTH_SHORT).show();

                                //take user to home
                                Intent takeUserHome = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(takeUserHome);
                            } else {
                                //there was an error in signing up


                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                                builder.setMessage(e.getMessage());
                                builder.setTitle("Sorry!!!");
                                builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //close the dialog
                                        dialog.dismiss();

                                        userNAME.setText("");
                                        emaIL.setText("");

                                        passWORD.setText("");
                                        confirm_PASSWORD.setText("");
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

        reSET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userNAME.setText("");
                emaIL.setText("");

                passWORD.setText("");
                confirm_PASSWORD.setText("");

            }
        });

            }
}