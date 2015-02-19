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

import java.util.logging.Logger;

import org.semtinel.core.skos.api.Concept;
import org.semtinel.core.skos.api.Label;
import org.semtinel.core.skos.api.Language;
import org.semtinel.core.skos.api.SKOS;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.StmtIterator;


/**
 *
 * @author kai
 */
public class LabelImpl extends RDFNodeFacade implements Label {
    private Logger log = Logger.getLogger(getClass().getName());
    public LabelImpl(RDFNode node) {
        super(node);
    }

    public Concept getConcept() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Language getLanguage() {
         if (getLiteralForm()==null) {
            log.warning("LiteralForm is null: " + asResource().getURI());
            return new LanguageImpl("en");
        }
         Language res = new LanguageImpl(getLiteralForm().getLanguage());
        log.finest("Language: " + res.getName());
         return res;
    }

   protected Literal getLiteralForm() {
        if (isLiteral()) return this.asLiteral();
        StmtIterator it = asResource().listProperties(getProperty(SKOS.xlLiteralForm));
        if (!it.hasNext()) {
            log.warning("No literal form: " + asResource().getURI());
            return null;
        }
        return it.nextStatement().getLiteral();
    }

    public String getText() {
        if (getLiteralForm()==null) {
            log.warning("LiteralForm is null: " + asResource().getURI());
            return "";
        }
        return getLiteralForm().getLexicalForm();
    }

    public int getType() {
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

    public void setType(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
