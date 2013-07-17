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
    public static Bridge bridge = new Bridge();
    public static String workPath;
    public final static String rxComment = "/\\*(?>(?:(?>[^*]+)|\\*(?!/))*)\\*/|(//.*)";
    public final static String rxMethod = "\\w+ +\\w+ *\\([^\\)]*\\) *\\{";
    public final static String rxEmptyLine = "^\\s*$";
}
