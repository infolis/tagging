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

package de.unima.ki.infolis.fastjoin.indexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semtinel.core.data.api.AnnotationSource;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.skos.impl.SKOSManager;

import de.unima.ki.infolis.fastjoin.core.FastJoinIndexer;
import de.unima.ki.infolis.fastjoin.core.Settings;
import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.fastjoin.util.LinkResolver;
import de.unima.ki.infolis.fastjoin.util.Reader;
import de.unima.ki.infolis.lohai.IflAnnotationSource;
import de.unima.ki.infolis.lohai.IflConcept;
import de.unima.ki.infolis.lohai.IflConceptScheme;
import de.unima.ki.infolis.lohai.IflRecord;
import de.unima.ki.infolis.lohai.IflRecordSet;

/*
* A link indexer is an an indexer that uses links between studies and documents to
* suggest keywords for a study.
*/
public class LinkIndexer implements Indexer {
	
	private LinkResolver lr;
	private IflRecordSet ssoarRS;
	private IflRecordSet daraRS;
	private LinkIndexerMode mode;
	private HashMap<IflRecord, HashSet<ConceptWithScore>> results;
	
	/**
	 * Constructs a LinkIndexer by specifying the path to the link file.
	 * 
	 * @param linkFile The path to the file that contains the links.
	 */
	public LinkIndexer(String linkFile, LinkIndexerMode mode) {
		this.lr = new LinkResolver(linkFile);
		this.mode = mode;
		this.results = new HashMap<IflRecord, HashSet<ConceptWithScore>>();
	}
	
	
	/**
	* Loads the studies and attaches them to the link resolver.
	* 
	* @param ssoarDir Directory with the ssoar files.
	*/
	public void loadSSOARStudies(String ssoarDir) {
		System.out.println("\nLoading ssoar docs ...");
        this.ssoarRS = Reader.createSSOARRecordSet(ssoarDir);
        lr.setPublicationSet(ssoarRS);
	}
	
	/**
	* Loads the publications and attaches them to the link resolver.
	* 
	* @param daraDir Directory with dara files.
	*/
	public void loadDaraPublications(String daraDir) {
        System.out.println("\nLoading dara studies ...");
        this.daraRS = Reader.createDaraRecordSet(daraDir);
        lr.setStudySet(this.daraRS);
	}

	public void run() {
		// set preferred language
        IflConcept.preferredLangaue = Settings.chosenLang;
        // load concept scheme
		SKOSManager man = new SKOSManager(Settings.thesozURL);
		ConceptScheme iflCs = new IflConceptScheme(man.getConceptSchemes().get(0), Settings.thesozURL);
		 AnnotationSource target = new IflAnnotationSource(iflCs);
		// load record set of ssoar documents
		this.loadSSOARStudies(Settings.ssoarDirPath);
        // load recordset of dara studies
		this.loadDaraPublications(Settings.daraDirPath);
		Settings.TOP_K_UNNORMALIZED = 0;
		// !! -> index and then join keywords <- !!
		if(LinkIndexerMode.INDEX_AND_JOIN_KEYWORDS==mode){
			this.indexAndJoin(iflCs, target);
		}
		// !! -> join documents and then index <- !!
		if(LinkIndexerMode.JOIN_DOCUMENTS_AND_INDEX==mode){
			this.joinAndIndex(iflCs, target);
		}
		
	}
	
	public void printStatus() {
		System.out.println("Dara-Records (Studien): " + ((this.daraRS != null) ? this.daraRS.getSize() : 0));
		System.out.println("SSOAR-Records (Publikationen):      " + ((this.ssoarRS != null) ? this.ssoarRS.getSize() : 0));
	}
	

	@Override
	public HashMap<IflRecord, HashSet<ConceptWithScore>> getResults() {
		return this.results;
	}
	
