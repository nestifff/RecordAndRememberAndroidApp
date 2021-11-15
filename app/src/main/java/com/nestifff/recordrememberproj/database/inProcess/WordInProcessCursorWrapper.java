package com.nestifff.recordrememberproj.database.inProcess;

// Cursor класс не слишком удобен для работы с данными талицы
// поэтому впихнем всю жуть сюда и будем пользоваться как будто так и надо

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nestifff.recordrememberproj.model.WordInProcess;
import com.nestifff.recordrememberproj.database.inProcess.WordsInProcessDB.WordsInProcessTable;

import java.util.UUID;

public class WordInProcessCursorWrapper extends CursorWrapper {

    public WordInProcessCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WordInProcess getWord () {

        String uuid = getString(getColumnIndex(WordsInProcessTable.Cols.UUID));
        String eng = getString(getColumnIndex(WordsInProcessTable.Cols.ENG));
        String rus = getString(getColumnIndex(WordsInProcessTable.Cols.RUS));
        int numOnFirstTry = getInt(getColumnIndex(WordsInProcessTable.Cols.NUM_ON_FIRST_TRY));

        WordInProcess word = new WordInProcess(UUID.fromString(uuid), eng, rus, numOnFirstTry);
        return word;
    }
}
