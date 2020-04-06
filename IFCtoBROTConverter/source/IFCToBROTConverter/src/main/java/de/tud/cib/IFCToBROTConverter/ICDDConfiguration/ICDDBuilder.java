package de.tud.cib.IFCToBROTConverter.ICDDConfiguration;


import de.tud.cib.IFCToBROTConverter.ontologyConfiguration.LinkedOntology;
import de.tud.cib.IFCToBROTConverter.ontologyFileTransformation.OntologyModelWriter;
import icdd.beans.Container;
import icdd.beans.ICDDInternalDocument;
import icdd.beans.ICDDLink;
import icdd.beans.LinkSet;
import org.apache.jena.ontology.OntModel;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder for creating an ICDD Container that consists of at least 1 IFC model, 1 construction ontology and
 * the corresponding ICDD Linkset
 */
public class ICDDBuilder {

    //Auslagerungsobjekte, da selektieren von ICDDDocument Objekten in ICDD-Framework relativ umständlich
    private ICDDInternalDocument ifcDoc;
    private ICDDInternalDocument owlDoc;

    private String containerName = "BridgeICDD";
    private String containerNS = "https://wisib.de/bridgeICDD";
    private String linkName = "Ifc-BridgeOntology";
    private String linkNS = "https://wisib.de/linksetIFC-BridgeOntology";

    public String getContainerName() {
        return containerName;
    }

    public String getContainerNS() {
        return containerNS;
    }

    public String getLinkName() {
        return linkName;
    }

    public String getLinkNS() {
        return linkNS;
    }

    public ICDDInternalDocument getIfcDoc() {
        return ifcDoc;
    }

    public ICDDInternalDocument getOwlDoc() {
        return owlDoc;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public void setContainerNS(String containerNS) {
        this.containerNS = containerNS;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public void setLinkNS(String linkNS) {
        this.linkNS = linkNS;
    }

    /**
     * Creates the ICDD container, containing the IFC-model, corresponding ontology representation and
     * related ICDD Linkset
     * @param ifc The IFC model
     * @param ontologyFile The ontology representation of the IFC model
     * @param linkInformation The corresponding Linkset information for the IFC model and ontology
     * @return The ICDD container
     */
    public Container createICDD(Path ifc, Path ontologyFile, HashMap<String, String> linkInformation) throws IOException {
        Container container = this.createContainerWithFiles(ifc, ontologyFile);
        container = this.createIfcOwlLinkset(container, linkInformation);
        return container;
    }

    /**
     * Creates the ICDD container, containing the IFC-model, corresponding ontology representation and
     * related ICDD Linkset
     * @param ifc The IFC model
     * @param constructionOntology The ontology representation of the IFC model
     * @param linkInformation The corresponding Linkset information for the IFC model and ontology
     * @param tempDirectory The temporary directory for storing the ontology file
     * @return The ICDD container
     */
    public Container createICDD(Path ifc, OntModel constructionOntology, HashMap<String, String> linkInformation, String tempDirectory) throws IOException {
        OntologyModelWriter.writeOntology(constructionOntology, tempDirectory);
        Path ontologyFile = Paths.get(tempDirectory);
        return this.createICDD(ifc, ontologyFile, linkInformation);
    }

    /**
     * Creates the ICDD container, containing the IFC-model, corresponding ontology representation and
     * related ICDD Linkset
     * @param ifc The IFC model
     * @param constructionOntology The LinkedOntology, consisting of an ontology representation of the IFC model and the Linkset
     * @param tempDirectory The temporary directory for storing the ontology file
     * @return The ICDD container
     */
    public Container createICDD(Path ifc, LinkedOntology constructionOntology, String tempDirectory) throws IOException {
        return this.createICDD(ifc, constructionOntology.getOntology(), constructionOntology.getLinkInformation(), tempDirectory);
    }

    //TODO:Methoden für das hinzufügen weiterer Modelle hinzufügen, am besten in separater Klasse oder Interface auslagern

    protected Container createContainerWithFiles(Path ifc, Path ontologyFile) {
        Container container = new Container(containerName, containerNS);
        this.ifcDoc = container.addInternalDocument("IFC", ifc.toFile().getName(), ".ifc", ifc.toFile());
        this.owlDoc = container.addInternalDocument("Construction Ontology", ontologyFile.toFile().getName(), ".ttl", ontologyFile.toFile());
        return container;
    }

    private Container createIfcOwlLinkset(Container container, HashMap<String, String> linkInformation) {
        LinkSet linkSet = container.addLinkSet(linkName, linkNS).getLinkSetOnt();
        for (Map.Entry<String, String> entry :
                linkInformation.entrySet()) {
            ICDDLink link = linkSet.createNullLink();
            link.addStringLinkElement(ifcDoc, entry.getKey(), null, ICDDLink.LinkEleType.HAS);
            link.addURLLinkElement(owlDoc, entry.getValue(), ICDDLink.LinkEleType.HAS);
        }
        return container;
    }

}
