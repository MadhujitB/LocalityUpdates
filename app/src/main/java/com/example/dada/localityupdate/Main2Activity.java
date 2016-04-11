package com.example.dada.localityupdate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.List;

/* Created by Madhujit
 */

/* This Main2 Activity mainly displays the items which includes username, statuses and time in each of a list view.
   This datas are fetched from Parse and with the help of an adapter, they are getting displayed.
 */

public class Main2Activity extends AppCompatActivity {



    private Button smallButton;
    private ListView listView;
    private Spinner spinnerMain2;

    private String[] bangaLORE = {"Select An Area","Banshankari", "BTM Layout","Jayanagar","JP Nagar","Majestic","Kumaraswamy Layout","MG Road"};

    private String[] mumBAI = {"Select An Area","Colaba", "Churchgate", "Andheri", "Bandra", "Dadar" };
    private String[] chenNAI = {"Select An Area","Marina Beach","Ashok Nagar","Madipakkam","Egmore","Kottur"};

    private String[] kolkATA = {"Select An Area","Godiya Hut", "Howrah", "Alipore", "Kalighat"};
    private String[] deLHI = {"Select An Area","Dwarka","Chittaranjan Park", "Karol Bagh", "Connaught Place"};


    private ArrayAdapter<String> spinnerBangalore;
    private ArrayAdapter<String> spinnerKolkata;
    private ArrayAdapter<String> spinnerMumbai;
    private ArrayAdapter<String> spinnerChennai;
    private ArrayAdapter<String> spinnerDelhi;


    private String selectedItem;
    private String nameOfTheCities;

    private String selectedCity;
    private String cityHello;

    private String cityNames;
    private String areaNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        savedInstanceState = this.getIntent().getExtras();
        final String cityName = savedInstanceState.getString("CityName");
        final String areaName = savedInstanceState.getString("AreaName");

        Log.d("UpdateMain","City Main2: "+cityName);
        Log.d("UpdateMain","Area Main2: "+areaName);

        smallButton = (Button) findViewById(R.id.button_Main2Activity);
        listView = (ListView) findViewById(R.id.listView_Main2Activity);
        spinnerMain2 = (Spinner) findViewById(R.id.spinner_Main2Activity);

