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

package org.semtinel.core.data.api;

import java.util.List;

/**
 *
 * @author kai
 */
public interface Concept {
	
    public void setId(Long id);
    public Long getId();

    public List<Label> getPrefLabels();
    
    public Label getPrefLabel(Language language);

    public List<Label> getAltLabels();
    public List<Label> getAltLabels(Language language);

    public List<Concept> getBroader();

    public ConceptScheme getConceptScheme();
    public void setConceptScheme(ConceptScheme conceptScheme);

    public List<Label> getLabels();
    public List<Label> getLabels(Language language);

    public List<Concept> getNarrower();



    /**
     * @deprecated Pref Label is ambiguous due to language! Use getPrefLabel(Language lang) or getOrefLabels()
     * @return
     */
    @Deprecated
    public Label getPrefLabel();

    public String getURI();

}
