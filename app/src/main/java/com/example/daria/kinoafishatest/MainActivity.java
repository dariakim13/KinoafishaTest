package com.example.daria.kinoafishatest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;
    ListView listView;
    ListOfCinemas list;
    public static SQLiteHelper sqLiteHelper;
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);

        sqLiteHelper = new SQLiteHelper(this);
        mDatabase = sqLiteHelper.getWritableDatabase();

        listView = (ListView) findViewById(R.id.lvMain);



        startReadCinemasActivity();
    }

    public void startReadCinemasActivity()
    {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        if( netInfo != null && netInfo.isConnected())
        {
            dialog.setMessage("Please, wait...");
            dialog.show();
            //sqLiteHelper.deleteMyDatabase();
            mDatabase.delete(sqLiteHelper.TABLE_SESSIONS, null, null);
            mDatabase.delete(sqLiteHelper.TABLE_DESCRIPTION, null, null);

            CurrencyAsyncTask task = new CurrencyAsyncTask();
            task.execute("http://176.126.167.231:83/api/crawl");
        }
        else
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();

            String[] cinemas = sqLiteHelper.cinemasFromDatabase(7);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.cinema_list_item, cinemas);

            // присваиваем адаптер списку
            listView.setAdapter(adapter);


        }
    }

    public String readFile(InputStream stream) throws IOException {
        try {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                strBuilder.append(line);
            }
            bufferedReader.close();
            return strBuilder.toString();
        } catch (IOException ex) {
        }
        return " ";
    }

    public void readJsonDescription(String jsonText, String name) {
        try {
            Log.d("film", "js");
            JSONObject root = new JSONObject(jsonText);
            JSONArray movies = root.getJSONArray(name);

            String trailer = "";
            for (int i = 0; i < movies.length(); ++i) {
                JSONObject desc = movies.getJSONObject(i);
                try {
                    try {
                        trailer = desc.getString("trailer");
                    } catch (JSONException e) {
                        trailer = "-";
                    }

                    ContentValues mContentValues = new ContentValues();
                    mContentValues.put(JSONKeyNames.TITLE, desc.getString("title"));
                    mContentValues.put(JSONKeyNames.PRODUCTION, desc.getString("production"));
                    mContentValues.put(JSONKeyNames.GENRE, desc.getString("genre"));
                    mContentValues.put(JSONKeyNames.DIRECTOR, desc.getString("director"));
                    mContentValues.put(JSONKeyNames.TIME_MOVIE, desc.getString("time"));
                    mContentValues.put(JSONKeyNames.DESCRIPTION, desc.getString("description"));
                    mContentValues.put(JSONKeyNames.ACTORS, desc.getString("actors"));
                    mContentValues.put(JSONKeyNames.POSTER, desc.getString("poster"));

                    mContentValues.put(JSONKeyNames.TRAILER, trailer);

                    SQLiteDatabase mDatabase = sqLiteHelper.getWritableDatabase();

                    mDatabase.insert(SQLiteHelper.TABLE_DESCRIPTION, null, mContentValues);
                    Log.d("film", mContentValues.toString());
                    sqLiteHelper.close();

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class CurrencyAsyncTask extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... urls)
        {
            try
            {
                return downloadUrl(urls[0]);
            }
            catch(Exception e){

            }
            return " ";
        }

        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(100000 /* milliseconds */);
                conn.setConnectTimeout(150000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();

                return readFile(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        public void onPostExecute(String result)
        {
            dialog.dismiss();

            try {
                list = new ListOfCinemas(result);
                readJsonDescription(result, "movies");
                Log.i("Hello", list.toString());

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            Collections.sort(list.listOfCinemas, new CmpByName());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.cinema_list_item, list.listOfCinemas);

            // присваиваем адаптер списку
            listView.setAdapter(adapter);

            sqLiteHelper.createMyDataBase(list);
        }
    }

    static class CmpByName implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    public void show(View view)
    {
        Button button = (Button) view;
        String name = button.getText().toString();

        ArrayList <String> strSessions = new ArrayList<>();

        Intent intent = new Intent(MainActivity.this, SessionsActivity.class);

        strSessions = sqLiteHelper.readFromMyDatabase(name);
        intent.putExtra(SessionsActivity.LIST_OF_SESSIONS_ID, strSessions);
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sqLiteHelper.close();
    }
}
