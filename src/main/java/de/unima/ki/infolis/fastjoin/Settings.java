package de.unima.ki.infolis.fastjoin;

import java.io.File;
import java.util.Properties;

import org.semtinel.core.data.api.Language;

import de.unima.ki.infolis.lohai.IflLanguage;

public class Settings {
	
	
	// ******************************
	// *** dirty things *************
	// ******************************
	
	// ignores all entities in the thesaurus within this namespace
	public static final String THESAURUS_NS_FILTER = "http://lod.gesis.org/thesoz/classification";
	
	
	// ******************************
	// *****  fast join params ******
	// ******************************
	
	public static final String FASTJOIN_EXE_WIN  = "E:/projects/infolis/code/uma-cs/LOHAI2/fastjoin/win32/FastJoin.exe";//meilicke
	// public static final String FASTJOIN_EXE_WIN = "/home/chschrec/infolis/code/uma-cs/LOHAI2/fastjoin/linux/FastJoin";
	// public static final String FASTJOIN_EXE_WIN  = "D:/projects/infolis/LOHAI2/fastjoin/win32/FastJoin.exe";
	public static final String FASTJOIN_MEASURE  = "FJACCARD";
	public static final double FASTJOIN_DELTA    = 0.8; // string innerhalb der token
	public static final double FASTJOIN_TAU      = 0.95; // 2 von 3 �hnliche token

	// ******************************
	// *** parsing and splitting ****
	// ******************************
	
	public static final boolean USE_2_GRAM = true; 
	public static final boolean USE_3_GRAM = true;
	
	public static final boolean USE_TITLE = true;
	public static final boolean USE_ABSTRACT = true;
	public static final boolean USE_SUBJECTHEADINGS = true;
	
	public static final String DELIMITER = "()\"'.:;,?!\t\n\r=& ";
	
	public static final String LANGUAGE = "de";
	
	// ********************************
	// *** special params for dara ****
	// ********************************
	
	public static final boolean USE_TERMS = true;
	public static final int WEIGH_TERMS = 5;
	
	// ********************************************
	// *** keyword blacklist for the thesaurus ****
	// ********************************************
	
	public static final String [] BLACKLIST={"autor","müller","faktoren","straße","regelung",
											"interesse","objekt","kolumne","ursache",
											"betrieb","text","stimmung","grafik","aufstieg",
											"tausch","fehler","abstieg","angst","gewinn",
											"bedeutung","erfolg","messung","hören","rechnen",
											"land","denken","kriterium","spiel","effizienz",
											"subjekt","spielen","ereignis","auswahl","ziel",
											"inhalt","beitrag","raum","problem","verlag",
											"erhebung","person","dauer"};
	
	public static final boolean USE_BLACKLIST=true;
	
	/**
	* Sets the maximum number of documents that are analyzed. Set to low value for test purpose, set to Integer.MAX_VALUE for real usage
	*/
	public static final int MAX_DOCS = 8000;
	
	
	/**
	* Sets the maximum of top-k keywords that are used for indexation.
	*/
	public static final int TOP_K = 5;
	
	/**
	* String representation of the URL of the thesaurus that is used for indexing.
	*/

	/**
	* Only documents (or parts of document) in the specified language are analyzed.
	*/
	public static final Language chosenLang = new IflLanguage(LANGUAGE);

	
	// ******************************
	// ***   filepath to input   ****
	// ******************************
	
	public static final String ssoarDirPath = "E:/projects/infolis/code/uma-cs/AP3/ssoar";
	// public static final String ssoarDirPath = "D:/projects/infolis/AP3/ssoar";
	
	
	public static final String daraDirPath = "E:/projects/infolis/code/uma-cs/AP3/dara";//meilicke
	// public static final String daraDirPath = "/home/chschrec/XMLDateien/daraXML";//schreckenberger
	//  public static final String ssoarDirPath = "D:/projects/infolis/AP3/ssoar";
	
	public static final String thesozURL = "file:///E:/projects/infolis/code/uma-cs/AP3/thesaurus/thesoz.rdf";//meilicke
	// public static final String thesozURL = "file:////home/chschrec/infolis/code/uma-cs/AP3/thesaurus/thesoz.rdf";//schreckenberger
	// public static final String thesozURL = "file:///D:/projects/infolis/AP3/thesaurus/thesoz.rdf";
	
	public static final String linkFilePath = "E:/projects/infolis/code/uma-cs/AP3/links/links_doi_laptop.txt";
	
	// ********************************************
	// *** relative filepath to temp out files ****
	// ********************************************
	
	public static final String THESAURUS_TOKEN_FILEPATH = "thesaurus-token.txt";
	public static final String RECORDS_TOKEN_FILEPATH   = "records-token.txt";
	
	public static void loadProperties(File config) {
		Properties p = new Properties();
	}
	
}