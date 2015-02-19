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

import org.semtinel.core.skos.api.Concept;
import org.semtinel.core.skos.api.Language;
import org.semtinel.core.skos.api.Notation;

import com.hp.hpl.jena.rdf.model.RDFNode;


/**
 *
 * @author kai
 */
public class NotationImpl extends RDFNodeFacade implements Notation {

    public NotationImpl(RDFNode node) {
        super(node);
    }

    public Concept getConcept() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Language getLanguage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setConcept(Concept concept) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLanguage(Language language) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setText(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
