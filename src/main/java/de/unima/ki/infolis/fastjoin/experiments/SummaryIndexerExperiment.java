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

package de.unima.ki.infolis.fastjoin.experiments;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.unima.ki.infolis.fastjoin.core.Settings;
import de.unima.ki.infolis.fastjoin.indexer.SummaryIndexer;
import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.fastjoin.util.LinkResolver;
import de.unima.ki.infolis.fastjoin.util.Reader;
import de.unima.ki.infolis.lohai.IflRecord;

public class SummaryIndexerExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SummaryIndexer sI = new SummaryIndexer();
		sI.run();
		HashMap<IflRecord, HashSet<ConceptWithScore>> results = sI.getResults();
//		LinkResolver lr = new LinkResolver(Settings.linkFilePath);
//		lr.setStudySet(Reader.createDaraRecordSet(Settings.daraDirPath));
//		lr.setPublicationSet(Reader.createSSOARRecordSet(Settings.ssoarDirPath));
		// Writes a testset of 15 Studies.
		HashMap<IflRecord,HashSet<ConceptWithScore>> result = sI.getResults();
		FileWriter write=null;
		try {
			//write = new FileWriter("test.txt"); //meilicke
			write = new FileWriter("C:/Users/Lernzone/Desktop/res10SH.txt"); //schreckenberger
			int count = 0;
			for(IflRecord study:result.keySet()){
				
				if(count == 200) break;
				HashSet<ConceptWithScore> cons = result.get(study);
				
				if(cons.size()>=10){
					write.write(study.getIdentifier()+"|"+study.getTitle()+"|"+"abstractPlaceholder");
					ArrayList<ConceptWithScore> orderedList = new ArrayList<ConceptWithScore>();
					for(ConceptWithScore con:cons){
						orderedList.add(con);
					}
					Collections.sort(orderedList);
					for(int i = 0; i < orderedList.size();i++){
						write.write("|" + orderedList.get(i).toString().split("\\(")[0].trim());
					}
					count++;
					write.write("\n");
				}
			}
			write.flush();
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
