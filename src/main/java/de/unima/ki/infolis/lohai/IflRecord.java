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

package de.unima.ki.infolis.lohai;

import java.util.ArrayList;
import java.util.List;

import org.semtinel.core.data.api.Annotation;
import org.semtinel.core.data.api.AnnotationSource;
import org.semtinel.core.data.api.Record;

public class IflRecord implements Record {

	
	private List<Annotation> annotations = new ArrayList<Annotation>();
	
	/**
	* Abstract or summary. 
	*/
	private String abstractText;
	
	/**
	* Title. 
	*/
	private String title;
	
	/**
	* The filepath of the record. 
	*/
	private String origin;
	
	/**
	* The identifier (can be URN or DOI). 
	*/
	private String identifier;
	
	/**
	*Already available classifications. 
	*/
	private List<String> terms;
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setUrnAsIdentifier(String urnText) {
		String[] parts = urnText.split("/urn:");
		if (parts.length == 2) {
			this.setIdentifier("urn:" + parts[1]);
		}
		else {
			this.setIdentifier(urnText);
		}
	}
	
	public void setDoiAsIdentifier(String doi) {
		this.setIdentifier(doi);
	}
	
	public void setIdentifier(String identifier) {
		identifier = identifier.trim();
		this.identifier = identifier;
	}



	
	private ArrayList<String> subjectHeadings = new ArrayList<String>();
	

	
	public void setSubjectHeadings(ArrayList<String> subjectHeadings) {
		this.subjectHeadings = subjectHeadings;
	}
	
	public ArrayList<String> getSubjectHeadings() {
		return this.subjectHeadings;
	}

	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title.trim();
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}

	public String getAbstractText() {
		return abstractText;
	}

	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
		
	}

	public void addAnnotation(Annotation annotation) {
		this.annotations.add(annotation);
	}

	public List<Annotation> getAnnotations(AnnotationSource annotationSource) {
		return this.annotations;
	} 

	public String toString() {
		return this.title + " (" + this.origin + ")";
	
	}
	
	public String toFullString() {
		return "TITLE: " + this.title + "\nABSTRACT:" + this.abstractText;
	}


	
}