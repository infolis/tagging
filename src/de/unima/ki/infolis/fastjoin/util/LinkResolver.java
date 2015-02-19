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

package de.unima.ki.infolis.fastjoin.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.unima.ki.infolis.lohai.IflRecord;
import de.unima.ki.infolis.lohai.IflRecordSet;

/**
* Reads a link file and stores internally the links between studies and
* publications for later access.
* 
*/
public class LinkResolver {
	
	
	private HashMap<String, HashSet<String>> publicationToStudy;
	private HashMap<String, HashSet<String>> studyToPublication;
	
	
	private HashMap<String, IflRecord> urnToSPublication = new HashMap<String, IflRecord>();
	private HashMap<String, IflRecord> doiToStudy = new HashMap<String, IflRecord>();
	
	/**
	* Reads a file with links between between studies and publications and stores them in 
	* an internal has structure.
	*  
	* @param linkFile the input file that specified the links.
	* 
	*/
	public LinkResolver(String linkFile) {
		this.publicationToStudy = new HashMap<String, HashSet<String>>();
		this.studyToPublication = new HashMap<String, HashSet<String>>();
		InputStream fis;
		BufferedReader br;
		String line;
		try {
			fis = new FileInputStream(linkFile);
			br = new BufferedReader(new InputStreamReader(fis));
			String publicationId;
			String studyId;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\\|");
				//You may have to adjust details here depending on the provided linkfile!!
				publicationId = parts[1];
				studyId = "doi:"+parts[6];//"doi:" f�r die neuste version wenn man auf die alte zur�ck greifen will muss dies gel�scht werden
				if (!(this.publicationToStudy.containsKey(publicationId))) {
					this.publicationToStudy.put(publicationId, new HashSet<String>());
				}
				if (!(this.studyToPublication.containsKey(studyId))) {
					this.studyToPublication.put(studyId, new HashSet<String>());
				}
				this.publicationToStudy.get(publicationId).add(studyId);
				this.studyToPublication.get(studyId).add(publicationId);	
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	* Checks if a publication is linked.
	* 
	* @param record The record that represents the publication.
	* @return True if the publication is via its urn linked with one or more studies.
	*/
	public boolean hasLinkedStudies(IflRecord record) {
		return this.publicationToStudy.containsKey(record.getIdentifier());
	}
	
	/**
	* Checks if a study is linked.
	* 
	* @param record The record that represents the study.
	* @return True if the study is via its doi linked with one or more publications.
	*/
	public boolean hasLinkedPublications(IflRecord record) {
		return this.studyToPublication.containsKey(record.getIdentifier());
	}
	
	/**
	* Returns the publications linked to a study.
	* 
	* @param study The record that represent the study.
	* @return the set of linked publications as set of records.
	*/
	public Set<IflRecord> getPublicationsByStudy(IflRecord study) {
		HashSet<IflRecord> publications = new HashSet<IflRecord>();
		for (String urn : this.studyToPublication.get(study.getIdentifier())) {
			IflRecord publication = this.urnToSPublication.get(urn);
			publications.add(publication);
		}
		return publications;
	}
	
	/*

	
	public Set<String> getStudyIdsByPublicationIds(String publicationId) {
		return this.publicationToStudy.get(publicationId);
	}
	
	public Set<String> getStudyIds() {
		return this.studyToPublication.keySet();
	}
	
	public Set<String> getPublicationIds() {
		return this.publicationToStudy.keySet();
	}
	*/


	public void setPublicationSet(IflRecordSet rs) {
		for (IflRecord r : rs) {
			this.urnToSPublication.put(r.getIdentifier(), r);
		}	
	}
	
	public void setStudySet(IflRecordSet rs) {
		for (IflRecord r : rs) {
			this.doiToStudy.put(r.getIdentifier(), r);
		}	
	}
	
}
