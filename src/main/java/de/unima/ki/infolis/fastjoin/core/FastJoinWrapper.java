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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;


/**
* Connects the indexer with the FastJoin executable.
*
*/
public class FastJoinWrapper {
	


	public static void main(String[] args) {
		FastJoinWrapper fjw = new FastJoinWrapper();
		fjw.join("fastjoin/test/source.txt", "fastjoin/test/target.txt");
	}
	
        private Logger log = Logger.getLogger(getClass().getName());
        
	/**
	*  Constructs and returns a similarity based mapping between token from the thesaurus and token from the records.
	*  
	* @param sourcePath The path to the file that contains the tokens of the thesaurus.
	* @param targetPath The path to the file that contains the tokens of the records.
	* @return A mapping that maps each token from the records to the most similar token in the thesaurus.
	*/
	public HashMap<String, String> join(String sourcePath, String targetPath) {
		HashMap<String,String> recordTokenToThesaurusToken = new HashMap<String, String>();
		HashMap<String,Double> recordTokenToConfidence = new HashMap<String, Double>();
		try {
                        log.info("fast join started with: " +sourcePath + " --- " + targetPath);
			String line;
			Process p = Runtime.getRuntime().exec(Settings.FASTJOIN_EXE_WIN + " " + Settings.FASTJOIN_MEASURE + " " + Settings.FASTJOIN_DELTA + " " + Settings.FASTJOIN_TAU + " " + sourcePath + " " + targetPath + "");
			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
	
			int type = 0;
			String sourceLabel = "";
			String targetLabel = "";
			double confidence = 0.0;
                        log.info("fast join output:");
			while ((line = bri.readLine()) != null) {
                                log.info(line);
				String[] fields = line.split(" ");
				// *** type == 1 ***
				if (type == 1) {
					sourceLabel = line;
					type = 2;
				}
				// *** type == 2 ***
				else if (type == 2) {
					targetLabel = line;
					// 
					if (!(recordTokenToThesaurusToken.containsKey(targetLabel))) {
						recordTokenToThesaurusToken.put(targetLabel, sourceLabel);
						recordTokenToConfidence.put(targetLabel, confidence);
					}
					else if (recordTokenToConfidence.get(targetLabel) < confidence) {
						recordTokenToThesaurusToken.put(targetLabel, sourceLabel);
						recordTokenToConfidence.put(targetLabel, confidence);
					}
					type = 0;
				}	
				// *** type == 0 ***
				else if (type == 0) {
					try {
						confidence = Double.parseDouble(fields[0]);
						if (confidence >= 0.0 && confidence <= 1.0) {
							type = 1;
						}
					}
					catch (NumberFormatException e) { }
				}
			}
			bri.close();
			p.waitFor();
		}
		catch (Exception err) {
                        log.info(err.getMessage());
			err.printStackTrace();
		}
		return recordTokenToThesaurusToken;
	}

}
