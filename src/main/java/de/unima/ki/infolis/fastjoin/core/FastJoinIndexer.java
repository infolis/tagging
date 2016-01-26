// *****************************************************************************
//
// Copyright (c) 2013 Christian Schreckenberger / Christian Meilicke / Kai Eckert (University of Mannheim)
//
// Permission is hereby granted, free of charge, to any person
// obtaining a copy of this software and associated documentation
// files (the "Software"), to deal in the Software without restriction,
// including without limitation the rights to use, copy, modify, merge,
// publish, distribute, sublicense, and/or sell copies of the Software,
// and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
// IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// *********************************************************************************

package de.unima.ki.infolis.fastjoin.core;

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

import de.unima.ki.infolis.fastjoin.util.ConceptRecord;
import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.fastjoin.util.StringOccurence;
import de.unima.ki.infolis.fastjoin.util.StringRecord;
import de.unima.ki.infolis.lohai.IflConcept;
import de.unima.ki.infolis.lohai.IflRecord;
import de.unima.ki.infolis.lohai.IflRecordSet;

/**
* Indexer that computes TFIDF given a set of records (with title/abstract) and indexes the against
* a thesaurus. Uses the wrapper to FastJoin in doing so. More or less, this is the Abstract/Summary Indexer.
*
*/
public class FastJoinIndexer {
	
	
	private HashMap<StringRecord, HashSet<StringOccurence>> srToSo = new HashMap<StringRecord, HashSet<StringOccurence>>();
	private HashMap<ConceptRecord, HashSet<StringOccurence>> crToSo = new HashMap<ConceptRecord, HashSet<StringOccurence>>();
	
	/**
	* Stores for each token the records that contains this token.
	*/
	private HashMap<String,HashSet<IflRecord>> tokenToRecords = new HashMap<String, HashSet<IflRecord>>();
	
	/**
	* Stores for each record the token counts, i.e. a hashmap in which the number of token occurrences is stored.
	*/
	private HashMap<IflRecord,HashMap<String,Integer>> recordToTokenCount = new HashMap<IflRecord,HashMap<String,Integer>>();

	// private HashMap<String,HashSet<IflRecord>> mTokenToRecords = new HashMap<String, HashSet<IflRecord>>();
	