	private void indexAndJoin(ConceptScheme iflCs, AnnotationSource target){
		if(LinkIndexerMode.INDEX_AND_JOIN_KEYWORDS==mode){
			FastJoinIndexer indexer = new FastJoinIndexer(Settings.chosenLang, target, iflCs, ssoarRS);
	        HashMap<IflRecord, HashSet<ConceptWithScore>> indexedRecords = indexer.index();
//	        this.results = new HashMap<IflRecord, HashSet<ConceptWithScore>>();
	        for (IflRecord study : daraRS) {
	        	
	        	if (lr.hasLinkedPublications(study)) {
	        		Set<IflRecord> publications = lr.getPublicationsByStudy(study);
	        		if(publications.size()<Settings.AT_LEAST_LINKED_BY)continue;
	        		HashMap<IflRecord,List<ConceptWithScore>> res = new HashMap<IflRecord, List<ConceptWithScore>>();
	        		for (IflRecord publication : publications) {
	        			if (publication == null) continue;
	        			ArrayList<ConceptWithScore> topXofPub = new ArrayList<ConceptWithScore>();
	        			//the way it is counted
	        			if (indexedRecords.containsKey(publication)) {
	            			for (ConceptWithScore cws :  indexedRecords.get(publication)) {
	            				topXofPub.add(cws);
	            			}
	            			res.put(publication, topXofPub);
	        			}
	        		}
	        		HashMap <IflConcept,Integer> scoredConcept = new HashMap<IflConcept, Integer>();
	        		for(IflRecord pub: res.keySet()){
	        			List<ConceptWithScore> tempList= res.get(pub);
	        			for(ConceptWithScore cws:tempList){
	        				if(scoredConcept.containsKey(cws.getConcept())){
	        					scoredConcept.put(cws.getConcept(), scoredConcept.get(cws.getConcept())+1);
	        				}else{
	        					scoredConcept.put(cws.getConcept(), 1);
	        				}
	        			}	
	        		}
	        		HashSet<ConceptWithScore> resSet = new HashSet<ConceptWithScore>();
	        		ArrayList<ConceptWithScore> temp = new ArrayList<ConceptWithScore>();
	        		for(IflConcept con:scoredConcept.keySet()){
	        			temp.add(new ConceptWithScore(con, scoredConcept.get(con)));
	        		}
	        		for(int i = temp.size()-1;i>=0;i--){
	        			if(Settings.THRESHOLD<=temp.get(i).getScore()/publications.size()){
	        				double a=temp.get(i).getScore()/publications.size();
	        				ConceptWithScore cws = new ConceptWithScore(temp.get(i).getConcept(), temp.get(i).getScore()/publications.size());
	        				resSet.add(cws);
	        			}
	        		}
	        		//end of counting
	        		results.put(study, resSet);
	        	}
	        }
		}
	}
	
	private void joinAndIndex (ConceptScheme iflCs, AnnotationSource target){
		IflRecordSet setOfLinkedPubs = new IflRecordSet();
		HashMap<String, String> doiToStudyTitle = new HashMap<String, String>();
		for(IflRecord study : daraRS){
			if(lr.hasLinkedPublications(study)){
				doiToStudyTitle.put(study.getIdentifier(), study.getTitle());
            	IflRecord combinedAbstracts = new IflRecord();
            	combinedAbstracts.setIdentifier(study.getIdentifier());
            	Set<IflRecord> publications = lr.getPublicationsByStudy(study);
            	for(IflRecord publication : publications){
            		if(publication==null)continue;
            		if(combinedAbstracts.getAbstractText()==null){
            			combinedAbstracts.setAbstractText(publication.getAbstractText());
            		}else {
            			combinedAbstracts.setAbstractText(publication.getAbstractText()+" "+combinedAbstracts.getAbstractText());
            		}
            		if(combinedAbstracts.getTitle()==null&&publication.getTitle()!=null){
            			combinedAbstracts.setTitle(publication.getTitle());
            		}else{
            			combinedAbstracts.setTitle(publication.getTitle()+" "+combinedAbstracts.getTitle());
            		}
            	}
            	setOfLinkedPubs.addRecord(combinedAbstracts);
			}
		}
		FastJoinIndexer indexer = new FastJoinIndexer(Settings.chosenLang, target, iflCs, setOfLinkedPubs);
        HashMap<IflRecord, HashSet<ConceptWithScore>> indexedRecords = indexer.index();
        for(IflRecord record:indexedRecords.keySet()){
        	String title= doiToStudyTitle.get(record.getIdentifier());
        	HashSet<ConceptWithScore> cwsSet = indexedRecords.get(record);
        	record.setTitle(title);
        	indexedRecords.put(record, cwsSet);
        }
        this.results=indexedRecords;
        System.out.println("postprocessing index and join: " + System.currentTimeMillis());
	}

}
