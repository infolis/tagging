package de.unima.ki.infolis.fastjoin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;


public class FastJoinWrapper {
	


	public static void main(String[] args) {
		FastJoinWrapper fjw = new FastJoinWrapper();
		fjw.join("fastjoin/test/source.txt", "fastjoin/test/target.txt");
	}
	
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
			String line;
			Process p = Runtime.getRuntime().exec(Settings.FASTJOIN_EXE_WIN + " " + Settings.FASTJOIN_MEASURE + " " + Settings.FASTJOIN_DELTA + " " + Settings.FASTJOIN_TAU + " " + sourcePath + " " + targetPath + "");
			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
	
			int type = 0;
			String sourceLabel = "";
			String targetLabel = "";
			double confidence = 0.0;
			while ((line = bri.readLine()) != null) {
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
					// System.out.println("XXX:" + sourceLabel + " ~ " + targetLabel + "  =>" + confidence);
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
			err.printStackTrace();
		}
		System.out.println("LINKS: " + recordTokenToThesaurusToken.size());
		return recordTokenToThesaurusToken;
	}

}
