package batchprocessor;

import java.util.*;
import org.w3c.dom.*;

/**
 * Builds and returns an executable Batch from a DOM Document constructed from
 * an XML Batch file.
 *
 * @author Travis LaGrone
 * @version 1.0.0, 2016-10-01
 * @since 1.0.0
 */
public class BatchBuilder
{
    /**
     * Constructs a BatchBuilder.  As of version 1.0.0, this is a no-op since
     * the BatchBuilder has no fields.
     */
    public BatchBuilder()
    {
        // no-op
    }
    
    /**
     * Builds and returns a Batch constructed from a Document.  Verifies that
     * the Document is identified as a batch.
     * 
     * As of version 1.0.0, this method resolves references to named filenames
     * (e.g. an ExecCommand's "in" reference to the ID of a FilenameCommand)
     * immediately at build time.  This is similar to "eager evaluation" strategy
     * that optimizes named constants by at compile time by replacing references
     * with literals.
     * 
     * @param doc the Document to build a Batch from
     * @return a new Batch built from the Document argument
     */
    public Batch buildBatch(Document doc)
    {
        Batch batch = new Batch();
        Map<String,String> filenames = new TreeMap<>();  // helper symbol table
        
        // Get root Node and verify that doc is identified as a batch.
        Element root = doc.getDocumentElement();
        String name = root.getNodeName();
        if (!name.equalsIgnoreCase("batch")) {  // not equals
            // TODO throw new Exception("XML document is not identified as a batch")
        }
        
        // Loop through children of root, parsing each Element (and only Elements)
        // as a Command, then adding that Command to the Batch.
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                Command cmd = parseCommand(elem, filenames);
                batch.addCommand(cmd);
            }
        }
        
        return batch;
    }
    
    /**
     * Parses the Element argument as a Command.  Identifies the type of Command
     * then instantiates a new Command of that subclass, parses the Element into
     * that Command, then returns the Command.
     * 
     * @param elem the Element to parse as a Command
     * @param filenames a Map of named filenames IDs to paths, used to pass onto
     *                  {@link Command#parse(org.w3c.dom.Element, java.util.Map)}
     * @return a new Command parsed from the Element argument
     */
    public Command parseCommand(Element elem, Map<String,String> filenames)
    {
        Command cmd = null;
        String name = elem.getNodeName();
        
        if (name == null) {
            // TODO throw new exception("unable to parse command from " + elem.getTextContent());
        }
        else if (name.equalsIgnoreCase("filename")) {
            System.out.println("Parsing filename...");
            cmd = new FilenameCommand();
            cmd.parse(elem, filenames);
        }
        else if (name.equalsIgnoreCase("exec")) {
            System.out.println("Parsing exec...");
            cmd = new ExecCommand();
            cmd.parse(elem, filenames);
        }
        else if (name.equalsIgnoreCase("pipecmd")) {
            System.out.println("Parsing pipecmd...");
            cmd = new PipecmdCommand();
            cmd.parse(elem, filenames);
        }
        else {
            // TODO throw new ProcessException("Unknown command " + name + " from: " + elem.getBaseURI());
        }
        
        return cmd;
    }
}
