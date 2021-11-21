package com.nestifff.recordrememberproj.database.inProcess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nestifff.recordrememberproj.database.inProcess.WordsInProcessDB.WordsInProcessTable;

public class WordsInProcessDBHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public WordsInProcessDBHelper(Context context) {
        super(context, WordsInProcessTable.NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + WordsInProcessTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                WordsInProcessTable.Cols.UUID + ", " +
                WordsInProcessTable.Cols.ENG + ", " +
                WordsInProcessTable.Cols.RUS + ", " +
                WordsInProcessTable.Cols.NUM_ON_FIRST_TRY + ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + WordsInProcessTable.NAME);
        onCreate(db);
    }

}
