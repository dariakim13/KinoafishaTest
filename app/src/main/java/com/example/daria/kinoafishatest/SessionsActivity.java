package com.example.daria.kinoafishatest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SessionsActivity extends AppCompatActivity {

    public static final String LIST_OF_SESSIONS_ID = "list_of_sessions_id";
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        Intent callingIntent = getIntent();
        ArrayList <String> strSessions = callingIntent.getStringArrayListExtra(LIST_OF_SESSIONS_ID);
        Log.i("example", strSessions.toString());
        listView = (ListView) findViewById(R.id.listView);


        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SessionsActivity.this, R.layout.sessions_list_item, strSessions);

        // присваиваем адаптер списку
        listView.setAdapter(adapter);

    }

    public void showFilmInfo(View view) {
        TextView text = (TextView) view;
        Intent intent = new Intent(SessionsActivity.this, DescriptionActivity.class);

        String result = text.getText().toString();
        /*String infos[] = result.split("\\r?\\n");*/
        String infos[] = result.split(", ");
        intent.putExtra(DescriptionActivity.EXTRA_MOVIE, infos[0]);
        /*intent.putExtra(DescriptionActivity.EXTRA_CINEMA, cinema);
        intent.putExtra(DescriptionActivity.EXTRA_TIME, );
        intent.putExtra(DescriptionActivity.EXTRA_DATE, infos[0]);*/
        Log.d("film", infos[0]);
        startActivity(intent);
    }
}
