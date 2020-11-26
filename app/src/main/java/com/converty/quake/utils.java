package com.converty.quake;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public final class utils {
    public static final String LOG_TAG = utils.class.getSimpleName();
    public static  String fetchdata(String url)
    {
        URL inputurl=createurl(url);
        String response=null;
        response=make_url_request(inputurl);
        return response;
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
}
