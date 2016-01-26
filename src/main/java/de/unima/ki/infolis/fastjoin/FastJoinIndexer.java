/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unima.ki.infolis.fastjoin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.semtinel.core.data.api.AnnotationSource;
import org.semtinel.core.data.api.Concept;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.data.api.Label;
import org.semtinel.core.data.api.Language;
import org.semtinel.core.data.api.Record;

import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.lohai.IflConcept;
import de.unima.ki.infolis.lohai.IflRecord;
import de.unima.ki.infolis.lohai.IflRecordSet;

public class FastJoinIndexer {
	
	/**
	* Stores for each token the records that contains this token.
	*/
	private HashMap<String,HashSet<IflRecord>> tokenToRecords = new HashMap<String, HashSet<IflRecord>>();
	
	/**
	* Stores for each record the token counts, i.e. a hashmap in which the number of token occurrences is stored.
	*/
	private HashMap<IflRecord,HashMap<String,Integer>> recordToTokenCount = new HashMap<IflRecord,HashMap<String,Integer>>();

	private HashMap<String,HashSet<IflRecord>> mTokenToRecords = new HashMap<String, HashSet<IflRecord>>();
	

	private HashMap<IflRecord,HashMap<String,Integer>> mRecordToTokenCount = new HashMap<IflRecord,HashMap<String,Integer>>();
	
	
	private HashMap<Concept,HashSet<IflRecord>> conceptToRecords = new HashMap<Concept, HashSet<IflRecord>>();
	

	private HashMap<IflRecord,HashMap<Concept,Integer>> recordToConceptCount = new HashMap<IflRecord,HashMap<Concept,Integer>>();
	
	
	/**
	* Stores for each (thesaurus based) token the concepts from the thesaurus that use this token.
	*/
	private HashMap<String,HashSet<Concept>> tokenToConcept = new HashMap<String,HashSet<Concept>>();
	
	private HashMap<String,String> recordTokenToThesaurusToken = new HashMap<String,String>();
	
	// private HashMap<String,Record> urnToRecord = new HashMap<String,Record>();
	

	private ConceptScheme conceptScheme;
	private IflRecordSet recordSet;
	private Logger log = Logger.getLogger(getClass().getName());
	private Language lang;
	private AnnotationSource target;





	public FastJoinIndexer(Language lang, AnnotationSource target, ConceptScheme conceptScheme, IflRecordSet recordSet) {
		this.lang = lang;
		this.target = target;
		this.conceptScheme = conceptScheme;
		this.recordSet = recordSet;
	}

	public HashMap<IflRecord, HashSet<ConceptWithScore>> index() {
		HashMap<IflRecord, HashSet<ConceptWithScore>> indexedRecords = new HashMap<IflRecord, HashSet<ConceptWithScore>>();
		AnnotationSource as = target;
		as.setName("FastJoin");
		as.setConceptScheme(this.conceptScheme);
		log.info("Indexing started");
		List<IflRecord> records = recordSet.getRecords();
		log.info("Number of records: " + records.size());
		
		this.prepareConcepts(Settings.THESAURUS_TOKEN_FILEPATH);
		this.prepareRecords(Settings.RECORDS_TOKEN_FILEPATH);
		
		FastJoinWrapper fjw = new FastJoinWrapper();
		this.recordTokenToThesaurusToken = fjw.join(Settings.THESAURUS_TOKEN_FILEPATH, Settings.RECORDS_TOKEN_FILEPATH);
		this.recreateMatchedHashes();
		/*
		for (IflRecord record : this.recordToConceptCount.keySet()) {
			this.urnToRecord.put(record.getIdentifier(), record);
		}
		*/

		// new version
		for (IflRecord record : this.recordToConceptCount.keySet()) {
			System.out.println();
			System.out.println("Record: " + record);
			ArrayList<ConceptWithScore> scoredConcepts = new ArrayList<ConceptWithScore>();
			for (Concept concept : this.recordToConceptCount.get(record).keySet()) {
				double tfidf = this.tfIdf(record, concept);
				ConceptWithScore cws = new ConceptWithScore((IflConcept)concept, tfidf);
				scoredConcepts.add(cws);
			}
			Collections.sort(scoredConcepts);
			if (scoredConcepts.size() > 0) {
				indexedRecords.put(record, new HashSet<ConceptWithScore>());
			}
			for (int i = 0; (i < Settings.TOP_K && i < scoredConcepts.size()); i++) {
				indexedRecords.get(record).add(scoredConcepts.get(i));
				// System.out.println("\t" + scoredConcepts.get(i));
			}
		}
		return indexedRecords;
	}
	
