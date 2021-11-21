package com.nestifff.recordrememberproj.model.word;

import java.util.UUID;

public class Word implements Comparable<Word> {

    public String eng;
    public String rus;
    public UUID id;

    protected Word(String eng, String rus) {
        this.eng = eng;
        this.rus = rus;
        id = UUID.randomUUID();
    }

    protected Word(UUID id, String eng, String rus) {
        this.eng = eng;
        this.rus = rus;
        this.id = id;
    }

    public boolean isCorrect(boolean rusEngWay, String tryAns) {
        String rightAns;

        if (rusEngWay) {
            rightAns = eng;
        } else {
            rightAns = rus;
        }

        rightAns = rightAns.toLowerCase();
        tryAns = tryAns.toLowerCase();

        if (rightAns.equals(tryAns)) {
            return true;
        }
        // one incorrect letter
        else if (rightAns.length() == tryAns.length()) {

            boolean oneIncorrect = false;
            for (int i = 0; i < rightAns.length(); ++i) {
                if (rightAns.charAt(i) != tryAns.charAt(i)) {
                    if(oneIncorrect) {
                        return false;
                    }
                    else {
                        oneIncorrect = true;
                    }
                }
            }

            return true;

        }

        // one letter is skipped
        else if (rightAns.length() == tryAns.length() + 1) {

            boolean oneIsSkipped = false;
            for (int i = 0; i < rightAns.length() - 1; ++i) {

                int ind = i;
                if (oneIsSkipped) {
                    ++ind;
                }

                if (rightAns.charAt(ind) != tryAns.charAt(i)) {
                    if (!oneIsSkipped) {
                        oneIsSkipped = true;
                    }
                    else {
                        return false;
                    }
                }
            }

            return true;

        }

        return false;
    }

    public String getAnswer(boolean rusEngWay) {
        if (rusEngWay) {
            return eng;
        }
        return rus;
    }

    @Override
    public int compareTo(Word w) {
        return this.eng.compareToIgnoreCase(w.eng);
    }
}
