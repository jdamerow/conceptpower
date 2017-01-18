package edu.asu.conceptpower.app.lucene;

import edu.asu.conceptpower.core.IndexingEvent;

public interface ILuceneDAO {

    public void storeValues(long numberOfIndexedWords,String action);

    public IndexingEvent getTotalNumberOfWordsIndexed();
}
