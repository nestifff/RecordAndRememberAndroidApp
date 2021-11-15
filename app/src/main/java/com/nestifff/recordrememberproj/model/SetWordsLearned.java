package com.nestifff.recordrememberproj.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.nestifff.recordrememberproj.database.inProcess.WordInProcessCursorWrapper;
import com.nestifff.recordrememberproj.database.learned.WordLearnedCursorWrapper;
import com.nestifff.recordrememberproj.database.learned.WordsLearnedDB;
import com.nestifff.recordrememberproj.database.learned.WordsLearnedDBHelper;import com.nestifff.recordrememberproj.model.SetWordsInProcess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SetWordsLearned {
    
    private static SetWordsLearned setWordsLearned;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Word> wordsList;

    public static SetWordsLearned get(Context context) {
        if (setWordsLearned == null) {
            setWordsLearned = new SetWordsLearned(context); // context
        }
        return setWordsLearned;
    }

    private SetWordsLearned(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new WordsLearnedDBHelper(mContext).getWritableDatabase();
        wordsList = getAllWordsFromDB();
    }

    public boolean addWord(WordLearned word) {

        if (wordsList.contains(word)) {
            return false;
        }
        ContentValues values = getContentValues(word);
        mDatabase.insert(WordsLearnedDB.WordsLearnedTable.NAME, null, values);
        wordsList.add(word);

        return true;
    }

    public void deleteWord(WordLearned word) {

        String uuidString = word.id.toString();
        mDatabase.delete(WordsLearnedDB.WordsLearnedTable.NAME,
                WordsLearnedDB.WordsLearnedTable.Cols.UUID + " = ?",
                new String[]{uuidString});
        wordsList.remove(word);
    }


    public void moveToInProcess(WordLearned word) {
        deleteWord(word);
        wordsList.remove(word);
        WordInProcess newWord = new WordInProcess(word);
        SetWordsInProcess.get(mContext).addWord(newWord);
    }

    private WordLearnedCursorWrapper queryWords(String whereClause, String[] whereArgs) {

        Cursor cursor = mDatabase.query(WordsLearnedDB.WordsLearnedTable.NAME, null,
                whereClause, whereArgs, null,
                null, null);

        return new WordLearnedCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(WordLearned word) {
        ContentValues values = new ContentValues();

        values.put(WordsLearnedDB.WordsLearnedTable.Cols.UUID, word.id.toString());
        values.put(WordsLearnedDB.WordsLearnedTable.Cols.ENG, word.eng);
        values.put(WordsLearnedDB.WordsLearnedTable.Cols.RUS, word.rus);

        return values;
    }

    public List<Word> getWords(int count) throws Exception {

        if (wordsList == null) {
            wordsList = getAllWordsFromDB();
        }

        List<Word> words = new ArrayList<>();

        int numInBD = wordsList.size();
        if (numInBD == 0) {
            throw new Exception("SetWordsLearned: List<Word> getWords(int count): No words learned");
        }

        if (count >= numInBD) {
            return getAllWords();
        }

        // indexes of randomly chosen words
        ArrayList<Integer> inds = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < count; ++i) {

            int temp;
            do {
                temp = random.nextInt((int) numInBD);
            } while (inds.contains(temp));
            inds.add(temp);
        }

        for (int ind : inds) {
            words.add(wordsList.get(ind));
        }

        return words;
    }

    private List<Word> getAllWordsFromDB() {

        List<Word> words = new ArrayList<>();
        WordLearnedCursorWrapper cursor = queryWords(null, null);

        try {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                words.add(cursor.getWord());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        return new ArrayList<>(words);
    }

    public List<Word> getAllWords() {

        if (wordsList != null) {
            return new ArrayList<>(wordsList);
        }

        wordsList = getAllWordsFromDB();
        return new ArrayList<>(wordsList);
    }

    public int getWordsNum() {

        if (wordsList == null) {
            wordsList = getAllWords();
        }
        return wordsList.size();
    }
}
