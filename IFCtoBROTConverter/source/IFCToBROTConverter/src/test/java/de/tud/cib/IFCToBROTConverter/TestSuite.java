package de.tud.cib.IFCToBROTConverter;

import de.tud.cib.IFCToBROTConverter.annotation.AnnotationParserTest;
import de.tud.cib.IFCToBROTConverter.ontologyConfiguration.IfcBrotOntologyBuilderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        AnnotationParserTest.class,
        IfcBrotOntologyBuilderTest.class,
})

public class TestSuite {
}
