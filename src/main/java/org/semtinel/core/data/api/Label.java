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


/**
 *
 * @author kai
 */
public interface Label {
    
	int ALT_LABEL = 1;
    int HIDDEN_LABEL = 2;
    int PREFERRED_LABEL = 0;

    public Concept getConcept();
    public void setConcept(Concept concept);

    public Language getLanguage();
    public void setLanguage(Language language);

    public String getText();
    public void setText(String text);



}
