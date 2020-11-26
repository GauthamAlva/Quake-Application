package com.converty.quake;

import android.content.AsyncQueryHandler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */



    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    public static final String LOG_TAG = utils.class.getSimpleName();
    public static List<mag> fetchdata(String url)
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL inputurl=createurl(url);
        String response=null;
        response=make_url_request(inputurl);
        List<mag> earthq=extractEarthquakes(response);
        return  earthq;
    }
    public static URL createurl(String urls){
        URL newurl=null;
        try{
            newurl=new URL(urls);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error forming the request",e);
        }
        return newurl;
    }
    public static String make_url_request(URL url){
        String response="";
        HttpURLConnection urlconnect=null;
        InputStream inputStream=null;
        if(url==null) return null;

        try{
            urlconnect =(HttpURLConnection) url.openConnection();
            urlconnect.setReadTimeout(10000);
            urlconnect.setConnectTimeout(15000);
            urlconnect.setRequestMethod("GET");
            urlconnect.connect();
            if(urlconnect.getResponseCode()==200){
                inputStream=urlconnect.getInputStream();
                response=read_from_stream(inputStream);
            }
            else{
                Log.e(LOG_TAG,"error response code"+urlconnect.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"errror retriving earthquake info",e);
        }finally {
            if(urlconnect!=null) urlconnect.disconnect();
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
    public static final String read_from_stream(InputStream inputStream)throws IOException{
        StringBuilder builder=new StringBuilder();
        String response="";
        if(inputStream!=null){
            InputStreamReader streamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(streamReader);
            String line=reader.readLine();
            while (line!=null){
                builder.append(line);
                line=reader.readLine();
            }
        }
        return  builder.toString();
    }
    /**
     * Return a list of {@link mag} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<mag> extractEarthquakes( String response) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<mag> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
//create an obeject of the root as"basejsonresponse" using the response as above

            JSONObject basejsonresponse=new JSONObject(response);
            //create an object of the json array from the basejsonresponse obj and go to the features as parameters
            JSONArray earthquakearray=basejsonresponse.getJSONArray("features");
            //iterate through the features array using for loop
            for(int i=0;i<earthquakearray.length();i++){
                //create a jason obj when iterating and for every single values
                JSONObject currenterthqake=earthquakearray.getJSONObject(i);
                //create an another obj from the "currenterthqake" obj and get inside the properties
                JSONObject properties=currenterthqake.getJSONObject("properties");
                //take the mag,city,date info from properties
                Double magnitude=properties.getDouble("mag");
                String city=properties.getString("place");
                long time = properties.getLong("time");
                String urls=properties.getString("url");
                //we need to convert the unix time to normal time
                //long num=Long.parseLong(date);
                //use thr obj of " SimpleDateFormat" and"Date" and covet it to readable time

                //create a new mag obj and store the values
                mag earthquake =new mag(magnitude,city,time,urls);
               // add these info to the  earthquakes list created
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}