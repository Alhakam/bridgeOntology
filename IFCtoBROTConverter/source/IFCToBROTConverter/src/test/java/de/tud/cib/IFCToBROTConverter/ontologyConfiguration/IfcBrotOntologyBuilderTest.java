package de.tud.cib.IFCToBROTConverter.ontologyConfiguration;


import de.tud.cib.IFCToBROTConverter.annotation.AnnotatedIfcEntity;
import de.tud.cib.IFCToBROTConverter.ifcFilter.IfcEntity;
import de.tud.cib.IFCToBROTConverter.ontologyFileTransformation.OntologyModelWriter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class IfcBrotOntologyBuilderTest {

    IfcBrotOntologyBuilder ifcBrotOntologyBuilder;
    ArrayList<AnnotatedIfcEntity> annotatedEntities;
    ArrayList<IfcEntity> ifcEntities;
    String uri="testuri#";

    @Before
    public void initOntologyBuilder() {
        ifcBrotOntologyBuilder = new IfcBrotOntologyBuilder();
        annotatedEntities = new ArrayList<>();
        annotatedEntities.add(new AnnotatedIfcEntity("3eRhty6cDFcvSLj8ZTOvjd","2320", "IfcAbutment"));
        ifcEntities = new ArrayList<>();
        IfcEntity ifcEntity = new IfcEntity("3eRhty6cDFcvSLj8ZTOvjd", "Component");
        ifcEntity.addBuildingElementAsDecomposed("testcomponent");
        IfcEntity aggregatedIfcEntity = new IfcEntity("testcomponent", "Component");
        ifcEntities.add(ifcEntity);
        ifcEntities.add(aggregatedIfcEntity);
    }

    @Test
    public void testOntologyCreation() throws FileNotFoundException {
        LinkedOntology linkedOntology = ifcBrotOntologyBuilder.createBrotOntologyWithAnnotations(annotatedEntities, uri);
        OntologyModelWriter.writeOntology(linkedOntology.getOntology(), "src/test/java/TestResources/TestOutput/generatedOntology.ttl");
        assertNotNull(linkedOntology.getOntology().getIndividual(uri + "3eRhty6cDFcvSLj8ZTOvjd"));
        assertNotNull(linkedOntology.getLinkInformation().containsValue("3eRhty6cDFcvSLj8ZTOvjd"));
    }

    @Test
    public void testOntologyCreationWithoutAnnotation() throws FileNotFoundException {
        LinkedOntology linkedOntology = ifcBrotOntologyBuilder.createBrotOntology(ifcEntities, uri);
        OntologyModelWriter.writeOntology(linkedOntology.getOntology(), "src/test/java/TestResources/TestOutput/generatedOntology.ttl");
        assertNotNull(linkedOntology.getOntology().getIndividual(uri + "3eRhty6cDFcvSLj8ZTOvjd"));
        assertNotNull(linkedOntology.getLinkInformation().containsValue("3eRhty6cDFcvSLj8ZTOvjd"));
    }

}
