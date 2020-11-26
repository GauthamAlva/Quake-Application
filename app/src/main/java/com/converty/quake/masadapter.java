package com.converty.quake;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class masadapter extends ArrayAdapter<mag> {
    public masadapter(Activity context, ArrayList<mag> quakeArrayList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, quakeArrayList);

    }

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_layout, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        mag current = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView magni = (TextView) listItemView.findViewById(R.id.magnitude);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        String formatedmag=magformat(current.getmag());
        magni.setText(formatedmag);
        GradientDrawable magcircle=(GradientDrawable)magni.getBackground();
        int magcolor=getmagnicolor(current.getmag());
        magcircle.setColor(magcolor);
//---------------------------------------------------------------------------------
        // Find the TextView in the list_item.xml layout with the ID version_number
        String tobeformated= current.getcity();
        String[] arrofstring=tobeformated.split("of");
        TextView prim = (TextView) listItemView.findViewById(R.id.primaryloc);
        TextView second= (TextView) listItemView.findViewById(R.id.secondaryloc);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        for(int i=0;i<arrofstring.length;i++){
            if(i==0)  prim.setText(arrofstring[i]+"of");
            else  second.setText(arrofstring[i]);
        }


        //----------------------------------------------------------------------
        Date dateobj=new Date(current.getdate());
        TextView date=(TextView)listItemView.findViewById(R.id.date);
        String formatteddate=formatDate(dateobj);
      date.setText(formatteddate);

//--------------------------------------------------------------------------------
        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateobj);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);
//--------------------------------------------------------------------------------
        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private String magformat(Double mag){
        DecimalFormat magformat=new DecimalFormat("0.0");
        return magformat.format(mag);
    }
    private  int getmagnicolor(Double mag){
        int madnitudecoloure;
        int magfloor=(int) Math.floor(mag);
        switch (magfloor){
            case 0:
            case 1:madnitudecoloure=R.color.magnitude1;
            break;
            case 2:madnitudecoloure=R.color.magnitude2;
            break;
            case 3:madnitudecoloure=R.color.magnitude3;
            break;
            case 4:
                madnitudecoloure = R.color.magnitude4;
                break;
            case 5:
                madnitudecoloure = R.color.magnitude5;
                break;
            case 6:
                madnitudecoloure = R.color.magnitude6;
                break;
            case 7:
                madnitudecoloure = R.color.magnitude7;
                break;
            case 8:
                madnitudecoloure= R.color.magnitude8;
                break;
            case 9:
                madnitudecoloure = R.color.magnitude9;
                break;
            default:
                madnitudecoloure = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),madnitudecoloure);
    }
}
