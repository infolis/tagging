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

package de.unima.ki.infolis.fastjoin.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.unima.ki.infolis.fastjoin.core.Settings;
import de.unima.ki.infolis.lohai.IflRecord;
import de.unima.ki.infolis.lohai.IflRecordSet;

/**
* Utility class that reads the study and publications files and creates record sets
* out of file sets specified via the directories where these files are placed.
* 
*/
public class Reader {

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
			
			File directory = new File(dir);
			File[] flist = directory.listFiles();
			int counter = 0;
			double progress = 0.0;
			for(File f:flist){
				counter++;
				if(counter>Settings.MAX_DOCS){
					break;//grenze
				}
				if (progress < ((double)(counter) / (double)(flist.length))) {
					System.out.print(Math.round(progress * 100.0) + " % ");
					progress += 0.1;
				}
				IflRecord r = new IflRecord();
				r.setOrigin(f.getName());
				Document doc=null;
				try {
					doc = builder.parse(f);
				}
				catch (SAXException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				// read identifier from record
				r.setUrnAsIdentifier(doc.getElementsByTagName("wgl:identifier").item(0).getTextContent());
				// read title from record (if title in the chosen languagee is available)
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
			for(File f : files) {
				Document doc = null;
				counter++;
				if(counter>Settings.MAX_DOCS){
					break;//grenze
				}
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
				
				r.setOrigin(f.getName());
				//terms
				NodeList nlClassification=doc.getElementsByTagName("classification");
				List<String> terms=new ArrayList<String>();
				for(int i=0;i<nlClassification.getLength();i++){
					NodeList nlCOfC=nlClassification.item(i).getChildNodes();
					boolean langCheck=false;
					String term="";
					for(int j=0;j<nlCOfC.getLength();j++){
						String nodeName = nlCOfC.item(j).getNodeName();
						if(nodeName.equals("term")){
							term=nlCOfC.item(j).getTextContent().trim();
						}
						if(nodeName.equals("language")){
							if(nlCOfC.item(j).getTextContent().trim().equals(Settings.LANGUAGE)){
								langCheck=true;
							}
						}
					}
					if(langCheck){
						langCheck=false;
						terms.add(term);
					}
				}
				r.setTerms(terms);
				//titel
				NodeList nl=doc.getElementsByTagName("title");
				for(int i=0;i<nl.getLength();i++){
					NodeList nl2=nl.item(i).getChildNodes();
					boolean langCheck=false;
					String title="";
					for(int j=0;j<nl2.getLength();j++){
						String nodeName = nl2.item(j).getNodeName();
						if(nodeName.equals("title")){
							title=nl2.item(j).getTextContent().trim();
						}
						if(nodeName.equals("language")){
							if(nl2.item(j).getTextContent().trim().replace("\n", "").equals(Settings.LANGUAGE)){
								langCheck=true;
							}
						}
						if(langCheck){
							r.setTitle(title);
							if(!title.equals("")){
								break;
							}
						}
					}
				}
				NodeList doiNl = doc.getElementsByTagName("doi");
				if(doiNl.item(0)!=null){
					r.setUrnAsIdentifier(doiNl.item(0).getTextContent());
				}
				//abstract
				nl=doc.getElementsByTagName("summary");
				for(int i=0;i<nl.getLength();i++){
					NodeList nl2=nl.item(i).getChildNodes();
					boolean langCheck=false;
					String abstractText="";
					for(int j=0;j<nl2.getLength();j++){
						String nodeName = nl2.item(j).getNodeName();
						if(nodeName.equals("text")){
							abstractText=nl2.item(j).getTextContent().trim();
						}
						if(nodeName.equals("language")){
							if(nl2.item(j).getTextContent().trim().equals(Settings.LANGUAGE)){
								langCheck=true;
							}
						}
					}
					if(langCheck){
						r.setAbstractText(abstractText);
						if(!abstractText.equals("")){
							rs.addRecord(r);
							break;
						}
						
					}
				}	
			}	
			System.out.println();
			return rs;
		}


}
