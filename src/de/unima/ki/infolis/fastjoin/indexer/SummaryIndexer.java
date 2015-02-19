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

package de.unima.ki.infolis.fastjoin.indexer;

import java.util.HashMap;
import java.util.HashSet;

import org.semtinel.core.data.api.AnnotationSource;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.skos.impl.SKOSManager;

import de.unima.ki.infolis.fastjoin.core.FastJoinIndexer;
import de.unima.ki.infolis.fastjoin.core.Settings;
import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.fastjoin.util.Reader;
import de.unima.ki.infolis.lohai.IflAnnotationSource;
import de.unima.ki.infolis.lohai.IflConcept;
import de.unima.ki.infolis.lohai.IflConceptScheme;
import de.unima.ki.infolis.lohai.IflRecord;
import de.unima.ki.infolis.lohai.IflRecordSet;

public class SummaryIndexer implements Indexer {

	private HashMap<IflRecord, HashSet<ConceptWithScore>> results;
	
	
	public void run() {
		// set preferred language
        IflConcept.preferredLangaue = Settings.chosenLang;
        // load concept scheme
		SKOSManager man = new SKOSManager(Settings.thesozURL);
		ConceptScheme iflCs = new IflConceptScheme(man.getConceptSchemes().get(0), Settings.thesozURL);

		// load recordset of dara studies
        System.out.println("Loading studies ...");
        IflRecordSet daraRS = Reader.createDaraRecordSet(Settings.daraDirPath);
        AnnotationSource target = new IflAnnotationSource(iflCs);
        
        // init the indexer and run it
        System.out.println("Start the string matching / indexing ...");
        
        FastJoinIndexer indexer = new FastJoinIndexer(Settings.chosenLang, target, iflCs, daraRS);
        results = indexer.index();
        System.out.println("Indexing finished!");
	}


	@Override
	public HashMap<IflRecord, HashSet<ConceptWithScore>> getResults() {
		return results;
	}


}
