package com.example.dada.localityupdate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/* Created by Madhujit
 */
/* This My Comments Activity is used to display statuses of current user including area, city and time.
*/


public class MyComments extends AppCompatActivity {


    protected ListView myCmntsLv;
    protected List<ParseObject> mComments;
    protected TextView mUsername;
    private Dialog loadingScreens;
    protected String objectIds;
    ParseObject statusObjects;
    String uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        myCmntsLv = (ListView) findViewById(R.id.listView_MyComments);
        mUsername = (TextView) findViewById(R.id.textView_MyComments);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            uname = currentUser.getUsername();
            String userName = uname.substring(0,1).toUpperCase()+uname.substring(1);
            mUsername.setText(userName);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Status");
            loadingScreens= ProgressDialog.show(MyComments.this, "Loading", "Please Wait...",false,true);
            query.whereEqualTo("userName",uname);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {

                public void done(List<ParseObject> status, ParseException e) {
                    if (e == null) {

                        //success
                        mComments = status;
                        MyStatusesAdapter statusesadapt = new MyStatusesAdapter(MyComments.this, mComments);

                        myCmntsLv.setAdapter(statusesadapt);
                        loadingScreens.dismiss();
                        registerForContextMenu(myCmntsLv);


                    }
                    else
                    {
                        //during Parse exception
                        //This alert dialog code will prompt the user about the exception

                        AlertDialog.Builder builder = new AlertDialog.Builder(MyComments.this);
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

        else
        {
        // show the signup or login screen

        Intent toLogin = new Intent(this,LoginActivity.class);
        startActivity(toLogin);
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Status");
                loadingScreens=ProgressDialog.show(MyComments.this,"Loading","Please Wait",false,true);
                query.whereEqualTo("userName", uname);
                query.orderByDescending("createdAt");
                query.findInBackground(new FindCallback<ParseObject>() {

                    public void done(List<ParseObject> status, ParseException e) {
                        if (e == null) {
                            //success

                            mComments = status;
                            MyStatusesAdapter statusesadapt = new MyStatusesAdapter(MyComments.this, mComments);
                            myCmntsLv.setAdapter(statusesadapt);
                            loadingScreens.dismiss();
                            registerForContextMenu(myCmntsLv);



                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MyComments.this);
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


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {



        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;
        statusObjects = mComments.get(position);
        objectIds = statusObjects.getObjectId();

        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Edit");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Delete");



    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle()=="Edit")
        {
           //Editing the content
            Intent gotoDetailView = new Intent(MyComments.this, StatusDetailActivity.class);
            gotoDetailView.putExtra("objectId", objectIds);

            startActivity(gotoDetailView);
            finish();

        }
        else if(item.getTitle()=="Delete")
        {
           //Deleting status
            ParseObject.createWithoutData("Status", objectIds).deleteInBackground();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Status");
            loadingScreens=ProgressDialog.show(MyComments.this,"Deleting","Please Wait",true);
            query.whereEqualTo("userName", uname);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {

                public void done(List<ParseObject> status, ParseException e) {
                    if (e == null) {
                        //success
                        mComments = status;
                        MyStatusesAdapter statusesadapt = new MyStatusesAdapter(MyComments.this, mComments);
                        myCmntsLv.setAdapter(statusesadapt);
                        loadingScreens.dismiss();


                    } else {

                        Toast.makeText(MyComments.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });

            Toast.makeText(MyComments.this,"One row deleted", Toast.LENGTH_SHORT).show();

        }
        else
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_comments, menu);
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

            case R.id.action_logout:
                //logging out
                AlertDialog.Builder builder = new AlertDialog.Builder(MyComments.this);
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
                        Intent takeUserToLogin = new Intent(MyComments.this, LoginActivity.class);
                        startActivity(takeUserToLogin);
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onBackPressed() {
        //finish();
        super.onBackPressed();

    }


}
