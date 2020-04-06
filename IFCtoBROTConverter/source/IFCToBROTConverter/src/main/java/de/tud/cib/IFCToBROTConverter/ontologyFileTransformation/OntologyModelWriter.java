package de.tud.cib.IFCToBROTConverter.ontologyFileTransformation;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 *  Provides methods for ontology output
 */
public class OntologyModelWriter {

    /**
     * Writes a given Ontmodel to a specifc file location
     * @param model The ontology java representation as OntModel
     * @param outputPath The file path where the ontology should be written
     * @throws FileNotFoundException
     */
    public static void writeOntology(OntModel model, String outputPath) throws FileNotFoundException {
        File file = new File(outputPath);
        OutputStream outputStream = new FileOutputStream(file);
        RDFDataMgr.write(outputStream, model, RDFFormat.TTL);
    }

}
