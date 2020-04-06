package de.tud.cib.IFCToBROTConverter.ICDDConfiguration;


import de.tud.cib.IFCToBROTConverter.ontologyConfiguration.LinkedOntology;
import de.tud.cib.IFCToBROTConverter.ontologyFileTransformation.OntologyModelFactory;
import icdd.beans.ICDDContainerDescription;
import org.apache.jena.ontology.OntModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ICDDBuilderTest {

    private ICDDBuilder icddBuilder;
    private Path ifc;
    private LinkedOntology linkedOntology;
    private String ifcInput = "src/test/java/TestResources/Vogelsangbruecke_Bauwerk_D_West.ifc";
    private String ontologyInput = "src/test/java/TestResources/bridge-Abox.ttl";
    private String tempOntOutput = "src/test/java/TestResources/tempDirectory/temp.ttl";
    private Path ontologyFile;

    @Before
    public void initICCDBuilder() {
        icddBuilder = new ICDDBuilder();
        ifc = Paths.get(ifcInput);
        linkedOntology = this.initLinkedOntology();
        ontologyFile = Paths.get(ontologyInput);
    }

    @After
    public void cleanTempDirectory() {
        Path tempFile = Paths.get(tempOntOutput);
        tempFile.toFile().delete();
    }

    private LinkedOntology initLinkedOntology() {
        OntModel ontModel = OntologyModelFactory.createOntologyFromString(ontologyInput);
        HashMap<String, String> linkinfo = new HashMap<>();
        linkinfo.put("d1#0GGyOhV4P81v$QMMai2$r$", "https://wisib.de/ontologie/abox/vogelsangbridge/d1#0GGyOhV4P81v$QMMai2$r$");
        return new LinkedOntology(ontModel, linkinfo);
    }

    @Test
    public void testCreateICDDLinkedOntology() throws IOException {
        ICDDContainerDescription containerDescription = icddBuilder.createICDD(ifc, linkedOntology, tempOntOutput).getContainerDescription();
        assertTrue(containerDescription.listDocuments().hasNext());
        assertTrue(containerDescription.listLinkSet().hasNext());
    }

    @Test
    public void testCreateICDDOntologyWithLinkInfo() throws IOException {
        ICDDContainerDescription containerDescription = icddBuilder.createICDD(ifc, linkedOntology.getOntology(), linkedOntology.getLinkInformation(), tempOntOutput).getContainerDescription();
        assertTrue(containerDescription.listDocuments().hasNext());
        assertTrue(containerDescription.listLinkSet().hasNext());
    }

    @Test
    public void testCreateICDDFileWithLinkInfo() throws IOException {
        ICDDContainerDescription containerDescription = icddBuilder.createICDD(ifc, ontologyFile, linkedOntology.getLinkInformation()).getContainerDescription();
        assertTrue(containerDescription.listDocuments().hasNext());
        assertTrue(containerDescription.listLinkSet().hasNext());
    }



}
