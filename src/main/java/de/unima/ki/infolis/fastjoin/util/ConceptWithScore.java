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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.semtinel.core.data.api.Label;

import de.unima.ki.infolis.lohai.IflConcept;

/**
* A scored concept is a concept from a thesaurus with additionaly
* an attached score (= can be tf-idf or some other aggregated score).
*
*/
public class ConceptWithScore implements Comparable<ConceptWithScore> {

	
	private ArrayList<StringOccurence> occurences = new ArrayList<StringOccurence>();

	private IflConcept concept;
	double score;
	
	private boolean normalized;
	
	boolean unigram = false;
	boolean bigramm = false;
	boolean trigram = false;
	
	boolean foundInTitle     = false;
	boolean foundInAbstract = false;
	ArrayList<Integer> foundInAbstractAt;
	boolean foundInSubjectHeading = false;
	
	
	public ConceptWithScore(IflConcept concept, double score) {
		this.concept = concept;
		this.score = score;	
	}
	
	public void addAllOccurences(HashSet<StringOccurence> occurences) {
		this.occurences.addAll(occurences);
	}
	

	public int compareTo(ConceptWithScore that) {
		if (this.score > that.score) {
			return -1;
		}
		else if(this.score < that.score) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public String toString() {
		List<Label> altLabelList=this.concept.getAltLabels(IflConcept.preferredLangaue);
		String altLabelString="";
		for(Label label : altLabelList){
			altLabelString +=  label.getText()+"; ";
		}
		
		StringBuffer sbo = new StringBuffer("");
		for (StringOccurence so : this.occurences) {
			sbo.append("\t" + so + "\n");	
		}
		
		return this.concept.toString() + " (" + this.score + ")" + " Normalized: "+ this.normalized +"  AltLabels:" + altLabelString + "\n" + sbo;
	}
	
	
	public String toHTML() {
		StringBuffer sb = new StringBuffer("");
		List<Label> altLabelList=this.concept.getAltLabels(IflConcept.preferredLangaue);
		String altLabelString = "";
		boolean notFirst = false;
		for(Label label:altLabelList){
			if (notFirst) altLabelString +=  "; " + label.getText();
			else altLabelString = label.getText();
			notFirst = true;
		}
		if (notFirst) altLabelString = "AltLabels: " + altLabelString;
	
		DecimalFormat df = new DecimalFormat( "0.00" );
		sb.append("<li><b>" + this.concept.toString() + "</b> (" + df.format(this.score) + ")  "+" Normalized: "+ this.normalized + " " +altLabelString + "\n");
		sb.append("\t<ul>\n");
		for (StringOccurence so : this.occurences) {
			sb.append("\t\t<li>" + so+ "</li>\n");
		}
		sb.append("\t</ul>\n");
		sb.append("</li>\n\n");
		return sb.toString();
	}
	
	
	public IflConcept getConcept(){
		return this.concept;
	}
	
	public double getScore(){
		return this.score;
	}
	
	public ArrayList<StringOccurence> getOccurences(){
		return this.occurences;
	}

	public boolean isNormalized() {
		return normalized;
	}

	public void setNormalized(boolean normalized) {
		this.normalized = normalized;
	}

}