package com.nestifff.recordrememberproj.model;

import com.nestifff.recordrememberproj.model.WordInProcess;import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class WordLearned extends Word {

    public WordLearned(String eng, String rus) {
        super(eng, rus);
    }

    public WordLearned(UUID id, String eng, String rus) {
        super(id, eng, rus);
    }

    public WordLearned(WordInProcess word) {
        super(word.id, word.eng, word.rus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordLearned word = (WordLearned) o;
        return Objects.equals(eng, word.eng) &&
                Objects.equals(rus, word.rus) &&
                id.equals(word.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eng, rus, id);
    }

}
