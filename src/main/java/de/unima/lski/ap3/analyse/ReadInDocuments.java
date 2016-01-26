package de.unima.lski.ap3.analyse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.unima.lski.ap3.util.StudyDocument;
import de.unima.lski.ap3.util.StudyDocumentSet;
import de.unima.lski.ap3.util.Utility;


public class ReadInDocuments {
	private StudyDocumentSet studyDocumentSet;
	/**
	 * Creates a StudyDocumentSet you can retrieve by using this instance and getStudyDocumentSet.
	 * @param dir The directory of the documents
	 */
	public ReadInDocuments(String dir){
		studyDocumentSet = new StudyDocumentSet();
		studyDocumentSet.setStudyDocumentSet(this.analyzeDocs(dir));
	}

	public StudyDocumentSet getStudyDocumentSetObject() {
		return studyDocumentSet;
	}

	public void setStudyDocumentSetObject(StudyDocumentSet studyDocumentSet) {
		this.studyDocumentSet = studyDocumentSet;
	}
	
	public ArrayList<StudyDocument> analyzeDocs(String dir){
		ArrayList<StudyDocument> docList=new ArrayList<StudyDocument>();
		DocumentBuilderFactory builderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=null;
		try {
			builder =builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		}
		File[] files=Utility.getFileNames(dir);
		int count =0;
		for(File f:files){
			count++;
			if(count>=200){
				break;
			}
			Document doc=null;
			try {
				doc =builder.parse(f.getAbsoluteFile());
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			StudyDocument document =new StudyDocument();
			document.setIdentifier(doc.getElementsByTagName("identifier").item(0).getTextContent());
			document.setWglIdentifier(doc.getElementsByTagName("wgl:identifier").item(0).getTextContent());
			NodeList nl=doc.getElementsByTagName("wgl:description");
			for(int j=0;j<nl.getLength();j++){
				String text=nl.item(j).getTextContent();
				int h=Utility.getLanguage(text);
				switch(h){
				case 1:document.setAbstractGerman(text);break;
				case 2:document.setAbstractEnglish(text);break;
				}
			}
			docList.add(document);
		}
		return docList;
	}
}