	// private HashMap<IflRecord,HashMap<String,Integer>> mRecordToTokenCount = new HashMap<IflRecord,HashMap<String,Integer>>();
	
	
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
			ArrayList<ConceptWithScore> scoredConceptsNormalized = new ArrayList<ConceptWithScore>();
			ArrayList<ConceptWithScore> scoredConceptsNotNormalized = new ArrayList<ConceptWithScore>();
			for (Concept concept : this.recordToConceptCount.get(record).keySet()) {
				//for normalized with tf-idf
				double tfidf = this.tfIdf(record, concept);
				ConceptWithScore cws = new ConceptWithScore((IflConcept)concept, tfidf);
				cws.setNormalized(true);
				cws.addAllOccurences(crToSo.get(new ConceptRecord(concept, record)));
				scoredConceptsNormalized.add(cws);
				//for unnormalized
				double score = this.tf(record, concept);
				ConceptWithScore cwsUn = new ConceptWithScore((IflConcept)concept, score);
				cwsUn.setNormalized(false);
				cwsUn.addAllOccurences(crToSo.get(new ConceptRecord(concept, record)));
				scoredConceptsNotNormalized.add(cwsUn);
			}
			Collections.sort(scoredConceptsNormalized);
			Collections.sort(scoredConceptsNotNormalized);
			if (scoredConceptsNormalized.size() > 0 || scoredConceptsNotNormalized.size() > 0) {
				indexedRecords.put(record, new HashSet<ConceptWithScore>());
			}
			for (int i = 0; (i < Settings.TOP_K && i < scoredConceptsNormalized.size()); i++) {
				indexedRecords.get(record).add(scoredConceptsNormalized.get(i));
			}
			for (int i = 0; (i < Settings.TOP_K_UNNORMALIZED && i < scoredConceptsNotNormalized.size()); i++) {
				indexedRecords.get(record).add(scoredConceptsNotNormalized.get(i));
			}
		}
		return indexedRecords;
	}

	
	
	
	private void recreateMatchedHashes() {
		for (String token : this.tokenToRecords.keySet()) {
			String keyword = this.recordTokenToThesaurusToken.get(token);
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
				
				StringRecord sr = new StringRecord(token, record);
				
				
				int number = tokenCount.get(token);
				String keyword = this.recordTokenToThesaurusToken.get(token);
				if (keyword == null) { continue; }
				for (Concept concept : this.tokenToConcept.get(keyword)) {
					
					HashSet<StringOccurence> so = srToSo.get(sr);
					ConceptRecord cr = new ConceptRecord(concept, record);
					if (crToSo.containsKey(cr)) {
						crToSo.get(cr).addAll(so);
					}
					else {
						crToSo.put(cr, so);
					}
					
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
		
		
		// TESTOUTPUT
		
		/*
		for (StringRecord sr : srToSo.keySet()) {
			System.out.println("+++  STRING: " + sr.getString());
			System.out.println("+++  RECORD: " + sr.getRecord());
			for (StringOccurence so : srToSo.get(sr)) {
				System.out.println("******* " + so);	
			}
			System.out.println();	
		}

		
		
		for (ConceptRecord cr : crToSo.keySet()) {
			System.out.println(">>>  CONCEPT: " + cr.getConcept());
			System.out.println(">>>  RECORD: " + cr.getRecord());
			for (StringOccurence so : crToSo.get(cr)) {
				System.out.println(">>>>>>>> " + so);	
			}
		}
		*/
		
		
	}
	
	/**
	* Stores the concepts of the thesaurus internally in hashmap that maps lowercased tokens to concepts and writes
	* the set of token to a file as input to the string matching phase.
	* 
	* @param filePath The filepath to which the output is written.
	*/
	@SuppressWarnings("deprecation")
	private void prepareConcepts(String filePath) {
		for (Concept concept : conceptScheme.getConcepts()) {
			// filter all desriptors
			if (concept.getURI().startsWith(Settings.THESAURUS_NS_FILTER)) continue;
			// no preferred label in the chosen languageu available
			if (concept.getPrefLabel() == null) continue; 
			String token;
			for (Label label : concept.getLabels(lang)) {
				token = label.getText().toLowerCase();
				if(Settings.USE_BLACKLIST){
					boolean escape=false;
					for(String blackListed:Settings.BLACKLIST){
						escape=blackListed.equalsIgnoreCase(token);
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
			HashMap<String,Integer> tokenCount = new HashMap<String, Integer>();
			IflRecord r = (IflRecord)record;
			
			
			// r.getTerms();
			
			
			if (Settings.USE_TERMS) {
				// TODO i added an if here, because otherwise there is an exception, why ???
				List<String> terms=r.getTerms();
				StringBuffer  text = new StringBuffer("");
				if (terms != null) {
					for (String b:terms){
						text.append(b);
						text.append("");
					}
				}
				parseText(record, text, "TERM", tokenCount);
			}
			
			
			if (r.getTitle() != null && Settings.USE_TITLE) {
				StringBuffer text = new StringBuffer(r.getTitle());
				parseText(record, text, "TITLE", tokenCount);
			}
			
			if (r.getAbstractText() != null && Settings.USE_ABSTRACT) {
				StringBuffer text = new StringBuffer(r.getAbstractText());
				parseText(record, text, "ABSTRACT", tokenCount);
			}
			
			if (r.getSubjectHeadings() != null && Settings.USE_SUBJECTHEADINGS) {
				StringBuffer text = new StringBuffer();
				for (String sh : r.getSubjectHeadings()) {
					text.append(sh);
					text.append(" ");
				}
				parseText(record, text, "SUBJECTHEADINGS", tokenCount);
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

	private void parseText(IflRecord record, StringBuffer recordText, String origin, HashMap<String,Integer> tokenCount) {
		StringTokenizer st = new StringTokenizer(recordText.toString(), Settings.DELIMITER);
		String token;
		String prevToken = null; // the previous token of a token
		String prev2Token = null; // the previous token of a previous token
		
		int tokenCounter = 0;
		

		
		while (st.hasMoreTokens()) {
			tokenCounter++;
			token = (st.nextToken());
			if(Settings.USE_CAMELCASESPLIT){
				String[] parts = token.split("(?=\\p{Upper})");
				if(parts.length>1){
					for(int i=0; i<parts.length; i++) {
						extendTokenToRecordMapping(record, parts[i].toLowerCase(), tokenCount, tokenCounter, 1,  origin);
						if(i != 0 && Settings.USE_2_GRAM){
							extendTokenToRecordMapping(record, parts[i-1].toLowerCase() + " " + parts[i].toLowerCase(), tokenCount, tokenCounter, 2, origin);
						}
					}
				}
			}

			token = token.toLowerCase();
			extendTokenToRecordMapping(record, token, tokenCount, tokenCounter, 1, origin);
			if (Settings.USE_2_GRAM && prevToken != null) extendTokenToRecordMapping(record, prevToken + " " + token, tokenCount, tokenCounter, 2, origin);
			if (Settings.USE_3_GRAM && prev2Token != null) extendTokenToRecordMapping(record, prev2Token + " " + prevToken + " " + token, tokenCount, tokenCounter, 3,  origin);
			prev2Token = prevToken;
			prevToken = token;
		}
	}

	private void extendTokenToRecordMapping(IflRecord record, String token, HashMap<String, Integer> tokenCount, int tokenCounter, int ngram, String origin) {
		
		if (token != null) {
			StringRecord sr = new StringRecord(token, record);
			if (!(srToSo.containsKey(sr))) {
				srToSo.put(sr, new HashSet<StringOccurence>());
			}
			
			StringOccurence so = new StringOccurence(token, tokenCounter, ngram, origin);					
			srToSo.get(sr).add(so);	
		}
		
		
		
		
		
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
	



}