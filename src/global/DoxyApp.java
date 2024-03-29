/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package global;

/**
 *
 * @author Rendra
 */
public class DoxyApp {
    public static int progressCounter;
    public static String workPath;
    
    public static Bridge bridge             = new Bridge();    
    public final static String rxComment    = "/\\*(?>(?:(?>[^*]+)|\\*(?!/))*)\\*/|(//.*)";    
    public final static String rxMethod     = "\\w+ +\\w+ *\\([^\\)]*\\) *\\{";
    public final static String allMethod    = "((public|protected|private|\\s) [\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;]))|"+
            "(\\w+ \\w+ \\w+ +\\w+ *\\([^\\)]*\\) *\\{)";
    public final static String encMethod    = "((protected|private) +\\w+ [\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;]))|"+
            "((protected|private) +[\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;]))";
    public final static String ovrMethod    = "@Override\\s*(.*)";
    public final static String rxEmptyLine  = "^\\s*$";    
    public final static String myServer     = "http://localhost/2013/doxy/";
    public final static String myWiki       = "http://code.google.com/p/doxy/";    
    public final static String doxletpath   = "D:\\Doclava\\doclava.jar";    
    public final static String doxlet       = "com.google.doclava.Doclava";
    public final static String myAppName    = "Doxy";
    public final static String myVersion    = "2.0.1";
}
