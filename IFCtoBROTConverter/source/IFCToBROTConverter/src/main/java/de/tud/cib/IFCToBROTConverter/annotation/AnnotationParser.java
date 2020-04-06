package de.tud.cib.IFCToBROTConverter.annotation;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * Parser for parsing annotation files that are related to an IFC model
 */
public class AnnotationParser {

    private String annotationLink = "bim_sem_links.xml";
    private String annotationContent = "semantic_data.xml";
    private String ifcIDValue = "FM1";
    private String stepIDValue = "FM2";

    /**
     * Parses the annotation data of a given annotation Multimodel Container
     * @param annotationMMC
     * @return ArrayList of AnnotatedEntity objects
     */
    public ArrayList<AnnotatedIfcEntity> parseAnnotationData(Path annotationMMC) throws IOException, JDOMException {
        ZipFile annotationArchive = new ZipFile(annotationMMC.toFile());
        Document linkXML = this.getXMLFromMMC(annotationArchive, this.annotationLink);
        Document contentXML = this.getXMLFromMMC(annotationArchive, this.annotationContent);
        ArrayList<AnnotatedIfcEntity> annotatedIfcEntityList = this.createAnnotatedEntityList(linkXML);
        annotatedIfcEntityList = this.addAnnotationsToAnnotatedEntityList(contentXML, annotatedIfcEntityList);
        return annotatedIfcEntityList;
    }

    private Document getXMLFromMMC(ZipFile zipFile, String filename) throws IOException, JDOMException {
        InputStream linkStream = zipFile.getInputStream(zipFile.getEntry(filename));
        Document xml = new SAXBuilder().build(linkStream);
        return xml;
    }

    private ArrayList<AnnotatedIfcEntity> createAnnotatedEntityList(Document linkXML) {
        ArrayList<AnnotatedIfcEntity> annotatedIfcEntityList = new ArrayList<>();
        List<Element> linkObjectList = linkXML.getRootElement().getChildren("linkObject");
        for (Element linkObject :
                linkObjectList) {
            annotatedIfcEntityList.add(this.createAnnotatedEntityWithID(linkObject));
        }
        return annotatedIfcEntityList;
    }

    private AnnotatedIfcEntity createAnnotatedEntityWithID(Element linkObject) {
        AnnotatedIfcEntity annotatedIfcEntity = new AnnotatedIfcEntity();
        List<Element> linkList = linkObject.getChildren("link");
        for (Element link :
                linkList) {
            if (link.getAttributeValue("m").equals(this.ifcIDValue))
                annotatedIfcEntity.setIfcID(link.getAttributeValue("id"));
            if (link.getAttributeValue("m").equals(this.stepIDValue))
                annotatedIfcEntity.setStepID(link.getAttributeValue("id"));
        }
        return annotatedIfcEntity;
    }

    private ArrayList<AnnotatedIfcEntity> addAnnotationsToAnnotatedEntityList(Document contentXML, ArrayList<AnnotatedIfcEntity> annotatedIfcEntityList) {
        ArrayList<AnnotatedIfcEntity> changedAnnotatedIfcEntityList = new ArrayList<>();
        for (AnnotatedIfcEntity annotatedIfcEntity :
                annotatedIfcEntityList) {
            changedAnnotatedIfcEntityList.add(this.setAnnotationFromAnnotationXML(contentXML, annotatedIfcEntity));
        }
        return changedAnnotatedIfcEntityList;
    }

    private AnnotatedIfcEntity setAnnotationFromAnnotationXML(Document contentXML, AnnotatedIfcEntity annotatedIfcEntity) {
        List<Element> annotationList = contentXML.getRootElement().getChildren("IfcClass");
        for (Element annotation :
                annotationList) {
            if (annotation.getAttributeValue("STEP_ID").equals(annotatedIfcEntity.getStepID()))
                annotatedIfcEntity.setAnnotation(annotation.getAttributeValue("Name"));
        }
        return annotatedIfcEntity;
    }

}