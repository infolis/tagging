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

import java.util.HashMap;
import java.util.HashSet;

import de.unima.ki.infolis.fastjoin.indexer.SummaryIndexer;
import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.lohai.IflRecord;

public class SummaryIndexerExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SummaryIndexer sI = new SummaryIndexer();
		sI.run();
		HashMap<IflRecord, HashSet<ConceptWithScore>> results = sI.getResults();
		
		int count = 0;
		for(IflRecord rec:results.keySet()){
			count++;if(count == 15)break;
			System.out.println(rec.getTitle()+"\n");
			HashSet<ConceptWithScore> set = results.get(rec);
			for(ConceptWithScore con: set){
				System.out.println(con.toString());
			}
			System.out.println("\n");
		}
		

	}

}
