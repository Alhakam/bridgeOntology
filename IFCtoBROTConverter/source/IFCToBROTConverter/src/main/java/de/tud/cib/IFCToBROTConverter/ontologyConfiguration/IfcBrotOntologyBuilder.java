package de.tud.cib.IFCToBROTConverter.ontologyConfiguration;


import de.tud.cib.IFCToBROTConverter.annotation.AnnotatedIfcEntity;
import de.tud.cib.IFCToBROTConverter.ifcFilter.IfcEntity;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.impl.OntModelImpl;
import org.apache.jena.rdf.model.Property;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Builder for creating ontology representations of IFC models
 */
public class IfcBrotOntologyBuilder {

    private final String defaultUri = "https://wisib.de/ontologie/Abox/bridgeOntology#";
    private final String aboxNS = "inst";

    private final String brotUri = "https://wisib.de/ontologie/brot/#";
    private final String brotNS = "brot";
    private final String brcompUri = "https://wisib.de/ontologie/brcomp#";
    private final String brcompNS = "brcomp";

    private final String bridgeName = "Bridge";
    private final String siteName = "Site";
    private final String componentName = "Component";

    /**
     * Creates the ontology representation of a given annotated IFC model using a default URI
     * @param annotatedEntities The Arraylist of annotated entities which are the result of a parsed annotation model
     * @return The LinkedOntology, consisting of the ontology representation and the related Linkset information
     */
    public LinkedOntology createBrotOntologyWithAnnotations(ArrayList<AnnotatedIfcEntity> annotatedEntities) {
        return this.createBrotOntologyWithAnnotations(annotatedEntities, defaultUri);
    }

    /**
     * Creates the ontology representation of a given annotated IFC model
     * @param annotatedIfcEntityArrayList The Arraylist of annotated entities which are the result of a parsed annotation model
     * @param uri The Uri of the created ontology
     * @return The LinkedOntology, consisting of the ontology representation and the related Linkset information
     */
    public LinkedOntology createBrotOntologyWithAnnotations(ArrayList<AnnotatedIfcEntity> annotatedIfcEntityArrayList, String uri) {
        OntModel ontModel = this.initOntology(uri);
        ontModel = this.setNamespaces(ontModel, uri);
        ontModel = this.createIndividualsByAnnotation(ontModel, annotatedIfcEntityArrayList);
        HashMap<String, String> linkInformation = this.createLinkInformation((ArrayList)annotatedIfcEntityArrayList, ontModel.getNsPrefixURI(aboxNS));
        return new LinkedOntology(ontModel, linkInformation);
    }

    public LinkedOntology createBrotOntology(ArrayList<IfcEntity> ifcEntities) {
        return this.createBrotOntology(ifcEntities, defaultUri);
    }

    public LinkedOntology createBrotOntology(ArrayList<IfcEntity> ifcEntities, String uri) {
        OntModel ontModel = this.initOntology(uri);
        ontModel = this.setNamespaces(ontModel, uri);
        ontModel = this.createIndividuals(ontModel, ifcEntities);
        HashMap<String, String> linkInformation = this.createLinkInformation(ifcEntities, ontModel.getNsPrefixURI(aboxNS));
        return new LinkedOntology(ontModel, linkInformation);
    }

    private OntModel initOntology(String uri) {
        OntModel ontModel = new OntModelImpl(OntModelSpec.OWL_DL_MEM);
        ontModel.createOntology(uri);
        return ontModel;
    }

    private OntModel setNamespaces(OntModel ontmodel, String uri) {
        ontmodel.setNsPrefix(aboxNS, uri);
        ontmodel.setNsPrefix(brotNS, brotUri);
        ontmodel.setNsPrefix(brcompNS, brcompUri);
        return ontmodel;
    }

