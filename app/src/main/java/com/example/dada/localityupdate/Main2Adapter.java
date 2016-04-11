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

/* This Main2 Adapter class extends Array Adapter whose task is to fetch data from the Parse and display it on the Activity by
   inflating view(s)
 */
public class Main2Adapter extends ArrayAdapter<ParseObject>
{
    private Context mContext;
    private List<ParseObject> mStatus;

    public Main2Adapter(Context context, List<ParseObject> status)
    {
        super(context, R.layout.custommainactivity,status);
        mContext = context;
        mStatus = status;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            //Convert view is null during the first time of object creation

            //Layout Inflation is done where custom xml layout is embedded into the List View
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custommainactivity,parent,false);

            //holder object of ViewHolder is initialised
            holder = new ViewHolder();

            //initialising the views
            holder.usernameMP2 = (TextView) convertView.findViewById(R.id.custom_main_activity_Username);
            holder.statusMP2 = (TextView) convertView.findViewById(R.id.custom_main_activity_Status);
            holder.timeMP = (TextView) convertView.findViewById(R.id.custom_main_activity_Time);

            //This setTag method is used to hold objects
            convertView.setTag(holder);


        }
        else
        {
            //This getTag method gives us the old objects which can be reused
            holder = (ViewHolder) convertView.getTag();
        }


        ParseObject statusObj = mStatus.get(position);


        //title
        String uname = statusObj.getString("userName");
        String newUsername=uname.substring(0,1).toUpperCase()+uname.substring(1);
        holder.usernameMP2.setText(newUsername);

        //status
        String stsup = statusObj.getString("newStatus");
        String newStatus=stsup.substring(0,1).toUpperCase()+stsup.substring(1);
        holder.statusMP2.setText(newStatus);


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

        //Parsing from String to integer
        int dbaseYear = Integer.parseInt(dbYr);

        //Year format from device
        SimpleDateFormat deviceYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        String deviceYr = deviceYear.format(new Date());

        //Parsing from String to integer
        int deviceYears = Integer.parseInt(deviceYr);

        //Getting month from Parse
        SimpleDateFormat dbMonths = new SimpleDateFormat("MM", Locale.getDefault());
        String dbMnts = dbMonths.format(statusObj.getCreatedAt());

        //Parsing from String to integer
        int dbaseMnths = Integer.parseInt(dbMnts);

        SimpleDateFormat deviceMonths = new SimpleDateFormat("MM", Locale.getDefault());
        String deviceMnts = deviceMonths.format(new Date());

        //Parsing from String to integer
        int deviceMnths = Integer.parseInt(deviceMnts);


        //Getting date format from Parse
        SimpleDateFormat dbDate = new SimpleDateFormat("dd", Locale.getDefault());
        String dbDates = dbDate.format(statusObj.getCreatedAt());


        //Getting date format from device
        SimpleDateFormat dateType = new SimpleDateFormat("dd", Locale.getDefault());
        String dateTypes = dateType.format(new Date());


        //Parsing from String to integer
        int dBaseDate = Integer.parseInt(dbDates);
        int deviceDate = Integer.parseInt(dateTypes);


        if(dbaseYear == deviceYears && dbaseMnths == deviceMnths && dBaseDate == deviceDate)

            holder.timeMP.setText(R.string.today);


        else if(dbaseYear == deviceYears && dbaseMnths == deviceMnths && dBaseDate == deviceDate-1)

            holder.timeMP.setText(R.string.yesterday);


        else if(dbaseYear == deviceYears)

            holder.timeMP.setText(date);

        else

            holder.timeMP.setText(dateII);


        return convertView;
    }

    public static class ViewHolder
    {
        TextView usernameMP2;
        TextView statusMP2;
        TextView timeMP;
    }

}
