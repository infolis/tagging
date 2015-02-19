package de.unima.ki.infolis.fastjoin.indexer;

import java.util.HashMap;
import java.util.HashSet;

import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.lohai.IflRecord;

public interface Indexer {
	
	/**
	* Computes the subject headings for the chosen set of documents or studies.
	*/
	public void run() ;

	
	/**
	* Returns the results of the indexing.
	* 
	* @return The indexed record set as a has from record to concepts.
	*/
	public HashMap<IflRecord, HashSet<ConceptWithScore>> getResults();

}
