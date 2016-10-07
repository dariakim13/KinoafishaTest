package com.example.daria.kinoafishatest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Dasha on 09.06.2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "kinoafisha"; // Имя базы данных
    public static final int DB_VERSION = 3; // Версия базы данных

    public static final String TABLE_SESSIONS = "sessions";
    public static final String TABLE_DESCRIPTION = "description";

    public static final String ID = "_id";

    public static final String MOVIES = "movies";
    public static final String ACTORS = "actors";
    public static final String PRODUCTION = "production";
    public static final String DIRECTOR = "director";
    public static final String GENRE = "genre";
    public static final String DESCRIPTION = "description";
    public static final String TIME_MOVIE = "time";
    public static final String TITLE = "title";
    public static final String POSTER = "poster";
    public static final String PICTURES = "pictures";
    public static final String TRAILER = "trailer";

    public SQLiteHelper(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+ TABLE_SESSIONS +" ("
                + "id integer primary key autoincrement,"
                + "cinema text,"
                + "session text" + ");");

        db.execSQL("CREATE TABLE " + TABLE_DESCRIPTION + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT, " + PRODUCTION + " TEXT, " + GENRE + " TEXT, " + DIRECTOR + " TEXT, "
                + TIME_MOVIE + " TEXT, " + DESCRIPTION + " TEXT, " + ACTORS + " TEXT, " + POSTER + " TEXT, "
                + TRAILER + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion)

        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESCRIPTION);
            onCreate(db);
        }

    }

    public void createMyDataBase(ListOfCinemas list) {
        // подключаемся к БД
        SQLiteDatabase db = this.getWritableDatabase();
        // создаем объект для данных
        ContentValues cv = new ContentValues();

        for (int i = 0; list.listOfCinemas != null && i < list.listOfCinemas.size(); ++i)
        {
            String name = list.listOfCinemas.get(i);
            ArrayList<Session> sessions = list.get(name, 0);
            ArrayList <String> strSessions = new ArrayList<>();
            if (sessions != null) {
                for(Session s : sessions)
                {
                    strSessions.add(s.toString());
                    // получаем данные из полей ввода
                    cv.put("cinema", name);
                    cv.put("session", s.toString());
                    // вставляем запись и получаем ее ID
                    long rowID = db.insert(TABLE_SESSIONS, null, cv);
                    //Log.d("LOG_TAG", "row inserted, ID = " + rowID);
                }
            }

        }

        db.close();
    }

    public void deleteMyDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        int clearCount = db.delete("mytable", null, null);
        Log.d("LOG_TAG", "deleted rows count = " + clearCount);
        db.close();
    }

    public ArrayList<String> readFromMyDatabase(String cinemaName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> result = new ArrayList<>();
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        // переменные для query
        String[] columns = null;
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        // orderBy = session

        selection = "cinema = ?";
        selectionArgs = new String[] { cinemaName };
        Cursor c = db.query(TABLE_SESSIONS, columns, selection, selectionArgs, groupBy, having, orderBy);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int cinemaColIndex = c.getColumnIndex("cinema");
            int sessionColIndex = c.getColumnIndex("session");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("tagread",
                        "ID = " + c.getInt(idColIndex) +
                                ", cinema = " + c.getString(cinemaColIndex) +
                                ", sesion = " + c.getString(sessionColIndex));
                result.add(c.getString(sessionColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("read", "0 rows");
        c.close();
        db.close();
        return result;

    }

    public String[] cinemasFromDatabase(int size)
    {
        String[] cins = new String[size];
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> result;

        String selection = null;
        String[] selectionArgs = null;
        String[] columns = new String[] {"cinema"};
        selection = "cinema != ?";
        selectionArgs = new String[] { "Кыргызкиносу" };
        Cursor c = db.query(true, TABLE_SESSIONS, columns, selection, selectionArgs, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c != null && c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int cinemaColIndex = c.getColumnIndex("cinema");

            int i = 0;
            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("tagread",

                        ", cinema = " + c.getString(cinemaColIndex)
                );
                cins[i] = c.getString(cinemaColIndex);
                ++i;
// переход на следующую строку
// а если следующей нет (текущая - последняя), то false - выходим из цикла
} while (c.moveToNext());
        } else
        Log.d("tagread", "0 rows");
        c.close();
        db.close();
        return cins;

        }
}
