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


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


import de.unima.ki.infolis.fastjoin.indexer.SummaryIndexer;
import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;

import de.unima.ki.infolis.lohai.IflRecord;

public class SummaryIndexerExperimentNew {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		HashSet<String> chosenDois = new HashSet<String>();
		chosenDois.add("99.999/999");
		chosenDois.add("10.4232/1.1941");
		chosenDois.add("10.4232/1.1941");
		chosenDois.add("10.4232/1.4491");
		chosenDois.add("10.4232/1.2994");
		chosenDois.add("10.4232/1.1534");
		chosenDois.add("10.4232/1.10051");
		chosenDois.add("10.4232/1.3143");
		chosenDois.add("10.4232/1.1206");
		chosenDois.add("10.4232/1.2346");
		chosenDois.add("10.4232/1.10977");
		chosenDois.add("10.4232/1.10239");
		
		
		System.out.println("Init Summary Indexer:");
		SummaryIndexer summaryIndexer = new SummaryIndexer();
		
		
		summaryIndexer.run();
		
		
		PrintWriter writer = new PrintWriter("experiments/formative-evaluation/indexing-results.html");
		
		
		writer.println("<html><meta charset=\"utf-8\" /><head> <title>Indexing Results</title></head>");
		writer.println("<body>");
		
		HashMap<IflRecord, HashSet<ConceptWithScore>> results = summaryIndexer.getResults();
		
		int counter = 0;
		for (IflRecord record : results.keySet()) {
			boolean chosen = false;
			chosen = choseFixed(chosenDois, record, chosen);
			if (!chosen) continue;
			
			writer.println("<h3>" + record + "</h3>");
			writer.println("<ul>");
			writer.println("\t<li>Lokales File: " + record.getOrigin() + "</li>");
			writer.println("\t<li>URL: <a target=\"_blank\" href=\"http://dx.doi.org/"  + record.getIdentifier() + "\">" + record.getIdentifier() + "</a></li>");
			writer.println("</ul>");
			writer.println("<ol>");
			
			ArrayList<ConceptWithScore> scoredConceptsNormalized = new ArrayList<ConceptWithScore>();
			ArrayList<ConceptWithScore> scoredConceptsNotNormalized = new ArrayList<ConceptWithScore>();
			for(ConceptWithScore cws:results.get(record)){
				if(cws.isNormalized()){
					scoredConceptsNormalized.add(cws);
				}else{
					scoredConceptsNotNormalized.add(cws);
				}
			}
			
			
			Collections.sort(scoredConceptsNormalized);
			Collections.sort(scoredConceptsNotNormalized);
			for (ConceptWithScore scoredConcept :  scoredConceptsNormalized) {
				writer.println(scoredConcept.toHTML());
			}
			for (ConceptWithScore scoredConcept :  scoredConceptsNotNormalized) {
				writer.println(scoredConcept.toHTML());
			}
			counter++;
			writer.println("</ol>");
		}
		
		writer.println("\n\n</body>");
		writer.println("</html>");
		writer.close();
	
		

	}

	private static boolean choseFixed(HashSet<String> chosenDois,
			IflRecord record, boolean chosen) {
		for (String doifrag : chosenDois) {
			System.out.println("id: " + record.getIdentifier());
			System.out.println("doifrag: " + doifrag);
			if (record.getIdentifier().endsWith(doifrag))  {
				chosen = true;

			}
		}
		return chosen;
	}
	


}
