package de.unima.ki.infolis.tagging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import de.unima.ki.infolis.fastjoin.core.Settings;
import de.unima.ki.infolis.fastjoin.indexer.LinkIndexer;
import de.unima.ki.infolis.fastjoin.indexer.LinkIndexerMode;
import de.unima.ki.infolis.fastjoin.indexer.SummaryIndexer;
import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.fastjoin.util.LinkResolver;
import de.unima.ki.infolis.lohai.IflRecord;

public class Tagging {
	
	public static void main(String args[]) throws IOException {
		
		File config=null, output=null;
		String type=null;
		
		try {
		config = new File(args[0]);
		type = args[1];
		output = new File(args[2]);
		Settings.loadProperties(config);
		
		}catch(Exception e) {
			System.out.println("Usage: java -jar InFoLiSTagging.jar CONFIGFILE TYPE OUTPUTFILE");
			System.out.println("TYPE can either be Link or Abstract to indicate if the links or the abstracts should be used for tagging.");
		}
		BufferedWriter write = new BufferedWriter(new FileWriter(output));
		
		if(type.equals("Link")) {			
			LinkIndexer indexer = new LinkIndexer(Settings.linkFilePath, LinkIndexerMode.JOIN_DOCUMENTS_AND_INDEX);
			indexer.run();
			indexer.printStatus();
			HashMap<IflRecord, HashSet<ConceptWithScore>> results = indexer.getResults();
			LinkResolver res = new LinkResolver(Settings.linkFilePath);
			
			for (IflRecord study : results.keySet()) {
				if(res.hasLinkedPublications(study)){					
					HashSet<ConceptWithScore> set = results.get(study);
					for(ConceptWithScore con: set){
						write.write(study.getIdentifier()+"\t");
						write.write(con.getConcept().getURI()+"\t");
						write.write(con.getScore()+"\n");
					}
				}
			}			
		}
		if(type.equals("Abstract")) {
			SummaryIndexer sI = new SummaryIndexer();
			sI.run();
			HashMap<IflRecord, HashSet<ConceptWithScore>> results = sI.getResults();
			
			for(IflRecord rec:results.keySet()){				
				HashSet<ConceptWithScore> set = results.get(rec);
				for(ConceptWithScore con: set){
					write.write(rec.getIdentifier()+"\t");
					write.write(con.getConcept().getURI()+"\t");
					write.write(con.getScore()+"\n");
				}
			}
		}
		write.flush();
		write.close();
	}

}
