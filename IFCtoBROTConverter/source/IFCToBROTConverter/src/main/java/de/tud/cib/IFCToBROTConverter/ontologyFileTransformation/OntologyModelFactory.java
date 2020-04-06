package de.tud.cib.IFCToBROTConverter.ontologyFileTransformation;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Creates an ontology from a given input
 */
public class OntologyModelFactory {

    /**
     * Creates an ontology from a given String
     * The string defines the input path of the corresponding file.
     * @param path
     * @return the Jena Model of the related ontology file
     */
    public static OntModel createOntologyFromString(String path) {
        OntModel ontology = ModelFactory.createOntologyModel();
        ontology.read(path);
        return ontology;
    }

}
