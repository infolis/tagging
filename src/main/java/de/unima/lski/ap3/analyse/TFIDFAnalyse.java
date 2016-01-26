package de.unima.lski.ap3.analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import de.unima.lski.ap3.util.StudyDocument;

public class TFIDFAnalyse {
	private int numOfGerAbstracts=0;
	private int numOfEngAbstracts=0;
	private ArrayList<StudyDocument> docList;
	private HashMap<String,Integer> gerIDFMap;
	private HashMap<String,Integer> engIDFMap;
	public HashMap<String,HashMap<String,Double>> gerResultMap;
	public HashMap<String,HashMap<String,Double>> engResultMap;
	
	public TFIDFAnalyse(String dir){
		ReadInDocuments read =new ReadInDocuments(dir);
		docList=read.getStudyDocumentSetObject().getStudyDocumentSet();
		gerIDFMap = new HashMap<String,Integer>();
		engIDFMap = new HashMap<String, Integer>();
		gerResultMap = new HashMap<String, HashMap<String,Double>>();
		engResultMap = new HashMap<String, HashMap<String,Double>>();
	}
	
	
	public void calcTFIDF(){
		//IDF
		for(StudyDocument studDoc:docList){
			String abstractGer=studDoc.getAbstractGerman();
			String abstractEng=studDoc.getAbstractEnglish();
			if(abstractGer!=null){
				numOfGerAbstracts++;
				this.calcIDF(gerIDFMap, abstractGer);
			}
			if(abstractEng!=null){
				numOfEngAbstracts++;
				this.calcIDF(engIDFMap, abstractEng);
			}
		}
		System.out.println(numOfGerAbstracts+" deutsche Abstracts");
		System.out.println(numOfEngAbstracts+" englische Abstracts");
		//TF
		for(StudyDocument studDoc:docList){
			String abstractGer=studDoc.getAbstractGerman();
			String abstractEng=studDoc.getAbstractEnglish();
			if(abstractGer!=null){
				this.calcTF(abstractGer, studDoc.getIdentifier(), this.gerResultMap, this.gerIDFMap, 0);
			}
			if(abstractEng!=null){
				this.calcTF(abstractEng, studDoc.getIdentifier(), this.engResultMap, this.engIDFMap, 1);
			}
		}
	}
	private void calcIDF (HashMap<String, Integer> map,String text){
		String[] tempTextArray;
		HashSet<String> uniqueTerms=new HashSet<String>();
		text=text.toLowerCase().trim().replaceAll("\\p{Punct}", "");
		tempTextArray=text.split(" ");
		for(int i=0;i<tempTextArray.length;i++){
			String term=tempTextArray[i].trim();
			uniqueTerms.add(term);
		}
		for(String c:uniqueTerms){
			if(map.containsKey(c)){
				int value=map.get(c);
				value++;
				map.put(c, value);
			}else{
				map.put(c, 1);
			}
		}
	}
	/**
	 * 
	 * @param text
	 * @param id
	 * @param tfidfResultMap
	 * @param lang i=0->German, i=1->English
	 */
	private void calcTF(String text, String id,HashMap<String,HashMap<String,Double>> tfidfResultMap,HashMap<String,Integer> idfMap,int lang){
		String[] tempTextArray;
		text=text.toLowerCase().trim().replaceAll("\\p{Punct}", "");
		tempTextArray=text.split(" ");
		HashMap<String,Integer> temp=new HashMap<String, Integer>();
		HashMap<String, Double> result= new HashMap<String,Double>();
		for(int i=0;i<tempTextArray.length;i++){
			String word=tempTextArray[i].trim();
			if(temp.containsKey(word)){
				int value=temp.get(word);
				value++;
				temp.put(word, value);
			}else{
				temp.put(word, 1);
			}
		}
		for(String c:temp.keySet()){
			double tf=(temp.get(c).doubleValue())/tempTextArray.length;
			double idf=0;
			if(lang==0){
				idf = Math.log((this.numOfGerAbstracts)/idfMap.get(c));
			}
			if(lang==1){
				idf = Math.log((this.numOfEngAbstracts)/idfMap.get(c));
			}
			double q=tf*idf;
			result.put(c, q);
		}
		tfidfResultMap.put(id, result);
	}
	
}
