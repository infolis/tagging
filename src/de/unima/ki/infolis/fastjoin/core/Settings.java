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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.semtinel.core.data.api.Language;

import de.unima.ki.infolis.lohai.IflLanguage;

public class Settings {

	// ******************************
	// *** dirty things *************
	// ******************************
	
	// ignores all entities in the thesaurus within this namespace
	public static String THESAURUS_NS_FILTER;
	
	
	// ******************************
	// *****  fast join params ******
	// ******************************
	
    public static String FASTJOIN_EXE_WIN;
	public static  String FASTJOIN_MEASURE;
	public static  double FASTJOIN_DELTA; // string innerhalb der token
	public static  double FASTJOIN_TAU; // 2 von 3 �hnliche token

	// ******************************
	// *** parsing and splitting ****
	// ******************************
	
	public static boolean USE_2_GRAM;
	public static boolean USE_3_GRAM;
	public static boolean USE_CAMELCASESPLIT;
	
	public static boolean USE_TITLE;
	public static boolean USE_ABSTRACT;
	public static boolean USE_SUBJECTHEADINGS;
	
	public static String DELIMITER;
	
	public static String LANGUAGE;
	
	// ********************************
	// *** special params for dara ****
	// ********************************
	
	public static boolean USE_TERMS;
	public static int WEIGH_TERMS;
	
	// ********************************************
	// *** keyword blacklist for the thesaurus ****
	// ********************************************
	
	public static String [] BLACKLIST;
	
	public static boolean USE_BLACKLIST;
	
	/**
	* Sets the maximum number of documents that are analyzed. Set to low value for test purpose, set to Integer.MAX_VALUE for real usage
	*/
	public static int MAX_DOCS;
	// public static final int MAX_DOCS = 5000;
	
	
	
	/**
	* Sets the maximum of top-k keywords that are used for indexation.
	*/
	public static int TOP_K;
	
	/**
	 * 
	 */
	public static int TOP_K_UNNORMALIZED;
	
	/**
	* String representation of the URL of the thesaurus that is used for indexing.
	*/

	/**
	* Only documents (or parts of document) in the specified language are analyzed.
	*/
	public static Language chosenLang;
	
	// **********************************************************************
	// ***	special params for LinkIndexerMode.INDEX_AND_JOIN_KEYWORDS   ****
	// **********************************************************************

	
	/**
	* Sets the fault tolerance of how many publications are allowed to miss the keyword in percent. (Has to be 0<=FAULT_TOLERANCE<=1)
	*/
	public static double THRESHOLD;
	
	/**
	 * The number of links that at least must exist that keywords are matched to the study
	 */
	public static int AT_LEAST_LINKED_BY;
	
	// ******************************
	// ***   filepath to input   ****
	// ******************************
	
	public static String ssoarDirPath; 
	public static String daraDirPath;
    public static String thesozURL;
	public static String linkFilePath;
	
	// ********************************************
	// *** relative filepath to temp out files ****
	// ********************************************
	
