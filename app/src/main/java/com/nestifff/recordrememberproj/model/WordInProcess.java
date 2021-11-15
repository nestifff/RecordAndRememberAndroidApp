package com.nestifff.recordrememberproj.model;

import java.util.Objects;
import java.util.UUID;

public class WordInProcess extends Word {

    public int numOnFirstTry;

    public WordInProcess(UUID id, String eng, String rus) {
        super(id, eng, rus);
        numOnFirstTry = 0;
    }

    public WordInProcess(String eng, String rus) {
        super(eng, rus);
        numOnFirstTry = 0;
    }

    public WordInProcess(WordLearned word) {
        super(word.id, word.eng, word.rus);
        numOnFirstTry = 0;
    }

    public WordInProcess(UUID id, String eng, String rus, int firstTry) {
        super(id, eng, rus);
        numOnFirstTry = firstTry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordInProcess word = (WordInProcess) o;
        return Objects.equals(eng, word.eng) &&
                Objects.equals(rus, word.rus) &&
                Objects.equals(numOnFirstTry, word.numOnFirstTry) &&
                id.equals(word.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eng, rus, id, numOnFirstTry);
    }

}
