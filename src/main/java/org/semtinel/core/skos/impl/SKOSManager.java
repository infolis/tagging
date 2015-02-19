/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semtinel.core.skos.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.semtinel.core.skos.api.ConceptScheme;
import org.semtinel.core.skos.api.SKOS;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 *
 * @author kai
 */
public class SKOSManager {
    private String skosURI;
    private Model model;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public SKOSManager(String skosURI) {
        this.skosURI = skosURI;
        model = ModelFactory.createDefaultModel().read(skosURI.toString());
        log.info("Dataset loaded: " + skosURI + ", Triples: " + model.size());

    }

    public List<ConceptScheme> getConceptSchemes() {
        ResIterator it = model.listSubjectsWithProperty(RDF.type, model.getResource(SKOS.ConceptScheme));
        List<ConceptScheme> res = new ArrayList<ConceptScheme>();
        while (it.hasNext()) {
            Resource s = it.nextResource();
            res.add(new ConceptSchemeImpl(s));
        }

        log.info("Concept Schemes: " + res.size());
        return res;

    }


}
