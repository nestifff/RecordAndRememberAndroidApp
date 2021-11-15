package com.nestifff.recordrememberproj.model;

import static com.nestifff.recordremember.GlobalVariablesKt.NUM_ON_FIRST_TRY_TO_MOVE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.nestifff.recordrememberproj.database.inProcess.WordInProcessCursorWrapper;
import com.nestifff.recordrememberproj.database.inProcess.WordsInProcessDB.WordsInProcessTable;
import com.nestifff.recordrememberproj.database.inProcess.WordsInProcessDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class SetWordsInProcess {

    private static SetWordsInProcess setWordsInProcess;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Word> wordsList;

    public static SetWordsInProcess get(Context context) {
        if (setWordsInProcess == null) {
            setWordsInProcess = new SetWordsInProcess(context); // context
        }
        return setWordsInProcess;
    }

    private SetWordsInProcess(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new WordsInProcessDBHelper(mContext).getWritableDatabase();
        wordsList = getAllWordsFromDB();
    }

    public boolean addWord(WordInProcess word) {

        if (wordsList.contains(word)) {
            return false;
        }
        ContentValues values = getContentValues(word);
        mDatabase.insert(WordsInProcessTable.NAME, null, values);
        wordsList.add(word);

        return true;
    }

    public void deleteWord(WordInProcess word) {

        String uuidString = word.id.toString();
        mDatabase.delete(WordsInProcessTable.NAME,
                WordsInProcessTable.Cols.UUID + " = ?",
                new String[]{uuidString});
        wordsList.remove(word);
    }

    public void updateWord(WordInProcess word) {

        String uuidString = word.id.toString();
        ContentValues values = getContentValues(word);

        mDatabase.update(WordsInProcessTable.NAME, values,
                WordsInProcessTable.Cols.UUID + " = ?",
                new String[]{uuidString});

        if (wordsList != null) {

            for (Word w : wordsList) {
                if (w.id.equals(word.id)) {
                    w.rus = word.rus;
                    w.eng = word.eng;
                    ((WordInProcess) w).numOnFirstTry = word.numOnFirstTry;
                }
            }
        }

    }

    // true if add to learned (numOnFirstTry >= someValue)
    public boolean addNumOnFirstTry(WordInProcess word) {

        word.numOnFirstTry += 1;

        if (word.numOnFirstTry < NUM_ON_FIRST_TRY_TO_MOVE) {
            updateWord(word);
            return false;
        }

        deleteWord(word);
        WordLearned newWord = new WordLearned(word);
        SetWordsLearned.get(mContext).addWord(newWord);
        return true;
    }

    private WordInProcessCursorWrapper queryWords(String whereClause, String[] whereArgs) {

        Cursor cursor = mDatabase.query(WordsInProcessTable.NAME, null,
                whereClause, whereArgs, null,
                null, null);

        return new WordInProcessCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(WordInProcess word) {
        ContentValues values = new ContentValues();

        values.put(WordsInProcessTable.Cols.UUID, word.id.toString());
        values.put(WordsInProcessTable.Cols.ENG, word.eng);
        values.put(WordsInProcessTable.Cols.RUS, word.rus);
        values.put(WordsInProcessTable.Cols.NUM_ON_FIRST_TRY, word.numOnFirstTry);

        return values;
    }

    public List<Word> getWords(int count) throws Exception {

        if (wordsList == null) {
           wordsList = getAllWordsFromDB();
        }

        List<Word> words = new ArrayList<>();

        int numInBD = wordsList.size();
        if (numInBD == 0) {
            throw new Exception("SetWordsInProcess: List<Word> getWords(int count): No words in process");
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
        WordInProcessCursorWrapper cursor = queryWords(null, null);

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