        spinnerBangalore = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,bangaLORE);
        spinnerKolkata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,kolkATA);
        spinnerMumbai =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mumBAI);
        spinnerChennai =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,chenNAI);
        spinnerDelhi =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,deLHI);


    //Below there is switch case which displays a spinner of area names in dropdown according to the city names.

        switch(cityName)
        {
            case "Bangalore":

                spinnerMain2.setAdapter(spinnerBangalore);
                int bangalore = spinnerBangalore.getPosition(areaName);
                Log.d("Main2","City Name"+bangalore);

                spinnerMain2.setSelection(bangalore,false);
                break;

            case "Mumbai":

                spinnerMain2.setAdapter(spinnerMumbai);
                int mumbai = spinnerMumbai.getPosition(areaName);
                spinnerMain2.setSelection(mumbai);
                break;

            case "Delhi":

                spinnerMain2.setAdapter(spinnerDelhi);
                int delhi = spinnerDelhi.getPosition(areaName);
                spinnerMain2.setSelection(delhi);
                break;

            case "Chennai":

                spinnerMain2.setAdapter(spinnerChennai);
                int chennai = spinnerChennai.getPosition(areaName);
                spinnerMain2.setSelection(chennai);
                break;

            case "Kolkata":

                spinnerMain2.setAdapter(spinnerKolkata);
                int kolkata = spinnerKolkata.getPosition(areaName);
                spinnerMain2.setSelection(kolkata);
                break;
        }


        cityNames = cityName;
        areaNames = areaName;


        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user



            ParseQuery<ParseObject> query = ParseQuery.getQuery("Status"); //This 'Status' is the table name in parse
            query.whereEqualTo("cityName",cityName);
            query.whereEqualTo("area",areaName);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {

                public void done(List<ParseObject> status, ParseException e) {
                    if (e == null) {
                        //success

                        Main2Adapter main2adapter = new Main2Adapter(Main2Activity.this, status);
                        listView.setAdapter(main2adapter);


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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

            Intent toLogin = new Intent(Main2Activity.this, LoginActivity.class);
            startActivity(toLogin);
        }

        //This button is used to filter the updates of a selected area
        smallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedItem = spinnerMain2.getSelectedItem().toString();
                Log.d("Area","Name: "+selectedItem);
                ParseQuery<ParseObject> queries = ParseQuery.getQuery("Status"); //This 'Status' is the table name in parse
                queries.whereEqualTo("cityName",cityNames);
                queries.whereEqualTo("area", selectedItem);
                queries.findInBackground(new FindCallback<ParseObject>() {

                    public void done(List<ParseObject> status, ParseException e) {
                        if (e == null) {
                            //success

                            Main2Adapter adapt = new Main2Adapter(Main2Activity.this, status);
                            listView.setAdapter(adapt);
                            registerForContextMenu(listView);

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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


        //This floating button is used for refreshing the page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing Please Wait...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                ParseQuery<ParseObject> queries = ParseQuery.getQuery("Status");//This 'Status' is the table name in parse

                queries.orderByDescending("createdAt");
                queries.findInBackground(new FindCallback<ParseObject>() {

                    public void done(List<ParseObject> statusTable, ParseException e) {
                        if (e == null) {
                            //success

                            StatusAdapter adaptS = new StatusAdapter(Main2Activity.this, statusTable);
                            listView.setAdapter(adaptS);


                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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

        helloCity(cityNames);


    }

    public void helloCity(String helloCITY)
    {
        cityHello = helloCITY;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_my_comments_main2:


                Intent myCmnts = new Intent(this, MyComments.class);

                startActivity(myCmnts);
                break;

            case R.id.action_logout_main2:

                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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
                        Intent takeUserToLogin = new Intent(Main2Activity.this, LoginActivity.class);
                        startActivity(takeUserToLogin); //Take user to Login screen
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.action_update_main2:

                final String newArea = spinnerMain2.getSelectedItem().toString();
                Log.d("Main2","Area"+newArea);
                String selectArea = "Select An Area";

                if(newArea.equals(selectArea))
                    Toast.makeText(Main2Activity.this,"Please Select An Area From The list",Toast.LENGTH_LONG).show();

                else {

                    Intent intent = new Intent(Main2Activity.this, UpdateActivity.class);
                    if (selectedCity == null)
                        intent.putExtra("CityNames", cityNames);
                    else
                        intent.putExtra("CityNames", selectedCity);
                    intent.putExtra("AreaNames", newArea);


                    startActivity(intent);
                }

                break;

            case R.id.action_change_city_main2:

                String[] cityArray = {"Bangalore","Mumbai","Delhi","Kolkata","Chennai" };

                final ArrayAdapter<String> cityADP = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, cityArray);

                AlertDialog.Builder cityALERTS = new AlertDialog.Builder(Main2Activity.this);



                final Spinner spinnerCITY = new Spinner(Main2Activity.this);
                spinnerCITY.setPrompt("Select One From Below");
                spinnerCITY.setAdapter(cityADP);

                cityALERTS.setTitle("Select Your City");
                cityALERTS.setView(spinnerCITY);

                cityALERTS.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        nameOfTheCities = spinnerCITY.getSelectedItem().toString();

                        Log.d("Received", "Got 1:" + nameOfTheCities);


                        ParseUser currentUser = ParseUser.getCurrentUser();
                        if (currentUser != null) {
                            // do stuff with the user


                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Status"); //This 'Status' is the table name in parse
                            query.whereEqualTo("cityName", nameOfTheCities);
                            query.orderByDescending("createdAt");
                            query.findInBackground(new FindCallback<ParseObject>() {

                                public void done(List<ParseObject> status, ParseException e) {
                                    if (e == null) {
                                        //success

                                        Main2Adapter adapt = new Main2Adapter(Main2Activity.this, status);
                                        listView.setAdapter(adapt);


                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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

                            Intent toLogin = new Intent(Main2Activity.this, LoginActivity.class);
                            startActivity(toLogin);
                        }
                        changeTheCity(nameOfTheCities);


                    }
                });



                AlertDialog dialogs = cityALERTS.create();
                dialogs.setCanceledOnTouchOutside(false);
                dialogs.show();

                break;

        }
        return super.onOptionsItemSelected(item);

    }


    public void changeTheCity(String nameOfCities)
    {
         final String citIES = nameOfCities;




        switch(citIES)
        {
            case "Bangalore":
                spinnerMain2.setAdapter(spinnerBangalore);
                break;

            case "Chennai":
                spinnerMain2.setAdapter(spinnerChennai);
                break;

            case "Kolkata":
                spinnerMain2.setAdapter(spinnerKolkata);
                break;

            case "Mumbai":
                spinnerMain2.setAdapter(spinnerMumbai);
                break;

            case "Delhi":
                spinnerMain2.setAdapter(spinnerDelhi);
                break;
        }

        smallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedItem = spinnerMain2.getSelectedItem().toString();
                Log.d("Area","Name: "+selectedItem);
                ParseQuery<ParseObject> queries = ParseQuery.getQuery("Status"); //This 'Status' is the table name in parse
                queries.whereEqualTo("cityName",citIES);
                queries.whereEqualTo("area", selectedItem);
                queries.findInBackground(new FindCallback<ParseObject>() {

                    public void done(List<ParseObject> status, ParseException e) {
                        if (e == null) {
                            //success

                            StatusAdapter adapt = new StatusAdapter(Main2Activity.this, status);
                            listView.setAdapter(adapt);
                            registerForContextMenu(listView);


                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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
        exchaNGE(citIES);

    }

    public void exchaNGE(String exchangeCity)

    {
        selectedCity = exchangeCity;
    }
}
