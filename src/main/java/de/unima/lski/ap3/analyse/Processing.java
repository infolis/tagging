package de.unima.lski.ap3.analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import de.unima.lski.ap3.util.POStaggedDocument;
import de.unima.lski.ap3.util.POStaggedDocumentSet;
import de.unima.lski.ap3.util.Utility;

public class Processing {
	public HashMap<String,HashMap<String,Double>> posTaggedResultEN;
	public HashMap<String,HashMap<String,Double>> posTaggedResultDE;
	
	public void init(){
		posTaggedResultEN = new HashMap<String, HashMap<String,Double>>();
		posTaggedResultDE = new HashMap<String, HashMap<String,Double>>();
		POStagging pos = new POStagging("C:/project/ssoarxml/");
		pos.tagging();
		TFIDFAnalyse analyse = new TFIDFAnalyse("C:/project/ssoarxml/");
		analyse.calcTFIDF();
		ArrayList<POStaggedDocument> posSet =pos.getPosDocSet().getPosTaggedDocumentSet();
		for(int i=0;i<posSet.size();i++){//docs durchlaufen
			POStaggedDocument posDoc=posSet.get(i);
			HashSet<String> germanSet=posDoc.getTaggedWordsDE();
			HashSet<String> englishSet=posDoc.getTaggedWordsEN();
			HashMap<String, Double> tempDE = new HashMap<String, Double>();
			HashMap<String,Double> tempEN = new HashMap<String, Double>();
			for(String c:analyse.gerResultMap.keySet()){
				if(c.equals(posDoc.getIdentifier())){
					HashMap<String,Double> werte=analyse.gerResultMap.get(c);
					HashMap<String,String> normalizedSequences =Utility.normalizeTaggedWords(germanSet);
					try{
						for(String sequence:normalizedSequences.keySet()){
							String[] arry=sequence.split(" ");
							double durschnitt=0d;
							for(int j=0;j<arry.length;j++){
								double d=werte.get(arry[j]);
								durschnitt=durschnitt+d;
							}
							durschnitt=durschnitt/arry.length;
							tempDE.put(sequence, durschnitt);}
						
						}catch(Exception e){
						}
						break;
				}
			}
			for(String c:analyse.engResultMap.keySet()){//resultmap durchlaufen
				if(c.equals(posDoc.getIdentifier())){//wenn identifier gleich->
					HashMap<String,Double> werte=analyse.engResultMap.get(c);
					HashMap<String,String> normalizedSequences =Utility.normalizeTaggedWords(englishSet);
					try{
						for(String sequence:normalizedSequences.keySet()){
							String[] arry=sequence.split(" ");
							double durschnitt=0d;
							for(int j=0;j<arry.length;j++){
								double d=werte.get(arry[j]);
								durschnitt=durschnitt+d;
							}
							durschnitt=durschnitt/arry.length;
							tempEN.put(sequence, durschnitt);}

					}catch(Exception e){
					}
					break;
				}
			}
			posTaggedResultEN.put(posDoc.getIdentifier(), tempEN);
			posTaggedResultDE.put(posDoc.getIdentifier(), tempDE);
		}
	}
}
