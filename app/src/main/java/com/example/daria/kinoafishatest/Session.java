package com.example.daria.kinoafishatest;

import org.json.JSONObject;

/**
 * Created by Dasha on 08.06.2016.
 */
public class Session  {
    boolean isNight;
    String limitation;
    String movie;
    String children;
    String student;
    String format;
    String hall;
    String day;
    String time;
    String lang;
    String adult;
    String vip;

    int mm;
    int hh;

    public Session(String date, JSONObject jsonObj) throws Exception {
        //this.isNight = jsonObj.getBoolean(JSONKeyNames.IS_NIGHT);
        this.limitation = jsonObj.getString(JSONKeyNames.LIMITATION);
        this.movie = jsonObj.getString(JSONKeyNames.MOVIE);
        this.children = jsonObj.getString(JSONKeyNames.CHILDREN);
        this.student = jsonObj.getString(JSONKeyNames.STUDENT);
        this.format = jsonObj.getString(JSONKeyNames.FORMAT);
        this.hall = jsonObj.getString(JSONKeyNames.HALL);
        this.day = date;
        this.time = jsonObj.getString(JSONKeyNames.TIME);
        this.lang = jsonObj.getString(JSONKeyNames.LANG);
        this.adult = jsonObj.getString(JSONKeyNames.ADULT);
        this.vip = jsonObj.getString(JSONKeyNames.VIP);

        hh = Integer.parseInt(time.substring(0, 2));
        mm = Integer.parseInt(time.substring(3, time.length()));
    }

    @Override
    public String toString() {
//		return "[" + isNight + "," + limitation + "," + movie + "," + children + "," +
//	           student + "," + format + "," + hall + "," + day + "," + time + "," +
//			   lang + "," + adult + ","+ vip + "]";
        return movie + ", " + time;
    }
}
