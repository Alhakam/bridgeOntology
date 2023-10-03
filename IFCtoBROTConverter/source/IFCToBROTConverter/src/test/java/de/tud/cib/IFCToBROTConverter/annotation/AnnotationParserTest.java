package de.tud.cib.IFCToBROTConverter.annotation;

import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class AnnotationParserTest {

    private AnnotationParser annotationParser;
    private Path annotationZip;

    @Before
    public void initAnnotationParser() {
        annotationParser = new AnnotationParser();
        annotationZip = Paths.get("src/test/java/TestResources/annotation.zip");
    }

    @Test
    public void testParsing() throws IOException, JDOMException {
        ArrayList<AnnotatedIfcEntity> annotatedEntities = annotationParser.parseAnnotationData(annotationZip);
        AnnotatedIfcEntity assertionEntity = null;
        for (AnnotatedIfcEntity annotatedIfcEntity :
                annotatedEntities) {
            if(annotatedIfcEntity.getStepID().equals("2320")) {
                assertionEntity = annotatedIfcEntity;
            }
        }
        assert assertionEntity != null;
        assertEquals("2320", assertionEntity.getStepID());
        assertEquals("IfcAbutment", assertionEntity.getAnnotation());
        assertEquals("3eRhty6cDFcvSLj8ZTOvjd", assertionEntity.getIfcID());
    }

}
