package batchprocessor;

import java.io.IOException;
import java.util.*;

/**
 * An executable Batch job of Command(s).  Executes in sequential order.
 * 
 * <p>As of version 1.0.0, references to {@link FilenameCommand} commands are
 * resolved at build time rather than execution time for the sake of optimization.
 * However, FilenameCommand(s) may still be added to the Batch for the sake of
 * tracing the Batch's execution.
 *
 * @author Travis LaGrone
 * @version 1.0.0, 2016-10-01
 * @since 1.0.0
 */
public class Batch
{
    private List<Command> commandList;  // ordered list of Command(s) to execute
    
    /**
     * Constructs a new, empty Batch.  instantiates {@link Batch#commandList}
     * as an ArrayList.
     */
    public Batch()
    {
        commandList = new ArrayList<>();
    }
    
    /**
     * Adds the given Command to the end of the Batch.
     * 
     * @param cmd the Command to add to the Batch
     * @throws NullPointerException if cmd argument is null
     */
    public void addCommand(Command cmd) throws NullPointerException
    {
        if (cmd == null) {  // input validation
            throw new NullPointerException("Unable to add Command. Batch does "
                    + "not support null elements.");
        } else {
            commandList.add(cmd);
        }
    }
    
    /**
     * Executes the Commands in the Batch in the order added.  Will print the
     * return value of {@link Command#describe()} before executing each Command.
     * 
     * @throws InterruptedException 
     * @throws java.io.IOException 
     */
    public void execute()
            throws InterruptedException, IOException  // TODO figure out what InterruptedException is
    {
        Iterator<Command> i = commandList.iterator();
        Command c;
        Process p;
        
        while (i.hasNext()) {
            c = i.next();
            System.out.println(c.describe());  // print Command description
            p = c.execute();  // execute Command
            if (p != null) {
                p.waitFor();  // wait for last Command's Process to terminate, if applicable
            }
        }
    }
}
