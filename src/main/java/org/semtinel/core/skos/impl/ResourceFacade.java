/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semtinel.core.skos.impl;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 *
 * @author kai
 */
public class ResourceFacade implements Resource {
    private Resource resource;

    public ResourceFacade(Resource resource) {
        this.resource = resource;
    }

    public Property getProperty(String uri) {
        return getModel().getProperty(uri);
    }


    public AnonId getId() {
        return resource.getId();
    }

    public Resource inModel(Model m) {
        return resource.inModel(m);
    }

    public boolean hasURI(String uri) {
        return resource.hasURI(uri);
    }

    public String getURI() {
        return resource.getURI();
    }

    public String getNameSpace() {
        return resource.getNameSpace();
    }

    public String getLocalName() {
        return resource.getLocalName();
    }

    public Statement getRequiredProperty(Property p) {
        return resource.getRequiredProperty(p);
    }

    public Statement getProperty(Property p) {
        return resource.getProperty(p);
    }

    public StmtIterator listProperties(Property p) {
        return resource.listProperties(p);
    }

    public StmtIterator listProperties() {
        return resource.listProperties();
    }

    public Resource addLiteral(Property p, boolean o) {
        return resource.addLiteral(p, o);
    }

    public Resource addLiteral(Property p, long o) {
        return resource.addLiteral(p, o);
    }

    public Resource addLiteral(Property p, char o) {
        return resource.addLiteral(p, o);
    }

    public Resource addLiteral(Property value, double d) {
        return resource.addLiteral(value, d);
    }

    public Resource addLiteral(Property value, float d) {
        return resource.addLiteral(value, d);
    }

    public Resource addLiteral(Property p, Object o) {
        return resource.addLiteral(p, o);
    }

    public Resource addLiteral(Property p, Literal o) {
        return resource.addLiteral(p, o);
    }

    public Resource addProperty(Property p, String o) {
        return resource.addProperty(p, o);
    }

    public Resource addProperty(Property p, String o, String l) {
        return resource.addProperty(p, o, l);
    }

    public Resource addProperty(Property p, String lexicalForm, RDFDatatype datatype) {
        return resource.addProperty(p, lexicalForm, datatype);
    }

    public Resource addProperty(Property p, RDFNode o) {
        return resource.addProperty(p, o);
    }

    public boolean hasProperty(Property p) {
        return resource.hasProperty(p);
    }

    public boolean hasLiteral(Property p, boolean o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, long o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, char o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, double o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, float o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, Object o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasProperty(Property p, String o) {
        return resource.hasProperty(p, o);
    }

    public boolean hasProperty(Property p, String o, String l) {
        return resource.hasProperty(p, o, l);
    }

    public boolean hasProperty(Property p, RDFNode o) {
        return resource.hasProperty(p, o);
    }

    public Resource removeProperties() {
        return resource.removeProperties();
    }

    public Resource removeAll(Property p) {
        return resource.removeAll(p);
    }

    public Resource begin() {
        return resource.begin();
    }

    public Resource abort() {
        return resource.abort();
    }

    public Resource commit() {
        return resource.commit();
    }

    public Resource getPropertyResourceValue(Property p) {
        return resource.getPropertyResourceValue(p);
    }

    public boolean isAnon() {
        return resource.isAnon();
    }

    public boolean isLiteral() {
        return resource.isLiteral();
    }

    public boolean isURIResource() {
        return resource.isURIResource();
    }

    public boolean isResource() {
        return resource.isResource();
    }

    public <T extends RDFNode> T as(Class<T> view) {
        return resource.as(view);
    }

    public <T extends RDFNode> boolean canAs(Class<T> view) {
        return resource.canAs(view);
    }

    public Model getModel() {
        return resource.getModel();
    }

    public Object visitWith(RDFVisitor rv) {
        return resource.visitWith(rv);
    }

    public Resource asResource() {
        return resource.asResource();
    }

    public Literal asLiteral() {
        return resource.asLiteral();
    }

    public Node asNode() {
        return resource.asNode();
    }

}
