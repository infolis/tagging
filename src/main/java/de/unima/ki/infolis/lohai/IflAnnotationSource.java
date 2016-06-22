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
import org.semtinel.core.data.api.ConceptScheme;

public class IflAnnotationSource implements AnnotationSource {
	
	private String name;
	private String description;
	private long id;
	
	private List<Annotation> annotations = new ArrayList<Annotation>();
	private ConceptScheme conceptScheme = null;
	
	
	public IflAnnotationSource(ConceptScheme conceptScheme) {
		this.conceptScheme = conceptScheme;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ConceptScheme getConceptScheme() {
		return this.conceptScheme;
	}

	public void setConceptScheme(ConceptScheme conceptScheme) {
		this.conceptScheme = conceptScheme;
	}

	public List<Annotation> getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

	public int getSize() {
		this.annotations.size();
		return 0;
	}

}
