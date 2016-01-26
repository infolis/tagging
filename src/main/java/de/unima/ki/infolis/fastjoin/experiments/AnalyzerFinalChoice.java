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

public class AnalyzerFinalChoice {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		HashSet<String> chosenDois = new HashSet<String>();
		chosenDois.add("10.5684/soep.v27"); // 46 SOEP
		chosenDois.add("10.4232/1.10074");  // 29 Eurobarometer trend
		chosenDois.add("10.4232/1.3131");   // 28 Studiensituation und studentische Orientierungen 1994/95
		chosenDois.add("10.4232/1.10080");  // 21 ALLBUS - Kumulation 1980-2008
		chosenDois.add("10.4232/1.4263");   // 14 Studiensituation und studentische Orientierungen 2006/07 (Studierenden-Survey)
		chosenDois.add("10.4232/1.3398");   // 10  Wohlfahrtssurvey 1998'
		chosenDois.add("10.4232/1.2800");   // 10  Allgemeine Bevölkerungsumfrage der Sozialwissenschaften ALLBUS 1996']		
		chosenDois.add("10.4232/1.11159");  // 10  European and World Values Surveys Four-Wave Integrated Data File, 1981-2004'
		chosenDois.add("10.4232/1.11005");  // 7 European Values Study Longitudinal Data File 1981-2008 (EVS 1981-2008)
		chosenDois.add("10.4232/1.3452");   // 7 Allgemeine Bevölkerungsumfrage der Sozialwissenschaften ALLBUS 2000 (PAPI-Version)
		
		
		chosenDois.add("10.4232/1.2792");  // 5 Wohlfahrtssurvey 1993
		chosenDois.add("10.4232/1.3886");  // 5 Eurobarometer 58.2 (2002)"
		chosenDois.add("10.4232/1.3680");  // 3 International Social Survey Programme: Social Relations and Support Systems / Social Networks II - ISSP 2001
		chosenDois.add("10.4232/1.3264");  // 3 Alters-Survey - Lebenszusammenhänge, Selbst- und Lebenskonzeptionen
		chosenDois.add("10.4232/1.4493");  // 2 IKG-Jugendpanel 2001-2005 (Integration, Interaktion sowie die Entwicklung von Feindbildern und Gewaltbereitschaft bei Jugendlichen)'
		chosenDois.add("10.4232/1.2527");  // 2 DJI-Jugendsurvey 1992 (Jugend und Politik)'
		chosenDois.add("10.4232/1.2897");  // 2 Flash Eurobarometer 55 (Dublin Summit)
		chosenDois.add("10.4232/1.1233");  // 1 ZUMA-Standarddemographie (Zeitreihe)'
		chosenDois.add("10.4232/1.4184");  // 1 Flash Eurobarometer 160 (Entrepreneurship)'
		chosenDois.add("10.4232/1.3601");  // 1 Flash Eurobarometer 112 (Internet and the Public at large 4)',


		
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
