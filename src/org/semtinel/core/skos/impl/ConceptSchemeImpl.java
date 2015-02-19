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
import org.semtinel.core.skos.api.SKOS;

import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;


/**
 *
 * @author kai
 */
public class ConceptSchemeImpl extends ResourceFacade implements ConceptScheme {

    public ConceptSchemeImpl(Resource resource) {
        super(resource);
    }



    public List<Concept> getConcepts() {
        ResIterator it = getModel().listResourcesWithProperty(getProperty(SKOS.inScheme), this);
        List<Concept> res = new ArrayList<Concept>();
        while (it.hasNext()) {
            Resource next = it.nextResource();
            res.add(new ConceptImpl(next));
        }
        return res;
    }

    public List<Concept> getTopConcepts() {
        StmtIterator it = this.listProperties(getProperty(SKOS.hasTopConcept));
        List<Concept> res = new ArrayList<Concept>();
        while (it.hasNext()) {
            Statement next = it.nextStatement();
            res.add(new ConceptImpl(next.getResource()));
        }
        return res;
    }

    public void setTopConcept(Concept topConcept) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
