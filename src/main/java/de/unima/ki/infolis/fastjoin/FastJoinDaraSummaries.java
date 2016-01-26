/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.unima.ki.infolis.fastjoin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.semtinel.core.data.api.AnnotationSource;

import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.skos.impl.SKOSManager;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
// import org.semtinel.core.skos.impl.SKOSManager;

import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.fastjoin.util.LinkResolver;
import de.unima.ki.infolis.lohai.IflAnnotationSource;
import de.unima.ki.infolis.lohai.IflConcept;
import de.unima.ki.infolis.lohai.IflConceptScheme;
import de.unima.ki.infolis.lohai.IflRecord;
import de.unima.ki.infolis.lohai.IflRecordSet;
// import de.unima.ki.infolis.lohai.IflConceptScheme;
import de.unima.lski.ap3.util.Utility;



public class FastJoinDaraSummaries {
	

    public static void main(String[] args) {
    	System.out.println("Running ...");
        
    	// set preferred language
        IflConcept.preferredLangaue = Settings.chosenLang;
        // load concept scheme
		SKOSManager man = new SKOSManager(Settings.thesozURL);
		ConceptScheme iflCs = new IflConceptScheme(man.getConceptSchemes().get(0), Settings.thesozURL);
		

        // load recordset of dara studies
        System.out.println("Loading dara studies ...");
        IflRecordSet daraRS = createDaraRecordSet(Settings.daraDirPath);

        AnnotationSource target = new IflAnnotationSource(iflCs);
        
        // init the indexer and run it
        System.out.println("Start the string matching / indexing ...");
        FastJoinIndexer indexer = new FastJoinIndexer(Settings.chosenLang, target, iflCs, daraRS);
        HashMap<IflRecord, HashSet<ConceptWithScore>> indexedRecords = indexer.index();
        
        for (IflRecord record : indexedRecords.keySet()) {
        	System.out.println("Record: " + record);
        	for (ConceptWithScore cws : indexedRecords.get(record)) {
        		System.out.println("\tConcept: " + cws);
        	}
        	
        }
        
        
        

        
        
        
        
        
  
        
        
       
    }
    
