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

import org.semtinel.core.data.api.Concept;
import org.semtinel.core.data.api.Record;

/**
 * 
 * Class for using a String and Concept as key for a Hashmap.
 *
 */
public class ConceptRecord {
	


	private Concept c;
	private Record r;
	
	public ConceptRecord(Concept c, Record r) {
		this.c = c;
		this.r = r;
	}
	
	public Concept getConcept() {
		return c;
	}

	public Record getRecord() {
		return r;
	}
	
	public boolean equals(Object that) {
		ConceptRecord thatSr = (ConceptRecord)that;
		if (thatSr.getConcept().equals(this.getConcept()) && thatSr.getRecord().equals(this.getRecord())) {
			return true;
		}
		return false;
	}
	
	public int hashCode() {
		return this.getRecord().hashCode() / 2 + this.getConcept().hashCode() / 2;
	}

}
