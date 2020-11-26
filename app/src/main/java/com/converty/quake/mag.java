package com.converty.quake;

public class mag {
   private double mmag;
   private String mcity;
   private  long mdate;
   private String murl;
    public mag(double mag,String city,long date,String url){
        mmag=mag;
        mcity=city;
        mdate=date;
        murl=url;
    }
    double getmag(){
        return mmag;
    }
    String getcity(){
        return mcity;
    }
      public long getdate(){
        return mdate;
    }
    public String geturl(){
        return murl;
    }

}
