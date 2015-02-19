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

package org.semtinel.core.skos.api;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author kai
 */
public interface Concept extends Resource {

    List<Label> getPrefLabels();
    Label getPrefLabel(Language language);

    List<Label> getAltLabels();
    List<Label> getAltLabels(Language language);

    List<Concept> getBroader();

    ConceptScheme getConceptScheme();

    List<Label> getHiddenLabels();
    List<Label> getHiddenLabels(Language language);


    List<Label> getLabels();
    List<Label> getLabels(Language language);

    List<Concept> getNarrower();

    List<Notation> getNotations();

    List<Note> getNotes();
    
    
    public String getURI();

    void setBroader(List<Concept> broader);

    void setConceptScheme(ConceptScheme conceptScheme);

    void setLabels(List<Label> labels);

    void setNarrower(List<Concept> narrower);

    void setNotations(List<Notation> notations);

    void setNotes(List<Note> notes);
    
}
