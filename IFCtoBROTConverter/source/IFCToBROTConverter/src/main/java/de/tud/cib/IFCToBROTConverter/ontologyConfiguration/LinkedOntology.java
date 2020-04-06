package de.tud.cib.IFCToBROTConverter.ontologyConfiguration;

import org.apache.jena.ontology.OntModel;

import java.util.HashMap;

/**
 * Container class consisting of an ontology representation of an IFC model and its related linkset information
 */
public class LinkedOntology {

    private OntModel ontology;
    private HashMap<String, String> linkInformation;

    public LinkedOntology(OntModel ontModel, HashMap<String, String> linkInformation) {
        this.ontology = ontModel;
        this.linkInformation = linkInformation;
    }

    /**
     * Returns the link information of the linkedOntology
     * @return Hashmap containing the link information. 1st string is the IFC GUID, 2nd String the ontology URI
     */
    public HashMap<String, String> getLinkInformation() {
        return linkInformation;
    }

    public OntModel getOntology() {
        return ontology;
    }

    /**
     * Sets the link information of the LinkedOntology
     * @param linkInformation Hashmap containing the link information. 1st string is the IFC GUID, 2nd String the ontology URI
     */
    public void setLinkInformation(HashMap<String, String> linkInformation) {
        this.linkInformation = linkInformation;
    }

    public void setOntology(OntModel ontology) {
        this.ontology = ontology;
    }
}
