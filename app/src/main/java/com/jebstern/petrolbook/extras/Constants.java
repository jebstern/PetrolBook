package com.jebstern.petrolbook.extras;


public class Constants {

    public static String BASE_URL = "http://api.jebstern.com/petrolbook/api/";


    public static String convertDatetime(String datetime) {

        String[] parts = datetime.split(" ");
        String date = parts[0]; // 2015-12-31
        String time = parts[1]; // 08:15:00

        String[] mDate = date.split("-");
        String newDate = mDate[2]+"."+mDate[1]+"."+mDate[0];

        String[] mTime = time.split(":");
        String newTime = mTime[0]+":"+mTime[1];

        return newDate+" "+newTime;
    }

    public static String convertDatetimeNoYear(String datetime) {

        String[] parts = datetime.split(" ");
        String date = parts[0]; // 2015-12-31
        String time = parts[1]; // 08:15:00

        String[] mDate = date.split("-");
        String newDate = mDate[2]+"."+mDate[1];

        String[] mTime = time.split(":");
        String newTime = mTime[0]+":"+mTime[1];

        return newDate+" "+newTime;
    }




}
