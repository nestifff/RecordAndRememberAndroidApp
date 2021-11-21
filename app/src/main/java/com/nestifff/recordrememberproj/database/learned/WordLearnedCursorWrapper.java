package com.nestifff.recordrememberproj.database.learned;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nestifff.recordrememberproj.database.inProcess.WordsInProcessDB;
import com.nestifff.recordrememberproj.model.word.WordLearned;

import java.util.UUID;

public class WordLearnedCursorWrapper extends CursorWrapper {

    public WordLearnedCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WordLearned getWord() {

        String uuid = getString(getColumnIndex(WordsInProcessDB.WordsInProcessTable.Cols.UUID));
        String eng = getString(getColumnIndex(WordsInProcessDB.WordsInProcessTable.Cols.ENG));
        String rus = getString(getColumnIndex(WordsInProcessDB.WordsInProcessTable.Cols.RUS));

        return new WordLearned(UUID.fromString(uuid), eng, rus);
    }
}