/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semtinel.core.skos.impl;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author kai
 */
public class RDFNodeFacade implements RDFNode {

    private RDFNode rdfnode;

    public RDFNodeFacade(RDFNode rdfnode) {
        this.rdfnode = rdfnode;
    }

    public Property getProperty(String uri) {
        return getModel().getProperty(uri);
    }


    public <T extends RDFNode> T as(Class<T> view) {
        return this.rdfnode.as(view);
    }

    public Literal asLiteral() {
        return this.rdfnode.asLiteral();
    }

    public Resource asResource() {
        return this.rdfnode.asResource();
    }

    public <T extends RDFNode> boolean canAs(Class<T> view) {
        return this.rdfnode.canAs(view);
    }

    public Model getModel() {
        return this.rdfnode.getModel();
    }

    public RDFNode inModel(Model m) {
        return this.rdfnode.inModel(m);
    }

    public boolean isAnon() {
        return this.rdfnode.isAnon();
    }

    public boolean isLiteral() {
        return this.rdfnode.isLiteral();
    }

    public boolean isResource() {
        return this.rdfnode.isResource();
    }

    public boolean isURIResource() {
        return this.rdfnode.isURIResource();
    }

    public Object visitWith(RDFVisitor rv) {
        return this.rdfnode.visitWith(rv);
    }

    public Node asNode() {
        return this.rdfnode.asNode();
    }


}
