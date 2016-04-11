package com.example.dada.localityupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Madhujit
 */

/* This MyStatuses Adapter class extends Array Adapter whose task is to fetch data from the Parse and display it on the Activity by
   inflating view(s)
 */
public class MyStatusesAdapter extends ArrayAdapter<ParseObject>
{
    protected Context mContext;
    protected List<ParseObject> mStatuses;


    public MyStatusesAdapter(Context context, List<ParseObject> statuses)
    {
        super(context, R.layout.custommycomments,statuses);
        mContext = context;
        mStatuses = statuses;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            //Convert view is null during the first time of object creation

            //Layout Inflation is done where custom xml layout is embedded into the List View
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custommycomments,parent,false);

            //holder object of ViewHolder is initialised
            holder = new ViewHolder();

            //initialising the views
            holder.timeMyComments = (TextView) convertView.findViewById(R.id.custom_my_comments_Time);
            holder.statusMyComments = (TextView) convertView.findViewById(R.id.custom_my_comments_Status);
            holder.areaMyComments = (TextView) convertView.findViewById(R.id.custom_my_comments_Area);
            holder.cityMyComments = (TextView) convertView.findViewById(R.id.custom_my_comments_City);

            //This setTag method is used to hold objects
            convertView.setTag(holder);
        }
        else
        {
            //This getTag method gives us the old objects which can be reused
            holder = (ViewHolder) convertView.getTag();
        }


        ParseObject statusObject = mStatuses.get(position);

        //Status
       String newStatuses = statusObject.getString("newStatus");
       String newStatus=newStatuses.substring(0,1).toUpperCase()+newStatuses.substring(1);
       holder.statusMyComments.setText(newStatus);

        //Area
        String areaName = statusObject.getString("area");
        holder.areaMyComments.setText(areaName);

        //City
        String cityName = statusObject.getString("cityName");
        holder.cityMyComments.setText(cityName);

        //Time
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
        String dateS = formatter.format(statusObject.getCreatedAt());
        String newdateS=dateS.replace("AM","am").replace("PM", "pm");
        holder.timeMyComments.setText(newdateS);






        return convertView;
    }

    public static class ViewHolder
    {
        TextView timeMyComments;
        TextView statusMyComments;
        TextView areaMyComments;
        TextView cityMyComments;


    }

}
