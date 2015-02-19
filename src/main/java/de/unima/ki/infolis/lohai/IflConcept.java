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

import org.semtinel.core.data.api.Concept;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.data.api.Label;
import org.semtinel.core.data.api.Language;

public class IflConcept implements Concept {
	
	private long id;
	
	private String uri;
	
	private List<Label> prefLabels;
	private List<Label> altLabels;
	
	
	private List<Concept> narrower;
	private List<Concept> broader;
	
	
	private ConceptScheme conceptScheme;
	
	public static Language preferredLangaue = null;  
	
	// TODO
	private static long idCounter = 0;

	public IflConcept(org.semtinel.core.skos.api.Concept skosConcept, ConceptScheme conceptScheme) {
		
		this.uri = skosConcept.getURI();
		conceptScheme.addConcept(this);
		
		
		idCounter++;
		this.setId(idCounter);
		this.setConceptScheme(conceptScheme);
		// set preferred labels
		this.prefLabels = new ArrayList<Label>();
		for (org.semtinel.core.skos.api.Label skosLabel : skosConcept.getPrefLabels()) { 
			this.prefLabels.add(new IflLabel(skosLabel, this));
		}
		// set alt labels
		this.altLabels = new ArrayList<Label>();
		for (org.semtinel.core.skos.api.Label skosLabel : skosConcept.getAltLabels()) { 
			this.altLabels.add(new IflLabel(skosLabel, this));
		}
		// set broader concepts
		this.broader = new ArrayList<Concept>();
		for (org.semtinel.core.skos.api.Concept skosBConcept : skosConcept.getBroader()) { 
			if (!conceptScheme.existsConcept(skosBConcept.getURI())) {
				IflConcept bConcept = new IflConcept(skosBConcept, conceptScheme);
				this.broader.add(bConcept);
			}	
		}
		// set narrower concepts
		this.narrower = new ArrayList<Concept>();
		for (org.semtinel.core.skos.api.Concept skosNConcept : skosConcept.getNarrower()) { 
			if (!conceptScheme.existsConcept(skosNConcept.getURI())) {
				IflConcept nConcept = new IflConcept(skosNConcept, conceptScheme);
				this.broader.add(nConcept);
			}
		}

	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	
	@Override
	public Long getId() {
		return this.id;
	}
	
	// *** LABEL STUFF ***

	@Override
	public List<Label> getPrefLabels() {
		return prefLabels;
	}

	@Override
	public List<Label> getAltLabels() {
		return this.altLabels;
	}
	

	@Override
	@Deprecated
	public Label getPrefLabel() {
		if (preferredLangaue == null) {
			System.err.println("You have to set the preferred langaue first!");
			System.exit(1);
			return null;
		}
		else {
			return this.getPrefLabel(preferredLangaue);
		}
	}
	
	
	@Override
	public Label getPrefLabel(Language language) {
		for (Label label : this.getPrefLabels()) {
			if (label.getLanguage().getName().equals(language.getName())) {
				return label;
			}
		}
		return null;
	}

	
	private List<Label> getFilteredLabels(Language language, List<Label> labels) {
		ArrayList<Label> filteredLabels = new ArrayList<Label>();
		for (Label label : labels) {
			if (label.getLanguage().getName().equals(language.getName())) {
				filteredLabels.add(label);
			}
		}
		return filteredLabels;
	}

	@Override
	public List<Label> getAltLabels(Language language) {
		return this.getFilteredLabels(language, this.getAltLabels());
	}
	
	
	@Override
	public List<Label> getLabels() {
		ArrayList<Label> unionLabels = new ArrayList<Label>();
		unionLabels.addAll(this.prefLabels);
		unionLabels.addAll(this.altLabels);
		return unionLabels;
	}

	@Override
	public List<Label> getLabels(Language language) {
		List<Label> unionLabels = this.getLabels();
		return this.getFilteredLabels(language, unionLabels);
	}
	
	
	// **** CONCEPT HIERARCHY ****


	@Override
	public List<Concept> getBroader() {
		return this.broader;
	}



	@Override
	public List<Concept> getNarrower() {
		return this.narrower;
	}


	
	@Override
	public ConceptScheme getConceptScheme() {
		return this.conceptScheme;
	}

	@Override
	public void setConceptScheme(ConceptScheme conceptScheme) {
		this.conceptScheme = conceptScheme;
		
	}
	
	public String getURI() {
		return this.uri;
	}

	public String toString() {
		return getPrefLabel().getText() + " (" + this.getURI() + ")";
	
	}


}