	public static final String THESAURUS_TOKEN_FILEPATH = "thesaurus-token.txt";
	public static final String RECORDS_TOKEN_FILEPATH   = "records-token.txt";
	
	
	public static void loadProperties(File propertyFile){
		Properties prop = new Properties();
		try{
//			File f = new File("src/de/unima/ki/infolis/fastjoin/core/infolis.properties");
//			System.out.println(f.getAbsolutePath());
//			System.out.println(f.toString().equals(new File("C:/Users/Lernzone/infolis/code/uma-cs/LOHAI2/src/de/unima/ki/infolis/fastjoin/core/infolis.properties").toString()));
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(propertyFile));
			prop.load(stream);
			stream.close();
			// ******************************
			// *** dirty things *************
			// ******************************
			// ignores all entities in the thesaurus within this namespace
			THESAURUS_NS_FILTER = prop.getProperty("THESAURUS_NS_FILTER");
			
			// ******************************
			// *****  fast join params ******
			// ******************************
			
		    FASTJOIN_EXE_WIN  = prop.getProperty("FASTJOIN_EXE_WIN");
			FASTJOIN_MEASURE  = prop.getProperty("FASTJOIN_MEASURE");
			FASTJOIN_DELTA    = Double.parseDouble(prop.getProperty("FASTJOIN_DELTA")); // string innerhalb der token
			FASTJOIN_TAU      = Double.parseDouble(prop.getProperty("FASTJOIN_TAU")); // 2 von 3 �hnliche token

			// ******************************
			// *** parsing and splitting ****
			// ******************************
			
			USE_2_GRAM = Boolean.parseBoolean(prop.getProperty("USE_2_GRAM")); 
			USE_3_GRAM = Boolean.parseBoolean(prop.getProperty("USE_3_GRAM"));
			USE_CAMELCASESPLIT = Boolean.parseBoolean(prop.getProperty("USE_CAMELCASESPLIT"));
			
			USE_TITLE = Boolean.parseBoolean(prop.getProperty("USE_TITLE"));
			USE_ABSTRACT = Boolean.parseBoolean(prop.getProperty("USE_ABSTRACT"));
			USE_SUBJECTHEADINGS = Boolean.parseBoolean(prop.getProperty("USE_SUBJECTHEADINGS"));
			
			DELIMITER = prop.getProperty("DELIMITER");
			
			LANGUAGE = prop.getProperty("LANGUAGE");
			
			// ********************************
			// *** special params for dara ****
			// ********************************
			
			USE_TERMS = Boolean.parseBoolean(prop.getProperty("USE_TERMS"));
			WEIGH_TERMS = Integer.parseInt(prop.getProperty("WEIGH_TERMS"));
			
			// ********************************************
			// *** keyword blacklist for the thesaurus ****
			// ********************************************
			
			BLACKLIST = prop.getProperty("BLACKLIST").split(",");
			
			USE_BLACKLIST = Boolean.parseBoolean(prop.getProperty("USE_BLACKLIST"));
			
			/**
			* Sets the maximum number of documents that are analyzed. Set to low value for test purpose, set to Integer.MAX_VALUE for real usage
			*/
			MAX_DOCS = Integer.parseInt(prop.getProperty("MAX_DOCS"));
			// public static final int MAX_DOCS = 5000;
			
			
			
			/**
			* Sets the maximum of top-k keywords that are used for indexation.
			*/
			TOP_K = Integer.parseInt(prop.getProperty("TOP_K"));
			
			/**
			 * 
			 */
			TOP_K_UNNORMALIZED = Integer.parseInt(prop.getProperty("TOP_K_UNNORMALIZED"));
			
			/**
			* String representation of the URL of the thesaurus that is used for indexing.
			*/

			/**
			* Only documents (or parts of document) in the specified language are analyzed.
			*/
			chosenLang = new IflLanguage(LANGUAGE);
			
			// **********************************************************************
			// ***	special params for LinkIndexerMode.INDEX_AND_JOIN_KEYWORDS   ****
			// **********************************************************************

			
			/**
			* Sets the fault tolerance of how many publications are allowed to miss the keyword in percent. (Has to be 0<=FAULT_TOLERANCE<=1)
			*/
			THRESHOLD = Double.parseDouble(prop.getProperty("THRESHOLD"));
			
			/**
			 * The number of links that at least must exist that keywords are matched to the study
			 */
			AT_LEAST_LINKED_BY = Integer.parseInt(prop.getProperty("AT_LEAST_LINKED_BY"));
			
			// ******************************
			// ***   filepath to input   ****
			// ******************************
			
			ssoarDirPath = prop.getProperty("publicationDir"); 
			daraDirPath = prop.getProperty("researchDataDir");
		    thesozURL = prop.getProperty("thesaurusURI");
			linkFilePath = prop.getProperty("linkFilePath");
			
	
		}catch(IOException io){
			io.printStackTrace();
			System.out.println("A problem occured with the properties file");
			System.exit(1);
		}
	}
	
}