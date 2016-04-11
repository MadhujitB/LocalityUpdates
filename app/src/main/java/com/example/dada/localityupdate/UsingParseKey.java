package com.example.dada.localityupdate;

import android.app.Application;


import com.parse.Parse;
import com.parse.ParseInstallation;


/**
 * Created by Madhujit.
 */

/*This class extends the Application class and it is main usage is to initialise the Parse keys
 */

public class UsingParseKey extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "Your Parse APP-ID", "Your Parse Client Key");

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
