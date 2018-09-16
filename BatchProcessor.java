package batchprocessor;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Processes a Batch of jobs (native command line processes) as specified by an
 * XML Batch file.
 *
 * @author Travis LaGrone
 * @version 1.0.0, 2016-10-01
 * @since 1.0.0
 */
public class BatchProcessor
{

    /**
     * Processes a Batch specified by the XML file command line argument.  The
     * XML file should be the first command line argument.  All subsequent
     * command line arguments (if any) will be ignored.
     * 
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args)  // TODO exception handling (see XML example file)
            throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, InterruptedException
    {
        // Get and validate filename from command line arguments.
        String filename = null;
        if (args.length > 0) {
            filename = args[0];
        } else {
            // TODO throw new Exception("no command line arguments found")
        }
        
        // Open filename as a file input stream.
        System.out.println("Opening " + filename + "...");
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        
        // Build a DOM Document from the XML file.
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fis);
        
        // Build a Batch from the DOM Document.
        System.out.println("Building Batch...");
        BatchBuilder bBuilder = new BatchBuilder();
        Batch batch = bBuilder.buildBatch(doc);
        
        // Execute the Batch.
        System.out.println("Executing Batch...");
        batch.execute();
    }
}
