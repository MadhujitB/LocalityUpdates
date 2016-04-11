package com.example.dada.localityupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Madhujit
 */

/* This Status Adapter class extends Array Adapter whose task is to fetch data from the Parse and display it on the Activity by
   inflating view(s)
 */
public class StatusAdapter extends ArrayAdapter<ParseObject> {


    protected Context mContext;
    protected List<ParseObject> mStatus;

    public StatusAdapter(Context context, List<ParseObject> status)
    {
        super(context, R.layout.custommainactivity,status);
        mContext = context;
        mStatus = status;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custommainactivity,parent,false);
            holder = new ViewHolder();

            holder.usernameMP = (TextView) convertView.findViewById(R.id.custom_main_activity_Username);
            holder.statusMP = (TextView) convertView.findViewById(R.id.custom_main_activity_Status);
            holder.timeMP = (TextView) convertView.findViewById(R.id.custom_main_activity_Time);
            convertView.setTag(holder);


        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        ParseObject statusObj = mStatus.get(position);


        //title
        String uname = statusObj.getString("userName");
        String userName=uname.substring(0,1).toUpperCase()+uname.substring(1);
        holder.usernameMP.setText(userName);

        //status
        String stsup = statusObj.getString("newStatus");
        String newStatus=stsup.substring(0,1).toUpperCase()+stsup.substring(1);
        holder.statusMP.setText(newStatus);


        //time

        //Date, month and year from Parse
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String date = formatter.format(statusObj.getCreatedAt());

        //Month and year from Parse
        SimpleDateFormat deviceFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        String dateII = deviceFormat.format(statusObj.getCreatedAt());

        //Year format from Parse
        SimpleDateFormat dbYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        String dbYr = dbYear.format(statusObj.getCreatedAt());

        //Parsing String to integer
        int dbaseYear = Integer.parseInt(dbYr);

        SimpleDateFormat deviceYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        String deviceYr = deviceYear.format(new Date());

        //Parsing String to integer
        int deviceYears = Integer.parseInt(deviceYr);

        SimpleDateFormat dbMonths = new SimpleDateFormat("MM", Locale.getDefault());
        String dbMnts = dbMonths.format(statusObj.getCreatedAt());

        //Parsing String to integer
        int dbaseMnths = Integer.parseInt(dbMnts);

        SimpleDateFormat deviceMonths = new SimpleDateFormat("MM", Locale.getDefault());
        String deviceMnts = deviceMonths.format(new Date());

        //Parsing String to integer
        int deviceMnths = Integer.parseInt(deviceMnts);

        //Getting date format from Parse
        SimpleDateFormat dbDate = new SimpleDateFormat("dd", Locale.getDefault());
        String dbDates = dbDate.format(statusObj.getCreatedAt());

        //Getting date format from device
        SimpleDateFormat dateType = new SimpleDateFormat("dd", Locale.getDefault());
        String dateTypes = dateType.format(new Date());

        //Parsing String to integer
        int dBaseDate = Integer.parseInt(dbDates);
        int deviceDate = Integer.parseInt(dateTypes);




      if(dbaseYear == deviceYears && dbaseMnths == deviceMnths && dBaseDate == deviceDate)

          holder.timeMP.setText(R.string.today);


      else if(dbaseYear == deviceYears && dbaseMnths == deviceMnths && dBaseDate == deviceDate - 1)

              holder.timeMP.setText(R.string.yesterday);


      else if(dbaseYear == deviceYears)

              holder.timeMP.setText(date);

      else

            holder.timeMP.setText(dateII);


        return convertView;
    }

    public static class ViewHolder
    {
        TextView usernameMP;
        TextView statusMP;
        TextView timeMP;


    }


}
