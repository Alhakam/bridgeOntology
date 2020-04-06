package de.tud.cib.IFCToBROTConverter;


import icdd.beans.Container;
import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class OntoICDDBuilderTest {

    private OntoICDDBuilder ontoICDDBuilder;
    String ifcPath = "src/test/java/TestResources/AnonymBridge.ifc";
    String annotationPath = "src/test/java/TestResources/annotation.zip";
    String outputPath = "src/test/java/TestResources/TestOutput/";
    String tempDirectory = "src/test/java/TestResources/tempDirectory/generatedBridgeOntology.ttl";
    Path ifc;
    Path annotation;

    @Before
    public void initOntoICDDBuilder() {
        ontoICDDBuilder = new OntoICDDBuilder();
        ifc = Paths.get(ifcPath);
        annotation = Paths.get(annotationPath);
    }

    @After
    public void cleanTempDirectory() {
        Path tempFile = Paths.get(tempDirectory);
        tempFile.toFile().delete();
    }

    @Test
    public void testCreateICDDWithAnnotation() throws IOException, JDOMException {
        Container container = ontoICDDBuilder.createICDDWithAnnotation(ifc, annotation, tempDirectory);
        assertNotNull(container);
    }

    @Test
    public void testExportICDDWithAnnotation() throws IOException, JDOMException {
        Path outputFile = null;
        ontoICDDBuilder.createAndExportICDDWithAnnotations(ifc, annotation, outputPath, tempDirectory);
        outputFile = Paths.get(outputPath);
        assertTrue(Files.exists(outputFile));
        ontoICDDBuilder.createAndExportICDDWithAnnotations(ifcPath, annotationPath, outputPath, tempDirectory);
        outputFile = Paths.get(outputPath);
        assertTrue(Files.exists(outputFile));
    }

    @Test
    public void testCreateICDD() throws Exception {
        Container container = ontoICDDBuilder.createICDD(ifc, tempDirectory);
        assertNotNull(container);
    }

    @Test
    public void testExportICDD() throws Exception {
        Path outputFile = null;
        ontoICDDBuilder.createAndExportICDD(ifc, outputPath, tempDirectory);
        outputFile = Paths.get(outputPath);
        assertTrue(Files.exists(outputFile));
        ontoICDDBuilder.createAndExportICDD(ifcPath, outputPath, tempDirectory);
        outputFile = Paths.get(outputPath);
        assertTrue(Files.exists(outputFile));
    }

}
