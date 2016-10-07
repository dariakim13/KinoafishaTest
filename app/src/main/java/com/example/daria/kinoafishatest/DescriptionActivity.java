package com.example.daria.kinoafishatest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DescriptionActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";
    public static final String EXTRA_TIME = "time";
    public static final String EXTRA_CINEMA = "cinema";
    public static final String EXTRA_DATE = "date";
    private static final String TAG = "myLogs";
    private ImageView mImageView;
    private TextView mTextViewDesc, mTextViewOther,
            mTextViewTrailer, mTextViewAdditional;
    private SQLiteHelper dbHelper;
    private SQLiteDatabase mDatabase;
    private Cursor cursor;
    private Cursor cursor1;
    private String movie;
    private String cinema;
    private String time;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mTextViewDesc = (TextView) findViewById(R.id.textView3);
        mTextViewOther = (TextView) findViewById(R.id.textView4);
        mTextViewTrailer = (TextView) findViewById(R.id.textView6);
        mTextViewAdditional = (TextView) findViewById(R.id.textView5);
        dbHelper = new SQLiteHelper(this);
        //dbHelper = MainActivity.sqLiteHelper;
        mDatabase = dbHelper.getReadableDatabase();
        movie = getIntent().getStringExtra(EXTRA_MOVIE);
        /*cinema = getIntent().getStringExtra(EXTRA_CINEMA);
        time = getIntent().getStringExtra(EXTRA_TIME);
        date = getIntent().getStringExtra(EXTRA_DATE);*/
        setCursor();
        if (cursor1 != null && cursor1.moveToFirst()) {
            int imageIndex = cursor1.getColumnIndex(SQLiteHelper.POSTER);
            Log.d("p", Integer.toString(imageIndex));

            new DownloadImageTask(mImageView)
                    .execute(cursor1.getString(/*imageIndex*/7));
        }else
            Log.d("read", "0 rows");

        setDescription();


    }


    private String checkPremiera(String movie) {
        Log.d("p", movie);
        String splitBySpace[] = movie.split("\\s+");
        if (splitBySpace[splitBySpace.length - 1].equals("Премьера")) {
            StringBuilder con = new StringBuilder();
            for (int i = 0; i < splitBySpace.length - 1; ++i) {
                con.append(splitBySpace[i]);
                if (i < splitBySpace.length - 2) {
                    con.append(" ");
                }
            }
            //Log.d(MainActivity.TAG, con.toString());
            return con.toString();
        }
        return movie;
    }

    private void setCursor() {

        StringBuilder res = new StringBuilder(checkPremiera(movie));
        res.append("\"");
        res.insert(0, "\"");
        cursor1 = mDatabase.query(SQLiteHelper.TABLE_DESCRIPTION,
                new String[]{SQLiteHelper.TITLE, SQLiteHelper.PRODUCTION, SQLiteHelper.GENRE,
                        SQLiteHelper.DIRECTOR, SQLiteHelper.TIME_MOVIE,
                        SQLiteHelper.DESCRIPTION, SQLiteHelper.ACTORS,
                        SQLiteHelper.POSTER, SQLiteHelper.TRAILER},
                SQLiteHelper.TITLE + " = ?",
                new String[]{res.toString()},
                null, null, null);
        Log.d("film", res.toString());
        Log.d(TAG, " 0000000000000000");
    }

    private void setDescription() {
        if (cursor1.moveToFirst()) {
            int titleIndex = cursor1.getColumnIndex(SQLiteHelper.TITLE);
            int productionIndex = cursor1.getColumnIndex(SQLiteHelper.PRODUCTION);
            int genreIndex = cursor1.getColumnIndex(SQLiteHelper.GENRE);
            int directorIndex = cursor1.getColumnIndex(SQLiteHelper.DIRECTOR);
            int periodIndex = cursor1.getColumnIndex(SQLiteHelper.TIME_MOVIE);
            int descriptionIndex = cursor1.getColumnIndex(SQLiteHelper.DESCRIPTION);
            int actorsIndex = cursor1.getColumnIndex(SQLiteHelper.ACTORS);
            int trailerIndex = cursor1.getColumnIndex(SQLiteHelper.TRAILER);
            StringBuilder info = new StringBuilder();
            info.append(cursor1.getString(/*titleIndex*/0)).append("\n");
            info.append("Производство: " + cursor1.getString(1/*productionIndex*/)).append("\n");
            info.append("Жанр: " + cursor1.getString(2/*genreIndex*/)).append("\n");
            info.append("Режиссер: " + cursor1.getString(3/*directorIndex*/)).append("\n");
         info.append("Длительность: " + cursor1.getString(4/*periodIndex*/));
            mTextViewDesc.setText(info.toString());
            info = new StringBuilder();
            info.append("   Описание: " + cursor1.getString(/*descriptionIndex*/5)).append("\n\n");
            info.append("   Актеры: " + cursor1.getString(/*actorsIndex*/6)).append("\n------------------------------------------\n");

            /*mTextViewOther.setText(info.toString());
            info = new StringBuilder("Трайлер: ");
            info.append(cursor1.getString(trailerIndex));
            mTextViewTrailer.setTextColor(Color.BLUE);
            mTextViewTrailer.setText(info.toString());*/

        }
    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        cursor1.close();
    }

}