    private OntModel createIndividualsByAnnotation(OntModel ontModel, ArrayList<AnnotatedIfcEntity> annotatedIfcEntityArrayList) {
        String uri = ontModel.getNsPrefixURI(aboxNS);
        //Individual-Creation
        Individual bridge = ontModel.createIndividual(uri + bridgeName, ontModel.createClass(brotUri + "Bridge"));
        Individual substructure = ontModel.createIndividual(uri + bridgeName + "-Substructure", ontModel.createClass(brotUri + "Substructure"));
        Individual superstructure = ontModel.createIndividual(uri + bridgeName + "Superstructure", ontModel.createClass(brotUri + "Superstructure"));
        Individual girder = ontModel.createIndividual(uri + bridgeName + "-Girder", ontModel.createClass(brcompUri + "Girder"));
        //Property-Creation
        Property containsZone = ontModel.createObjectProperty(brotUri + "containsZone");
        Property containsElement = ontModel.createObjectProperty(brotUri + "containsElement");
        Property aggregates = ontModel.createObjectProperty(brotUri + "aggregates");
        //Statement-Implementation
        ontModel.add(bridge, containsZone, substructure);
        ontModel.add(bridge, containsZone, superstructure);
        ontModel.add(superstructure, containsElement, girder);
        //Bridge-Element Creation
        for (AnnotatedIfcEntity annotatedIfcEntity :
                annotatedIfcEntityArrayList) {
            //sollte mit Annotator-Klassen konform sein
            //TODO:Fehlerabhandlung für unerlaubte BROT-Klassen bzw. Softwareseitige Interpretation
            OntClass annotationClass = ontModel.createClass(brcompUri + annotatedIfcEntity.getAnnotation().substring(3)); //Annotation without Ifc prefix
            Individual annotatedIndividual = ontModel.createIndividual(uri + annotatedIfcEntity.getIfcID(), annotationClass);
            //Zuweisung Zone (nur für spezifische Klassen, ansonsten gesamter Brücke zugewiesen
            switch(annotatedIfcEntity.getAnnotation()) {
                case "IfcAbutment":
                    ontModel.add(substructure, containsElement, annotatedIndividual);
                    break;
                case "IfcGirderSegment":
                    ontModel.add(girder, aggregates, annotatedIndividual);
                    break;
                case "IfcPotBearings":
                    ontModel.add(substructure, containsElement, annotatedIndividual);
                    break;
                case "IfcGirderDeck":
                    ontModel.add(girder, aggregates, annotatedIndividual);
                    break;
                case "IfcCap":
                    ontModel.add(superstructure, containsElement, annotatedIndividual);
                    break;
                case "IfcFoundation":
                    ontModel.add(substructure, containsElement, annotatedIndividual);
                    break;
                case "IfcPile":
                    ontModel.add(substructure, containsElement, annotatedIndividual);
                    break;
                default:
                    ontModel.add(bridge, containsElement, annotatedIndividual);
            }
        }
        return ontModel;
    }

    private OntModel createIndividuals(OntModel ontModel, ArrayList<IfcEntity> ifcEntityList) {
        String uri = ontModel.getNsPrefixURI(aboxNS);
        //Individual-Creation
        Individual site = ontModel.createIndividual(uri + this.getSiteID(ifcEntityList), ontModel.createClass(brotUri + siteName));
        Individual bridge = ontModel.createIndividual(uri + this.getBridgeID(ifcEntityList), ontModel.createClass(brotUri + bridgeName));
        Individual substructure = ontModel.createIndividual(uri + this.getBridgeID(ifcEntityList) + "-Substructure", ontModel.createClass(brotUri + "Substructure"));
        Individual superstructure = ontModel.createIndividual(uri + this.getBridgeID(ifcEntityList) + "Superstructure", ontModel.createClass(brotUri + "Superstructure"));
        //Property-Creation
        Property containsZone = ontModel.createObjectProperty(brotUri + "containsZone");
        Property containsElement = ontModel.createObjectProperty(brotUri + "containsElement");
        Property aggregates = ontModel.createObjectProperty(brotUri + "aggregates");
        //Statement-Implementation
        ontModel.add(site, containsZone, bridge);
        ontModel.add(bridge, containsZone, substructure);
        ontModel.add(bridge, containsZone, superstructure);
        //BridgeElement-Creation
        for (IfcEntity ifcEntity:
             ifcEntityList) {
            if (ifcEntity.getType().equals(componentName)) {
                Individual component = ontModel.createIndividual(uri + ifcEntity.getIfcID(), ontModel.createClass(brotUri + componentName));
            }
        }
        //Relationship-Assignment
        for (IfcEntity ifcEntity:
             ifcEntityList) {
            Individual ifcEntityIndividual = ontModel.getIndividual(uri + ifcEntity.getIfcID());
            ArrayList<String> decomposedElements = ifcEntity.getDecomposedBuildingElementGUIDs();
            if (decomposedElements != null) {
                for (String decomposedElementGUID :
                        decomposedElements) {
                    Individual relatingObject = ontModel.getIndividual(uri + decomposedElementGUID);
                    ontModel.add(ifcEntityIndividual, containsElement, relatingObject);
                }
            }
        }
        return ontModel;
    }

    private String getSiteID(ArrayList<IfcEntity> ifcEntitiyList) {
        for (IfcEntity ifcEntity :
                ifcEntitiyList) {
            if (ifcEntity.getType().equals(siteName))
                return ifcEntity.getIfcID();
        }
        return siteName;
    }

    private String getBridgeID(ArrayList<IfcEntity> ifcEntitiyList) {
        for (IfcEntity ifcEntity :
                ifcEntitiyList) {
            if (ifcEntity.getType().equals(bridgeName))
                return ifcEntity.getIfcID();
        }
        return bridgeName;
    }

    private HashMap<String, String> createLinkInformation(ArrayList<IfcEntity> ifcEntityArrayList, String uri) {
        HashMap<String, String> linkInformation = new HashMap<>();
        for (IfcEntity ifcEntity :
                ifcEntityArrayList) {
            linkInformation.put(ifcEntity.getIfcID(), uri + ifcEntity.getIfcID());
        }
        return linkInformation;
    }

}