	/*
	private void recreateMatchedHashesX() {
		for (String token : this.tokenToRecords.keySet()) {
			String keyword = this.recordTokenToThesaurusToken.get(token);
			if (!(this.mTokenToRecords.containsKey(keyword))) {
				this.mTokenToRecords.put(keyword, new HashSet<Record>());
			}
			this.mTokenToRecords.get(keyword).addAll(this.tokenToRecords.get(token));	
		}
		boolean display = false;
		for (Record record : this.recordToTokenCount.keySet()) {
			if (record.toString().equals("Fragen nach den Antworten eines Jahrhunderts der Psychologie")) display = true;
			else display = false;
			HashMap<String, Integer> tokenCount = this.recordToTokenCount.get(record);
			if (!(this.mRecordToTokenCount.containsKey(record))) {
				this.mRecordToTokenCount.put(record, new HashMap<String, Integer>());
			}
			for (String token : tokenCount.keySet()) {
				int number = tokenCount.get(token);
				if (display) System.out.println("token " + token + " occurs " +  number + " times");
				String keyword = this.recordTokenToThesaurusToken.get(token);
				if (display) System.out.println("token " + token + " is associated with keyword " +  keyword);
				if (!(this.mRecordToTokenCount.get(record).containsKey(keyword))) {
					this.mRecordToTokenCount.get(record).put(keyword, number);
				}
				else {
					this.mRecordToTokenCount.get(record).put(keyword, number + this.mRecordToTokenCount.get(record).get(keyword));
				}
			}
		}
	}
	*/
	
	
	
	private void recreateMatchedHashes() {
		for (String token : this.tokenToRecords.keySet()) {
			String keyword = this.recordTokenToThesaurusToken.get(token);
			// System.out.println("keyword: " +  keyword + " associated with token " +  token);
			if (keyword == null) { continue; }
			for (Concept concept : this.tokenToConcept.get(keyword)) {
				if (!(this.conceptToRecords.containsKey(concept))) {
					this.conceptToRecords.put(concept, new HashSet<IflRecord>());
				}
				this.conceptToRecords.get(concept).addAll(this.tokenToRecords.get(token));
			}
		}
		for (IflRecord record : this.recordToTokenCount.keySet()) {
			HashMap<String, Integer> tokenCount = this.recordToTokenCount.get(record);
			if (!(this.recordToConceptCount.containsKey(record))) {
				this.recordToConceptCount.put(record, new HashMap<Concept, Integer>());
			}
			for (String token : tokenCount.keySet()) {
				int number = tokenCount.get(token);
				String keyword = this.recordTokenToThesaurusToken.get(token);
				if (keyword == null) { continue; }
				for (Concept concept : this.tokenToConcept.get(keyword)) {
					// new
					if (!(this.recordToConceptCount.get(record).containsKey(concept))) {
						this.recordToConceptCount.get(record).put(concept, number);
					}
					else {
						this.recordToConceptCount.get(record).put(concept, number + this.recordToConceptCount.get(record).get(concept));
					}
				}
			}
		}
	}
	
	/**
	* Stores the concepts of the thesaurus internally in hashmap that maps lowercased tokens to concepts and writes
	* the set of token to a file as input to the string matching phase.
	* 
	* @param filePath The filepath to which the output is written.
	*/
	private void prepareConcepts(String filePath) {
		for (Concept concept : conceptScheme.getConcepts()) {
			// filter all desriptors
			if (concept.getURI().startsWith(Settings.THESAURUS_NS_FILTER)) continue;
			// no preferred label in the chosen languageu available
			if (concept.getPrefLabel() == null) continue; 
			String token;
			// System.out.println("CONCEPT: " + concept);
			for (Label label : concept.getLabels(lang)) {
				token = label.getText().toLowerCase();
				// System.out.println("\t>>> " + token);
				if(Settings.USE_BLACKLIST){
					boolean escape=false;
					for(String blackListed:Settings.BLACKLIST){
						escape=blackListed.equals(token);
						if(escape)break;
					}
					if(escape)continue;
				}
				
				if (!(this.tokenToConcept.containsKey(token))) { this.tokenToConcept.put(token, new HashSet<Concept>()); }
				this.tokenToConcept.get(token).add(concept);
			}
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
			for (String token : this.tokenToConcept.keySet()) {
				out.write(token + "\n");
			}
			out.close();
		}
		catch (IOException e) { e.printStackTrace(); }
		log.info("Number of labels in thesaurus: " + this.tokenToConcept.size());
	}
	
