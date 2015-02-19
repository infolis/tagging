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

import org.semtinel.core.data.api.Concept;
import org.semtinel.core.data.api.Label;
import org.semtinel.core.data.api.Language;



public class IflLabel implements Label {
	
	private Concept concept;
	private String text;
	private Language lang;
	
	public IflLabel(org.semtinel.core.skos.api.Label skosLabel, IflConcept iflConcept) {
		this.concept = iflConcept;
		this.text = skosLabel.getText();
		this.lang = new IflLanguage(skosLabel.getLanguage());
	}


	@Override
	public Concept getConcept() {
		return concept;
	}

	@Override
	public void setConcept(Concept concept) {
		this.concept = concept;
		
	}

	@Override
	public Language getLanguage() {
		return this.lang;
	}

	@Override
	public void setLanguage(Language language) {
		this.lang = language;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}



}
