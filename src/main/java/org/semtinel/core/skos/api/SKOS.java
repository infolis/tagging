/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semtinel.core.skos.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kai
 */
public class SKOS {

    // SKOS Core
    public static final String Concept = "http://www.w3.org/2004/02/skos/core#Concept"; 	// Section 3. The http://www.w3.org/2004/02/skos/core#Concept Class
    public static final String ConceptScheme = "http://www.w3.org/2004/02/skos/core#ConceptScheme"; 	// Section 4. Concept Schemes
    public static final String inScheme = "http://www.w3.org/2004/02/skos/core#inScheme"; 	// Section 4. Concept Schemes
    public static final String hasTopConcept = "http://www.w3.org/2004/02/skos/core#hasTopConcept"; 	// Section 4. Concept Schemes
    public static final String topConceptOf = "http://www.w3.org/2004/02/skos/core#topConceptOf"; 	// Section 4. Concept Schemes
    public static final String altLabel = "http://www.w3.org/2004/02/skos/core#altLabel"; 	// Section 5. Lexical Labels
    public static final String hiddenLabel = "http://www.w3.org/2004/02/skos/core#hiddenLabel"; 	// Section 5. Lexical Labels
    public static final String prefLabel = "http://www.w3.org/2004/02/skos/core#prefLabel"; 	// Section 5. Lexical Labels
    public static final String notation = "http://www.w3.org/2004/02/skos/core#notation"; 	// Section 6. Notations
    public static final String changeNote = "http://www.w3.org/2004/02/skos/core#changeNote"; 	// Section 7. Documentation Properties
    public static final String definition = "http://www.w3.org/2004/02/skos/core#definition"; 	// Section 7. Documentation Properties
    public static final String editorialNote = "http://www.w3.org/2004/02/skos/core#editorialNote"; 	// Section 7. Documentation Properties
    public static final String example = "http://www.w3.org/2004/02/skos/core#example"; 	// Section 7. Documentation Properties
    public static final String historyNote = "http://www.w3.org/2004/02/skos/core#historyNote"; 	// Section 7. Documentation Properties
    public static final String note = "http://www.w3.org/2004/02/skos/core#note"; 	// Section 7. Documentation Properties
    public static final String scopeNote = "http://www.w3.org/2004/02/skos/core#scopeNote"; 	// Section 7. Documentation Properties
    public static final String broader = "http://www.w3.org/2004/02/skos/core#broader"; 	// Section 8. Semantic Relations
    public static final String broaderTransitive = "http://www.w3.org/2004/02/skos/core#broaderTransitive"; 	// Section 8. Semantic Relations
    public static final String narrower = "http://www.w3.org/2004/02/skos/core#narrower"; 	// Section 8. Semantic Relations
    public static final String narrowerTransitive = "http://www.w3.org/2004/02/skos/core#narrowerTransitive"; 	// Section 8. Semantic Relations
    public static final String related = "http://www.w3.org/2004/02/skos/core#related"; 	// Section 8. Semantic Relations
    public static final String semanticRelation = "http://www.w3.org/2004/02/skos/core#semanticRelation"; 	// Section 8. Semantic Relations
    public static final String Collection = "http://www.w3.org/2004/02/skos/core#Collection"; 	// Section 9. Concept Collections
    public static final String OrderedCollection = "http://www.w3.org/2004/02/skos/core#OrderedCollection"; 	// Section 9. Concept Collections
    public static final String member = "http://www.w3.org/2004/02/skos/core#member"; 	// Section 9. Concept Collections
    public static final String memberList = "http://www.w3.org/2004/02/skos/core#memberList"; 	// Section 9. Concept Collections
    public static final String broadMatch = "http://www.w3.org/2004/02/skos/core#broadMatch"; 	// Section 10. Mapping Properties
    public static final String closeMatch = "http://www.w3.org/2004/02/skos/core#closeMatch"; 	// Section 10. Mapping Properties
    public static final String exactMatch = "http://www.w3.org/2004/02/skos/core#exactMatch"; 	// Section 10. Mapping Properties
    public static final String mappingRelation = "http://www.w3.org/2004/02/skos/core#mappingRelation"; 	// Section 10. Mapping Properties
    public static final String narrowMatch = "http://www.w3.org/2004/02/skos/core#narrowMatch"; 	// Section 10. Mapping Properties
    public static final String relatedMatch = "http://www.w3.org/2004/02/skos/core#relatedMatch";

    // SKOS XL
    public static final String xlLabel = "http://www.w3.org/2008/05/skos-xl#Label"; 	// The skosxl:Label Class
    public static final String xlLiteralForm = "http://www.w3.org/2008/05/skos-xl#literalForm"; 	// The skosxl:Label Class
    public static final String xlPrefLabel = "http://www.w3.org/2008/05/skos-xl#prefLabel"; 	// Preferred, Alternate and Hidden skosxl:Labels
    public static final String xlAltLabel = "http://www.w3.org/2008/05/skos-xl#altLabel"; 	// Preferred, Alternate and Hidden skosxl:Labels
    public static final String xlHiddenLabel = "http://www.w3.org/2008/05/skos-xl#hiddenLabel"; 	// Preferred, Alternate and Hidden skosxl:Labels
    public static final String xlLabelRelation = "http://www.w3.org/2008/05/skos-xl#labelRelation"; 	// Links Between skosxl:Labels

    public static URI asURI(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(SKOS.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Bad URI syntax: " + ex.getMessage());
        }
    }
}