	private void prepareRecords(String filePath) {
		List<IflRecord> records = recordSet.getRecords();
		// loop over all records and hash the token that appear there
		for (IflRecord record : records) {
			IflRecord r = (IflRecord)record;
			StringBuffer recordText = new StringBuffer("");
			if (Settings.USE_TERMS) {
				List<String> terms=r.getTerms();
				String c="";
				for(String b:terms){
					c+=" " + b;
				}
				for (int i = 0;i < Settings.WEIGH_TERMS;i++) {
					recordText.append(c);
					recordText.append(" ");
				}
			}
			if (Settings.USE_TITLE) {
				recordText.append(r.getTitle());
				recordText.append(" ");
			}
			if (Settings.USE_ABSTRACT) {
				recordText.append(r.getAbstractText());
				recordText.append(" ");
			}
			if (Settings.USE_SUBJECTHEADINGS) {
				for (String sh : r.getSubjectHeadings()) {
					recordText.append(sh);
					recordText.append(" ");
				}
			}
			StringTokenizer st = new StringTokenizer(recordText.toString(), Settings.DELIMITER);
			String token;
			HashMap<String,Integer> tokenCount = new HashMap<String, Integer>();
			String prevToken = null; // the previous token of a token
			String prev2Token = null; // the previous token of a previous token
			while (st.hasMoreTokens()) {
				token = (st.nextToken());
				String[] parts = token.split("(?=\\p{Upper})");
				token = token.toLowerCase();
				if(parts.length>1){
					for(int i=0;i<parts.length;i++){
						extendTokenToRecordMapping(record, parts[i].toLowerCase(), tokenCount);
					}
				}
				extendTokenToRecordMapping(record, token, tokenCount);
				if (Settings.USE_2_GRAM && prevToken != null) extendTokenToRecordMapping(record, prevToken + " " + token, tokenCount);
				if (Settings.USE_3_GRAM && prev2Token != null) extendTokenToRecordMapping(record, prev2Token + " " + prevToken + " " + token, tokenCount);
				prev2Token = prevToken;
				prevToken = token;
	
			}
			this.recordToTokenCount.put(record, tokenCount);
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
			for (String token : this.tokenToRecords.keySet()) {
				out.write(token + "\n");
			}
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void extendTokenToRecordMapping(IflRecord record, String token, HashMap<String, Integer> tokenCount) {
		if (!tokenCount.containsKey(token)) tokenCount.put(token, 1);
		else tokenCount.put(token, tokenCount.get(token) + 1);
		if (this.tokenToRecords.containsKey(token)) {
			this.tokenToRecords.get(token).add(record);
		}
		else {
			this.tokenToRecords.put(token, new HashSet<IflRecord>());
			this.tokenToRecords.get(token).add(record);
		}
	}

	
	/**
	* Returns the tf-idf.
	* 
	* @param record The record for which the value is computed.
	* @param token The token for which the values is computed.
	* @return
	*/
	private double tfIdf(Record record, Concept concept) {
		return this.tf(record, concept) * this.idf(concept);
		
	}
	
	/**
	* Returns the term frequency of a concept within a record.
	* 
	* @param record the record that is analyzed.
	* @param token The concept for which all token occurrences are counted.
	* @return The number of occurrences of the concept within the specific document.
	*/
	private double tf(Record record, Concept concept) {
		return this.recordToConceptCount.get(record).get(concept);
		
	}
	
	/**
	* Returns the inverse document frequency.
	* 
	* @param token The conceptfor which the inverse document frequency is counted.
	* @return Inverse document frequency = log(number of all records / number of records that contain tokens related to this concept)
	*/
	private double idf(Concept concept) {
		if (!this.conceptToRecords.containsKey(concept)) {
			return Double.MAX_VALUE;
		}
		else {
			return Math.log(this.recordSet.getSize() / this.conceptToRecords.get(concept).size());
		}
		
	}
	
	
	// **** old code, left there for cross comparison
	
	/**
	* Returns the tf-idf.
	* 
	* @param record The record for which the value is computed.
	* @param token The token for which the values is computed.
	* @return
	*/
	private double tfIdfX(Record record, String token) {
		return this.tfX(record, token) * this.idfX(token);
		
	}
	
	/**
	* Returns the term frequency of a concept within a record.
	* 
	* @param record the record that is analyzed.
	* @param token The concept for which all token occurrences are counted.
	* @return The number of occurrences of the concept within the specific document.
	*/
	private double tfX(Record record, String token) {
		return this.mRecordToTokenCount.get(record).get(token);
		
	}
	
	/**
	* Returns the inverse document frequency.
	* 
	* @param token The conceptfor which the inverse document frequency is counted.
	* @return Inverse document frequency = log(number of all records / number of records that contain tokens related to this concept)
	*/
	private double idfX(String token) {
		if (!this.mTokenToRecords.containsKey(token)) {
			return Double.MAX_VALUE;
		}
		else {
			return Math.log(this.recordSet.getSize() / this.mTokenToRecords.get(token).size());
		}
		
	}



}