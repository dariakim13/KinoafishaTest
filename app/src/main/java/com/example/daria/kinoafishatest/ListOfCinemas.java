package com.example.daria.kinoafishatest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by Dasha on 08.06.2016.
 */
public class ListOfCinemas {
    private TreeMap<Integer, String> mapOfDates = new TreeMap<>();
    public ArrayList<String> listOfCinemas = new ArrayList<>();
    private HashMap<String, ArrayList<ArrayList<Session>>> mapOfData = new HashMap<>();

    public ListOfCinemas(String text) throws Exception {

        JSONObject jsonMain = new JSONObject(text);
        JSONObject jsonCinemas = jsonMain.getJSONObject(JSONKeyNames.CINEMAS);

        Iterator iterator = jsonCinemas.keys();
        while (iterator.hasNext()) {

            String currentCinema = iterator.next().toString();
            listOfCinemas.add(currentCinema);

            JSONArray days = jsonCinemas.getJSONArray(currentCinema);
            ArrayList<ArrayList<Session>> currentList = new ArrayList<>();
            for (int i = 0; i < days.length(); ++i) {

                ArrayList<Session> currentDayList = new ArrayList<>();
                try {

                    JSONObject currentDay = days.getJSONObject(i);
                    String date = currentDay.getString(JSONKeyNames.DATE);
                    mapOfDates.put(i, date);

                    JSONArray halls = currentDay.getJSONArray(JSONKeyNames.HALLS);
                    for (int j = 0; j < halls.length(); ++j) {

                        JSONObject currentHall = halls.getJSONObject(j);
                        JSONArray sessions = currentHall.getJSONArray(JSONKeyNames.SESSIONS);
                        for (int k = 0; k < sessions.length(); ++k) {
                            Session currentSession = new Session(date, sessions.getJSONObject(k));
                            currentDayList.add(currentSession);
                        }

                    }
                } catch (JSONException e) {
                    // pass
                }

                Collections.sort(currentDayList, new CmpByTime());
                currentList.add(currentDayList);
            }

            mapOfData.put(currentCinema, currentList);
        }
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append("Dates: " + mapOfDates + "\n");

        for (String cinema : listOfCinemas) {

            text.append(cinema + "\n");
            ArrayList<ArrayList<Session>> days = mapOfData.get(cinema);

            for (int i = 0; i < days.size(); ++i) {

                text.append("    " + "Day: " + i + "\n");
                ArrayList<Session> list = days.get(i);

                for (int j = 0; j < list.size(); ++j) {
                    text.append("        " + list.get(j) + "\n");
                }
            }
        }

        return text.toString();
    }

    public ArrayList<Session> get(String cinema, int day) {

        if (!mapOfData.containsKey(cinema) || mapOfData.get(cinema).size() <= day) {
            return null;
        }

        return mapOfData.get(cinema).get(day);
    }
}

class CmpByTime implements Comparator<Session> {

    @Override
    public int compare(Session o1, Session o2) {
        if (o1.hh < o2.hh) {
            return -1;
        }

        if (o1.hh > o2.hh) {
            return 1;
        }

        if (o1.mm < o2.mm) {
            return -1;
        }

        if (o1.mm > o2.mm) {
            return 1;
        }

        return 0;
    }
}
