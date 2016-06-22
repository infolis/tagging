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
import java.util.HashMap;
import java.util.List;

import org.semtinel.core.data.api.Concept;
import org.semtinel.core.data.api.ConceptScheme;

public class IflConceptScheme implements ConceptScheme {
	
	private String uri;
	private HashMap<String, Concept> uriHashedConcepts = new HashMap<String, Concept>();

	private ArrayList<Concept> concepts;
	
	public IflConceptScheme(org.semtinel.core.skos.api.ConceptScheme skosScheme, String uri) {
		this.setUri(uri);
		this.concepts = new ArrayList<Concept>();
		for (org.semtinel.core.skos.api.Concept skosConcept : skosScheme.getConcepts()) { 
			this.concepts.add(new IflConcept(skosConcept, this));
		}
	}


	public List<Concept> getConcepts() {
		return this.concepts;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}


	public boolean existsConcept(String uri) {
		if (this.uriHashedConcepts.containsKey(uri)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void addConcept(Concept concept) {
		this.uriHashedConcepts.put(concept.getURI(),  concept);
	}

}
