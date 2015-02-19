/*
 * This file is part of Semtinel (http://www.semtinel.org).
 * Copyright (c) 2007-2010 Kai Eckert (http://www.kaiec.org).
 *
 * Semtinel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Semtinel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Semtinel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.semtinel.core.skos.impl;

import java.util.ArrayList;
import java.util.List;

import org.semtinel.core.skos.api.Concept;
import org.semtinel.core.skos.api.ConceptScheme;
import org.semtinel.core.skos.api.Label;
import org.semtinel.core.skos.api.Language;
import org.semtinel.core.skos.api.Notation;
import org.semtinel.core.skos.api.Note;
import org.semtinel.core.skos.api.SKOS;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 *
 * @author kai
 */
public class ConceptImpl extends ResourceFacade implements Concept {

    public ConceptImpl(Resource resource) {
        super(resource);
    }

    
    protected List<Label> getLabels(String typeUri, String language) {
        StmtIterator it = getModel().listStatements(this, getProperty(typeUri), null, language);
        List<Label> res = new ArrayList<Label>();
        while (it.hasNext()) {
            Statement next = it.nextStatement();
            res.add(new LabelImpl(next.getObject()));
        }
        return res;
    }

    protected List<Label> getXLLabels(String typeUri) {
        StmtIterator it = getModel().listStatements(this, getProperty(typeUri), (Resource) null);
        List<Label> res = new ArrayList<Label>();
        while (it.hasNext()) {
            Statement next = it.nextStatement();
            res.add(new LabelImpl(next.getResource()));
        }
        return res;
    }


    protected List<Note> getNotes(String typeUri, String language) {
        StmtIterator it = getModel().listStatements(this, getProperty(typeUri), null, language);
        List<Note> res = new ArrayList<Note>();
        while (it.hasNext()) {
            Statement next = it.nextStatement();
            res.add(new NoteImpl(next.getObject()));
        }
        return res;
    }
    
    public boolean isConcept() {
    	
    	
    	return true;
    	
    }


    public List<Label> getPrefLabels() {
        List<Label> l1 = getLabels(SKOS.prefLabel, null);
        List<Label> l2 = getXLLabels(SKOS.xlPrefLabel);
        l1.addAll(l2);
        return l1;
    }

    public Label getPrefLabel(Language language) {
        List<Label> res = getLabels(SKOS.prefLabel, language.getName());
        if (res.size()>0) return res.get(0);
        return null;
    }

    public List<Label> getAltLabels() {
        List<Label> l1 = getLabels(SKOS.altLabel, null);
        List<Label> l2 = getXLLabels(SKOS.xlAltLabel);
        l1.addAll(l2);
        return l1;
    }

    public List<Label> getAltLabels(Language language) {
        return getLabels(SKOS.altLabel, language.getName());
    }

    public List<Concept> getBroader() {
        StmtIterator it = listProperties(getProperty(SKOS.broader));
        List<Concept> res = new ArrayList<Concept>();
        while (it.hasNext()) {
            Statement next = it.nextStatement();
            res.add(new ConceptImpl(next.getResource()));
        }
        return res;
    }

    public ConceptScheme getConceptScheme() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Label> getHiddenLabels() {
        List<Label> l1 = getLabels(SKOS.hiddenLabel, null);
        List<Label> l2 = getXLLabels(SKOS.xlHiddenLabel);
        l1.addAll(l2);
        return l1;
    }

    public List<Label> getHiddenLabels(Language language) {
        return getLabels(SKOS.hiddenLabel, language.getName());
    }

    public List<Label> getLabels() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Label> getLabels(Language language) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Concept> getNarrower() {
        StmtIterator it = listProperties(getProperty(SKOS.narrower));
        List<Concept> res = new ArrayList<Concept>();
        while (it.hasNext()) {
            Statement next = it.nextStatement();
            res.add(new ConceptImpl(next.getResource()));
        }
        return res;
    }

    public List<Notation> getNotations() {
        StmtIterator it = getModel().listStatements(this, getProperty(SKOS.notation), (String) null);
        List<Notation> res = new ArrayList<Notation>();
        while (it.hasNext()) {
            Statement next = it.nextStatement();
            res.add(new NotationImpl(next.getObject()));
        }
        return res;
    }

    public List<Note> getNotes() {
        return getNotes(SKOS.note, null);
    }

    public void setBroader(List<Concept> broader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setConceptScheme(ConceptScheme conceptScheme) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLabels(List<Label> labels) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNarrower(List<Concept> narrower) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNotations(List<Notation> notations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNotes(List<Note> notes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setURI(String uri) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    

    
}
