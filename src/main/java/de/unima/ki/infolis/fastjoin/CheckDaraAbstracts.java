package de.unima.ki.infolis.fastjoin;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.semtinel.core.data.api.AnnotationSource;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.skos.impl.SKOSManager;

import de.unima.ki.infolis.fastjoin.util.ConceptWithScore;
import de.unima.ki.infolis.lohai.IflAnnotationSource;
import de.unima.ki.infolis.lohai.IflConcept;
import de.unima.ki.infolis.lohai.IflConceptScheme;
import de.unima.ki.infolis.lohai.IflRecord;
import de.unima.ki.infolis.lohai.IflRecordSet;

public class CheckDaraAbstracts {


	
	public static void main(String[] args) {
		System.out.println("Running ...");
        
    	// set preferred language
        IflConcept.preferredLangaue = Settings.chosenLang;
        // load concept scheme
		SKOSManager man = new SKOSManager(Settings.thesozURL);
		ConceptScheme iflCs = new IflConceptScheme(man.getConceptSchemes().get(0), Settings.thesozURL);
		
		
        // load recordset of dara studies
        System.out.println("Loading dara studies ...");
        IflRecordSet daraRS = RunFastJoinIndexer.createDaraRecordSet(Settings.daraDirPath);

        AnnotationSource target = new IflAnnotationSource(iflCs);
        
        // init the indexer and run it
        System.out.println("Start the string matching / indexing ...");
        
        FastJoinIndexer indexer = new FastJoinIndexer(Settings.chosenLang, target, iflCs, daraRS);
        HashMap<IflRecord, HashSet<ConceptWithScore>> indexedRecords = indexer.index();
        System.out.println("==========================");
        FileWriter write=null;
        try {
        	write = new FileWriter("test.txt"); //meilicke
        	// write = new FileWriter("/home/chschrec/test.txt"); schreckenberger
        	for(IflRecord study:indexedRecords.keySet()){
        		write.write(study.toFullString()+"\n");
        		HashSet<ConceptWithScore> set=indexedRecords.get(study);
        		for(ConceptWithScore con:set){
        			write.write(con+"\n");
        		} 

        		write.write("\n========================\n\n");
        	}
        	write.flush();
        	write.close();
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
       
        

	}

}
