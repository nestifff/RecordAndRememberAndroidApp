package com.nestifff.recordrememberproj.database.learned;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class WordsLearnedDBHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public WordsLearnedDBHelper(Context context) {
        super(context, WordsLearnedDB.WordsLearnedTable.NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + WordsLearnedDB.WordsLearnedTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                WordsLearnedDB.WordsLearnedTable.Cols.UUID + ", " +
                WordsLearnedDB.WordsLearnedTable.Cols.ENG + ", " +
                WordsLearnedDB.WordsLearnedTable.Cols.RUS + ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + WordsLearnedDB.WordsLearnedTable.NAME);
        onCreate(db);
    }


}