    /**
    * 
    * Creates a record set based on the xml metadata description of SSOAR corpus.
    * 
    * @param dir the path to the directory where the files can be found
    * @return An IflRecordSet that can be used for later annotations.
    */
	public static IflRecordSet createSSOARRecordSet(String dir) {
		IflRecordSet rs = new IflRecordSet();
		DocumentBuilderFactory builderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=null;
		try {
			builder =builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		}
		int c=0;
		File file=new File(dir+"/"+c+".xml");
		while(file.exists()){
			c++;
			file=new File(dir+"/"+c+".xml");
			if (c > Settings.MAX_DOCS) break;
		}
		c--;
		
		int counter = 0;
		double progress = 0.0;
		
		for(int i=0;i<=c;i++){
			
			counter++;
			if (progress < ((double)(counter) / (double)(c))) {
				System.out.print(Math.round(progress * 100.0) + " % ");
				progress += 0.1;
			}
			// System.out.println(i);
			IflRecord r = new IflRecord();
			r.setOrigin( + i + ".xml");
			Document doc=null;
			try {
				doc = builder.parse(dir + "/" + i + ".xml");
			}
			catch (SAXException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			// read identifier from record
			r.setUrnAsIdentifier(doc.getElementsByTagName("wgl:identifier").item(0).getTextContent());
			// read title from record (if title in the chosen langaue is available)
			NodeList nlLang = doc.getElementsByTagName("wgl:language");
			for (int j = 0; j <  nlLang.getLength(); j++) {
				String lang = nlLang.item(j).getTextContent();
				if ((lang.equals("Deutsch") && Settings.LANGUAGE.equals("de")) || (lang.equals("Englisch") && Settings.LANGUAGE.equals("en"))) {
					NodeList titles = doc.getElementsByTagName("wgl:title");
					r.setTitle(titles.item(0).getTextContent());
				}		
			}

			// read abstract from record (if abstract in the chosen language is available)
			NodeList nl=doc.getElementsByTagName("wgl:description");
			int h = 0;
			for(int j=0; j<nl.getLength(); j++) {	
				String text=nl.item(j).getTextContent();
				h= Utility.getLanguage(text);
				if ((h == 2 && Settings.LANGUAGE.equals("en")) || (h == 1 && Settings.LANGUAGE.equals("de"))) {
					r.setAbstractText(text);
					break;
				}
			}
			rs.addRecord(r);
			// reading subjectheadings (they are always available in german langaue only)
			ArrayList<String> headings = new ArrayList<String>();
			if (Settings.LANGUAGE.equals("de")) {
				readSubjectHeadings(headings, doc, "wgl:wglsubject");
				readSubjectHeadings(headings, doc, "wgl:subject");
			}
			r.setSubjectHeadings(headings);
			
		}
		return rs;
	}

	private static void readSubjectHeadings(ArrayList<String> headings, Document doc, String tag) {
		NodeList subjectHeadings = doc.getElementsByTagName(tag);
		for(int j=0; j < subjectHeadings.getLength(); j++) {
			String subjectHeading = subjectHeadings.item(j).getTextContent();
			if (subjectHeading.contains(";")) {
				String[] sheadings = subjectHeading.split(";");
				for (String h : sheadings) {
					headings.add(h);
				}
			}
			else {
				headings.add(subjectHeading);
			}
		}
	}
	
	
	
    /**
    * 
    * Creates a record set based on the xml metadata description of dara studies.
    * 
    * @param dir the path to the directory where the files can be found
    * @return An IflRecordSet that can be used for later annotations.
    */
	public static IflRecordSet createDaraRecordSet(String dir) {
		
		int countStudiesWithAbstract = 0;
		
		IflRecordSet rs=new IflRecordSet();
		DocumentBuilderFactory builderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=null;
		try {
			builder =builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		}
		File directory=new File(dir);
		File[] files=directory.listFiles();
		int counter = 0;
		double progress = 0.0;
		for(File f:files) {
			Document doc = null;
			counter++;
			if (progress < ((double)(counter) / (double)(files.length))) {
				System.out.print(Math.round(progress * 100.0) + " % ");
				progress += 0.1;
			}
			try {
				doc =builder.parse(f.getAbsoluteFile());
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			IflRecord r=new IflRecord();
			NodeList nl=doc.getElementsByTagName("title");
			if(nl.item(0)!=null){
				r.setTitle(nl.item(0).getTextContent());
			}
			
			NodeList doiNl = doc.getElementsByTagName("doi");
			if(doiNl.item(0)!=null){
				
				r.setUrnAsIdentifier(doiNl.item(0).getTextContent());
//				System.out.println("DOI: " + r.getIdentifier());
				
			}
			rs.addRecord(r);
			boolean foundEnglishAbstract=false;
			nl=doc.getElementsByTagName("summary");
			for(int j=0;j<nl.getLength();j++){
				NodeList nl2=nl.item(j).getChildNodes();
				boolean escape = false;
				for(int h=0;h<nl2.getLength();h++){
					if(nl2.item(h).getNodeName().equals("text")){
						String abstractEN = nl2.item(h).getTextContent();
						int lang = Utility.getLanguage(abstractEN);
						
						if (lang == 1 || lang == 2) {
							countStudiesWithAbstract++;
							
							if (countStudiesWithAbstract % 100 == 13) {
								System.out.println("----------------------------");
								System.out.println(abstractEN);

							}
							escape = true;
							break;
							
						}
						

						if(lang==2){
							r.setAbstractText(abstractEN);
							foundEnglishAbstract=true;
						}
						if (foundEnglishAbstract) rs.addRecord(r);
					}
				}
				if (escape) break;
			}
			nl=doc.getElementsByTagName("text");
			
			

//			for(int i=0;i<nl.getLength();i++){
//				String abstractEN=nl.item(i).getTextContent();
//				int h = Utility.getLanguage(abstractEN);
//				
//				if (h == 1 || h == 2) {
//					countStudiesWithAbstract++;
//					
//					if (countStudiesWithAbstract % 100 == 13) {
//						System.out.println("----------------------------");
//						System.out.println(abstractEN);
//
//					}
//					break;
//				}
//				
//
//				if(h==2){
//					r.setAbstractText(abstractEN);
//					foundEnglishAbstract=true;
//				}
//				if (foundEnglishAbstract) rs.addRecord(r);
//			}
			
			
		}
		System.out.println("Studies with abstract in german or english " + countStudiesWithAbstract + "/" + counter);
		return rs;
	}



}
