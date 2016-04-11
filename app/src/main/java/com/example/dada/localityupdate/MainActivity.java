package com.example.dada.localityupdate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* Created by Madhujit
 */

/* This Main Activity mainly displays the items which includes username, statuses and time in each of a list view.
   This datas are fetched from Parse and with the help of an adapter, they are getting displayed.
 */



public class MainActivity extends AppCompatActivity {

    private static String receiVE;
    private ProgressDialog loadingScreen;

    protected ListView lv;
    protected List<ParseObject> mStatus;

    private String selectedArea;
    private String nameOfCities;

    private String cityChange;

    private Spinner mSpinnerChat;
    private String[] mBangalore = {"Select An Area","Banshankari", "BTM Layout","Jayanagar","JP Nagar","Majestic","Kumaraswamy Layout","MG Road"};

    private String[] mMumbai = {"Select An Area","Colaba", "Churchgate", "Andheri", "Bandra", "Dadar" };
    private String[] mChennai = {"Select An Area","Marina Beach","Ashok Nagar","Madipakkam","Egmore","Kottur"};

    private String[] mKolkata = {"Select An Area","Godiya Hut", "Howrah", "Alipore", "Kalighat"};
    private String[] mDelhi = {"Select An Area","Dwarka","Chittaranjan Park", "Karol Bagh", "Connaught Place"};

    private Button mSmallButton;
    private static String areas;

    private String cityNames;
    private String areaSelected;


   public ArrayAdapter<String> bangaloreSpinner;
   public ArrayAdapter<String> kolkataSpinner;
   public ArrayAdapter<String> mumbaiSpinner;
   public ArrayAdapter<String> chennaiSpinner;
   public ArrayAdapter<String> delhiSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        lv = (ListView) findViewById(R.id.listView_MainActivity);
        mSpinnerChat = (Spinner) findViewById(R.id.spinner_MainActivity);
        mSmallButton = (Button) findViewById(R.id.smallbutton_MainActivity);



       bangaloreSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mBangalore);
       kolkataSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mKolkata);
       mumbaiSpinner =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mMumbai);
       chennaiSpinner =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mChennai);
       delhiSpinner =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mDelhi);


        String[] s = {"Bangalore","Mumbai","Delhi","Kolkata","Chennai" };
        //The string array will display in spinner

        //Spinner will display the city names
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, s);


        AlertDialog.Builder alerts = new AlertDialog.Builder(MainActivity.this);

        final Spinner spinnerText = new Spinner(MainActivity.this);
        spinnerText.setPrompt("Select One From Below");
        spinnerText.setAdapter(adp);

        alerts.setTitle("Select Your City");
        alerts.setView(spinnerText);

        alerts.setCancelable(false);


        loadingScreen = ProgressDialog.show(this, "Loading", "Please Wait", true);

        alerts.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Below variable will store the selected spinner item
                receiVE = spinnerText.getSelectedItem().toString();

               //Current Parse user will be determined
                ParseUser currentUser = ParseUser.getCurrentUser();

                if (currentUser != null) {
                    //Here the updates will be displayed according to the selected city

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Status"); //This 'Status' is the table name in parse
                    query.whereEqualTo("cityName", receiVE);
                    query.orderByDescending("createdAt");
                    query.findInBackground(new FindCallback<ParseObject>() {

                        public void done(List<ParseObject> status, ParseException e) {
                            if (e == null) {
                                //success
                                mStatus = status;
                                StatusAdapter adapt = new StatusAdapter(MainActivity.this, mStatus);
                                lv.setAdapter(adapt);

                                loadingScreen.dismiss();

                            } else {
                                //On error
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("OOPs!!!");
                                builder.setMessage(e.getMessage());
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                } else {
                    // show the signup or login screen

                    Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(toLogin);
                }

                //The function selectedCity is called with an argument which contains the selected city name as its value
                selectedCity(receiVE);


            }
        });



        AlertDialog dialogs = alerts.create();
        dialogs.setCanceledOnTouchOutside(false);
        dialogs.show();

        SimpleDateFormat dateType = new SimpleDateFormat("M", Locale.getDefault());
        String dateTypes = dateType.format(new Date());
        Log.d("Minutes","Value Main: "+dateTypes);





    //This floating button is used for refreshing the page
                   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                   fab.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           Snackbar.make(view, "Refreshing The Page...", Snackbar.LENGTH_LONG)
                                   .setAction("Action", null).show();

                           ParseQuery<ParseObject> queries = ParseQuery.getQuery("Status"); //This 'Status' is the table name in parse

                           queries.orderByDescending("createdAt");
                           queries.findInBackground(new FindCallback<ParseObject>() {

                               public void done(List<ParseObject> statusTable, ParseException e) {
                                   if (e == null) {
                                       //success
                                       mStatus = statusTable;
                                       StatusAdapter adaptS = new StatusAdapter(MainActivity.this, statusTable);
                                       lv.setAdapter(adaptS);


                                   } else {
                                       AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                       builder.setTitle("OOPs!!!");
                                       builder.setMessage(e.getMessage());
                                       builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               dialog.dismiss();
                                           }
                                       });

                                       AlertDialog dialog = builder.create();
                                       dialog.show();
                               }
                               }
                           });

                       }
                   });
               }

    protected void selectedCity(String nameoftheCities)
    {
        Bundle bundle = getIntent().getExtras();
        final String cities = nameoftheCities;


        cityNames = nameoftheCities;

    //It will display the areas of the selected city
        switch(cities)
        {
            case "Bangalore":
                mSpinnerChat.setAdapter(bangaloreSpinner);
             break;

            case "Mumbai":
                mSpinnerChat.setAdapter(mumbaiSpinner);
             break;

            case "Chennai":
                mSpinnerChat.setAdapter(chennaiSpinner);
             break;

            case "Kolkata":
                mSpinnerChat.setAdapter(kolkataSpinner);
             break;

            case "Delhi":
                mSpinnerChat.setAdapter(delhiSpinner);
             break;

        }

//This button is used to filter the updates of a selected area
        mSmallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areas = mSpinnerChat.getSelectedItem().toString();


                String changeArea = "Select An Area";

                if(areas.equals(changeArea))
                    Toast.makeText(MainActivity.this,"Please Pick An Area",Toast.LENGTH_LONG).show();
                else
                {
                //Here the selected area's updates are displayed from Parse
                    ParseQuery<ParseObject> queries = ParseQuery.getQuery("Status"); //This 'Status' is the table name in parse
                    queries.whereEqualTo("cityName", cities);
                    queries.whereEqualTo("area", areas);
                    queries.findInBackground(new FindCallback<ParseObject>() {

                        public void done(List<ParseObject> status, ParseException e) {
                            if (e == null)
                            {
                                //success
                                mStatus = status;
                                StatusAdapter adapt = new StatusAdapter(MainActivity.this, mStatus);
                                lv.setAdapter(adapt);
                                registerForContextMenu(lv);


                            }
                            else
                            {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("OOPs!!!");
                                builder.setMessage(e.getMessage());
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        }
                    });
                }

                changeAreas(areas);
            }
        });


    }



    public void changeAreas(String selectArea)
    {
        areaSelected = selectArea;


    }
               @Override
               public boolean onCreateOptionsMenu(Menu menu) {
                   // Inflate the menu; this adds items to the action bar if it is present.
                   getMenuInflater().inflate(R.menu.menu_main, menu);



                   return true;
               }

               @Override
               public boolean onOptionsItemSelected(MenuItem item) {
                   // Handle action bar item clicks here. The action bar will
                   // automatically handle clicks on the Home/Up button, so long
                   // as you specify a parent activity in AndroidManifest.xml.
                   int id = item.getItemId();

                   //noinspection SimplifiableIfStatement
                   switch (id) {
                       //take user to update status activity



                       case R.id.action_update:

                           String areaofaCity = null;

                           if(areaSelected == null)
                               areaofaCity = mSpinnerChat.getSelectedItem().toString();
                           else
                               areaofaCity = areaSelected;

                    //This will take user to UpdateActivity where user can update status

                           Intent intent = new Intent(this, UpdateActivity.class);
                           if(cityChange == null)
                               intent.putExtra("City",cityNames);//Throwing the intent data to UpdateActivity
                           else
                                intent.putExtra("City", cityChange);//Throwing the intent data to UpdateActivity

                           intent.putExtra("Area", areaofaCity); //Throwing the intent data to UpdateActivity
                           startActivity(intent);

                           break;

                       case R.id.action_my_comments:

                    //This intent will take user to MyComments, where user can edit or delete statuses or comments

                           Intent myCmnts = new Intent(this, MyComments.class);

                           startActivity(myCmnts);
                            break;

                       case R.id.action_logout:

                    //This alert dialog asks user that whether they want to continue or logout form the app

                           AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                           builder.setMessage("Are You Sure To Log Out?");
                           builder.setTitle("LOG OUT!!!");



                           builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   //close the dialog
                                   dialog.dismiss();
                               }
                           });
                           builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   //close the dialog

                                   dialog.dismiss();
                                   ParseUser.logOut();
                                   Intent takeUserToLogin = new Intent(MainActivity.this, LoginActivity.class);
                                   startActivity(takeUserToLogin);
                                   finish();
                               }
                           });
                           AlertDialog dialog = builder.create();
                           dialog.show();
                           break;


                       case R.id.action_spinner:

                       //Following code is to change the city if user wants

                           String[] cityNames = {"Bangalore","Mumbai","Delhi","Kolkata","Chennai" };

                           final ArrayAdapter<String> cityADP = new ArrayAdapter<String>(this,
                                   android.R.layout.simple_spinner_dropdown_item, cityNames);

                           AlertDialog.Builder changeCityALERTS = new AlertDialog.Builder(MainActivity.this);



                           final Spinner spinnerCITY = new Spinner(MainActivity.this);
                           spinnerCITY.setPrompt("Select One From Below");
                           spinnerCITY.setAdapter(cityADP);

                           changeCityALERTS.setTitle("Select Your City");
                           changeCityALERTS.setView(spinnerCITY);

                           changeCityALERTS.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                                   nameOfCities = spinnerCITY.getSelectedItem().toString();

                                   Log.d("Received", "Got 1:" + nameOfCities);


                                   ParseUser currentUser = ParseUser.getCurrentUser();
                                   if (currentUser != null) {
                                       // do stuff with the user

                                       ParseQuery<ParseObject> query = ParseQuery.getQuery("Status"); //This 'Status' is the table name in parse
                                       query.whereEqualTo("cityName", nameOfCities);
                                       query.orderByDescending("createdAt");
                                       query.findInBackground(new FindCallback<ParseObject>() {

                                           public void done(List<ParseObject> status, ParseException e) {
                                               if (e == null) {
                                                   //success

                                                   StatusAdapter adapt = new StatusAdapter(MainActivity.this, status);
                                                   lv.setAdapter(adapt);

                                               } else {

                                                   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                   builder.setTitle("OOPs!!!");
                                                   builder.setMessage(e.getMessage());
                                                   builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialog, int which) {
                                                           dialog.dismiss();
                                                       }
                                                   });

                                                   AlertDialog dialog = builder.create();
                                                   dialog.show();

                                               }
                                           }
                                       });
                                   } else {
                                       // show the signup or login screen

                                       Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
                                       startActivity(toLogin);
                                   }
                                   cityNames(nameOfCities);


                               }
                           });



                           AlertDialog dialogs = changeCityALERTS.create();
                           dialogs.setCanceledOnTouchOutside(false);
                           dialogs.show();

                           break;


                   }

                   return super.onOptionsItemSelected(item);
               }

    public void cityNames(String nameCities)
    {
        final String changeTheCity=nameCities;
        switch(changeTheCity)
        {
            case "Bangalore":
                mSpinnerChat.setAdapter(bangaloreSpinner);
                break;

            case "Chennai":
                mSpinnerChat.setAdapter(chennaiSpinner);
                break;

            case "Kolkata":
                mSpinnerChat.setAdapter(kolkataSpinner);
                break;

            case "Mumbai":
                mSpinnerChat.setAdapter(mumbaiSpinner);
                break;

            case "Delhi":
                mSpinnerChat.setAdapter(delhiSpinner);
                break;
        }

        mSmallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedArea = mSpinnerChat.getSelectedItem().toString();
                Log.d("Area", "Name: " + selectedArea);
                ParseQuery<ParseObject> queries = ParseQuery.getQuery("Status");
                queries.whereEqualTo("cityName",changeTheCity);
                queries.whereEqualTo("area", selectedArea);
                queries.findInBackground(new FindCallback<ParseObject>() {

                    public void done(List<ParseObject> status, ParseException e) {
                        if (e == null) {
                            //success

                            StatusAdapter adapt = new StatusAdapter(MainActivity.this, status);
                            lv.setAdapter(adapt);


                        } else {

                        }
                    }
                });



            }
        });
        exchangeCities(changeTheCity);
    }

    public void exchangeCities(String cityExchange)
    {
        cityChange = cityExchange;

    }



               @Override
               public void onBackPressed() {

                   super.onBackPressed();

               }


           }
