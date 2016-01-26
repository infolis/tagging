package de.unima.lski.ap3.analyse;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import de.unima.lski.ap3.util.POStaggedDocument;
import de.unima.lski.ap3.util.POStaggedDocumentSet;
import de.unima.lski.ap3.util.StudyDocument;
import de.unima.lski.ap3.util.StudyDocumentSet;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.WhitespaceTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POStagging {
	private StudyDocumentSet docList;
	private POStaggedDocumentSet posDocSet;
	private MaxentTagger taggerDE;
	private MaxentTagger taggerEN;
	
	public void setPosDocSet(POStaggedDocumentSet posSet){
		this.posDocSet=posSet;
	}
	public POStaggedDocumentSet getPosDocSet(){
		return posDocSet;
	}
	
	public POStagging(String dir){
		ReadInDocuments read = new ReadInDocuments(dir);
		docList=new StudyDocumentSet();
		docList.setStudyDocumentSet(read.getStudyDocumentSetObject().getStudyDocumentSet());
		posDocSet =new POStaggedDocumentSet();
		try {
			taggerEN = new MaxentTagger("./tagger/english-left3words-distsim.tagger");
			taggerDE = new MaxentTagger("./tagger/german-dewac.tagger");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void tagging(){
		ArrayList<StudyDocument> documentList =docList.getStudyDocumentSet();
		ArrayList<POStaggedDocument> posTaggedDocs= new ArrayList<POStaggedDocument>();
		for(int i=0;i<documentList.size();i++){
			StudyDocument doc=documentList.get(i);
			POStaggedDocument posDoc = new POStaggedDocument();
			posDoc.setIdentifier(doc.getIdentifier());
			posDoc.setWglIdentifier(doc.getWglIdentifier());
			
			if(!(doc.getAbstractEnglish()==null)){
				
				HashSet<String> set = new HashSet<String>();
				String taggedText=taggerEN.tagString(doc.getAbstractEnglish());
				StringReader r=new StringReader(taggedText);
				ArrayList<HasWord> tokens = new ArrayList<HasWord>();
				WhitespaceTokenizer<HasWord> wst = new WhitespaceTokenizer<HasWord>(new WordTokenFactory(),r,false);
				tokens=(ArrayList<HasWord>) wst.tokenize();
				for(int j=1;j<tokens.size();j++){
					if(tokens.get(j).word().endsWith("_NN")&&tokens.get(j-1).word().endsWith("_JJ")){
						set.add(tokens.get(j-1).word().replace("_JJ", "")+" "+tokens.get(j).word().replace("_NN", ""));
					}
					if(j+1<tokens.size()){
						
						if(tokens.get(j-1).word().endsWith("_NN")&&tokens.get(j).word().endsWith("_ART")&&tokens.get(j+1).word().endsWith("_NN")){
							set.add(tokens.get(j-1).word().replace("_NN", "")+" "+tokens.get(j).word().replace("_ART", "")+" "+tokens.get(j-1).word().replace("_NN", ""));
						}
						if(tokens.get(j-1).word().endsWith("_NN")&&tokens.get(j).word().endsWith("_KON")&&tokens.get(j+1).word().endsWith("_NN")){
							set.add(tokens.get(j-1).word().replace("_NN", "")+" "+tokens.get(j).word().replace("_KON", "")+" "+tokens.get(j+1).word().replace("_NN", ""));
						}
					}
				}
				posDoc.setTaggedWordsEN(set);
			}//englisch
			if(!(doc.getAbstractGerman()==null)){
				HashSet<String> set = new HashSet<String>();
				String taggedText=taggerDE.tagString(doc.getAbstractGerman());
				StringReader r=new StringReader(taggedText);
				ArrayList<HasWord> tokens = new ArrayList<HasWord>();
				WhitespaceTokenizer<HasWord> wst = new WhitespaceTokenizer<HasWord>(new WordTokenFactory(),r,false);
				tokens=(ArrayList<HasWord>) wst.tokenize();
				for(int j=1;j<tokens.size();j++){
					if(tokens.get(j).word().endsWith("_NN")&&tokens.get(j-1).word().endsWith("_ADJA")){
						set.add(tokens.get(j-1).word().replace("_ADJA", "")+" "+tokens.get(j).word().replace("_NN", ""));
					}
					if(j+1<tokens.size()){
						if(tokens.get(j-1).word().endsWith("_NN")&&tokens.get(j).word().endsWith("_ART")&&tokens.get(j+1).word().endsWith("_NN")){
							set.add(tokens.get(j-1).word().replace("_NN", "")+" "+tokens.get(j).word().replace("_ART", "")+" "+tokens.get(j+1).word().replace("_NN", ""));
						}
						if(tokens.get(j-1).word().endsWith("_NN")&&tokens.get(j).word().endsWith("_KON")&&tokens.get(j+1).word().endsWith("_NN")){
							set.add(tokens.get(j-1).word().replace("_NN", "")+" "+tokens.get(j).word().replace("_KON", "")+" "+tokens.get(j+1).word().replace("_NN", ""));
						}
					}
				}
				posDoc.setTaggedWordsDE(set);
			}//deutsch
			posTaggedDocs.add(posDoc);
		}
		posDocSet.setPosTaggedDocumentSet(posTaggedDocs);
	}
}
