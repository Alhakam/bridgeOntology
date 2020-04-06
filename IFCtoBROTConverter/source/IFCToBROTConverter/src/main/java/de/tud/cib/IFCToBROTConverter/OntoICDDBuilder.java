package de.tud.cib.IFCToBROTConverter;


import de.tud.cib.IFCToBROTConverter.ICDDConfiguration.ICDDBuilder;
import de.tud.cib.IFCToBROTConverter.annotation.AnnotatedIfcEntity;
import de.tud.cib.IFCToBROTConverter.annotation.AnnotationParser;
import de.tud.cib.IFCToBROTConverter.ifcFilter.IfcEntity;
import de.tud.cib.IFCToBROTConverter.ifcFilter.IfcFilter;
import de.tud.cib.IFCToBROTConverter.ontologyConfiguration.IfcBrotOntologyBuilder;
import de.tud.cib.IFCToBROTConverter.ontologyConfiguration.LinkedOntology;
import icdd.beans.Container;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Builder for building and exporting an ICDD-Container.
 */
public class OntoICDDBuilder {

    /**
     * Creates an ICDD based on an annotated IFC file
     * @param ifc The IFC file
     * @param annotationMMC The MMC-zip file containing IFC annotations
     * @param tempDirectory The temporary directory for the generated ontology file. Needs appropriate ontology suffix.
     * @return the ICDD container containing the IFC file as well as the ontology and optional / additional files with Linksets
     */
    public Container createICDDWithAnnotation(Path ifc, Path annotationMMC, String tempDirectory) throws IOException, JDOMException {
        AnnotationParser annotationParser = new AnnotationParser();
        ArrayList<AnnotatedIfcEntity> annotatedEntities = annotationParser.parseAnnotationData(annotationMMC);
        IfcBrotOntologyBuilder ifcBrotOntologyBuilder = new IfcBrotOntologyBuilder();
        LinkedOntology linkedOntology = ifcBrotOntologyBuilder.createBrotOntologyWithAnnotations(annotatedEntities);
        ICDDBuilder icddBuilder = new ICDDBuilder();
        return icddBuilder.createICDD(ifc, linkedOntology, tempDirectory);
    }

    /**
     * Creates an ICDD based on a given IFC file
     * @param ifc The IFC file
     * @param tempDirectory The temporary directory for the generated ontology file. Needs appropriate ontology suffix.
     * @return the ICDD container containing the IFC file as well as the ontology and optional / additional files with Linksets
     * @throws IOException
     * @throws JDOMException
     */
    public Container createICDD(Path ifc, String tempDirectory) throws Exception {
        IfcFilter ifcFilter = new IfcFilter(ifc);
        ArrayList<IfcEntity> ifcEntities = ifcFilter.filterAllTypes();
        IfcBrotOntologyBuilder ifcBrotOntologyBuilder = new IfcBrotOntologyBuilder();
        LinkedOntology linkedOntology = ifcBrotOntologyBuilder.createBrotOntology(ifcEntities);
        ICDDBuilder icddBuilder = new ICDDBuilder();
        return icddBuilder.createICDD(ifc, linkedOntology, tempDirectory);
    }

    /**
     * Exports the ICDD to a given output path.
     * @param ifc The IFC file
     * @param annotationMMC The MMC-zip file containing IFC annotations
     * @param output The URI / path where the ICDD-file will be exported
     * @param tempDirectory The temporary directory for the generated ontology file. Needs appropriate ontology suffix.
     */
    public void createAndExportICDDWithAnnotations(Path ifc, Path annotationMMC, String output, String tempDirectory) throws IOException, JDOMException {
        Container container = this.createICDDWithAnnotation(ifc, annotationMMC, tempDirectory);
        container.writeICDDContainer(output);
    }

    /**
     * Exports the ICDD to a given output path.
     * @param ifc The URI / path where the IFC file is located
     * @param annotationMMC The URI / path where the MMC-zip file containing IFC annotations is located
     * @param output The URI / path where the ICDD-file will be exported
     * @param tempDirectory The temporary directory for the generated ontology file. Needs appropriate ontology suffix.
     */
    public void createAndExportICDDWithAnnotations(String ifc, String annotationMMC, String output, String tempDirectory) throws IOException, JDOMException {
        this.createAndExportICDDWithAnnotations(Paths.get(ifc), Paths.get(annotationMMC), output, tempDirectory);
    }

    /**
     * Exports the ICDD to a given output path.
     * @param ifc The IFC file
     * @param output The URI / path where the ICDD-file will be exported
     * @param tempDirectory The temporary directory for the generated ontology file. Needs appropriate ontology suffix.
     * @throws Exception
     */
    public void createAndExportICDD(Path ifc, String output, String tempDirectory) throws Exception {
        Container container = this.createICDD(ifc, tempDirectory);
        container.writeICDDContainer(output);
    }

    /**
     * Exports the ICDD to a given output path.
     * @param ifc The URI / path where the IFC file is located
     * @param output The URI / path where the ICDD-file will be exported
     * @param tempDirectory The temporary directory for the generated ontology file. Needs appropriate ontology suffix.
     * @throws Exception
     */
    public void createAndExportICDD(String ifc, String output, String tempDirectory) throws Exception {
        Container container = this.createICDD(Paths.get(ifc), tempDirectory);
        container.writeICDDContainer(output);
    }
}
